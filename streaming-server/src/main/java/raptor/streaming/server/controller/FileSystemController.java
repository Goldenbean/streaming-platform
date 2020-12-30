package raptor.streaming.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import raptor.streaming.server.common.constants.Constant;
import raptor.streaming.server.common.entity.CustomPage;
import raptor.streaming.server.common.entity.DataResult;
import raptor.streaming.server.common.entity.RestResult;
import raptor.streaming.server.entity.FileSystemEntity;
import raptor.streaming.server.service.FileSystemService;

/**
 * <p>
 * 数仓管理 前端控制器
 * </p>
 *
 * @author azhe
 * Created by azhe on 2020-12-16 17:27
 */
@RestController
@RequestMapping(value = Constant.API_PREFIX_URI + "/fileSystem")
@Api(tags = "文件系统")
public class FileSystemController {

  @Autowired
  private FileSystemService fileSystemService;


  @ApiOperation(value = "文件列表")
  @GetMapping(value = "/list")
  public DataResult list(
      @RequestParam(value = "appKey") Integer appKey,
      @RequestParam(value = "tabFolder") String tabFolder,
      @RequestParam(value = "parentId", required = false, defaultValue = "-1") Integer parentId,
      @RequestParam(value = "onlyDir", required = false, defaultValue = "false") boolean onlyDir,
      @RequestParam(value = "searchKey", required = false, defaultValue = "") String searchKey

  ) {

    List<FileSystemEntity> fileList = fileSystemService.lambdaQuery()
        .eq(FileSystemEntity::getTabFolder, tabFolder)
        .eq(FileSystemEntity::getAppKey, appKey)
        .eq(FileSystemEntity::getParentId, parentId)
        .orderByAsc(FileSystemEntity::getType)
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

    List<FileSystemEntity> fileList = fileSystemService.lambdaQuery()
        .eq(FileSystemEntity::getTabFolder, tabFolder)
        .eq(FileSystemEntity::getAppKey, appKey)
        .like(FileSystemEntity::getName, searchKey)
        .orderByAsc(FileSystemEntity::getType)
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
  public RestResult add(@RequestBody FileSystemEntity fileSystemEntity) {
    try {
      fileSystemService.save(fileSystemEntity);
      return new DataResult<>(fileSystemEntity.getId());
    } catch (Exception e) {
      e.printStackTrace();
      return RestResult.getFailed("添加失败！");
    }
  }

  @DeleteMapping(value = "/{name}/")
  public RestResult delete(@PathVariable("name") String name, @RequestParam(value = "id", required = true) long id) {
    if (fileSystemService.removeById(id)) {
      return RestResult.getSuccess();
    } else {
      return RestResult.getFailed();
    }
  }

  @PostMapping(value = "/update")
  public RestResult update(@RequestBody FileSystemEntity fileSystemEntity) {
    if (fileSystemService.updateById(fileSystemEntity)) {
      return RestResult.getSuccess();
    }
    return RestResult.getFailed();
  }

  @ApiOperation(value = "文件列表")
  @GetMapping(value = "/deleted")
  public DataResult deletedList(
      @RequestParam(value = "appKey") Integer appKey

  ) {
    List<FileSystemEntity> fileList = fileSystemService.selectLogicDeleted();
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


  public Map<String, Object> parseFile(FileSystemEntity file, boolean isLeaf) {
    Map<String, Object> map = Maps.newHashMap();
    map.put("key", file.getId());
    map.put("title", file.getName());
    map.put("isLeaf", isLeaf);
    map.put("parentPath", file.getParentPath());
    map.put("type", file.getType());
    return map;
  }

}
