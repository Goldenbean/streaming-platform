package raptor.streaming.server.boot.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import raptor.streaming.server.boot.dao.model.ClusterPO;

/**
 * Created by azhe on 2020-11-17 14:56
 */
public interface ClusterDao {
  ClusterPO getCluster(@Param("name") String name);

  boolean addCluster(ClusterPO ClusterPO);

  boolean updateCluster(ClusterPO ClusterPO);

  boolean updateClusterConf(@Param("name")String name,@Param("clusterConf")String clusterConf);

  boolean deleteCluster(@Param("name") String name);

  List<ClusterPO> getAllCluster();

  List<ClusterPO> getAllClusterByType(@Param("type") Integer type);

}
