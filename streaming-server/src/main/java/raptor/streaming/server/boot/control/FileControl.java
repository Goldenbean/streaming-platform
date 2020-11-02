package raptor.streaming.server.boot.control;

import raptor.streaming.server.boot.BootUtil;
import raptor.streaming.server.boot.bean.DataResult;
import raptor.streaming.server.boot.bean.RestResult;
import raptor.streaming.server.boot.service.HadoopService;
import raptor.streaming.server.domain.Tuple2;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import raptor.streaming.hadoop.bean.FilePO;

@RestController
@RequestMapping(value = "/api/v1/files/")
public class FileControl {

  private static final Logger logger = LoggerFactory.getLogger(FileControl.class);

  @Autowired
  private HadoopService hadoopService;


  @GetMapping(value = "/list")
  public RestResult list(
      @RequestParam(value = "path", required = true) String path) {
    logger.info("list: path [{}]", path);

    try {
      List<FilePO> ret = hadoopService.listFolder(path);
      List<FilePO> folderList = ret.stream()
          .filter(obj -> !obj.isDir())
          .collect(Collectors.toList());
      Collections.sort(folderList, new Comparator<FilePO>() {
        @Override
        public int compare(FilePO o1, FilePO o2) {
          return (int) (o2.getModificationTime() - o1.getModificationTime());
          //return o1.getName().compareTo(o2.getName());
        }
      });
      return new DataResult<>(folderList);
    } catch (Exception ex) {
      logger.error("", ex);
      return RestResult.getFailed();
    }
  }

  @PostMapping(value = "/upload")
  public RestResult upload(@RequestParam("path") String path,
      @RequestParam("file") MultipartFile file) {

    try {
      Tuple2<String, String> ret = BootUtil.saveMultipartFile(file, false);
      logger.info("upload: src [{}], dst [{}]", ret.getSecond(), path);
      hadoopService.upload(ret.getSecond(), path + "/" + ret.getFirst());
      return new RestResult(true, 200, "");

    } catch (Exception ex) {
      logger.error("", ex);
      return RestResult.getFailed();
    }

  }

  @GetMapping(value = "/read")
  public RestResult read(@RequestParam("path") String path) {
    logger.info("read: path [{}]", path);

    String ret = hadoopService.read(path);
    //   ret.replaceAll("\n", "<BR>");
    return new DataResult<>(ret);
  }


  @GetMapping(value = "/folder")
  public RestResult listFolder(
      @RequestParam(value = "path", required = true) String path) {
    logger.info("list: path [{}]", path);

    try {
      List<FilePO> ret = hadoopService.listFolder(path);
      List<FilePO> folderList = ret.stream()
          .filter(obj -> obj.isDir())
          .collect(Collectors.toList());
      return new DataResult<>(folderList);
    } catch (Exception ex) {
      logger.error("", ex);
      return RestResult.getFailed();
    }
  }

  @PostMapping(value = "/folder/create")
  public RestResult createFolder(
      @RequestParam(value = "path", required = true) String path) {
    try {

      hadoopService.createFolder(path);
      return new RestResult(true, 200, "");

    } catch (Exception ex) {
      logger.error("", ex);
      return RestResult.getFailed();
    }
  }

  @PostMapping(value = "/folder/rename")
  public RestResult renameFolder(
      @RequestParam(value = "path", required = true) String path,
      @RequestParam(value = "name", required = true) String name) {
    try {

      hadoopService.renameFolder(path, name);
      return new RestResult(true, 200, "");

    } catch (Exception ex) {
      logger.error("", ex);
      return RestResult.getFailed();
    }
  }

  @GetMapping(value = "/delete")
  public RestResult delete(@RequestParam("path") String path) {
    logger.info("delete: path [{}]", path);

    try {
      hadoopService.delete(path);
      return new RestResult(true, 200, "");

    } catch (Exception ex) {
      logger.error("", ex);
      return RestResult.getFailed();
    }
  }


}
