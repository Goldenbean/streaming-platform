package raptor.streaming.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author azhe
 * @since 2021-01-05
 */
@TableName("sys_project")
@ApiModel(value = "ProjectEntity对象", description = "")
public class ProjectEntity extends BaseEntity<ProjectEntity> {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "项目名称")
  @TableField("name")
  private String name;

  @ApiModelProperty(value = "项目代码")
  @TableField("code")
  private String code;

  @ApiModelProperty(value = "项目管理员")
  @TableField("admin_user_name")
  private String adminUserName;

  @ApiModelProperty(value = "项目成员")
  @TableField("users")
  private String users;

  @ApiModelProperty(value = "集群id")
  @TableField("cluster_id")
  private Long clusterId;

  @ApiModelProperty(value = "集群名称")
  @TableField("cluster_name")
  private String clusterName;


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getAdminUserName() {
    return adminUserName;
  }

  public void setAdminUserName(String adminUserName) {
    this.adminUserName = adminUserName;
  }

  public String getUsers() {
    return users;
  }

  public void setUsers(String users) {
    this.users = users;
  }

  public Long getClusterId() {
    return clusterId;
  }

  public void setClusterId(Long clusterId) {
    this.clusterId = clusterId;
  }

  public String getClusterName() {
    return clusterName;
  }

  public void setClusterName(String clusterName) {
    this.clusterName = clusterName;
  }

  @Override
  protected Serializable pkVal() {
    return null;
  }

  @Override
  public String toString() {
    return "ProjectEntity{" +
        "name=" + name +
        ", code=" + code +
        ", adminUserName=" + adminUserName +
        ", users=" + users +
        ", clusterId=" + clusterId +
        ", clusterName=" + clusterName +
        "}";
  }
}
