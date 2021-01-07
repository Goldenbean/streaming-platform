package raptor.streaming.server.repository;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raptor.streaming.dao.mapper.TaskMapper;
import raptor.streaming.dao.entity.TaskEntity;
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
public class TaskServiceImpl extends ServiceImpl<TaskMapper, TaskEntity> implements TaskService {

  @Autowired
  private TaskMapper taskMapper;

  @Autowired
  private HadoopService hadoopService;

  @Override
  public Job get(long id) {

    TaskEntity taskEntity = taskMapper.selectById(id);

    if (taskEntity != null) {
      return toJob(taskEntity);
    }

    return null;
  }


  @Override
  public Job addOrUpdate(String clusterName, Job job) {

//    JobPO jobPO = new JobPO();

    TaskEntity taskEntity = new TaskEntity();

    if (job.getDeployConfig() == null) {
      job.setDeployConfig(new DeployConfig());
    }

    taskEntity.setConfig(BootUtil.toJson(job));

    if (job.getId() > 0) {
      if (get(job.getId()) != null) {
        taskEntity.setId(job.getId());
        taskMapper.updateById(taskEntity);
        return get(taskEntity.getId());
      }
    }

    taskEntity.insert();
    return get(taskEntity.getId());
  }


  private Job toJob(TaskEntity taskEntity) {
    Job job = JSONObject.parseObject(taskEntity.getConfig(), Job.class);
    String clusterName = job.getDeployConfig().getClusterName();
    job.setId(taskEntity.getId());
    job.setYarnStatus(hadoopService.getYarnState(clusterName, job.getYarnId()));
    return job;
  }
}
