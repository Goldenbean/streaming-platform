package raptor.streaming.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * <p>
 * 资源管理
 * </p>
 *
 * @author azhe
 * @since 2020-12-29
 */
@TableName("dev_file_system")
@ApiModel(value = "FileSystemEntity对象", description = "资源管理")
public class FileSystem extends BaseEntity<FileSystem> {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "文件夹名称")
  @TableField("name")
  private String name;

  @ApiModelProperty(value = "项目id")
  @TableField("app_key")
  private Long appKey;

  @ApiModelProperty(value = "父节点ID，根节点为：-1")
  @TableField("parent_id")
  private Integer parentId;

  @ApiModelProperty(value = "功能目录：resource")
  @TableField("tab_folder")
  private String tabFolder;

  @ApiModelProperty(value = "作业路径目录")
  @TableField("parent_path")
  private String parentPath;

  @ApiModelProperty(value = "hdfs路径")
  @TableField("file_path")
  private String filePath;

  @ApiModelProperty(value = "是否逻辑删除，0 否 1 是")
  @TableField("deleted")
  @TableLogic
  private Integer deleted;

  @ApiModelProperty(value = "类型：目录、作业任务、资源、函数，枚举值：dir,job、res、fun")
  @TableField("type")
  private String type;


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getAppKey() {
    return appKey;
  }

  public void setAppKey(Long appKey) {
    this.appKey = appKey;
  }

  public Integer getParentId() {
    return parentId;
  }

  public void setParentId(Integer parentId) {
    this.parentId = parentId;
  }

  public String getTabFolder() {
    return tabFolder;
  }

  public void setTabFolder(String tabFolder) {
    this.tabFolder = tabFolder;
  }

  public String getParentPath() {
    return parentPath;
  }

  public void setParentPath(String parentPath) {
    this.parentPath = parentPath;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public Integer getDeleted() {
    return deleted;
  }

  public void setDeleted(Integer deleted) {
    this.deleted = deleted;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  protected Serializable pkVal() {
    return null;
  }

  @Override
  public String toString() {
    return "FileSystemEntity{" +
        "name=" + name +
        ", appKey=" + appKey +
        ", parentId=" + parentId +
        ", tabFolder=" + tabFolder +
        ", parentPath=" + parentPath +
        ", filePath=" + filePath +
        ", deleted=" + deleted +
        ", type=" + type +
        "}";
  }
}
