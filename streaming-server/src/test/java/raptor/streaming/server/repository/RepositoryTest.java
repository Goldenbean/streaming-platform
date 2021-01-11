package raptor.streaming.server.repository;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import raptor.streaming.dao.entity.Task;
import raptor.streaming.server.repository.TaskRepository;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoryTest {


  @Autowired
  private TaskRepository taskRepository;


  @Test
  public void taskRepositoryTest() {

    List<Task> userList = taskRepository.list();
    Assert.assertEquals(1, userList.size());
    userList.forEach(System.out::println);
  }

}