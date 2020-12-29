package raptor.streaming.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import raptor.streaming.server.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 作业文件表
 * </p>
 *
 * @author azhe
 * @since 2020-12-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dev_job_file")
@ApiModel(value="JobFileEntity对象", description="作业文件表")
public class JobFileEntity extends BaseEntity<JobFileEntity> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文件系统id")
    @TableField("file_id")
    private Long fileId;

    @ApiModelProperty(value = "作业名")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "作业类型,sql、 jar")
    @TableField("type")
    private String type;

    @ApiModelProperty(value = "项目id")
    @TableField("app_key")
    private Long appKey;

    @ApiModelProperty(value = "作业内容")
    @TableField("content")
    private String content;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
