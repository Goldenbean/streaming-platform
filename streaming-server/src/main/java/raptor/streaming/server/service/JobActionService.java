package raptor.streaming.server.service;

import raptor.streaming.server.common.entity.Job;
import com.google.common.base.Strings;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import raptor.streaming.hadoop.yarn.DeployConfig;


@Service
public class JobActionService {

  private final static Logger logger = LoggerFactory.getLogger(JobActionService.class);

  private Queue<Long> queue = new LinkedBlockingQueue<>();

  @Autowired
  private DeployService deployService;

  @Autowired
  private JobService jobService;

  @Autowired
  private HadoopService hadoopService;


  @Scheduled(fixedRate = 1000)
  public void update() {
    // logger.trace("deploy scheduled ~");
    // BootUtil.sleep(10);
    internalDeploy("flink-1");
  }


  public void deploy(long id) {
    logger.info("deploy queue add [{}]", id);
    queue.offer(id);
  }

  public void stop( String clusterName,long id) throws IOException, YarnException {
    logger.info("stop [{}]", id);
    Job job = jobService.get(clusterName,id);
    if (job == null) {
      logger.info("cannot find job id [{}]", id);
      return;
    }

    String yarnId = job.getYarnId();
    if (!Strings.isNullOrEmpty(yarnId)) {
      hadoopService.killYarnApplication(clusterName,yarnId);
    }
  }


  private void internalDeploy(String clusterName) {
    Long id = queue.poll();
    if (id == null) {
      // logger.info("queue is empty [{}]", id);
      return;
    }

    Job job = jobService.get(clusterName,id);
    if (job == null) {
      logger.info("cannot find job id [{}]", id);
      return;
    }

    if (!Strings.isNullOrEmpty(job.getYarnId())) {
      //ToDo 代码需要调整，添加集群

      String state = hadoopService.getYarnState(clusterName,job.getYarnId());

      if (!Arrays.asList("FINISHED", "FAILED", "KILLED", "").contains(state)) {
        logger.info("cannot deploy job, caused by state [{}]", state);
        return;
      }
    }

    // 生成单次发布UUID
    String uuid = UUID.randomUUID().toString();
    job.setDeployId(uuid);

    DeployConfig config = job.getDeployConfig();
    String jarPath = config.getJarFilePath();
    int spu = config.getSpu();
    if (spu < 1) {
      spu = 1;
    }

    List<String> args = new ArrayList<>(config.getProgramArgs());
    args.add("--uuid");
    args.add(uuid);

    String applicationName = config.getApplicationName() + "-" + uuid;
    //Iterable<String> argList = Splitter.on(" ").trimResults().split(args);
    try {
      String appId = deployService.deploy(applicationName, jarPath, args, spu);
      logger.info("app id [{}]", appId);
      if (!Strings.isNullOrEmpty(appId)) {
        job.setYarnId(appId);
        job.setYarnStatus("");
        jobService.addOrUpdate(clusterName,job);
      }
    } catch (Exception ex) {

    }

  }

}
