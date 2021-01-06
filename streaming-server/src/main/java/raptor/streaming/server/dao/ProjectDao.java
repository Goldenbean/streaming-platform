package raptor.streaming.server.dao;

import java.util.List;
import org.apache.ibatis.annotations.Select;
import raptor.streaming.server.entity.FileSystemEntity;
import raptor.streaming.server.entity.ProjectEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author azhe
 * @since 2020-12-03
 */
public interface ProjectDao extends BaseMapper<ProjectEntity> {

  @Select("select c.name from sys_cluster c,sys_project p  where c.id=p.cluster_id and p.id=#{id}")
  String selectClusterNameByPid(Long id);

}
