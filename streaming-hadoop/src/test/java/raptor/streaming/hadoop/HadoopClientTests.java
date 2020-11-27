package raptor.streaming.hadoop;


import com.alibaba.fastjson.JSON;
import java.io.File;
import java.io.IOException;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import raptor.streaming.hadoop.bean.YarnClusterPO;

public class HadoopClientTests {

  private static final Logger logger = LoggerFactory.getLogger(HadoopClientTests.class);

  private FileSystem fileSystem;
  private YarnClient yarnClient;
  private HadoopClient hadoopClient;

  private static final String FLINK_HOME = "../config";
  private static final String FLINK_VERSION = "flink-1.11";

  @Before
  public void setup() throws Exception {

    hadoopClient = new HadoopClient(FLINK_HOME + File.separator + "hadoop");
    hadoopClient.init();

    fileSystem = hadoopClient.getFileSystem();
    yarnClient = hadoopClient.getYarnClient();
  }

  @Test
  public void list() throws IOException {
    hadoopClient.list("/tmp")
        .forEach(obj -> logger.info("{}", JSON.toJSONString(obj, true)));
  }

  @Test
  public void recur() throws IOException {

    hadoopClient.listFile("/streaming-platform/flink/engines/")
        .forEach(obj -> logger.info("{}", JSON.toJSONString(obj, true)));
  }


  @Test
  public void testExistAndCreate() throws IOException {
    String path = "/user/demo";
    boolean exist = hadoopClient.exists(path, true);
    logger.info("path: [{}], exist: [{}]", path, exist);
  }


  @Test
  public void testHDFSMove() throws Exception {
    boolean ret = hadoopClient.move("/yarn/neishui/logs/application_1595986874344_0001",
        "/yarn/root/logs/application_1595986874344_0001");
    logger.info("move: [{}]", ret);
  }


  @Test
  public void testReadHDFSFile() {
    hadoopClient.readHDFSFile(
        "/streaming-platform/flink/engines/flink-1/system/job.sql",
        System.out);
  }

  @Test
  public void testUploadLib() throws IOException {

    String path = "";

    String src = FLINK_HOME + "/flink-1.11-SNAPSHOT/lib";
    File file = new File(src);

    for (String fileName : file.list()) {
      logger.info("file name [{}]", fileName);
      String localPath = src + "/" + fileName;
      hadoopClient.upload(localPath, path);
    }
  }

  @Test
  public void testUploadUserJar() throws IOException {
    String src = FLINK_HOME + "/flink-11-mirror-1.0.0-SNAPSHOT.jar";
    String dst = "";
    hadoopClient.upload(src, dst);
  }


  @Test
  public void testRename() throws IOException {
    boolean ret = hadoopClient.rename("/tmp-1", "/tmp");
    logger.info("rename {}", ret);
  }


  @Test
  public void testGetYarnApplication() throws YarnException, IOException {
    hadoopClient.getYarnApplication()
        .forEach(System.out::println);
  }

  @Test
  public void testGetYarnApplicationId() throws YarnException, IOException {
    hadoopClient.getYarnApplicationId(true)
        .forEach(System.out::println);
  }

  @Test
  public void testKillYarnApplication() throws YarnException, IOException {
    String appId = "application_1595986874344_0001";
    hadoopClient.killYarnApplication(appId);
  }

  @Test
  public void testGetYarnOverview() throws YarnException, IOException {

    YarnClusterPO yarnClusterPO = hadoopClient.getYarnOverview();
    logger.info("{}", JSON.toJSONString(yarnClusterPO, true));
  }

}
