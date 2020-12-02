package raptor.streaming.server.controller;

import java.util.Arrays;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raptor.streaming.server.common.entity.DataResult;
import raptor.streaming.server.constants.Constant;
import raptor.streaming.server.entity.AppPO;

/**
 * Created by azhe on 2020-11-17 20:02
 */
@RestController
@RequestMapping(Constant.API_PREFIX_URI)

public class AppController {

  @GetMapping("apps")
  public DataResult list() {
    return new DataResult<>(Arrays.asList(new AppPO("实时数仓项目", 1),new AppPO("测试项目", 2)));
  }

}
