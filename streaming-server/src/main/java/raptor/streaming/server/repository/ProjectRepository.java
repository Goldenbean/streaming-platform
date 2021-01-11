package raptor.streaming.server.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import raptor.streaming.dao.entity.Project;
import raptor.streaming.dao.mapper.ProjectMapper;

@Repository
public class ProjectRepository extends ServiceImpl<ProjectMapper, Project> implements
    IService<Project> {

  @Autowired
  private ProjectMapper projectMapper;


  public String selectClusterNameByPid(Long id) {
    return projectMapper.selectClusterNameByPid(id);
  }
}
