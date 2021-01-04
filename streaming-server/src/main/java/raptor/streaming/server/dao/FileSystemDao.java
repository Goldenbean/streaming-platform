package raptor.streaming.server.dao;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import raptor.streaming.server.entity.FileSystemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 资源管理 Mapper 接口
 * </p>
 *
 * @author azhe
 * @since 2020-12-16
 */
public interface FileSystemDao extends BaseMapper<FileSystemEntity> {

  @Select("select * from dev_file_system where deleted=1")
  List<FileSystemEntity> selectLogicDeleted();


  @Delete("delete from dev_file_system where id = #{id}")
  int removeByLogicId(Long id);

  @Update({"UPDATE dev_file_system SET parent_id=#{parentId}, parent_path=#{parentPath}, deleted=0 WHERE id = #{id}"})
  int updateLogicData(FileSystemEntity fileSystemEntity);

}
