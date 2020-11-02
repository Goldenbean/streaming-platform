package raptor.streaming.server.domain;

import raptor.streaming.deploy.yarn.DeployConfig;

public class Job {

  private long id = 0;
  private String yarnId = "";
  private String yarnStatus = "";
  private String deployId = "";
  private String description = "";
  private int spu = 1;
  private DeployConfig deployConfig = new DeployConfig();

  public Job() {

  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDeployId() {
    return deployId;
  }

  public void setDeployId(String deployId) {
    this.deployId = deployId;
  }

  public DeployConfig getDeployConfig() {
    return deployConfig;
  }

  public void setDeployConfig(DeployConfig deployConfig) {
    this.deployConfig = deployConfig;
  }

  public String getYarnId() {
    return yarnId;
  }

  public void setYarnId(String yarnId) {
    this.yarnId = yarnId;
  }

  public String getYarnStatus() {
    return yarnStatus;
  }

  public void setYarnStatus(String yarnStatus) {
    this.yarnStatus = yarnStatus;
  }

  public int getSpu() {
    return spu;
  }

  public void setSpu(int spu) {
    this.spu = spu;
  }
}
