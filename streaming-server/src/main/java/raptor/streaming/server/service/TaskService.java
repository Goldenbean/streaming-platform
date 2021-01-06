package raptor.streaming.server.service;

import raptor.streaming.server.common.entity.Job;
import raptor.streaming.server.entity.TaskEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 任务列表 服务类
 * </p>
 *
 * @author azhe
 * @since 2021-01-04
 */
public interface TaskService extends IService<TaskEntity> {
  public Job addOrUpdate(String clusterName,Job job);

  public Job get( long id);
}
