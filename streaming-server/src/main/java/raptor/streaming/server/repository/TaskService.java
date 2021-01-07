package raptor.streaming.server.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import raptor.streaming.dao.entity.TaskEntity;
import raptor.streaming.common.domain.Job;

/**
 * <p>
 * 任务列表 服务类
 * </p>
 *
 * @author azhe
 * @since 2021-01-04
 */
public interface TaskService extends IService<TaskEntity> {

  public Job addOrUpdate(String clusterName, Job job);

  public Job get(long id);
}
