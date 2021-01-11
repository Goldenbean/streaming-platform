package raptor.streaming.server.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raptor.streaming.common.domain.Job;
import raptor.streaming.common.utils.BootUtil;
import raptor.streaming.dao.entity.Task;
import raptor.streaming.dao.mapper.TaskMapper;
import raptor.streaming.hadoop.yarn.DeployConfig;


@Service
public class TaskService {

  @Autowired
  private TaskMapper taskMapper;

  @Autowired
  private HadoopService hadoopService;


  public Job get(long id) {

    Task task = taskMapper.selectById(id);

    if (task != null) {
      return toJob(task);
    }

    return null;
  }


  public Job addOrUpdate(String clusterName, Job job) {

//    JobPO jobPO = new JobPO();

    Task task = new Task();

    if (job.getDeployConfig() == null) {
      job.setDeployConfig(new DeployConfig());
    }

    task.setConfig(BootUtil.toJson(job));

    if (job.getId() > 0) {
      if (get(job.getId()) != null) {
        task.setId(job.getId());
        taskMapper.updateById(task);
        return get(task.getId());
      }
    }

    task.insert();
    return get(task.getId());
  }


  private Job toJob(Task task) {
    Job job = JSONObject.parseObject(task.getConfig(), Job.class);
    String clusterName = job.getDeployConfig().getClusterName();
    job.setId(task.getId());
    job.setYarnStatus(hadoopService.getYarnState(clusterName, job.getYarnId()));
    return job;
  }
}
