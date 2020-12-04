package raptor.streaming.server.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.base.Strings;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import raptor.streaming.hadoop.HadoopClient;
import raptor.streaming.hadoop.bean.FilePO;
import raptor.streaming.hadoop.bean.YarnAppPO;
import raptor.streaming.hadoop.bean.YarnClusterPO;
import raptor.streaming.server.common.constants.Constant;
import raptor.streaming.server.dao.ClusterDao;
import raptor.streaming.server.entity.ClusterEntity;
import raptor.streaming.server.utils.Utils;

@Service
@EnableAsync
public class HadoopService {

  private final static Logger logger = LoggerFactory.getLogger(HadoopService.class);

  private String configDirPath = Constant.CONFIG_DIR_BASE;

  @Autowired
  private ClusterDao clusterDao;

  private Map<String, HadoopClient> hadoopClientMap = new HashMap<>();


  @PostConstruct
  private void init() throws Exception {

    final List<ClusterEntity> clusterEntities = clusterDao.selectList(Wrappers.<ClusterEntity>query().eq("type", 1));

    File configDir = new File(configDirPath);

    if (configDir.exists()) {
      FileUtils.forceMkdir(configDir);
    }

    for (ClusterEntity cluster : clusterEntities) {
      String clusterConfigDirPath = configDirPath + File.separator + cluster.getName();

      File clusterConfigDir = new File(clusterConfigDirPath);

      if (!clusterConfigDir.exists()) {
        FileUtils.forceMkdir(clusterConfigDir);
      }
      addCluster(cluster);
    }
  }

  public void addCluster(ClusterEntity cluster) throws IOException {
    String latestConfigDir = initClusterConfigDir(cluster);
    if (latestConfigDir != null) {
      logger.info("init: hadoop conf [{}]", latestConfigDir);
      updateClusterConfig(cluster, latestConfigDir);
      HadoopClient hadoopClient = new HadoopClient(latestConfigDir);
      hadoopClient.init();

      hadoopClientMap.put(cluster.getName(), hadoopClient);
    }
    logger.info("addCluster , ok");
  }


  public void destroyCluster(String clusterName,String hdfsConfigPath) throws IOException {
    delete(clusterName,hdfsConfigPath);
    hadoopClientMap.remove(clusterName);
    logger.info("destroyCluster , ok");
  }

  private void updateClusterConfig(ClusterEntity cluster, String clusterConfigDirPath) throws IOException {
    File clusterConfigDir = new File(clusterConfigDirPath);
    File[] files = clusterConfigDir.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return !name.endsWith("txt");
      }
    });

    if (files.length > 0) {
      JSONObject clusterConf = new JSONObject();
      for (File file : files) {
        String fileString = FileUtils.readFileToString(file);
        clusterConf.put(file.getName(), fileString);
      }

      cluster.setClusterConf(clusterConf.toJSONString());
      clusterDao.updateById(cluster);
    }

  }

  private String initClusterConfigDir(ClusterEntity cluster) throws IOException {

    String clusterConfigDirPath = configDirPath + File.separator + cluster.getName();

    File clusterConfigDir = new File(clusterConfigDirPath);

    if (!clusterConfigDir.exists()) {
      FileUtils.forceMkdir(clusterConfigDir);
      return downloadConfig(cluster, clusterConfigDirPath) ? clusterConfigDirPath : null;
    } else {
      File md5File = new File(clusterConfigDirPath + File.separator + "md5.txt");
      // md5文件不存在，直接下载最新配置
      if (!md5File.exists()) {
        return downloadConfig(cluster, clusterConfigDirPath) ? clusterConfigDirPath : null;
      } else {
        String configFileMD5 = FileUtils.readFileToString(md5File);
        // md5文件不同，则更新配置文件
        if (!cluster.getConfigFileMd5().equalsIgnoreCase(configFileMD5)) {
          return downloadConfig(cluster, clusterConfigDirPath) ? clusterConfigDirPath : null;
        } else {
          return clusterConfigDirPath;
        }
      }
    }

  }


  private Boolean downloadConfig(ClusterEntity cluster, String downloadDir) throws IOException {

    byte[] data = cluster.getConfigFile();
    String configFileMD5 = cluster.getConfigFileMd5();

    String configFile = configDirPath + File.separator + cluster.getConfigFileName();

    if (Utils.zipData(data, configFile) == null) {
      logger.error("error load config of cluster {}", cluster.getName());
      return false;
    } else {
      if (Utils.unZip(configFile, downloadDir) != null) { // 把配置文件解压出来
        if ((new File(configFile)).delete()) {
          logger.info("success to delete tmp config file of cluster {}", cluster.getName());
        }
        // 写md5值到文件中，用于下次比较使用
        File md5File = new File(downloadDir + File.separator + "md5.txt");
        if (!md5File.exists()) {
          if (!md5File.createNewFile()) {
            logger.error("error create md5 file of cluster {}", cluster.getName());
            return false;
          }
        }
        FileOutputStream fos = new FileOutputStream(md5File);
        fos.write(configFileMD5.getBytes(StandardCharsets.UTF_8));
        fos.close();
      } else {
        return false;
      }
    }

    return true;
  }


  public String read(String clusterName, String path) {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    HadoopClient hadoopClient = hadoopClientMap.get(clusterName);
    hadoopClient.readHDFSFile(path, os);
    String ret = os.toString();
    return ret;
  }


  public List<FilePO> listFolder(String clusterName, String path) throws IOException {
    HadoopClient hadoopClient = hadoopClientMap.get(clusterName);

    return hadoopClient.list(path);
  }


  public void createFolder(String clusterName, String path) throws IOException {
    HadoopClient hadoopClient = hadoopClientMap.get(clusterName);

    hadoopClient.exists(path, true);
    logger.info("createFolder,成功,path: {}",path);

  }

  public void renameFolder(String clusterName, String path, String name) throws IOException {
    HadoopClient hadoopClient = hadoopClientMap.get(clusterName);

    hadoopClient.rename(path, name);
  }

  public void delete(String clusterName, String path) throws IOException {
    HadoopClient hadoopClient = hadoopClientMap.get(clusterName);

    hadoopClient.delete(path);
  }

  public void upload(String clusterName, String src, String dst) throws IOException {
    HadoopClient hadoopClient = hadoopClientMap.get(clusterName);

    hadoopClient.upload(src, dst);
    logger.info("上传成功,path: {}",dst);
  }

  @Async("clusterExecutor")
  public void upload(String clusterName,String dst,String... srcs) throws IOException {
    HadoopClient hadoopClient = hadoopClientMap.get(clusterName);
    hadoopClient.upload( dst,srcs);
    logger.info("上传成功,path: {}",dst);
  }



  public List<YarnAppPO> getYarnApplication(String clusterName, String state) throws YarnException, IOException {
    HadoopClient hadoopClient = hadoopClientMap.get(clusterName);

    if (Strings.isNullOrEmpty(state)) {
      return hadoopClient.getYarnApplication().stream()
          .map(obj -> new YarnAppPO(obj))
          .collect(Collectors.toList());
    }

    return hadoopClient.getYarnApplication(state).stream()
        .map(obj -> new YarnAppPO(obj))
        .collect(Collectors.toList());
  }

  public void killYarnApplication(String clusterName, String appId) throws YarnException, IOException {
    HadoopClient hadoopClient = hadoopClientMap.get(clusterName);

    hadoopClient.killYarnApplication(appId);
  }

  public YarnClusterPO getYarnOverview(String clusterName) throws YarnException, IOException {
    HadoopClient hadoopClient = hadoopClientMap.get(clusterName);

    return hadoopClient.getYarnOverview();
  }


  public HadoopClient getHadoopClient(String clusterName) {
    return hadoopClientMap.get(clusterName);
  }

  public String getYarnState(String clusterName, String id) {
    HadoopClient hadoopClient = hadoopClientMap.get(clusterName);

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
