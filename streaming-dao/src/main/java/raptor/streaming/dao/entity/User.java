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
 * @since 2020-12-29
 */
@TableName("sys_user")
@ApiModel(value = "UserEntity对象", description = "")
public class User extends BaseEntity<User> {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "账号")
  @TableField("account")
  private String account;

  @ApiModelProperty(value = "密码")
  @TableField("password")
  private String password;

  @ApiModelProperty(value = "用户名")
  @TableField("name")
  private String name;

  @ApiModelProperty(value = "角色")
  @TableField("roles")
  private String roles;


  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRoles() {
    return roles;
  }

  public void setRoles(String roles) {
    this.roles = roles;
  }

  @Override
  protected Serializable pkVal() {
    return null;
  }

  @Override
  public String toString() {
    return "UserEntity{" +
        "account=" + account +
        ", password=" + password +
        ", name=" + name +
        ", roles=" + roles +
        "}";
  }
}
