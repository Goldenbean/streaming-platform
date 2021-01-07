package raptor.streaming.dao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import raptor.streaming.dao.entity.ProjectEntity;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author azhe
 * @since 2020-12-03
 */
public interface ProjectService extends IService<ProjectEntity> {

  String selectClusterNameByPid(Long id);

}
