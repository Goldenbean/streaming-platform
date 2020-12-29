package raptor.streaming.server.controller;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import raptor.streaming.server.common.constants.Constant;
import raptor.streaming.server.common.entity.DataResult;
import raptor.streaming.server.common.entity.RestResult;
import raptor.streaming.server.common.entity.Tuple2;
import raptor.streaming.server.service.HadoopService;
import raptor.streaming.server.utils.BootUtil;

@RestController
@RequestMapping(value = Constant.API_PREFIX_URI + "/jars/engines")
public class EngineControl {

  private static final Logger logger = LoggerFactory.getLogger(EngineControl.class);

  @Autowired
  private HadoopService hadoopService;

  @GetMapping(value = "/")
  public RestResult get() {
    return new DataResult<>(Arrays.asList("flink-1.11"));
  }


  @PostMapping(value = "/upload")
  public RestResult add(@RequestParam("name") String clusterName,
      @RequestParam("version") String version, @RequestParam("file") MultipartFile file) {

    String path = String.format("/streaming-platform/flink/engines/%s/lib", version);

    try {
      Tuple2<String, String> ret = BootUtil.saveMultipartFile(file, false);
      logger.info("upload: file [{}], src [{}], dst [{}]", ret.getFirst(), ret.getSecond(), path);
      hadoopService.upload(clusterName, ret.getSecond(), path + "/" + ret.getFirst());
      return new RestResult(true, 200, "");
    } catch (Exception ex) {
      return RestResult.getFailed();
    }

  }


}
