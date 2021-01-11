package raptor.streaming.server.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import raptor.streaming.common.constants.Constant;
import raptor.streaming.common.utils.http.DataResult;
import raptor.streaming.common.utils.http.RestResult;
import raptor.streaming.dao.entity.FileSystem;
import raptor.streaming.server.repository.FileSystemRepository;


@RestController
@RequestMapping(value = Constant.API_PREFIX_URI + "/fileSystem")
@Api(tags = "文件系统")
public class FileSystemController {

  @Autowired
  private FileSystemRepository fileSystemRepository;


  @ApiOperation(value = "文件列表")
  @GetMapping(value = "/list")
  public DataResult list(
      @RequestParam(value = "appKey") Integer appKey,
      @RequestParam(value = "tabFolder") String tabFolder,
      @RequestParam(value = "parentId", required = false, defaultValue = "-1") Integer parentId,
      @RequestParam(value = "onlyDir", required = false, defaultValue = "false") boolean onlyDir,
      @RequestParam(value = "searchKey", required = false, defaultValue = "") String searchKey

  ) {

    List<FileSystem> fileList = fileSystemRepository.lambdaQuery()
        .eq(FileSystem::getTabFolder, tabFolder)
        .eq(FileSystem::getAppKey, appKey)
        .eq(FileSystem::getParentId, parentId)
        .orderByAsc(FileSystem::getType)
        .list();

    List<Map<String, Object>> list = Lists.newArrayList();
    fileList.forEach(file -> {
      boolean isLeaf = !file.getType().equals("dir");
      Map<String, Object> map = parseFile(file, isLeaf);
      if (onlyDir) {
        if (!isLeaf) {
          list.add(map);
        }
      } else {
        list.add(map);
      }
    });

    return new DataResult<>(list);
  }

  @ApiOperation(value = "搜索文件列表")
  @GetMapping(value = "/search")
  public DataResult search(
      @RequestParam(value = "appKey") Integer appKey,
      @RequestParam(value = "tabFolder") String tabFolder,
      @RequestParam(value = "searchKey", required = false, defaultValue = "") String searchKey

  ) {

    List<FileSystem> fileList = fileSystemRepository.lambdaQuery()
        .eq(FileSystem::getTabFolder, tabFolder)
        .eq(FileSystem::getAppKey, appKey)
        .like(FileSystem::getName, searchKey)
        .orderByAsc(FileSystem::getType)
        .list();

    List<Map<String, Object>> list = Lists.newArrayList();
    fileList.forEach(file -> {
      boolean isLeaf = !file.getType().equals("dir");
      Map<String, Object> map = parseFile(file, isLeaf);
      if (isLeaf) {
        list.add(map);
      }
    });

    return new DataResult<>(list);
  }


  @ApiOperation(value = "添加")
  @PostMapping(value = "/")
  public RestResult add(@RequestBody FileSystem fileSystem) {
    try {
      fileSystemRepository.saveOrUpdate(fileSystem);
      return new DataResult<>(fileSystem.getId());
    } catch (Exception e) {
      e.printStackTrace();
      return RestResult.getFailed("添加失败！");
    }
  }

  @ApiOperation(value = "逻辑删除")
  @DeleteMapping(value = "/{name}/")
  public RestResult delete(@PathVariable("name") String name,
      @RequestParam(value = "id", required = true) long id) {
    if (fileSystemRepository.removeById(id)) {
      return RestResult.getSuccess();
    } else {
      return RestResult.getFailed();
    }
  }

  @ApiOperation(value = "物理删除")
  @DeleteMapping(value = "/remove")
  public RestResult removeByLogicId(@RequestParam(value = "id", required = true) long id) {
    if (fileSystemRepository.removeByLogicId(id) > 0) {
      return RestResult.getSuccess();
    } else {
      return RestResult.getFailed();
    }
  }

  @ApiOperation(value = "文件还原")
  @PostMapping(value = "/rollBack")
  public RestResult rollBack(@RequestBody FileSystem fileSystem) {
    if (fileSystemRepository.updateLogicData(fileSystem) > 0) {
      return RestResult.getSuccess();
    } else {
      return RestResult.getFailed();
    }
  }


  @PostMapping(value = "/update")
  public RestResult update(@RequestBody FileSystem fileSystem) {
    if (fileSystemRepository.updateById(fileSystem)) {
      return RestResult.getSuccess();
    }
    return RestResult.getFailed();
  }

  @ApiOperation(value = "文件列表")
  @GetMapping(value = "/deleted")
  public DataResult deletedList(
      @RequestParam(value = "appKey") Integer appKey

  ) {
    List<FileSystem> fileList = fileSystemRepository.selectLogicDeleted();
    List<Map<String, Object>> list = Lists.newArrayList();
    fileList.forEach(file -> {
      boolean isLeaf = !file.getType().equals("dir");
      Map<String, Object> map = parseFile(file, isLeaf);
      if (isLeaf) {
        list.add(map);
      }
    });
    return new DataResult<>(list);
  }


  public Map<String, Object> parseFile(FileSystem file, boolean isLeaf) {
    Map<String, Object> map = Maps.newHashMap();
    map.put("key", file.getId());
    map.put("title", file.getName());
    map.put("isLeaf", isLeaf);
    map.put("parentPath", file.getParentPath());
    map.put("type", file.getType());
    if (file.getType().equals("file")) {
      map.put("filePath", file.getFilePath());
    }
    return map;
  }

}
