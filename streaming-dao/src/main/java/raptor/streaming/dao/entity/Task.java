package raptor.streaming.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * <p>
 * 任务列表
 * </p>
 *
 * @author azhe
 * @since 2021-01-13
 */
@TableName("ops_task")
@ApiModel(value = "Task对象", description = "任务列表")
public class Task extends BaseEntity<Task> {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "1 ready to deploy, 0 ready to stop")
  @TableField("action")
  private Integer action;

  @ApiModelProperty(value = "是否逻辑删除，0 否 1 是")
  @TableField("deleted")
  @TableLogic
  private Integer deleted;

  @ApiModelProperty(value = "启动相关的配置文件")
  @TableField("config")
  private String config;

  @ApiModelProperty(value = "运行时相关信息")
  @TableField("info")
  private String info;

  @ApiModelProperty(value = "状态")
  @TableField("state")
  private String state;

  @TableField("job_id")
  private Long jobId;

  @TableField("yarn_id")
  private String yarnId;

  @TableField("flink_id")
  private String flinkId;

  @TableField("deploy_id")
  private String deployId;

  @TableField("job_type")
  private String jobType;

  @TableField("job_name")
  private String jobName;


  public Integer getAction() {
    return action;
  }

  public void setAction(Integer action) {
    this.action = action;
  }

  public Integer getDeleted() {
    return deleted;
  }

  public void setDeleted(Integer deleted) {
    this.deleted = deleted;
  }

  public String getConfig() {
    return config;
  }

  public void setConfig(String config) {
    this.config = config;
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public Long getJobId() {
    return jobId;
  }

  public void setJobId(Long jobId) {
    this.jobId = jobId;
  }

  public String getYarnId() {
    return yarnId;
  }

  public void setYarnId(String yarnId) {
    this.yarnId = yarnId;
  }

  public String getFlinkId() {
    return flinkId;
  }

  public void setFlinkId(String flinkId) {
    this.flinkId = flinkId;
  }

  public String getDeployId() {
    return deployId;
  }

  public void setDeployId(String deployId) {
    this.deployId = deployId;
  }

  public String getJobType() {
    return jobType;
  }

  public void setJobType(String jobType) {
    this.jobType = jobType;
  }

  public String getJobName() {
    return jobName;
  }

  public void setJobName(String jobName) {
    this.jobName = jobName;
  }

  @Override
  protected Serializable pkVal() {
    return null;
  }

  @Override
  public String toString() {
    return "Task{" +
        "action=" + action +
        ", deleted=" + deleted +
        ", config=" + config +
        ", info=" + info +
        ", state=" + state +
        ", jobId=" + jobId +
        ", yarnId=" + yarnId +
        ", flinkId=" + flinkId +
        ", deployId=" + deployId +
        ", jobType=" + jobType +
        ", jobName=" + jobName +
        "}";
  }
}
