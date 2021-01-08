package raptor.streaming.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * <p>
 * 数据源列表
 * </p>
 *
 * @author azhe
 * @since 2020-12-29
 */
@TableName("dw_source")
@ApiModel(value = "SourceEntity对象", description = "数据源列表")
public class Source extends BaseEntity<Source> {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "数据源名称")
  @TableField("name")
  private String name;

  @ApiModelProperty(value = "数据源类型, 0 mysql 1 kafka 2 hive 3 clickhouse")
  @TableField("type")
  private Integer type;

  @ApiModelProperty(value = "IP地址")
  @TableField("ip")
  private String ip;

  @ApiModelProperty(value = "配置信息")
  @TableField("config")
  private String config;

  @ApiModelProperty(value = "项目id")
  @TableField("app_key")
  private Long appKey;


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public String getConfig() {
    return config;
  }

  public void setConfig(String config) {
    this.config = config;
  }

  public Long getAppKey() {
    return appKey;
  }

  public void setAppKey(Long appKey) {
    this.appKey = appKey;
  }

  @Override
  protected Serializable pkVal() {
    return null;
  }

  @Override
  public String toString() {
    return "SourceEntity{" +
        "name=" + name +
        ", type=" + type +
        ", ip=" + ip +
        ", config=" + config +
        ", appKey=" + appKey +
        "}";
  }
}
