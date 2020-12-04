package raptor.streaming.server.dao;

import raptor.streaming.server.entity.JobPO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface JobDao {

  List<JobPO> queryAll();

  JobPO get(@Param("id") long id);

  boolean add(JobPO jobPO);

  boolean update(JobPO jobPO);

  boolean delete(@Param("id") long id);


}
