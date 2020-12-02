package raptor.streaming.server.service;

import com.alibaba.fastjson.JSONObject;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raptor.streaming.hadoop.yarn.DeployConfig;
import raptor.streaming.server.utils.BootUtil;
import raptor.streaming.server.dao.JobDao;
import raptor.streaming.server.entity.JobPO;
import raptor.streaming.server.common.entity.Job;

@Service
public class JobService {

  private final static Logger logger = LoggerFactory.getLogger(JobService.class);

  @Autowired
  private JobDao jobDao;

  @Autowired
  private HadoopService hadoopService;

  public JobService() {

  }

  public Job get(String clusterName, long id) {

    JobPO jobPO = jobDao.get(id);

    if (jobPO != null) {
      return toJob(clusterName, jobPO);
    }

    return null;
  }

  public List<Job> getAll(String clusterName) {

    List<JobPO> ret = jobDao.queryAll();

    return ret.stream()
        .map(obj -> toJob(clusterName, obj))
        .collect(Collectors.toList());

  }

  public Job addOrUpdate(String clusterName,Job job) {

    JobPO jobPO = new JobPO();

    if (job.getDeployConfig() == null) {
      job.setDeployConfig(new DeployConfig());
    }

    jobPO.setConfig(BootUtil.toJson(job));

    if (job.getId() > 0) {
      if (get(clusterName,job.getId()) != null) {
        jobPO.setId(job.getId());
        boolean ret = jobDao.update(jobPO);
        return get(clusterName,jobPO.getId());
      }
    }

    boolean ret = jobDao.add(jobPO);
    return get(clusterName,jobPO.getId());
  }

  public boolean remove(long id) {
    if (id < 1) {
      return false;
    }

    return jobDao.delete(id);
  }


  private Job toJob(String clusterName, JobPO jobPO) {
    Job job = JSONObject.parseObject(jobPO.getConfig(), Job.class);
    job.setId(jobPO.getId());
    job.setYarnStatus(hadoopService.getYarnState(clusterName, job.getYarnId()));
    return job;
  }
}
