package raptor.streaming.server.service;

import com.google.common.base.Strings;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.flink.client.program.ClusterClientProvider;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import raptor.streaming.hadoop.HadoopClient;
import raptor.streaming.hadoop.yarn.ApplicationDeploy;
import raptor.streaming.hadoop.yarn.DeployConfig;
import raptor.streaming.server.common.entity.Job;


@Service
public class TaskActionService {

  private final static Logger logger = LoggerFactory.getLogger(TaskActionService.class);

  private Queue<Long> queue = new LinkedBlockingQueue<>();

  @Autowired
  private TaskService taskService;

  @Autowired
  private HadoopService hadoopService;


  @Scheduled(fixedRate = 1000)
  public void update() {
    // logger.trace("deploy scheduled ~");
    // BootUtil.sleep(10);
    internalDeploy();
  }


  public void deploy(long id) {
    logger.info("deploy queue add [{}]", id);
    queue.offer(id);
  }

  public void stop(String clusterName, long id) throws IOException, YarnException {
    logger.info("stop [{}]", id);
    Job job = taskService.get(id);
    if (job == null) {
      logger.info("cannot find job id [{}]", id);
      return;
    }

    String yarnId = job.getYarnId();
    if (!Strings.isNullOrEmpty(yarnId)) {
      hadoopService.killYarnApplication(clusterName, yarnId);
    }
  }


  private void internalDeploy() {
    Long id = queue.poll();
    if (id == null) {
      // logger.info("queue is empty [{}]", id);
      return;
    }

    Job job = taskService.get(id);
    if (job == null) {
      logger.info("cannot find job id [{}]", id);
      return;
    }

    String clusterName = job.getDeployConfig().getClusterName();
    if (!Strings.isNullOrEmpty(job.getYarnId())) {
      //ToDo 代码需要调整，添加集群

      String state = hadoopService.getYarnState(clusterName, job.getYarnId());

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

    HadoopClient hadoopClient = hadoopService.getHadoopClient(clusterName);

    FileSystem fileSystem = hadoopClient.getFileSystem();
    String path = fileSystem.getUri().toString();

    String engineDirs=path+"/streaming-platform/flink-cluster/engines/"+clusterName+"/system";
//    String engineDirs=path+"/streaming-platform/flink-cluster/engines/"+clusterName;
//    String engineDirs=path+"/streaming-platform/flink-cluster/engines/"+clusterName+"/sql "+path+"/streaming-platform/flink-cluster/engines/"+clusterName+"/system";

    String hadoopConfigPath = hadoopClient.getHadoopConfigPath();
    try {

      logger.debug("[{}] [{}] [{}]", applicationName, jarPath, args);

      ApplicationDeploy applicationDeploy = new ApplicationDeploy(hadoopConfigPath, hadoopConfigPath);

      ClusterClientProvider<ApplicationId> ret =
          applicationDeploy.deploy(applicationName, jarPath, args, spu,engineDirs);

      String appId = ret.getClusterClient().getClusterId().toString();

      logger.info("app id [{}]", appId);
      if (!Strings.isNullOrEmpty(appId)) {
        job.setYarnId(appId);
        job.setYarnStatus("");
        taskService.addOrUpdate(clusterName, job);
      }
    } catch (Exception ex) {
      ex.printStackTrace();

    }

  }

}
