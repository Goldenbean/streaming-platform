package raptor.streaming.hadoop.bean;

import org.apache.hadoop.yarn.api.records.NodeReport;

public class NodePO {

  private String id;
  private String rack;
  private String state;
  private int memUsed;
  private int memTotal;
  private int coresUsed;
  private int coresTotal;
  private long lastHealthTime;


  public NodePO(NodeReport nodeReport) {
    setId(nodeReport.getNodeId().toString());
    setRack(nodeReport.getRackName());
    setState(nodeReport.getNodeState().name());
    setMemUsed(nodeReport.getUsed().getMemory());
    setMemTotal(nodeReport.getCapability().getMemory());
    setCoresUsed(nodeReport.getUsed().getVirtualCores());
    setCoresTotal(nodeReport.getCapability().getVirtualCores());
    setLastHealthTime(nodeReport.getLastHealthReportTime());
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getRack() {
    return rack;
  }

  public void setRack(String rack) {
    this.rack = rack;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public int getMemUsed() {
    return memUsed;
  }

  public void setMemUsed(int memUsed) {
    this.memUsed = memUsed;
  }

  public int getMemTotal() {
    return memTotal;
  }

  public void setMemTotal(int memTotal) {
    this.memTotal = memTotal;
  }

  public int getCoresUsed() {
    return coresUsed;
  }

  public void setCoresUsed(int coresUsed) {
    this.coresUsed = coresUsed;
  }

  public int getCoresTotal() {
    return coresTotal;
  }

  public void setCoresTotal(int coresTotal) {
    this.coresTotal = coresTotal;
  }

  public long getLastHealthTime() {
    return lastHealthTime;
  }

  public void setLastHealthTime(long lastHealthTime) {
    this.lastHealthTime = lastHealthTime;
  }
}
