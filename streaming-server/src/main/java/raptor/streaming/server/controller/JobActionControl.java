package raptor.streaming.server.controller;

import raptor.streaming.server.common.entity.RestResult;
import raptor.streaming.server.constants.Constant;
import raptor.streaming.server.service.JobActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = Constant.API_PREFIX_URI +"/job/action")
public class JobActionControl {

  private static final Logger logger = LoggerFactory.getLogger(JobActionControl.class);

  @Autowired
  private JobActionService jobActionService;

  @GetMapping(value = "/start")
  public RestResult start(@RequestParam("id") long id) {

    try {
      jobActionService.deploy(id);
      return RestResult.getSuccess();
    } catch (Exception ex) {
      logger.error("", ex);
      return RestResult.getFailed();
    }
  }

  @GetMapping(value = "/stop")
  public RestResult stop(@RequestParam("name") String clusterName,@RequestParam("id") long id) {

    try {
      jobActionService.stop(clusterName,id);
      return RestResult.getSuccess();
    } catch (Exception ex) {
      logger.error("", ex);
      return RestResult.getFailed();
    }
  }


}
