package raptor.streaming.deploy.yarn;

import org.apache.flink.util.FlinkException;
import org.apache.flink.yarn.YarnClusterDescriptor;
import org.apache.hadoop.yarn.api.records.ApplicationId;

public class DeployUtil {


  public static void killApplication(YarnClusterDescriptor yarnClusterDescriptor,
      ApplicationId applicationId) throws FlinkException {
    yarnClusterDescriptor.killCluster(applicationId);
  }
}
