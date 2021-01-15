package raptor.streaming.hadoop.yarn;

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
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class YarnDeployTests {

  private static final Logger logger = LoggerFactory.getLogger(YarnDeployTests.class);

  public static final String DIST_ROOT = "../../streaming-conf";
  public static final String lib = DIST_ROOT + "/flink-1.11-SNAPSHOT/lib/";
  public static final String clusterConfigDirPath = DIST_ROOT + "/hadoop";

  @Test
  public void testLoadConfig() {
    Configuration conf = YarnDeploy.load("./");
    new TreeMap<>(conf.toMap())
        .forEach((k, v) -> logger.info("[{}] -> [{}]", k, v));
  }


  @Test
  public void testBuildJobGraph()
      throws ProgramInvocationException, IOException, ClusterDeploymentException {

    System.setProperty("HADOOP_USER_NAME", "root");

    //System.setProperty("hadoop.home.dir", "/tmp/flink-1.11/hadoop");
    //System.setProperty("FLINK_LIB_DIR", lib);

    //System.setProperty("HADOOP_HOME", "/tmp/flink-1.11/hadoop");
    //System.out.println("HADOOP_HOME: " + System.getenv("HADOOP_HOME"));
    //System.setProperty("HADOOP_CONF_DIR", "/tmp/flink-1.11/hadoop/conf");

    Configuration flinkConfiguration = YarnDeploy.load("/Users/azhe/stream/streaming-platform/config/hadoop-dev","flink-dev","hdfs://stream-hdfs/streaming-platform/flink-cluster/engines/flink-hadoop-dev/system");
    flinkConfiguration.setString(ConfigConstants.PATH_HADOOP_CONFIG, "/Users/azhe/stream/streaming-platform/config/hadoop-dev");

    List<String> userClassPaths = Arrays
        .asList("/Users/azhe/stream/streaming-platform/engines/flink-1.12.0/flink-dist_2.12-1.12.0.jar", "/Users/azhe/stream/streaming-platform/engines/flink-1.11.2/system/kafka-clients-2.4.1.jar");

//    List<String> userClassPaths = jars.stream()
//        .map(input -> lib + input)
//        .collect(Collectors.toList());

    String uuid = UUID.randomUUID().toString();
    List<String> args = Arrays.asList("--uuid", uuid,
        "--group", "flink-demo-" + uuid);

    PackagedProgram packagedProgram = YarnDeploy.buildPackagedProgram(
        "/Users/azhe/Documents/流平台/jars/app/flink-app-mirror-1.0.0-1.12.jar",
        flinkConfiguration, userClassPaths, args, null);

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
        .createClusterDescriptor(flinkConfiguration,
            YarnDeploy.initConfiguration("/Users/azhe/stream/streaming-platform/config/hadoop-dev"));

    YarnClusterClientFactory yarnClusterClientFactory = new YarnClusterClientFactory();
    ClusterSpecification clusterSpecification = yarnClusterClientFactory
        .getClusterSpecification(flinkConfiguration);

    logger.info("flinkConfiguration {}", flinkConfiguration);
    logger.info("clusterSpecification {}", clusterSpecification);

    org.apache.flink.core.fs.FileSystem
        .initialize(flinkConfiguration,
            PluginUtils.createPluginManagerFromRootFolder(flinkConfiguration));

    Path flinkPath = new Path(
        "hdfs://stream-hdfs/streaming-platform/flink-cluster/engines/flink-hadoop/system");
    FileSystem fs = flinkPath.getFileSystem();
    System.out.println(fs.isDistributedFS());

    YarnDeploy.deploy(clusterDescriptor, clusterSpecification, jobGraph);

  }


}
