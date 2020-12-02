package raptor.streaming.server.controller;

import org.springframework.web.bind.annotation.RequestParam;
import raptor.streaming.server.utils.BootUtil;
import raptor.streaming.server.common.entity.DataResult;
import raptor.streaming.server.common.entity.RestResult;
import raptor.streaming.server.constants.Constant;
import raptor.streaming.server.service.JobService;
import raptor.streaming.server.common.entity.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = Constant.API_PREFIX_URI + "/job/config")
public class JobConfigControl {

  private static final Logger logger = LoggerFactory.getLogger(JobConfigControl.class);

  @Autowired
  private JobService jobService;

  @GetMapping(value = "/")
  public RestResult getAll(String clusterName) {
    return new DataResult<>(jobService.getAll(clusterName));
  }

  @GetMapping(value = "/{id}")
  public RestResult get(@RequestParam("name") String clusterName,@PathVariable("id") long id) {
    return new DataResult<>(jobService.get(clusterName,id));
  }

  @DeleteMapping(value = "/{id}")
  public RestResult delete(@PathVariable("id") long id) {
    boolean ret = jobService.remove(id);
    return new RestResult(ret, 200, "delete element");
  }

  @PostMapping(value = "/")
  public RestResult addOrUpdate(@RequestParam("name") String clusterName,@RequestBody Job job) {

    try {
      logger.debug("addOrUpdate [{}]", BootUtil.toJson(job));
      if (job.getDeployConfig() == null) {
        logger.info("job deploy config is null");
      }
      Job ret = jobService.addOrUpdate(clusterName,job);
      return new DataResult(ret);
    } catch (Exception ex) {
      return RestResult.getFailed();
    }
  }


}
