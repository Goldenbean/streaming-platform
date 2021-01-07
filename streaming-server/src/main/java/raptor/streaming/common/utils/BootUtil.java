package raptor.streaming.common.utils;


import com.alibaba.fastjson.JSONObject;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import raptor.streaming.common.domain.Tuple2;

public class BootUtil {

  private static final Logger logger = LoggerFactory.getLogger(BootUtil.class);

  public static Tuple2<String, String> saveMultipartFile(MultipartFile multipartFile,
      boolean chksum)
      throws IOException {

    String srcDir = new File("/tmp").getAbsolutePath()
        + File.separator + "streaming-upload"
        + File.separator + UUID.randomUUID().toString();

    File localFile = new File(srcDir);

    if (!localFile.exists()) {
      localFile.mkdirs();
    }

    String filename = multipartFile.getOriginalFilename();

    if (chksum) {
      byte[] data = multipartFile.getBytes();
      String md5sum = md5(data);
      logger.info("md5sum [{}]", md5sum);

      filename = filename + "-" + md5sum;
    }

    String path = srcDir + File.separator + filename;

    multipartFile.transferTo(new File(path));
    return new Tuple2<>(filename, path);
  }

  public static String md5(String input) {
    return md5(input.getBytes(StandardCharsets.UTF_8));
  }

  public static String md5(byte[] input) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(input);
      byte[] digest = md.digest();
      return toHexString(digest);
    } catch (NoSuchAlgorithmException ex) {

    }
    return "";
  }

  public static String toHexString(byte bytes[]) {
    StringBuilder hs = new StringBuilder();
    String stmp = "";
    for (int n = 0; n < bytes.length; n++) {
      stmp = Integer.toHexString(bytes[n] & 0xff);
      if (stmp.length() == 1) {
        hs.append("0").append(stmp);
      } else {
        hs.append(stmp);
      }
    }
    return hs.toString();
  }


  public static String toJson(Object object) {
    return JSONObject.toJSONString(object, true);
  }


  public static Date now() {
    return Calendar.getInstance().getTime();
  }

  public static Timestamp timestamp() {
    return new Timestamp(now().getTime());
  }


  public static void sleep(int sec) {
    try {
      TimeUnit.SECONDS.sleep(sec);
    } catch (InterruptedException ex) {
      logger.info("", ex);
    }
  }
}
