package raptor.streaming.hadoop.yarn;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.flink.client.deployment.ClusterDeploymentException;
import org.apache.flink.client.deployment.ClusterDescriptor;
import org.apache.flink.client.deployment.ClusterSpecification;
import org.apache.flink.client.program.PackagedProgram;
import org.apache.flink.client.program.ProgramInvocationException;
import org.apache.flink.configuration.ConfigConstants;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.core.fs.Path;
import org.apache.flink.core.plugin.PluginUtils;
import org.apache.flink.runtime.jobgraph.JobGraph;
import org.apache.flink.yarn.YarnClusterClientFactory;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class YarnDeployTests {

  private static final Logger logger = LoggerFactory.getLogger(YarnDeployTests.class);

  public static final String DIST_ROOT = "../../flink";
  public static final String LIB_PATH = DIST_ROOT + "/lib";
  public static final String HADOOP_CONFIG = "../../hadoop";
  public static final String FLINK_CONFIG = DIST_ROOT + "/conf";
  public static final String PROVIDED_LIB_DIRS = "hdfs://streaming-cluster/streaming-platform/flink/engines/flink-1.12.0";

  public static final String LOCAL_FLINK_APP = "../../app/flink-app-mirror-1.0.0.jar";
  public static final String LOCAL_FLINK_YARN_NAME = "flink-app-mirror";

  public static List<String> USER_CLASS_PATHS = Arrays
      .asList("flink-dist_2.12-1.12.0.jar", "kafka-clients-2.4.1.jar")
      .stream()
      .map(input -> {
        File file = new File(LIB_PATH + "/" + input);
        return file.getAbsolutePath();
      })
      .collect(Collectors.toList());

  @Before
  public void setup() {
    System.out.println(LIB_PATH);
    USER_CLASS_PATHS.forEach(System.out::println);
  }

  @Test
  public void testLoadConfig() {
    Configuration conf = YarnDeploy.load("./");
    new TreeMap<>(conf.toMap())
        .forEach((k, v) -> logger.info("[{}] -> [{}]", k, v));
  }


  @Test
  public void testPerJobDeploy()
      throws ProgramInvocationException, IOException, ClusterDeploymentException {

    System.setProperty("HADOOP_USER_NAME", "root");

    //System.setProperty("hadoop.home.dir", "/tmp/flink-1.11/hadoop");
    //System.setProperty("FLINK_LIB_DIR", lib);

    //System.setProperty("HADOOP_HOME", "/tmp/flink-1.11/hadoop");
    //System.out.println("HADOOP_HOME: " + System.getenv("HADOOP_HOME"));
    //System.setProperty("HADOOP_CONF_DIR", "/tmp/flink-1.11/hadoop/conf");

    Configuration flinkConfiguration = YarnDeploy.load(FLINK_CONFIG,
        LOCAL_FLINK_YARN_NAME, PROVIDED_LIB_DIRS);
    flinkConfiguration.setString(ConfigConstants.PATH_HADOOP_CONFIG, HADOOP_CONFIG);

    String uuid = UUID.randomUUID().toString();
    List<String> args = Arrays.asList("--uuid", uuid, "--group", "flink-demo-" + uuid);

    PackagedProgram packagedProgram = YarnDeploy.buildPackagedProgram(
        LOCAL_FLINK_APP, flinkConfiguration, USER_CLASS_PATHS, args, null);

    JobGraph jobGraph = YarnDeploy.buildJobGraph(packagedProgram,
        flinkConfiguration, 1);

    logger.info("classpath: [{}] \n jobJar: [{}]", packagedProgram.getClasspaths(),
        packagedProgram.getJobJarAndDependencies());

    logger.info("JobGraph, id: [{}] \n name: {} \n vertices: {} \n user jars: {} \n classpath: {}",
        jobGraph.getJobID(), jobGraph.getName(), jobGraph.getVertices(),
        jobGraph.getUserJars(), jobGraph.getClasspaths());

//    org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration(false);
//    conf.addResource(new Path(clusterConfigDirPath + File.separator + "core-site.xml"));
//    conf.addResource(new Path(clusterConfigDirPath + File.separator + "hdfs-site.xml"));
//    conf.addResource(new Path(clusterConfigDirPath + File.separator + "yarn-site.xml"));
//    conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
//    logger.info("{}", conf);

    ClusterDescriptor<ApplicationId> clusterDescriptor = YarnDeploy
        .createClusterDescriptor(flinkConfiguration, YarnDeploy.initConfiguration(HADOOP_CONFIG));

    YarnClusterClientFactory yarnClusterClientFactory = new YarnClusterClientFactory();
    ClusterSpecification clusterSpecification = yarnClusterClientFactory
        .getClusterSpecification(flinkConfiguration);

    logger.info("flinkConfiguration {}", flinkConfiguration);
    logger.info("clusterSpecification {}", clusterSpecification);

    org.apache.flink.core.fs.FileSystem.initialize(flinkConfiguration,
        PluginUtils.createPluginManagerFromRootFolder(flinkConfiguration));

    Path flinkPath = new Path(PROVIDED_LIB_DIRS);
    FileSystem fs = flinkPath.getFileSystem();
    System.out.println(fs.isDistributedFS());

    YarnDeploy.deploy(clusterDescriptor, clusterSpecification, jobGraph);

  }


}
