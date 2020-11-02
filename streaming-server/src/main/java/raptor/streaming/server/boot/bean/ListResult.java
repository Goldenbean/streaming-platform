package raptor.streaming.server.boot.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;


@Deprecated
public class ListResult<T> extends RestResult {

  @JsonProperty("list")
  private List<T> list;

  public ListResult(List<T> list) {
    super(Boolean.TRUE, 200, null);
    this.list = list;
  }

  public List<T> getList() {
    return list;
  }

  public void setList(List<T> list) {
    this.list = list;
  }
}
