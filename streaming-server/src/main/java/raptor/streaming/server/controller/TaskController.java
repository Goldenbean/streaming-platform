package raptor.streaming.server.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import raptor.streaming.dao.entity.TaskEntity;
import raptor.streaming.hadoop.yarn.DeployConfig;
import raptor.streaming.common.constants.Constant;
import raptor.streaming.common.domain.CustomPage;
import raptor.streaming.common.domain.Job;
import raptor.streaming.common.http.DataResult;
import raptor.streaming.common.http.RestResult;
import raptor.streaming.server.service.HadoopService;
import raptor.streaming.server.service.TaskActionService;
import raptor.streaming.server.repository.TaskService;
import raptor.streaming.common.utils.BootUtil;


@RestController
@RequestMapping(value = Constant.API_PREFIX_URI + "/task")
@Api(tags = "任务管理")
public class TaskController {

  private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

  @Autowired
  private TaskService taskService;

  @Autowired
  private HadoopService hadoopService;

  private String clusterName = "flink-2";


  @Autowired
  private TaskActionService taskActionService;

  @GetMapping(value = "/start")
  public RestResult start(@RequestParam("id") long id) {

    try {
      taskActionService.deploy(id);
      return RestResult.getSuccess();
    } catch (Exception ex) {
      logger.error("", ex);
      return RestResult.getFailed();
    }
  }

  @GetMapping(value = "/stop")
  public RestResult stop(@RequestParam("name") String clusterName, @RequestParam("id") long id) {

    try {
      taskActionService.stop(clusterName, id);
      return RestResult.getSuccess();
    } catch (Exception ex) {
      logger.error("", ex);
      return RestResult.getFailed();
    }
  }


  @ApiOperation(value = "分页获取任务列表")
  @GetMapping(value = "/list")
  public DataResult<CustomPage<Job>> list(
      @RequestParam(value = "curPage", required = false, defaultValue = "1") Integer curPage,
      @RequestParam(value = "pageSize", required = false, defaultValue = "10"
      ) Integer pageSize
  ) {

    Page<TaskEntity> page = taskService.page(new Page<>(curPage, pageSize));

    List<Job> collect = page.getRecords().stream()
        .map(obj -> toJob(clusterName, obj))
        .collect(Collectors.toList());

    Page<Job> newPage = new Page<Job>(curPage, pageSize, page.getTotal());
    newPage.setRecords(collect);
    newPage.setPages(page.getPages());

    return new DataResult<>(new CustomPage<>(newPage));
  }


  @ApiOperation(value = "获取作业文件")
  @GetMapping(value = "/get")
  public RestResult get(
      @RequestParam(value = "id") Long id) {

    Job job = taskService.get(id);

    if (job != null) {
      return new DataResult<>(job);
    } else {
      return RestResult.getFailed();
    }
  }

  @ApiOperation(value = "保存任务")
  @PostMapping(value = "/save")
  public RestResult save(@RequestBody TaskEntity taskEntity) {
    if (taskService.saveOrUpdate(taskEntity)) {
      return RestResult.getSuccess();
    } else {
      return RestResult.getFailed();
    }
  }


  @ApiOperation(value = "发布任务")
  @GetMapping(value = "/publish")
  public RestResult publish(
      @RequestParam(value = "appName") String appName,
      @RequestParam(value = "clusterName") String clusterName,
      @RequestParam(value = "content") String content
  ) {

    Stream<String> stringStream = Arrays.stream(content.split("\n"))
        .filter(line -> line.contains("=") && !line.startsWith("--"));

    DeployConfig deployConfig = new DeployConfig();

    stringStream.forEach(elem -> {
      final String[] kv = elem.split("=", 2);
      if (kv.length == 2) {

        switch (kv[0]) {
          case "entryPointClass":
            deployConfig.setEntryPointClass(kv[1]);
            break;
          case "jarFilePath":
            deployConfig.setJarFilePath(kv[1]);
            break;
          case "programArgs":
            deployConfig.setProgramArgs(Lists.newArrayList(kv[1].split(",")));
            break;
          case "spu":
            deployConfig.setSpu(Integer.parseInt(kv[1]));
            break;
        }
      }

    });

    deployConfig.setApplicationName(appName);
    deployConfig.setClusterName(clusterName);

    Job job = new Job();
    job.setDeployConfig(deployConfig);

    String config = BootUtil.toJson(job);
    final TaskEntity taskEntity = new TaskEntity();
    taskEntity.setConfig(config);
    if (taskService.save(taskEntity)) {
      return RestResult.getSuccess();
    } else {
      return RestResult.getFailed();
    }
  }

  @DeleteMapping(value = "/{name}/")
  public RestResult delete(@PathVariable("name") String name,
      @RequestParam(value = "id", required = true) long id) {
    if (taskService.removeById(id)) {
      return RestResult.getSuccess();
    } else {
      return RestResult.getFailed();
    }
  }

  @PostMapping(value = "/")
  public RestResult addOrUpdate(@RequestParam("name") String clusterName, @RequestBody Job job) {

    try {
      logger.debug("addOrUpdate [{}]", BootUtil.toJson(job));
      if (job.getDeployConfig() == null) {
        logger.info("job deploy config is null");
      }
      Job ret = taskService.addOrUpdate(clusterName, job);
      return new DataResult<>(ret);
    } catch (Exception ex) {
      return RestResult.getFailed();
    }
  }

  private Job toJob(String clusterName, TaskEntity taskEntity) {
    Job job = JSONObject.parseObject(taskEntity.getConfig(), Job.class);
    job.setId(taskEntity.getId());
    job.setYarnStatus(hadoopService.getYarnState(clusterName, job.getYarnId()));
    return job;
  }
}

