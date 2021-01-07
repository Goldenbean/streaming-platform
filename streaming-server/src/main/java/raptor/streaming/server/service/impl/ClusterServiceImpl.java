package raptor.streaming.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.io.File;
import java.io.IOException;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import raptor.streaming.server.common.entity.RestResult;
import raptor.streaming.server.common.constants.Constant;
import raptor.streaming.server.dao.ClusterDao;
import raptor.streaming.server.service.HadoopService;
import raptor.streaming.server.entity.ClusterEntity;
import raptor.streaming.server.service.ClusterService;

/**
 * <p>
 * 集群列表 服务实现类
 * </p>
 *
 * @author azhe
 * @since 2020-12-02
 */
@Service
public class ClusterServiceImpl extends ServiceImpl<ClusterDao, ClusterEntity> implements ClusterService {

  @Autowired
  private ClusterDao clusterDao;

  @Autowired
  private HadoopService hadoopService;


  @Override
  public RestResult addCluster(String name, int type,
      String remark, String spuConf, MultipartFile file) throws IOException {

    ClusterEntity cluster = clusterDao.selectOne(new QueryWrapper<ClusterEntity>().lambda().eq(ClusterEntity::getName, name));

    if (cluster != null) {
      return new RestResult(false, 409, "集群已存在");
    }
    cluster = new ClusterEntity();
    cluster.setName(name);
    cluster.setType(type);
    setFileToDO(file, cluster);
    cluster.setRemark(remark);
    cluster.setSpuConf(spuConf);

    if (cluster.insert()) {

      hadoopService.addCluster(cluster);
      String clusterEngineDir = Constant.STREAM_ENGINES_BASE_DIR + File.separator + cluster.getName();
      hadoopService.createFolder(name, clusterEngineDir);
      hadoopService.upload(name,clusterEngineDir,"engines/flink-1.11.2/sql","engines/flink-1.11.2/system");
      return new RestResult(true, 200, "添加集群成功") ;
    }

    return  new RestResult(false, 200, "添加集群失败") ;
  }

  @Override
  public RestResult updateCluster(String name, int type,
      String remark,
      String spuConf, MultipartFile file) throws IOException {
    ClusterEntity cluster = clusterDao.selectOne(new QueryWrapper<ClusterEntity>().lambda().eq(ClusterEntity::getName, name));
    if (cluster == null) {
      return new RestResult(false, 404, "集群不存在");
    }
    setFileToDO(file, cluster);
    cluster.setType(type);
    cluster.setRemark(remark);
    cluster.setSpuConf(spuConf);

    if (clusterDao.updateById(cluster)>0) {
      return new RestResult(true, 200, "更新集群成功");
    }
    return new RestResult(false, 200, "更新集群失败");
  }

  private void setFileToDO(MultipartFile file, ClusterEntity cluster)
      throws IOException {
    if (file != null) {
      byte[] data = file.getBytes();
      cluster.setConfigFile(data);
      cluster.setConfigFileMd5(DigestUtils.md5Hex(data));
      cluster.setConfigFileName(file.getOriginalFilename());
    }
  }



}
