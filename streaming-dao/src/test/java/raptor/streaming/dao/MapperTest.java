package raptor.streaming.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import raptor.streaming.dao.entity.FileSystem;
import raptor.streaming.dao.entity.JobFile;
import raptor.streaming.dao.entity.Task;
import raptor.streaming.dao.mapper.FileSystemMapper;
import raptor.streaming.dao.mapper.JobFileMapper;
import raptor.streaming.dao.mapper.TaskMapper;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class MapperTest {

  @Resource
  private FileSystemMapper fileSystemMapper;


  @Autowired
  private JobFileMapper jobFileMapper;

  @Autowired
  private TaskMapper taskMapper;

  @Test
  public void selectAllTest() {

    JobFile jobFile = jobFileMapper.selectById(28L);
    System.out.println(jobFile);
  }


  @Test
  public void test(){
    List<FileSystem> fileSystemEntities1 = fileSystemMapper.selectList(Wrappers.<FileSystem>query());

    fileSystemEntities1.forEach(i-> System.out.println(i));
    List<FileSystem> fileSystemEntities2 = fileSystemMapper.selectLogicDeleted();
    fileSystemEntities2.forEach(i-> System.out.println(i));

  }


  @Test
  public void taskMapperTest(){

    List<Task> userList = taskMapper.selectList(null);
    Assert.assertEquals(1, userList.size());
    //userList.forEach(System.out::println);
  }

}