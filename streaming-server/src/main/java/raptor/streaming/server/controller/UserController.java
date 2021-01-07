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
import raptor.streaming.dao.entity.UserEntity;
import raptor.streaming.dao.service.UserService;
import raptor.streaming.server.common.constants.Constant;
import raptor.streaming.server.common.domain.CustomPage;
import raptor.streaming.server.common.http.DataResult;
import raptor.streaming.server.common.http.RestResult;


@RestController
@RequestMapping(value = Constant.API_PREFIX_URI + "/user")
@Api(tags = "用户管理")
public class UserController {

  @Autowired
  private UserService userService;


  @ApiOperation(value = "分页获取集群列表")
  @GetMapping(value = "/list")
  public DataResult list(
      @RequestParam(value = "curPage", required = false, defaultValue = "1") Integer curPage,
      @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize
  ) {
    Page<UserEntity> page = userService.page(new Page<>(curPage, pageSize));
    return new DataResult<>(new CustomPage(page));
  }

  @ApiOperation(value = "添加用户")
  @PostMapping(value = "/")
  public RestResult add(@RequestBody UserEntity userEntity) {
    if (userService.save(userEntity)) {
      return RestResult.getSuccess();
    } else {
      return RestResult.getFailed();
    }
  }

  @DeleteMapping(value = "/{name}/")
  public RestResult delete(@PathVariable("name") String name,
      @RequestParam(value = "id", required = true) long id) {
    if (userService.removeById(id)) {
      return RestResult.getSuccess();
    } else {
      return RestResult.getFailed();
    }
  }

  @PostMapping(value = "/update")
  public RestResult update(@RequestBody UserEntity userEntity) {
    if (userService.updateById(userEntity)) {
      return RestResult.getSuccess();
    }
    return RestResult.getFailed();
  }


}

