package raptor.streaming.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import raptor.streaming.dao.entity.ProjectEntity;

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
