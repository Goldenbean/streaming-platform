package raptor.streaming.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import raptor.streaming.hadoop.yarn.DeployConfig;
import raptor.streaming.server.common.entity.Job;
import raptor.streaming.server.dao.FileSystemDao;
import raptor.streaming.server.entity.JobPO;
import raptor.streaming.server.entity.TaskEntity;
import raptor.streaming.server.dao.TaskDao;
import raptor.streaming.server.service.HadoopService;
import raptor.streaming.server.service.TaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import raptor.streaming.server.utils.BootUtil;

/**
 * <p>
 * 任务列表 服务实现类
 * </p>
 *
 * @author azhe
 * @since 2021-01-04
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskDao, TaskEntity> implements TaskService {

  @Autowired
  private TaskDao taskDao;

  @Autowired
  private HadoopService hadoopService;

  @Override
  public Job get(long id) {

    TaskEntity taskEntity = taskDao.selectById(id);

    if (taskEntity != null) {
      return toJob( taskEntity);
    }

    return null;
  }


  @Override
  public Job addOrUpdate(String clusterName,Job job) {

//    JobPO jobPO = new JobPO();

    TaskEntity taskEntity = new TaskEntity();

    if (job.getDeployConfig() == null) {
      job.setDeployConfig(new DeployConfig());
    }

    taskEntity.setConfig(BootUtil.toJson(job));

    if (job.getId() > 0) {
      if (get(job.getId()) != null) {
        taskEntity.setId(job.getId());
        taskDao.updateById(taskEntity);
        return get(taskEntity.getId());
      }
    }

    taskEntity.insert();
    return get(taskEntity.getId());
  }


  private Job toJob( TaskEntity taskEntity) {
    Job job = JSONObject.parseObject(taskEntity.getConfig(), Job.class);
    String clusterName = job.getDeployConfig().getClusterName();
    job.setId(taskEntity.getId());
    job.setYarnStatus(hadoopService.getYarnState(clusterName, job.getYarnId()));
    return job;
  }
}
