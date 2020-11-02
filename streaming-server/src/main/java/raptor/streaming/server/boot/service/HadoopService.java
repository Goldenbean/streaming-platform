package raptor.streaming.server.boot.service;

import com.google.common.base.Strings;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import raptor.streaming.hadoop.HadoopClient;
import raptor.streaming.hadoop.bean.FilePO;
import raptor.streaming.hadoop.bean.YarnAppPO;
import raptor.streaming.hadoop.bean.YarnClusterPO;

@Service
public class HadoopService {

  private final static Logger logger = LoggerFactory.getLogger(HadoopService.class);

  @Value("${streaming.conf}")
  private String streamingConf;

  private HadoopClient hadoopClient;

  @PostConstruct
  private void init() throws Exception {

    if (Strings.isNullOrEmpty(streamingConf)) {
      throw new RuntimeException("streaming.hadoop.conf is null or empty");
    }

    String hadoopConf = streamingConf + "/hadoop";

    logger.info("init: hadoop conf [{}]", hadoopConf);
    hadoopClient = new HadoopClient(hadoopConf);
    hadoopClient.init();

  }


  public String read(String path) {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    hadoopClient.readHDFSFile(path, os);
    String ret = os.toString();
    return ret;
  }


  public List<FilePO> listFolder(String path) throws IOException {
    return hadoopClient.list(path);
  }

  public void createFolder(String path) throws IOException {
    hadoopClient.exists(path, true);
  }

  public void renameFolder(String path, String name) throws IOException {
    hadoopClient.rename(path, name);
  }

  public void delete(String path) throws IOException {
    hadoopClient.delete(path);
  }

  public void upload(String src, String dst) throws IOException {
    hadoopClient.upload(src, dst);
  }


  public List<YarnAppPO> getYarnApplication(String state)
      throws YarnException, IOException {

    if (Strings.isNullOrEmpty(state)) {
      return hadoopClient.getYarnApplication().stream()
          .map(obj -> new YarnAppPO(obj))
          .collect(Collectors.toList());
    }

    return hadoopClient.getYarnApplication(state).stream()
        .map(obj -> new YarnAppPO(obj))
        .collect(Collectors.toList());
  }

  public void killYarnApplication(String appId) throws YarnException, IOException {
    hadoopClient.killYarnApplication(appId);
  }

  public YarnClusterPO getYarnOverview() throws YarnException, IOException {
    return hadoopClient.getYarnOverview();
  }


  public String getYarnState(String id) {

    if (Strings.isNullOrEmpty(id)) {
      return "";
    }

    try {
      for (ApplicationReport applicationReport : hadoopClient.getYarnApplication()) {
        if (applicationReport.getApplicationId().toString().equals(id)) {
          return applicationReport.getYarnApplicationState().toString();
        }
      }
    } catch (Exception ex) {
    }
    return "";
  }

}
