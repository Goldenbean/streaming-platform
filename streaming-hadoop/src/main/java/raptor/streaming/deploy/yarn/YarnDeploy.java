package raptor.streaming.deploy.yarn;

import com.google.common.base.Strings;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import org.apache.flink.client.deployment.ClusterDeploymentException;
import org.apache.flink.client.deployment.ClusterDescriptor;
import org.apache.flink.client.deployment.ClusterSpecification;
import org.apache.flink.client.deployment.application.ApplicationConfiguration;
import org.apache.flink.client.program.ClusterClientProvider;
import org.apache.flink.client.program.PackagedProgram;
import org.apache.flink.client.program.PackagedProgramUtils;
import org.apache.flink.client.program.ProgramInvocationException;
import org.apache.flink.configuration.ConfigUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.configuration.PipelineOptions;
import org.apache.flink.runtime.jobgraph.JobGraph;
import org.apache.flink.runtime.jobgraph.SavepointRestoreSettings;
import org.apache.flink.yarn.RaptorYarnCluster;
import org.apache.flink.yarn.YarnClientYarnClusterInformationRetriever;
import org.apache.flink.yarn.YarnClusterClientFactory;
import org.apache.flink.yarn.YarnClusterDescriptor;
import org.apache.flink.yarn.YarnClusterInformationRetriever;
import org.apache.flink.yarn.configuration.YarnConfigOptions;
import org.apache.flink.yarn.configuration.YarnLogConfigUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class YarnDeploy {

  private static final Logger logger = LoggerFactory.getLogger(YarnDeploy.class);

  public static final YarnClusterClientFactory yarnClusterClientFactory = new YarnClusterClientFactory();


  public static org.apache.hadoop.conf.Configuration initConfiguration
      (String clusterConfigDirPath) throws IOException {

    File clusterConfigDir = new File(clusterConfigDirPath);

    if (!clusterConfigDir.exists()) {
      throw new IOException("cluster config dir path doesn't exists ! " + clusterConfigDirPath);
    }

    org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration(false);
    conf.addResource(new Path(clusterConfigDirPath + File.separator + "core-site.xml"));
    conf.addResource(new Path(clusterConfigDirPath + File.separator + "hdfs-site.xml"));
    conf.addResource(new Path(clusterConfigDirPath + File.separator + "yarn-site.xml"));

    logger.info("conf: [{}]", conf);
    conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");

    return conf;
  }

  public static Configuration load(String configurationDir) {
    return load(configurationDir, "");
  }

  public static Configuration load(String configurationDir, String applicationName) {

    logger.info("{}", configurationDir);

    Configuration configuration = GlobalConfiguration.loadConfiguration(configurationDir);
    YarnLogConfigUtil.setLogConfigFileInConfig(configuration, configurationDir);

    configuration.setString(YarnConfigOptions.APPLICATION_NAME, applicationName);
//    flinkConfiguration.setString(YarnConfigOptions.APPLICATION_QUEUE, "");
//    flinkConfiguration.setString(YarnConfigOptions.APPLICATION_TYPE, "");
//    flinkConfiguration.setString(YarnConfigOptions.NODE_LABEL, "");

    // -yD yarn.provided.lib.dirs="hdfs://streaming-cluster/streaming-platform/flink/engines/flink-1.11/lib"
    configuration.setString("yarn.provided.lib.dirs",
        "hdfs://streaming-cluster/streaming-platform/flink/engines/flink-1.11/lib");
    return configuration;
  }

  public static PackagedProgram buildPackagedProgram(String jobJarLocalPath,
      Configuration configuration, List<String> userClassPaths, @Nullable List<String> args,
      @Nullable String savePointPath) throws ProgramInvocationException {

    PackagedProgram.Builder builder = PackagedProgram.newBuilder()
        .setJarFile(new File(jobJarLocalPath));

    if (configuration != null) {
      builder.setConfiguration(configuration);
    }

    if (args != null && args.size() > 0) {
      //   args.split("\\s+");
      builder.setArguments(args.toArray(new String[args.size()]));
    }

    if (userClassPaths != null) {
      List<URL> urls = new ArrayList<>();

      for (String input : userClassPaths) {
        try {
          urls.add(new URL("file://" + input));
        } catch (MalformedURLException ex) {
          logger.warn("", ex);
        }
      }

      builder.setUserClassPaths(urls);
    }

    if (!Strings.isNullOrEmpty(savePointPath)) {
      boolean allowNonRestoredState = false;

      SavepointRestoreSettings savepointSettings = SavepointRestoreSettings
          .forPath(savePointPath, allowNonRestoredState);

      builder.setSavepointRestoreSettings(savepointSettings);

    }
    return builder.build();
  }

  public static JobGraph buildJobGraph(PackagedProgram packagedProgram,
      Configuration configuration,
      int defaultParallelism) throws ProgramInvocationException {

    JobGraph jobGraph = PackagedProgramUtils.createJobGraph(packagedProgram,
        configuration, defaultParallelism, false);

    return jobGraph;
  }


  public static YarnClient buildYarnClient(final YarnConfiguration yarnConfiguration) {
    final YarnClient yarnClient = YarnClient.createYarnClient();

    yarnClient.init(yarnConfiguration);
    yarnClient.start();
    return yarnClient;
  }

  public static ClusterDescriptor<ApplicationId> createClusterDescriptor(
      Configuration flinkConfiguration,
      org.apache.hadoop.conf.Configuration conf) {

    YarnConfiguration yarnConfiguration = new YarnConfiguration(conf);

    YarnClient yarnClient = buildYarnClient(yarnConfiguration);

    YarnClusterInformationRetriever yarnClusterInformationRetriever =
        YarnClientYarnClusterInformationRetriever.create(yarnClient);

    boolean sharedYarnClient = true;

    RaptorYarnCluster clusterDescriptor = RaptorYarnCluster.newInstance(flinkConfiguration,
        yarnConfiguration, yarnClient, yarnClusterInformationRetriever, sharedYarnClient);

//    YarnClusterDescriptor clusterDescriptor = new YarnClusterDescriptor(flinkConfiguration,
//        yarnConfiguration, yarnClient, yarnClusterInformationRetriever, sharedYarnClient);

    return clusterDescriptor;

  }


  public static ClusterClientProvider<ApplicationId> deploy(
      final ClusterDescriptor<ApplicationId> clusterDescriptor,
      final ClusterSpecification clusterSpecification, final JobGraph jobGraph)
      throws ClusterDeploymentException {

    ClusterClientProvider<ApplicationId> clusterClientProvider = ((YarnClusterDescriptor) clusterDescriptor)
        .deployJobCluster(clusterSpecification, jobGraph, true);

    return clusterClientProvider;
  }


  public static void conf(Configuration configuration, String jarPath,
      List<String> programArguments) {

    ConfigUtils.encodeCollectionToConfig(configuration,
        PipelineOptions.JARS,
        Collections.singletonList(jarPath),
        Object::toString);

    ConfigUtils.encodeArrayToConfig(configuration,
        ApplicationConfiguration.APPLICATION_ARGS,
        programArguments.toArray(new String[programArguments.size()]),
        Objects::toString);
  }

}
