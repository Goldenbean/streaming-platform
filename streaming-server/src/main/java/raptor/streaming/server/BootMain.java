package raptor.streaming.server;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@MapperScan("raptor.streaming.server.boot.dao")
@ImportResource(locations = {"classpath*:spring/spring-*.xml"})
@SpringBootApplication
public class BootMain {

  private final static Logger logger = LoggerFactory.getLogger(BootMain.class);

  public static void main(String[] args) throws Exception {
    SpringApplication.run(BootMain.class, args);
  }


}

