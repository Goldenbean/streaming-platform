package raptor.streaming.hadoop.bean;

import org.apache.hadoop.yarn.api.records.ApplicationReport;

public class YarnAppPO {

  private String applicationId;
  private String applicationState;
  private String user;
  private String name;
  private String applicationType;
  private long startTime;
  private long finishTime;
  private String finalApplicationStatus;


  public YarnAppPO() {

  }

  public YarnAppPO(ApplicationReport report) {
    applicationId = report.getApplicationId().toString();
    applicationState = report.getYarnApplicationState().toString();
    finalApplicationStatus = report.getFinalApplicationStatus().toString();

    applicationType = report.getApplicationType();
    startTime = report.getStartTime();
    finishTime = report.getFinishTime();

    name = report.getName();
    user = report.getUser();


  }


  public String getApplicationId() {
    return applicationId;
  }

  public void setApplicationId(String applicationId) {
    this.applicationId = applicationId;
  }

  public String getApplicationState() {
    return applicationState;
  }

  public void setApplicationState(String applicationState) {
    this.applicationState = applicationState;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getApplicationType() {
    return applicationType;
  }

  public void setApplicationType(String applicationType) {
    this.applicationType = applicationType;
  }

  public long getStartTime() {
    return startTime;
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  public long getFinishTime() {
    return finishTime;
  }

  public void setFinishTime(long finishTime) {
    this.finishTime = finishTime;
  }

  public String getFinalApplicationStatus() {
    return finalApplicationStatus;
  }

  public void setFinalApplicationStatus(String finalApplicationStatus) {
    this.finalApplicationStatus = finalApplicationStatus;
  }
}
