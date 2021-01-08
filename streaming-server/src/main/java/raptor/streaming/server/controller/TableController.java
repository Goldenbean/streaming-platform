package raptor.streaming.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import raptor.streaming.dao.entity.Table;
import raptor.streaming.common.constants.Constant;
import raptor.streaming.common.domain.CustomPage;
import raptor.streaming.common.utils.http.DataResult;
import raptor.streaming.common.utils.http.RestResult;
import raptor.streaming.server.repository.TableRepository;

/**
 * <p>
 * 数仓管理 前端控制器
 * </p>
 *
 * @author azhe Created by azhe on 2020-12-07 17:27
 */
@RestController
@RequestMapping(value = Constant.API_PREFIX_URI + "/table")
@Api(tags = "数仓管理")
public class TableController {

  @Autowired
  private TableRepository tableRepository;


  @ApiOperation(value = "分页获取数仓列表")
  @GetMapping(value = "/list")
  public DataResult list(
      @RequestParam(value = "curPage", required = false, defaultValue = "1") Integer curPage,
      @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
      @RequestParam(value = "type") Integer type,
      @RequestParam(value = "appKey") Integer appKey

  ) {
    Page<Table> page = tableRepository.lambdaQuery().eq(Table::getType, type)
        .eq(Table::getAppKey, appKey).page(new Page<>(curPage, pageSize));
    return new DataResult<>(new CustomPage(page));
  }

  @ApiOperation(value = "添加用户")
  @PostMapping(value = "/")
  public RestResult add(@RequestBody Table table) {
    if (tableRepository.save(table)) {
      return RestResult.getSuccess();
    } else {
      return RestResult.getFailed();
    }
  }

  @DeleteMapping(value = "/{name}/")
  public RestResult delete(@PathVariable("name") String name,
      @RequestParam(value = "id", required = true) long id) {
    if (tableRepository.removeById(id)) {
      return RestResult.getSuccess();
    } else {
      return RestResult.getFailed();
    }
  }

  @PostMapping(value = "/update")
  public RestResult update(@RequestBody Table table) {
    if (tableRepository.updateById(table)) {
      return RestResult.getSuccess();
    }
    return RestResult.getFailed();
  }
}
