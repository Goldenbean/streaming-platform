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
import raptor.streaming.common.constants.Constant;
import raptor.streaming.common.domain.CustomPage;
import raptor.streaming.common.utils.http.DataResult;
import raptor.streaming.common.utils.http.RestResult;
import raptor.streaming.dao.entity.Project;
import raptor.streaming.server.repository.ProjectRepository;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author azhe
 * @since 2020-12-03
 */
@RestController
@RequestMapping(value = Constant.API_PREFIX_URI + "/project")
@Api(tags = "项目管理")

public class ProjectController {

  @Autowired
  private ProjectRepository projectRepository;


  @ApiOperation(value = "分页获取集群列表")
  @GetMapping(value = "/list")
  public DataResult list(
      @RequestParam(value = "curPage", required = false, defaultValue = "1") Integer curPage,
      @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize
  ) {
    Page<Project> page = projectRepository.page(new Page<>(curPage, pageSize));
    return new DataResult<>(new CustomPage(page));
  }

  @ApiOperation(value = "添加项目")
  @PostMapping(value = "/")
  public RestResult add(@RequestBody Project project) {
    if (projectRepository.save(project)) {
      return RestResult.getSuccess();
    } else {
      return RestResult.getFailed();
    }
  }

  @DeleteMapping(value = "/{name}/")
  public RestResult delete(@PathVariable("name") String name,
      @RequestParam(value = "id", required = true) long id) {
    if (projectRepository.removeById(id)) {
      return RestResult.getSuccess();
    } else {
      return RestResult.getFailed();
    }
  }

  @PostMapping(value = "/update")
  public RestResult update(@RequestBody Project project) {
    if (projectRepository.updateById(project)) {
      return RestResult.getSuccess();
    }
    return RestResult.getFailed();
  }

  @ApiOperation(value = "根据AppKey获取集群名称")
  @GetMapping(value = "/getClusterName")
  public RestResult getClusterName(
      @RequestParam(value = "id", required = true, defaultValue = "1") Long id
  ) {
    String clusterName = projectRepository.selectClusterNameByPid(id);

    if (clusterName != null) {
      return new DataResult<>(clusterName);
    }
    return RestResult.getFailed("该项目指定的集群不存在!");
  }

}

