package raptor.streaming.server.repository;

import raptor.streaming.server.entity.JobPO;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JobRepository {

  private final static Logger logger = LoggerFactory.getLogger(JobRepository.class);


  private static final String querySql = "select * from job";

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public List<JobPO> query() {
    List<Map<String, Object>> ret = jdbcTemplate.queryForList(querySql);

    return ret.stream().map(m -> {
      JobPO obj = new JobPO();
      obj.setId(Integer.parseInt(String.valueOf(m.get("id"))));

//      cd.setGmtCreate();
//      cd.setGmtModify();
      return obj;
    }).collect(Collectors.toList());

  }

}
