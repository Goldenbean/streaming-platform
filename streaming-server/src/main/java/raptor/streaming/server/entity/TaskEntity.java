package raptor.streaming.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import raptor.streaming.server.common.entity.BaseEntity;

/**
 * <p>
 * 任务列表
 * </p>
 *
 * @author azhe
 * @since 2021-01-04
 */
@TableName("ops_task")
@ApiModel(value="TaskEntity对象", description="任务列表")
public class TaskEntity extends BaseEntity<TaskEntity> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务配置")
    @TableField("config")
    private String config;


    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "TaskEntity{" +
        "config=" + config +
        "}";
    }
}
