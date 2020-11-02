package raptor.streaming.server.boot.control;

import raptor.streaming.server.boot.BootUtil;
import raptor.streaming.server.boot.bean.DataResult;
import raptor.streaming.server.boot.bean.RestResult;
import raptor.streaming.server.boot.service.JobService;
import raptor.streaming.server.domain.Job;
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
@RequestMapping(value = "/api/v1/job/config")
public class JobConfigControl {

  private static final Logger logger = LoggerFactory.getLogger(JobConfigControl.class);

  @Autowired
  private JobService jobService;

  @GetMapping(value = "/")
  public RestResult getAll() {
    return new DataResult<>(jobService.getAll());
  }

  @GetMapping(value = "/{id}")
  public RestResult get(@PathVariable("id") long id) {
    return new DataResult<>(jobService.get(id));
  }

  @DeleteMapping(value = "/{id}")
  public RestResult delete(@PathVariable("id") long id) {
    boolean ret = jobService.remove(id);
    return new RestResult(ret, 200, "delete element");
  }

  @PostMapping(value = "/")
  public RestResult addOrUpdate(@RequestBody Job job) {

    try {
      logger.debug("addOrUpdate [{}]", BootUtil.toJson(job));
      if (job.getDeployConfig() == null) {
        logger.info("job deploy config is null");
      }
      Job ret = jobService.addOrUpdate(job);
      return new DataResult(ret);
    } catch (Exception ex) {
      return RestResult.getFailed();
    }
  }


}
