package raptor.streaming.server.boot.dao.model;


/**
 * Created by azhe on 2020-11-17 20:11
 */
public class AppPO {

  private String  name;
  private int key;

  public AppPO(String name, int key) {
    this.name = name;
    this.key = key;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getKey() {
    return key;
  }

  public void setKey(int key) {
    this.key = key;
  }
}
