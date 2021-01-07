package raptor.streaming.server.common.http;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataResult<T> extends RestResult {

  @JsonProperty(value = "data")
  private T data;

  public DataResult(T data) {
    super(Boolean.TRUE, 200, "success");
    this.data = data;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
