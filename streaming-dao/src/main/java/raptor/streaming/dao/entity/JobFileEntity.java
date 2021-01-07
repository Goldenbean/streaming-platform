package raptor.streaming.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * <p>
 * 作业文件表
 * </p>
 *
 * @author azhe
 * @since 2020-12-29
 */
@TableName("dev_job_file")
@ApiModel(value = "JobFileEntity对象", description = "作业文件表")
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


  public Long getFileId() {
    return fileId;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Long getAppKey() {
    return appKey;
  }

  public void setAppKey(Long appKey) {
    this.appKey = appKey;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Override
  protected Serializable pkVal() {
    return null;
  }

  @Override
  public String toString() {
    return "JobFileEntity{" +
        "fileId=" + fileId +
        ", name=" + name +
        ", type=" + type +
        ", appKey=" + appKey +
        ", content=" + content +
        "}";
  }
}
