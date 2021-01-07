package raptor.streaming.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import raptor.streaming.server.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 任务列表
 * </p>
 *
 * @author azhe
 * @since 2021-01-07
 */
@TableName("ops_task")
@ApiModel(value="TaskEntity对象", description="任务列表")
public class TaskEntity extends BaseEntity<TaskEntity> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "1 deploy, 0 stop")
    @TableField("action")
    private Long action;

    @ApiModelProperty(value = "启动相关的配置文件")
    @TableField("config")
    private String config;

    @TableField("job_id")
    private Long jobId;

    @TableField("yarn_id")
    private String yarnId;

    @TableField("flink_id")
    private String flinkId;

    @ApiModelProperty(value = "是否逻辑删除，0 否 1 是")
    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    @TableField("update")
    private String update;


    public Long getAction() {
        return action;
    }

    public void setAction(Long action) {
        this.action = action;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
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

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "TaskEntity{" +
        "action=" + action +
        ", config=" + config +
        ", jobId=" + jobId +
        ", yarnId=" + yarnId +
        ", flinkId=" + flinkId +
        ", deleted=" + deleted +
        ", update=" + update +
        "}";
    }
}
