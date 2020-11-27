package raptor.streaming.server.boot.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import raptor.streaming.server.boot.bean.RestResult;
import raptor.streaming.server.boot.constants.Constant;
import raptor.streaming.server.boot.dao.ClusterDao;
import raptor.streaming.server.boot.dao.model.ClusterPO;
import raptor.streaming.server.boot.service.ClusterService;
import raptor.streaming.server.boot.service.HadoopService;
import raptor.streaming.server.domain.Tuple2;


@Service
//@Transactional
public class ClusterServiceImpl implements ClusterService {
    private static final Logger log = LoggerFactory.getLogger(ClusterServiceImpl.class);
  @Autowired
  private ClusterDao clusterDao;

  @Autowired
  private HadoopService hadoopService;

  @Override
  public ClusterPO getCluster(String name) {
    return clusterDao.getCluster(name);
  }

  @Override
  public RestResult addCluster(String name, int type,
      String description,
      String spuConf, MultipartFile file) throws IOException {
    ClusterPO clusterPO = clusterDao.getCluster(name);
    if (clusterPO != null) {
      return new RestResult(false, 409, "集群已存在");
    }
    clusterPO = new ClusterPO();
    clusterPO.setName(name);
    clusterPO.setType(type);
    setFileToDO(file, clusterPO);
    clusterPO.setDescription(description);
    clusterPO.setSpuConf(spuConf);

    if (clusterDao.addCluster(clusterPO)) {

      hadoopService.addCluster(clusterPO);

      String clusterEngineDir = Constant.STREAM_ENGINES_BASE_DIR + File.separator + clusterPO.getName();

      hadoopService.createFolder(clusterPO.getName(), clusterEngineDir);

      hadoopService.upload(clusterPO.getName(),clusterEngineDir,"engines/flink-1/sql","engines/flink-1/system");

      return new RestResult(true, 200, "添加集群成功") ;
    }

    return  new RestResult(false, 200, "添加集群失败") ;
  }

  @Override
  public RestResult updateCluster(String name, int type,
      String description,
      String spuConf, MultipartFile file) throws IOException {
    ClusterPO clusterPO = clusterDao.getCluster(name);
    if (clusterPO == null) {
      return new RestResult(false, 404, "集群不存在");
    }
    setFileToDO(file, clusterPO);
    clusterPO.setType(type);
    clusterPO.setDescription(description);
    clusterPO.setSpuConf(spuConf);

    if (clusterDao.updateCluster(clusterPO)) {
      return new RestResult(true, 200, "更新集群成功");
    }

    return new RestResult(false, 200, "更新集群失败");
  }

  public void updateCluster(ClusterPO clusterPO) {

    clusterDao.updateCluster(clusterPO);

  }


  private void setFileToDO(MultipartFile file, ClusterPO clusterPO)
      throws IOException {
    if (file != null) {
      byte[] data = file.getBytes();
      clusterPO.setConfigFile(data);
      clusterPO.setConfigFileMD5(DigestUtils.md5Hex(data));
      clusterPO.setConfigFileName(file.getOriginalFilename());
    }
  }

  @Override
  public RestResult deleteCluster(String name)  {
    if (clusterDao.deleteCluster(name)) {

      String localConfigPath = Constant.CONFIG_DIR_BASE+ File.separator + name;

      File localConfigDir = new File(localConfigPath);
      String hdfsConfigPath = Constant.STREAM_ENGINES_BASE_DIR+ File.separator + name;

      try {
        FileUtils.deleteDirectory(localConfigDir);
        hadoopService.delete(name,hdfsConfigPath);
        hadoopService.removeHadoopClient(name);
      } catch (IOException e) {
        log.error("", e);
        return new RestResult(false, 500, "删除失败");
      }

      return new RestResult(true, 200, "删除成功");
    }
    return new RestResult(false, 500, "删除失败");
  }

  @Override
  public List<ClusterPO> getAllCluster() {
    return clusterDao.getAllCluster();
  }

  @Override
  public List<ClusterPO> getAllClusterByType(Integer type) {
    return clusterDao.getAllClusterByType(type);
  }
}
