package raptor.streaming.server.common.constants;

public enum ClusterTypeEnum {
  STANDALONE(0, "standalone"),
  YARN(1, "yarn"),
  KUBERNETES(2, "kubernetes");

  private String value;
  private int code;

  ClusterTypeEnum(int code, String value) {
    this.code = code;
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public int getCode() {
    return code;
  }

  public static ClusterTypeEnum codeOf(int code) {
    for (ClusterTypeEnum clusterTypeEnum : values()) {
      if (clusterTypeEnum.getCode() == code) {
        return clusterTypeEnum;
      }
    }
    throw new RuntimeException("没有找到对应的枚举");
  }
}
