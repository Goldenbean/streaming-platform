package raptor.streaming.server.common.http;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RestResult {

  @JsonProperty(value = "success")
  private boolean success = true;

  @JsonProperty(value = "code")
  private int code = 200;

  @JsonProperty(value = "message")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String message = "";


  public RestResult() {
  }

  public RestResult(boolean success, int code, String message) {
    this.success = success;
    this.code = code;
    this.message = message;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }


  private static final RestResult successInstance = new RestResult(true, 200, "success");

  private static final RestResult failedInstance = new RestResult(false, 500, "failed");

  public static RestResult getSuccess() {
    return successInstance;
  }

  public static RestResult getSuccess(String message) {
    return new RestResult(true, 200, message);
  }

  public static RestResult getFailed() {
    return failedInstance;
  }

  public static RestResult getFailed(String message) {
    return new RestResult(false, 500, message);
  }

}
