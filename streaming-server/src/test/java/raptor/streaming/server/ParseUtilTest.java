package raptor.streaming.server;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;
import raptor.streaming.hadoop.yarn.DeployConfig;

/**
 * Created by azhe on 2021-01-05 16:36
 */
public class ParseUtilTest {

  @Test
  public void split() {
    String s = "a=b=c";
    final String[] split = s.split("=",2);

    System.out.println(Lists.newArrayList(split));
  }

  @Test
  public void parse() {
    String s = "-- Jar\n"
        + "-- ********************************************************************--\n"
        + "-- Author: wangzheqing2@qq.com\n"
        + "-- CreateTime: 2020-09-23 19:45:28\n"
        + "-- Comment: 请输入业务注释信息\n"
        + "-- ********************************************************************--\n"
        + "\n"
        + "-- 完整主类名，选填，例如 a.b.c.Application\n"
        + "entryPointClass=\n"
        + "\n"
        + "-- 包含完整主类名的JAR包资源名称，必填，例如stream-hive-merge-1.0-SNAPSHOT.jar\n"
        + "jarFilePath=hdfs://tdhdfs/streaming-platform/resources/admin/flink-app-mirror-1.0.0.jar\n"
        + "\n"
        + "-- 计算单元个数，SPU(Stream Processing Units)：2C8G\n"
        + "spu=10\n";

    Stream<String> stringStream = Arrays.stream(s.split("\n")).filter(line -> line.contains("=") && !line.startsWith("--"));

    DeployConfig deployConfig = new DeployConfig();

    stringStream.forEach(elem -> {
      final String[] kv = elem.split("=",2);
      if (kv.length == 2) {

        switch (kv[0]) {
          case "entryPointClass":
            deployConfig.setEntryPointClass(kv[1]);
            break;
          case "jarFilePath":
            deployConfig.setJarFilePath(kv[1]);
            break;
          case "programArgs":
            deployConfig.setProgramArgs(Lists.newArrayList(kv[1].split(",")));
            break;
          case "spu":
            deployConfig.setSpu(Integer.parseInt(kv[1]));
            break;
        }
      }

    });

    deployConfig.setApplicationName("abc");
    deployConfig.setClusterName("flink-1");

    System.out.println(JSON.toJSONString(deployConfig, true));

  }

}
