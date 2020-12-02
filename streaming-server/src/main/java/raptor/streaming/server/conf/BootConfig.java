package raptor.streaming.server.conf;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BootConfig {

  @Value("${streaming.conf}")
  private String streamingConf;


  public String getStreamingConf() {
    if (Strings.isNullOrEmpty(streamingConf)) {
      return "./config";
      // throw new RuntimeException("streaming.hadoop.conf or streaming.flink.conf is null or empty");
    }

    return streamingConf;
  }

}
