package raptor.streaming.server.service;

import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.flink.client.program.ClusterClientProvider;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raptor.streaming.hadoop.yarn.ApplicationDeploy;
import raptor.streaming.server.conf.BootConfig;
@Service
public class DeployService {

  private final static Logger logger = LoggerFactory.getLogger(DeployService.class);

  @Autowired
  private BootConfig bootConfig;


  private ApplicationDeploy applicationDeploy;

  @PostConstruct
  public void init() {
    String streamingConf = bootConfig.getStreamingConf();

    String flinkConf = streamingConf + "/flink";
    String hadoopConf = streamingConf + "/hadoop";
    applicationDeploy = new ApplicationDeploy(flinkConf, hadoopConf);
    applicationDeploy.init();
  }

  public String deploy(String applicationName, String jarPath,
      List<String> args, int spu) throws Exception {

    logger.debug("[{}] [{}] [{}]", applicationName, jarPath, args);

    ClusterClientProvider<ApplicationId> ret =
        applicationDeploy.deploy(applicationName, jarPath, args, spu);

    ApplicationId id = ret.getClusterClient().getClusterId();
    return id.toString();
  }


}
