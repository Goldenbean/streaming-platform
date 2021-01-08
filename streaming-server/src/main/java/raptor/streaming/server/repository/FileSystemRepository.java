package raptor.streaming.server.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import raptor.streaming.dao.mapper.FileSystemMapper;
import raptor.streaming.dao.entity.FileSystem;

@Repository
public class FileSystemRepository extends ServiceImpl<FileSystemMapper, FileSystem> implements
    IService<FileSystem> {

  @Autowired
  private FileSystemMapper fileSystemMapper;


  public List<FileSystem> selectLogicDeleted() {
    return fileSystemMapper.selectLogicDeleted();
  }


  public int removeByLogicId(Long id) {
    return fileSystemMapper.removeByLogicId(id);
  }


  public int updateLogicData(FileSystem fileSystem) {
    return fileSystemMapper.updateLogicData(fileSystem);
  }


}
