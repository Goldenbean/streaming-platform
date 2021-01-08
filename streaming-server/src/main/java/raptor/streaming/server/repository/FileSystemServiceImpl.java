package raptor.streaming.server.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raptor.streaming.dao.mapper.FileSystemMapper;
import raptor.streaming.dao.entity.FileSystemEntity;

/**
 * <p>
 * 资源管理 服务实现类
 * </p>
 *
 * @author azhe
 * @since 2020-12-16
 */
@Service
public class FileSystemServiceImpl extends ServiceImpl<FileSystemMapper, FileSystemEntity> implements
    FileSystemService {

  @Autowired
  private FileSystemMapper fileSystemMapper;

  @Override
  public List<FileSystemEntity> selectLogicDeleted() {
    return fileSystemMapper.selectLogicDeleted();
  }

  @Override
  public int removeByLogicId(Long id) {
    return fileSystemMapper.removeByLogicId(id);
  }

  @Override
  public int updateLogicData(FileSystemEntity fileSystemEntity) {
    return fileSystemMapper.updateLogicData(fileSystemEntity);
  }


}
