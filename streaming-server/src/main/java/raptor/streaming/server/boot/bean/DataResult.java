package raptor.streaming.server.boot.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataResult<T> extends RestResult {

  @JsonProperty(value = "data")
  private T data;

  public DataResult(T data) {
    super(Boolean.TRUE, 200, null);
    this.data = data;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
