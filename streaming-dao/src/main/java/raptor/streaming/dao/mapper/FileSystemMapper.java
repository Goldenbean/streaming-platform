package raptor.streaming.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raptor.streaming.dao.entity.FileSystem;

/**
 * <p>
 * 资源管理 Mapper 接口
 * </p>
 *
 * @author azhe
 * @since 2020-12-16
 */
public interface FileSystemMapper extends BaseMapper<FileSystem> {

  @Select("select * from dev_file_system where deleted=1")
  List<FileSystem> selectLogicDeleted();


  @Delete("delete from dev_file_system where id = #{id}")
  int removeByLogicId(Long id);

  @Update({
      "UPDATE dev_file_system SET parent_id=#{parentId}, parent_path=#{parentPath}, deleted=0 WHERE id = #{id}"})
  int updateLogicData(FileSystem fileSystem);

}
