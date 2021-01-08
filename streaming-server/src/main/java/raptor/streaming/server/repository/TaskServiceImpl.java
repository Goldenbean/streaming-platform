package raptor.streaming.server.repository;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raptor.streaming.dao.mapper.TaskMapper;
import raptor.streaming.dao.entity.Task;
import raptor.streaming.hadoop.yarn.DeployConfig;
import raptor.streaming.common.domain.Job;
import raptor.streaming.server.service.HadoopService;
import raptor.streaming.common.utils.BootUtil;

/**
 * <p>
 * 任务列表 服务实现类
 * </p>
 *
 * @author azhe
 * @since 2021-01-04
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {

  @Autowired
  private TaskMapper taskMapper;

  @Autowired
  private HadoopService hadoopService;

  @Override
  public Job get(long id) {

    Task task = taskMapper.selectById(id);

    if (task != null) {
      return toJob(task);
    }

    return null;
  }


  @Override
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
