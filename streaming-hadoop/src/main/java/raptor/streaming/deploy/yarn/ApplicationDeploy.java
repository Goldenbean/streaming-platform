package raptor.streaming.deploy.yarn;

import com.google.common.base.Strings;
import java.io.IOException;
import java.util.List;
import org.apache.flink.client.deployment.ClusterDeploymentException;
import org.apache.flink.client.deployment.ClusterDescriptor;
import org.apache.flink.client.deployment.ClusterSpecification;
import org.apache.flink.client.deployment.application.ApplicationConfiguration;
import org.apache.flink.client.program.ClusterClientProvider;
import org.apache.flink.client.program.ProgramInvocationException;
import org.apache.flink.configuration.ConfigConstants;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.DeploymentOptions;
import org.apache.flink.core.plugin.PluginUtils;
import org.apache.flink.yarn.YarnClusterClientFactory;
import org.apache.flink.yarn.YarnClusterDescriptor;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationDeploy {

  private static final Logger logger = LoggerFactory.getLogger(ApplicationDeploy.class);

  private final String flinkConfPath;
  private final String hadoopConfPath;

  public ApplicationDeploy(String flinkConfPath, String hadoopConfPath) {
    this.flinkConfPath = flinkConfPath;
    this.hadoopConfPath = hadoopConfPath;
  }

  public void init() {
    if (Strings.isNullOrEmpty(flinkConfPath) || Strings.isNullOrEmpty(hadoopConfPath)) {
      throw new RuntimeException("flink config path or hadoop config path is null or empty ");
    }

  }

  public ClusterClientProvider<ApplicationId> deploy(DeployConfig deployConfig)
      throws ProgramInvocationException, IOException, ClusterDeploymentException {

    return deploy(deployConfig.getApplicationName(), deployConfig.getJarFilePath(), deployConfig
        .getProgramArgs(), 4);

  }

  public ClusterClientProvider<ApplicationId> deploy(String applicationName,
      String jarPath, List<String> args, int spu)
      throws ProgramInvocationException, IOException, ClusterDeploymentException {

    // Configuration flinkConfiguration = YarnDeploy.load(DIST_ROOT + "/conf");
    Configuration flinkConfiguration = YarnDeploy.load(flinkConfPath, applicationName);

    flinkConfiguration.setInteger("parallelism.default", spu * 4);
    flinkConfiguration.setString(ConfigConstants.PATH_HADOOP_CONFIG, hadoopConfPath);

    YarnDeploy.conf(flinkConfiguration, jarPath, args);

    ClusterDescriptor<ApplicationId> clusterDescriptor = YarnDeploy
        .createClusterDescriptor(flinkConfiguration,
            YarnDeploy.initConfiguration(hadoopConfPath));

    YarnClusterClientFactory yarnClusterClientFactory = new YarnClusterClientFactory();

    ClusterSpecification clusterSpecification = yarnClusterClientFactory
        .getClusterSpecification(flinkConfiguration);

    logger.info("ClusterSpecification: [{}]", clusterSpecification);

    org.apache.flink.core.fs.FileSystem
        .initialize(flinkConfiguration,
            PluginUtils.createPluginManagerFromRootFolder(flinkConfiguration));

//    final ApplicationConfiguration applicationConfiguration =
//        new ApplicationConfiguration(
//            programOptions.getProgramArgs(),
//            programOptions.getEntryPointClass()
//        );

    final ApplicationConfiguration applicationConfiguration =
        new ApplicationConfiguration(toArray(args), "");

    flinkConfiguration.set(DeploymentOptions.TARGET, "yarn-application");

    ClusterClientProvider<ApplicationId> clusterClientProvider = ((YarnClusterDescriptor) clusterDescriptor)
        .deployApplicationCluster(clusterSpecification, applicationConfiguration);

    return clusterClientProvider;

  }

  private String[] toArray(List<String> list) {
    return list.toArray(new String[list.size()]);
  }


}
