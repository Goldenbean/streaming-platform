package raptor.streaming.dao.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raptor.streaming.dao.mapper.ProjectMapper;
import raptor.streaming.dao.entity.ProjectEntity;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author azhe
 * @since 2020-12-03
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, ProjectEntity> implements
    ProjectService {

  @Autowired
  private ProjectMapper projectMapper;

  @Override
  public String selectClusterNameByPid(Long id) {
    return projectMapper.selectClusterNameByPid(id);
  }
}
