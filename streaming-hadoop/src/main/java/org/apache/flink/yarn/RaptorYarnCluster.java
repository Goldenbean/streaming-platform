package org.apache.flink.yarn;

import java.io.File;
import java.util.Collection;
import org.apache.flink.configuration.ConfigUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.yarn.configuration.YarnConfigOptions;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RaptorYarnCluster extends YarnClusterDescriptor {

  private static final Logger LOG = LoggerFactory.getLogger(RaptorYarnCluster.class);

  private static final String libDir = "./lib/";

  public static RaptorYarnCluster newInstance(Configuration flinkConfiguration,
      YarnConfiguration yarnConfiguration,
      YarnClient yarnClient,
      YarnClusterInformationRetriever yarnClusterInformationRetriever,
      boolean sharedYarnClient) {

    flinkConfiguration.setString(YarnConfigOptions.FLINK_DIST_JAR, libDir + "flink-dist_2.12-1.11-SNAPSHOT.jar");

  //  ConfigUtils.encodeArrayToConfig(flinkConfiguration, YarnConfigOptions.SHIP_DIRECTORIES, new String[]{libDir}, (path) -> path);

    return new RaptorYarnCluster(flinkConfiguration, yarnConfiguration,
        yarnClient, yarnClusterInformationRetriever, sharedYarnClient);
  }


  private RaptorYarnCluster(Configuration flinkConfiguration,
      YarnConfiguration yarnConfiguration,
      YarnClient yarnClient,
      YarnClusterInformationRetriever yarnClusterInformationRetriever,
      boolean sharedYarnClient) {

    super(flinkConfiguration, yarnConfiguration, yarnClient, yarnClusterInformationRetriever,
        sharedYarnClient);
  }

  @Override
  void addLibFoldersToShipFiles(Collection<File> effectiveShipFiles) {
    //super.addLibFoldersToShipFiles(effectiveShipFiles);

    LOG.info("addLibFoldersToShipFiles: {}, {}", libDir, effectiveShipFiles);
    File directoryFile = new File(libDir);
    if (directoryFile.isDirectory()) {
      effectiveShipFiles.add(directoryFile);
    }
    LOG.info("addLibFoldersToShipFiles: {}, {}", libDir, effectiveShipFiles);
  }
}
