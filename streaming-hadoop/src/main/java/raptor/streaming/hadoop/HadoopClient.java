package raptor.streaming.hadoop;

import com.google.common.base.Strings;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.api.records.NodeReport;
import org.apache.hadoop.yarn.api.records.YarnApplicationState;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import raptor.streaming.hadoop.bean.FilePO;
import raptor.streaming.hadoop.bean.NodePO;
import raptor.streaming.hadoop.bean.YarnClusterPO;

public class HadoopClient {

  private static final Logger logger = LoggerFactory.getLogger(HadoopClient.class);


  public static final List<String> APPLICATION_STATE = Arrays
      .asList("NEW", "NEW_SAVING", "SUBMITTED",
          "ACCEPTED", "RUNNING", "FINISHED", "FAILED",
          "KILLED");

  public static final String HADOOP_USER_NAME = "root";

  private final String hadoopConfigPath;

  private FileSystem fileSystem;
  private YarnClient yarnClient;


  public HadoopClient(String hadoopConfigPath) {
    this.hadoopConfigPath = hadoopConfigPath;
  }

  public void init() throws IOException {
    System.setProperty("HADOOP_USER_NAME", HADOOP_USER_NAME);
    Configuration configuration = initConfiguration(hadoopConfigPath);
    fileSystem = getFileSystem(configuration);
    yarnClient = getYarnClient(configuration);
  }

  // ==================== HDFS Function ====================

  public List<FilePO> list(String path) throws IOException {

    List<FilePO> ret = new ArrayList<>();

    FileStatus[] fileStatuses = getFileSystem().listStatus(new Path(path));

    for (FileStatus status : fileStatuses) {
      FilePO filePO = HdfsTools.toFilePO(status);
      ret.add(filePO);
    }
    return ret;
  }

  public List<FilePO> listFile(String path) throws IOException {

    List<FilePO> ret = new ArrayList<>();

    RemoteIterator<LocatedFileStatus> iterator = getFileSystem().listFiles(new Path(path), true);

    while (iterator.hasNext()) {
      LocatedFileStatus status = iterator.next();

      FilePO filePO = HdfsTools.toFilePO(status);
      ret.add(filePO);
    }
    return ret;
  }

  public boolean exists(String path, boolean mkdir) throws IOException {
    Path dir = new Path(path);

    if (getFileSystem().exists(dir)) {
      return true;
    }

    if (mkdir) {
      getFileSystem().mkdirs(dir);
      return true;
    }
    return false;
  }

  public boolean delete(String path) throws IOException {
    boolean recursive = true;

    Path dir = new Path(path);

    if (!getFileSystem().exists(dir)) {
      return false;
    }

    return getFileSystem().delete(dir, recursive);
  }

  public boolean move(String src, String dst)
      throws IOException, IllegalArgumentException {
    return getFileSystem().rename(new Path(src), new Path(dst));
  }

  public boolean rename(String src, String name)
      throws IOException, IllegalArgumentException {
    FileStatus fileStatus = getFileSystem().getFileStatus(new Path(src));
    FilePO filePO = HdfsTools.toFilePO(fileStatus);
    return move(src, filePO.getParent() + "/" + name);
  }


  public void readHDFSFile(String filePath, OutputStream out) {
    FSDataInputStream fsDataInputStream = null;

    try {
      Path path = new Path(filePath);
      fsDataInputStream = getFileSystem().open(path);
      IOUtils.copyBytes(fsDataInputStream, out, 4096, false);

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (fsDataInputStream != null) {
        IOUtils.closeStream(fsDataInputStream);
      }
    }

  }

  public void upload(String src, String dst) throws IOException {
    fileSystem.copyFromLocalFile(false, true, new Path(src), new Path(dst));
  }

  public void upload(String dst, String... srcs) throws IOException {
    Path[] srcArray = Arrays.stream(srcs).map(Path::new).toArray(Path[]::new);
    fileSystem.copyFromLocalFile(false, true, srcArray, new Path(dst));
  }

  public void download(String src, String dst) throws IOException {
    fileSystem.copyToLocalFile(false, new Path(src), new Path(dst), true);
  }

  // ==================== Yarn Function ====================


  public List<ApplicationReport> getYarnApplication()
      throws YarnException, IOException {
    List<ApplicationReport> apps = yarnClient.getApplications();
    return apps;
  }

  public List<ApplicationReport> getYarnApplication(String state)
      throws YarnException, IOException {

    return getYarnApplication().stream()
        .filter(app -> {
          if (!Strings.isNullOrEmpty(state)
              && APPLICATION_STATE.contains(state)
              && state.equals(app.getYarnApplicationState().toString())) {
            return true;
          }
          return false;
        })
        .collect(Collectors.toList());
  }


  public List<ApplicationId> getYarnApplicationId(boolean running)
      throws YarnException, IOException {

    return getYarnApplication(YarnApplicationState.RUNNING.toString())
        .stream()
        .map(obj -> obj.getApplicationId())
        .collect(Collectors.toList());
  }


  public void killYarnApplication(String appId) throws YarnException, IOException {
    ApplicationId applicationId = ApplicationId.fromString(appId);
    yarnClient.killApplication(applicationId);
  }


  public YarnClusterPO getYarnOverview() throws YarnException, IOException {

    List<NodeReport> nodeReports = yarnClient.getNodeReports();

    int memTotal = 0;
    int memUsed = 0;
    int vCoresTotal = 0;
    int vCoresUsed = 0;

    List<NodePO> nodeList = new ArrayList<>();

    for (NodeReport nodeReport : nodeReports) {
      memUsed += nodeReport.getUsed().getMemory();
      memTotal += nodeReport.getCapability().getMemory();
      vCoresUsed += nodeReport.getUsed().getVirtualCores();
      vCoresTotal += nodeReport.getCapability().getVirtualCores();
      nodeList.add(new NodePO(nodeReport));
    }

    YarnClusterPO yarnClusterPO = new YarnClusterPO();
    yarnClusterPO.setCoresTotal(vCoresTotal);
    yarnClusterPO.setCoresUsed(vCoresUsed);
    yarnClusterPO.setMemTotal(memTotal);
    yarnClusterPO.setMemUsed(memUsed);
    yarnClusterPO.setNodeList(nodeList);

    return yarnClusterPO;

  }

  // ==================== Configuration ====================

  private FileSystem getFileSystem(Configuration configuration) throws IOException {
    FileSystem fileSystem = FileSystem.get(configuration);
    return fileSystem;
  }

  private YarnClient getYarnClient(Configuration configuration) {
    YarnConfiguration yarnConfiguration = new YarnConfiguration(configuration);
    YarnClient yarnClient = YarnClient.createYarnClient();
    yarnClient.init(yarnConfiguration);
    yarnClient.start();
    return yarnClient;
  }


  private Configuration initConfiguration(String clusterConfigDirPath) {

    File clusterConfigDir = new File(clusterConfigDirPath);

    if (!clusterConfigDir.exists()) {
      throw new RuntimeException(
          "cluster config dir path doesn't exists ! " + clusterConfigDirPath);
    }

    Configuration conf = new Configuration();
    conf.addResource(new Path(clusterConfigDirPath + File.separator + "core-site.xml"));
    conf.addResource(new Path(clusterConfigDirPath + File.separator + "hdfs-site.xml"));
    conf.addResource(new Path(clusterConfigDirPath + File.separator + "yarn-site.xml"));
    conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
    return conf;
  }

  // ==================== Getter & Setter ====================

  public String getHadoopConfigPath() {
    return hadoopConfigPath;
  }

  public FileSystem getFileSystem() {
    return fileSystem;
  }

  public YarnClient getYarnClient() {
    return yarnClient;
  }


}
