package raptor.streaming.server.boot.dao.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * Created by azhe on 2020-11-17 14:46
 */
public class ClusterPO {

  private Long id;

  private String name;

  private int type;

  private byte[] configFile;

  private String configFileMD5;

  private String configFileName;

  private String spuConf;
  private String clusterConf;

  private String description;


  private int totalNodes;
  private double totalMemory;
  private int totalCores;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
  private Date gmtCreate;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
  private Date gmtModify;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public byte[] getConfigFile() {
    return configFile;
  }

  public void setConfigFile(byte[] configFile) {
    this.configFile = configFile;
  }

  public String getConfigFileMD5() {
    return configFileMD5;
  }

  public void setConfigFileMD5(String configFileMD5) {
    this.configFileMD5 = configFileMD5;
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getTotalNodes() {
    return totalNodes;
  }

  public void setTotalNodes(int totalNodes) {
    this.totalNodes = totalNodes;
  }

  public double getTotalMemory() {
    return totalMemory;
  }

  public void setTotalMemory(double totalMemory) {
    this.totalMemory = totalMemory;
  }

  public int getTotalCores() {
    return totalCores;
  }

  public void setTotalCores(int totalCores) {
    this.totalCores = totalCores;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  public Date getGmtModify() {
    return gmtModify;
  }

  public void setGmtModify(Date gmtModify) {
    this.gmtModify = gmtModify;
  }
}
