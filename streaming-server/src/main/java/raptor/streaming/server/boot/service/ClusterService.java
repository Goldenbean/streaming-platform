package raptor.streaming.server.boot.service;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import raptor.streaming.server.boot.bean.RestResult;
import raptor.streaming.server.boot.dao.model.ClusterPO;
import raptor.streaming.server.domain.Tuple2;

public interface ClusterService {

  ClusterPO getCluster(String name);

  RestResult addCluster(String name, int type,
      String description,
      String spuConf,
      MultipartFile file) throws IOException;

  RestResult updateCluster(String name, int type, String description,
      String spuConf,
      MultipartFile file) throws IOException;

  void updateCluster(ClusterPO clusterPO);

  RestResult deleteCluster(String name) ;

  List<ClusterPO> getAllCluster();


  List<ClusterPO> getAllClusterByType(Integer type);

}
