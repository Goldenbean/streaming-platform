package raptor.streaming.server.boot.service;

import com.google.common.base.Strings;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.flink.client.program.ClusterClientProvider;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import raptor.streaming.deploy.yarn.ApplicationDeploy;

@Service
public class DeployService {

  private final static Logger logger = LoggerFactory.getLogger(HadoopService.class);

  @Value("${streaming.conf}")
  private String streamingConf;


  private ApplicationDeploy applicationDeploy;

  @PostConstruct
  public void init() {

    if (Strings.isNullOrEmpty(streamingConf)) {
      throw new RuntimeException("streaming.hadoop.conf or streaming.flink.conf is null or empty");
    }

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
