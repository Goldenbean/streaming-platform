package raptor.streaming.server.boot.dao.model;

import java.util.Date;

public class JobPO {

  private long id;
  private String config;
  private Date gmtCreate;
  private Date gmtModify;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getConfig() {
    return config;
  }

  public void setConfig(String config) {
    this.config = config;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  public Date getGmtModify() {
    return gmtModify;
  }

  public void setGmtModify(Date gmtModify) {
    this.gmtModify = gmtModify;
  }
}
