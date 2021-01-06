package raptor.streaming.hadoop.yarn;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.flink.runtime.jobgraph.SavepointRestoreSettings;

public class DeployConfig {

  private String applicationName = "";

  private String jarFilePath = "";

  private String entryPointClass = "";

  private List<URL> classpaths = new ArrayList<>();

  private List<String> programArgs = new ArrayList<>();

  private int parallelism;

  private boolean detachedMode;

  private boolean shutdownOnAttachedExit;

  private SavepointRestoreSettings savepointSettings;

  private int spu = 1;

  private String clusterName="";

  public DeployConfig() {

  }

  public DeployConfig(String applicationName, String jarPath,
      List<String> args) {

    this.applicationName = applicationName;
    this.jarFilePath = jarPath;
    this.programArgs = args;
  }

  public String getClusterName() {
    return clusterName;
  }

  public void setClusterName(String clusterName) {
    this.clusterName = clusterName;
  }

  public String getApplicationName() {
    return applicationName;
  }

  public void setApplicationName(String applicationName) {
    this.applicationName = applicationName;
  }

  public String getJarFilePath() {
    return jarFilePath;
  }

  public void setJarFilePath(String jarFilePath) {
    this.jarFilePath = jarFilePath;
  }

  public String getEntryPointClass() {
    return entryPointClass;
  }

  public void setEntryPointClass(String entryPointClass) {
    this.entryPointClass = entryPointClass;
  }

  public List<URL> getClasspaths() {
    return classpaths;
  }

  public List<String> getProgramArgs() {
    return programArgs;
  }

  public int getParallelism() {
    return parallelism;
  }

  public boolean isDetachedMode() {
    return detachedMode;
  }

  public boolean isShutdownOnAttachedExit() {
    return shutdownOnAttachedExit;
  }

  public SavepointRestoreSettings getSavepointSettings() {
    return savepointSettings;
  }

  public void setClasspaths(List<URL> classpaths) {
    this.classpaths = classpaths;
  }

  public void setProgramArgs(List<String> programArgs) {
    this.programArgs = programArgs;
  }

  public void setParallelism(int parallelism) {
    this.parallelism = parallelism;
  }

  public void setDetachedMode(boolean detachedMode) {
    this.detachedMode = detachedMode;
  }

  public void setShutdownOnAttachedExit(boolean shutdownOnAttachedExit) {
    this.shutdownOnAttachedExit = shutdownOnAttachedExit;
  }

  public void setSavepointSettings(
      SavepointRestoreSettings savepointSettings) {
    this.savepointSettings = savepointSettings;
  }

  public int getSpu() {
    return spu;
  }

  public void setSpu(int spu) {
    this.spu = spu;
  }
}
