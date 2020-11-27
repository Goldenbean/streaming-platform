package raptor.streaming.server.boot.constants;

/**
 * Created by azhe on 2020-11-18 16:42
 */

import java.io.File;
import org.apache.commons.io.FileUtils;

public class Constant {

  public static final String API_PREFIX = "/api";
  public static final String URI_SEPARATOR = "/";
  public static final String VERSION = "v2";
  public static final String API_PREFIX_URI=API_PREFIX+URI_SEPARATOR+VERSION;
  public static final String STREAM_FLINK_DIST_JAR = "stream.flink.dist.jar";
  public static final String STREAM_YARN_QUEUE = "stream.yarn.queue";
  public static final String STREAM_YARN_CLUSTER_NAME = "stream.yarn.cluster.name";
  public static final String TASK_MANAGER_NUM_TASKS = "taskmanager.numberOfTasks";
  public static final String CONFIG_DIR_BASE;
  public static final String CONFIG_LOGBACK_PATH = "stream.logback.path";
  public static final String STREAM_USER_NAME = "stream.user.name";
  public static final String STREAM_PARALLELISM = "stream.parallelism";
  public static final String STREAM_ENTRY_CLASS = "stream.entry.class";
  public static final String STREAM_PROGRAM_ARGS = "stream.program.args";
  public static final String STREAM_SAVEPOINT_PATH = "stream.savepoint.path";
  public static final String STREAM_ALLOW_NON_RESTORE_STATE = "stream.allow.non.restore.state";
  public static final String STREAM_SPU_COUNT = "stream.spu.count";
  public static final String STREAM_JAR_NAMES = "stream.jar.name";
  public static final String STREAM_USER_JAR_DIR = "stream.user.jar.dir";
  public static final String STREAM_JOB_TYPE = "stream.job.type";
  public static final String STREAM_ENGINE_VERSION = "stream.engine.version";
  public static final String STREAM_ENGINES_BASE_DIR = "/streaming-platform/flink-cluster/engines";
  public static final String STREAM_PLUGINS_BASE_DIR = "/flink-plugins";
  public static final String LOCAL_SQL_PLUGINS_DIR;
  public static final int DEFAULT_STREAM_SPU_CPU = 1;
  public static final int DEFAULT_TASK_MANAGER_MEMORY = 5120;
  public static final int DEFAULT_JOB_MANAGER_MEMORY = 2048;
  public static final int DEFAULT_SLOTS_PER_TASKMANAGER = 2;
  public static final String STREAM_SPU_CPU_KEY = "stream.spu.cpu";
  public static final String STREAM_SPU_JM_KEY = "stream.spu.jm";
  public static final String STREAM_SPU_TM_KEY = "stream.spu.tm";
  public static final String STREAM_SPU_SLOTS_KEY = "stream.spu.slots";

  static {
    CONFIG_DIR_BASE = System.getProperty("user.home") + File.separator + ".streaming/" + VERSION + "/yarn";
    LOCAL_SQL_PLUGINS_DIR = System.getProperty("user.home") + File.separator + ".streaming/plugins";
  }

}

