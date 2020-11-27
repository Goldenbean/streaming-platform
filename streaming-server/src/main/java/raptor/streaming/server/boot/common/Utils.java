package raptor.streaming.server.boot.common;

/**
 * Created by azhe on 2020-11-18 16:44
 */

import com.alibaba.fastjson.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class Utils {

  private static final Logger log = LoggerFactory.getLogger(Utils.class);

  private static final String PATTERN_STR = "(?i)create\\s+table\\s+(\\S+)\\s*\\((.+)\\)\\s*with\\s*\\((.+)\\)";

  private static final Pattern PATTERN = Pattern.compile(PATTERN_STR);

  public static String zipData(byte[] data, String zipName) {
    InputStream inputStream = null;
    OutputStream outputStream = null;
    try {
      inputStream = new ByteArrayInputStream(data);
      File file = new File(zipName);
      outputStream = new FileOutputStream(file);
      byte[] buffer = new byte[1024 * 1024 * 2];
      int i;
      while ((i = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, i);
      }
      log.info("downloaded config file:" + zipName);
      return zipName;
    } catch (Exception e) {
      log.error("error download config file:" + ExceptionUtils
          .getRootCauseMessage(e));
      return null;
    } finally {
      try {
        if (outputStream != null) {
          outputStream.flush();
          outputStream.close();
        }
        if (inputStream != null) {
          inputStream.close();
        }
      } catch (Exception e) {
        log.error(e.getMessage());
      }
    }
  }

  public static String unZip(String zipFile, String destDir) {
    File dir = new File(destDir);
    if (!dir.exists()) {
      if (!dir.mkdirs()) {
        return null;
      }
    }
    destDir = destDir.endsWith(File.separator) ? destDir : destDir + File.separator;
    ZipArchiveInputStream is = null;
    OutputStream os = null;
    try {
      is = new ZipArchiveInputStream(
          new BufferedInputStream(new FileInputStream(zipFile), 1024));
      ZipArchiveEntry entry = null;

      while ((entry = is.getNextZipEntry()) != null) {
        if (!entry.isDirectory()) {
          String fileName = entry.getName();
          if (!fileName.startsWith("_") && fileName.contains("/")) {
            fileName = fileName.split("/")[1];
          }
          if (!fileName.startsWith(".") && !fileName.startsWith("_")) {
            os = new BufferedOutputStream(
                new FileOutputStream(new File(destDir, fileName)), 1024);
            IOUtils.copy(is, os);
            IOUtils.closeQuietly(os);
          }
        }
      }
      log.info("extract config file {} to {}", zipFile, destDir);
    } catch (Exception e) {
      log.error(e.getMessage());
      return null;
    } finally {
      IOUtils.closeQuietly(os);
      IOUtils.closeQuietly(is);
    }

    return destDir;
  }

  public static void uploadMultipartFileToHDFS(MultipartFile file, Path destDir,
      FileSystem fs) throws IOException {
    String srcDir = new File("").getAbsolutePath() + File.separator + file
        .getOriginalFilename();
    File localFile = new File(srcDir);
    file.transferTo(localFile);
    fs.copyFromLocalFile(true, true, new Path(srcDir), destDir);
  }

  public static String getDataCenter() {
    return StringUtils.isEmpty(System.getenv("DC")) || "TE"
        .equalsIgnoreCase(System.getenv("DC"))
        ? "default" : System.getenv("DC");
  }


  public static String getUserJarDir(String ownerId, FileSystem fs)
      throws IOException {
    String uri = File.separator + "user" + File.separator + ownerId;
    Path dir = new Path(uri);
    if (!fs.exists(dir)) {
      fs.mkdirs(dir);
    }
    return uri;
  }
}
