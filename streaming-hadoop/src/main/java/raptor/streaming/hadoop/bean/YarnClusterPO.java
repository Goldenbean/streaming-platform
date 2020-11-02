package raptor.streaming.hadoop.bean;

import java.util.ArrayList;
import java.util.List;

public class YarnClusterPO {

  private int memTotal;
  private int memUsed;
  private int coresTotal;
  private int coresUsed;

  private List<NodePO> nodeList = new ArrayList<>();

  public YarnClusterPO() {

  }

  public int getMemTotal() {
    return memTotal;
  }

  public void setMemTotal(int memTotal) {
    this.memTotal = memTotal;
  }

  public int getMemUsed() {
    return memUsed;
  }

  public void setMemUsed(int memUsed) {
    this.memUsed = memUsed;
  }

  public int getCoresTotal() {
    return coresTotal;
  }

  public void setCoresTotal(int coresTotal) {
    this.coresTotal = coresTotal;
  }

  public int getCoresUsed() {
    return coresUsed;
  }

  public void setCoresUsed(int coresUsed) {
    this.coresUsed = coresUsed;
  }

  public List<NodePO> getNodeList() {
    return nodeList;
  }

  public void setNodeList(List<NodePO> nodeList) {
    this.nodeList = nodeList;
  }
}
