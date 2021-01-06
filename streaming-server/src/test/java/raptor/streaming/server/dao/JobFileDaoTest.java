package raptor.streaming.server.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;
import raptor.streaming.server.entity.JobFileEntity;
import raptor.streaming.server.entity.TaskEntity;

/**
 * Created by azhe on 2021-01-05 16:21
 */
@MapperScan("raptor.streaming.server.boot.dao")
@RunWith(SpringRunner.class)
@SpringBootTest
public class JobFileDaoTest {


  @Autowired
  private JobFileDao jobFileDao;

  @Test
  public void selectAllTest() {

     JobFileEntity jobFileEntity = jobFileDao.selectById(28L);
    System.out.println(jobFileEntity);
  }
}