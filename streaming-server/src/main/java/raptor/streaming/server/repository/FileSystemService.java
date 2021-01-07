package raptor.streaming.server.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import raptor.streaming.dao.entity.FileSystemEntity;

/**
 * <p>
 * 资源管理 服务类
 * </p>
 *
 * @author azhe
 * @since 2020-12-16
 */
public interface FileSystemService extends IService<FileSystemEntity> {

  List<FileSystemEntity> selectLogicDeleted();

  int removeByLogicId(Long id);

  int updateLogicData(FileSystemEntity fileSystemEntity);

}
