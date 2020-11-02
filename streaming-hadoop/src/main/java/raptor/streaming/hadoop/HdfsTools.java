package raptor.streaming.hadoop;


import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import raptor.streaming.hadoop.bean.FilePO;

public class HdfsTools {

  private static final Logger logger = LoggerFactory.getLogger(HdfsTools.class);

  public static FilePO toFilePO(FileStatus status) {
    FilePO filePO = new FilePO();

    filePO.setName(status.getPath().getName());
    filePO.setParent(status.getPath().getParent().toString());
    filePO.setPath(status.getPath().toString());
    filePO.setLength(status.getLen());
    filePO.setGroup(status.getGroup());
    filePO.setOwner(status.getOwner());

    filePO.setDir(status.isDirectory());
    filePO.setAccessTime(status.getAccessTime());
    filePO.setModificationTime(status.getModificationTime());

    filePO.setReplication(status.getReplication());
    return filePO;
  }


  public static List<Map<String, Object>> list(FileSystem fileSystem, String path)
      throws IOException {
    return list(fileSystem, new Path(path));
  }

  public static List<Map<String, Object>> list(FileSystem fileSystem, Path dir) throws IOException {
    List<Map<String, Object>> ret = new ArrayList<>();

    RemoteIterator<LocatedFileStatus> files = fileSystem.listFiles(dir, false);

    while (files.hasNext()) {
      LocatedFileStatus file = files.next();

      ImmutableMap<String, Object> map = ImmutableMap.of(
          "name", file.getPath().getName(),
          "size", file.getLen(),
          "uploaded", file.getAccessTime(),
          "id", file.getPath().getName()
      );

      ret.add(map);
    }
    return ret;
  }


  public static void print(FileSystem fileSystem, String path) throws IOException {
    list(fileSystem, new Path(path)).forEach(obj -> logger.info("{}", obj));
  }


  public static InputStream readFile(FileSystem fileSystem, String filePath) throws IOException {
    Path path = new Path(filePath);
    if (fileSystem.exists(path)) {
      return fileSystem.open(path);
    }
    return null;
  }


  public static List<String> listStatus(FileSystem fileSystem, String path) throws IOException {

    List<String> versions = new ArrayList<>();

    FileStatus[] files = fileSystem.listStatus(new Path(path));
    for (FileStatus file : files) {
      versions.add(file.getPath().getName());
    }
    return versions;

  }


}
