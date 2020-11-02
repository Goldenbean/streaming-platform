package raptor.streaming.hadoop.bean;

public class FilePO {

  private String path;
  private String parent;
  private String name;
  private boolean isDir;
  private long length;
  private long accessTime;
  private long modificationTime;
  private String owner;
  private String group;
  private int replication;

  public FilePO() {

  }

  public String getParent() {
    return parent;
  }

  public void setParent(String parent) {
    this.parent = parent;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public boolean isDir() {
    return isDir;
  }

  public void setDir(boolean dir) {
    isDir = dir;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getLength() {
    return length;
  }

  public void setLength(long length) {
    this.length = length;
  }

  public long getAccessTime() {
    return accessTime;
  }

  public void setAccessTime(long accessTime) {
    this.accessTime = accessTime;
  }

  public long getModificationTime() {
    return modificationTime;
  }

  public void setModificationTime(long modificationTime) {
    this.modificationTime = modificationTime;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  public int getReplication() {
    return replication;
  }

  public void setReplication(int replication) {
    this.replication = replication;
  }
}
