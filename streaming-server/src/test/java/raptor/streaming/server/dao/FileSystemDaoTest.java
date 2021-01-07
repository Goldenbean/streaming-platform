package raptor.streaming.server.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import raptor.streaming.dao.mapper.FileSystemDao;
import raptor.streaming.dao.entity.FileSystemEntity;


@TestMethodOrder(MethodOrderer.Alphanumeric.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class FileSystemDaoTest {

  @Resource
  private FileSystemDao fileSystemDao;

  @Test
  public void test(){
    List<FileSystemEntity> fileSystemEntities1 = fileSystemDao.selectList(Wrappers.<FileSystemEntity>query());

    fileSystemEntities1.forEach(i-> System.out.println(i));
    List<FileSystemEntity> fileSystemEntities2 = fileSystemDao.selectLogicDeleted();
    fileSystemEntities2.forEach(i-> System.out.println(i));

  }


}