package raptor.streaming.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeControl {


  @GetMapping(value = "/")
  public String home() {
    return "ok";
  }


  @GetMapping(value = "/ok.htm")
  public String ok(@RequestParam(defaultValue = "false") String down) {
    return "ok";
  }


}
