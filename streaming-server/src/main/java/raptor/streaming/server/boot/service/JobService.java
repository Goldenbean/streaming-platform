package raptor.streaming.server.boot.service;

import com.alibaba.fastjson.JSONObject;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raptor.streaming.hadoop.yarn.DeployConfig;
import raptor.streaming.server.boot.BootUtil;
import raptor.streaming.server.boot.dao.JobDao;
import raptor.streaming.server.boot.dao.model.JobPO;
import raptor.streaming.server.domain.Job;

@Service
public class JobService {

  private final static Logger logger = LoggerFactory.getLogger(JobService.class);

  @Autowired
  private JobDao jobDao;

  @Autowired
  private HadoopService hadoopService;

  public JobService() {

  }

  public Job get(long id) {

    JobPO jobPO = jobDao.get(id);

    if (jobPO != null) {
      return toJob(jobPO);
    }

    return null;
  }

  public List<Job> getAll() {

    List<JobPO> ret = jobDao.queryAll();

    return ret.stream()
        .map(obj -> toJob(obj))
        .collect(Collectors.toList());

  }

  public Job addOrUpdate(Job job) {

    JobPO jobPO = new JobPO();

    if (job.getDeployConfig() == null) {
      job.setDeployConfig(new DeployConfig());
    }

    jobPO.setConfig(BootUtil.toJson(job));

    if (job.getId() > 0) {
      if (get(job.getId()) != null) {
        jobPO.setId(job.getId());
        boolean ret = jobDao.update(jobPO);
        return get(jobPO.getId());
      }
    }

    boolean ret = jobDao.add(jobPO);
    return get(jobPO.getId());
  }

  public boolean remove(long id) {
    if (id < 1) {
      return false;
    }

    return jobDao.delete(id);
  }


  private Job toJob(JobPO jobPO) {
    Job job = JSONObject.parseObject(jobPO.getConfig(), Job.class);
    job.setId(jobPO.getId());
    job.setYarnStatus(hadoopService.getYarnState(job.getYarnId()));
    return job;
  }
}
