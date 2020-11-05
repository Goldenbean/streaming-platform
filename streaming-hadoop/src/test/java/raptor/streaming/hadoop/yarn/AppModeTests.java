package raptor.streaming.hadoop.yarn;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.apache.flink.client.deployment.ClusterDeploymentException;
import org.apache.flink.client.program.ClusterClientProvider;
import org.apache.flink.client.program.ProgramInvocationException;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppModeTests {

  private static final Logger logger = LoggerFactory.getLogger(YarnDeployTests.class);

  public static final String DIST_ROOT = "../../streaming-conf";
  public static final String JAR_PATH = "hdfs://streaming-cluster/streaming-platform/flink/user/demo/flink-app-11-mirror-1.0.0-SNAPSHOT.jar";

  DeployConfig deployConfig;
  List<String> args;
  ApplicationDeploy applicationDeploy;

  @Before
  public void init() {
    String uuid = UUID.randomUUID().toString();
    args = Arrays.asList("--uuid", uuid, "--group", "flink-demo-" + uuid);

    deployConfig = new DeployConfig("flink-demo-test", JAR_PATH, args);

    applicationDeploy = new ApplicationDeploy(DIST_ROOT + "/flink",
        DIST_ROOT + "/hadoop");
  }

  @Test
  public void testFunction()
      throws ProgramInvocationException, IOException, ClusterDeploymentException {

    ClusterClientProvider<ApplicationId> ret =
        applicationDeploy.deploy(deployConfig);

    ApplicationId id = ret.getClusterClient().getClusterId();
    System.out.println(id);
    System.out.println(id.getId());
  }


}
