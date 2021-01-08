package raptor.streaming.server.controller;


import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import raptor.streaming.hadoop.bean.YarnAppPO;
import raptor.streaming.common.constants.Constant;
import raptor.streaming.common.utils.http.DataResult;
import raptor.streaming.common.utils.http.RestResult;
import raptor.streaming.server.service.HadoopService;

@RestController
@RequestMapping(value = Constant.API_PREFIX_URI + "/yarn")
public class YarnControl {

  private static final Logger logger = LoggerFactory.getLogger(FileControl.class);

  @Autowired
  private HadoopService hadoopService;

  @GetMapping(value = "/")
  public RestResult get(@RequestParam("name") String clusterName,
      @RequestParam(value = "state", required = false, defaultValue = "") String state) {
    try {
      List<YarnAppPO> ret = hadoopService.getYarnApplication(clusterName, state);
      return new DataResult<>(ret);
    } catch (Exception ex) {
      logger.error("", ex);
      return RestResult.getFailed();
    }
  }

  @GetMapping(value = "/action/stop")
  public RestResult actionStop(@RequestParam("name") String clusterName,
      @RequestParam("id") String id) {
    try {
      hadoopService.killYarnApplication(clusterName, id);
      return RestResult.getSuccess();
    } catch (Exception ex) {
      logger.error("", ex);
      return RestResult.getFailed();
    }
  }


}
