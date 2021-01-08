package raptor.streaming.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * <p>
 * 数仓表
 * </p>
 *
 * @author azhe
 * @since 2020-12-29
 */
@TableName("dw_table")
@ApiModel(value = "TableEntity对象", description = "数仓表")
public class Table extends BaseEntity<Table> {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "维度表表名")
  @TableField("name")
  private String name;

  @ApiModelProperty(value = "表类型, 0 实时数仓表 1 维度数仓表 2 离线数仓表")
  @TableField("type")
  private Integer type;

  @ApiModelProperty(value = "数据源")
  @TableField("source_name")
  private String sourceName;

  @ApiModelProperty(value = "项目id")
  @TableField("app_key")
  private Long appKey;

  @ApiModelProperty(value = "表结构")
  @TableField("table_schema")
  private String tableSchema;

  @ApiModelProperty(value = "其他配置")
  @TableField("config_json")
  private String configJson;


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

  public String getSourceName() {
    return sourceName;
  }

  public void setSourceName(String sourceName) {
    this.sourceName = sourceName;
  }

  public Long getAppKey() {
    return appKey;
  }

  public void setAppKey(Long appKey) {
    this.appKey = appKey;
  }

  public String getTableSchema() {
    return tableSchema;
  }

  public void setTableSchema(String tableSchema) {
    this.tableSchema = tableSchema;
  }

  public String getConfigJson() {
    return configJson;
  }

  public void setConfigJson(String configJson) {
    this.configJson = configJson;
  }

  @Override
  protected Serializable pkVal() {
    return null;
  }

  @Override
  public String toString() {
    return "TableEntity{" +
        "name=" + name +
        ", type=" + type +
        ", sourceName=" + sourceName +
        ", appKey=" + appKey +
        ", tableSchema=" + tableSchema +
        ", configJson=" + configJson +
        "}";
  }
}
