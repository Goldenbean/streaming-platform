package raptor.streaming.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * <p>
 * 集群列表
 * </p>
 *
 * @author azhe
 * @since 2020-12-29
 */
@TableName("sys_cluster")
@ApiModel(value = "Cluster对象", description = "集群列表")
public class Cluster extends BaseEntity<Cluster> {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "集群名称")
  @TableField("name")
  private String name;

  @ApiModelProperty(value = "集群类型, 0 standalone 1 yarn 2 k8s")
  @TableField("type")
  private Integer type;

  @ApiModelProperty(value = "yarn集群配置文件，zip压缩包")
  @TableField("config_file")
  private byte[] configFile;

  @ApiModelProperty(value = "yarn集群配置文件md5值")
  @TableField("config_file_md5")
  private String configFileMd5;

  @ApiModelProperty(value = "配置文件名称")
  @TableField("config_file_name")
  private String configFileName;

  @ApiModelProperty(value = "集群计算单元的配置")
  @TableField("spu_conf")
  private String spuConf;

  @ApiModelProperty(value = "集群配置信息")
  @TableField("cluster_conf")
  private String clusterConf;

  @ApiModelProperty(value = "计算节点数量")
  @TableField("total_nodes")
  private Integer totalNodes;

  @ApiModelProperty(value = "集群内存总量(GB)")
  @TableField("total_memory")
  private Double totalMemory;

  @ApiModelProperty(value = "集群核数总量")
  @TableField("total_cores")
  private Integer totalCores;


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

  public byte[] getConfigFile() {
    return configFile;
  }

  public void setConfigFile(byte[] configFile) {
    this.configFile = configFile;
  }

  public String getConfigFileMd5() {
    return configFileMd5;
  }

  public void setConfigFileMd5(String configFileMd5) {
    this.configFileMd5 = configFileMd5;
  }

  public String getConfigFileName() {
    return configFileName;
  }

  public void setConfigFileName(String configFileName) {
    this.configFileName = configFileName;
  }

  public String getSpuConf() {
    return spuConf;
  }

  public void setSpuConf(String spuConf) {
    this.spuConf = spuConf;
  }

  public String getClusterConf() {
    return clusterConf;
  }

  public void setClusterConf(String clusterConf) {
    this.clusterConf = clusterConf;
  }

  public Integer getTotalNodes() {
    return totalNodes;
  }

  public void setTotalNodes(Integer totalNodes) {
    this.totalNodes = totalNodes;
  }

  public Double getTotalMemory() {
    return totalMemory;
  }

  public void setTotalMemory(Double totalMemory) {
    this.totalMemory = totalMemory;
  }

  public Integer getTotalCores() {
    return totalCores;
  }

  public void setTotalCores(Integer totalCores) {
    this.totalCores = totalCores;
  }

  @Override
  protected Serializable pkVal() {
    return null;
  }

  @Override
  public String toString() {
    return "Cluster{" +
        "name=" + name +
        ", type=" + type +
        ", configFile=" + configFile +
        ", configFileMd5=" + configFileMd5 +
        ", configFileName=" + configFileName +
        ", spuConf=" + spuConf +
        ", clusterConf=" + clusterConf +
        ", totalNodes=" + totalNodes +
        ", totalMemory=" + totalMemory +
        ", totalCores=" + totalCores +
        "}";
  }
}
