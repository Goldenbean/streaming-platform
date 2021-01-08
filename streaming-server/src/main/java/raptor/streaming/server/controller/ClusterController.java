package raptor.streaming.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import raptor.streaming.dao.entity.ClusterEntity;
import raptor.streaming.hadoop.HadoopClient;
import raptor.streaming.hadoop.bean.YarnClusterPO;
import raptor.streaming.common.constants.Constant;
import raptor.streaming.common.domain.CustomPage;
import raptor.streaming.common.http.DataResult;
import raptor.streaming.common.http.RestResult;
import raptor.streaming.dao.service.ClusterService;
import raptor.streaming.server.service.HadoopService;



@RestController
@RequestMapping(value = Constant.API_PREFIX_URI + "/cluster")
@Api(tags = "集群管理")
public class ClusterController {

  private static final Logger log = LoggerFactory.getLogger(ClusterController.class);

  @Autowired
  private ClusterService clusterService;

  @Autowired
  private HadoopService hadoopService;

  @ApiOperation(value = "分页获取集群列表")
  @GetMapping(value = "/list")
  public DataResult list(
      @RequestParam(value = "curPage", required = false, defaultValue = "1") Integer curPage,
      @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize
  ) {
    Page<ClusterEntity> page = clusterService.page(new Page<>(curPage, pageSize));
    return new DataResult<>(new CustomPage(page));
  }

  @ApiOperation(value = "添加集群")
  @PostMapping(value = "/")
  public RestResult add(@RequestParam("name") String name,
      @RequestParam("type") int type,
      @RequestParam(value = "remark", required = false) String remark,
      @RequestParam(value = "spuConf", required = false) String spuConf,
      @RequestParam(value = "configFile", required = false) MultipartFile configFile) {

    try {
      return clusterService.addCluster(name, type, remark, spuConf, configFile);
    } catch (IOException e) {
      log.error(ExceptionUtils.getFullStackTrace(e));
    }
    return new RestResult(false, 500, "请求异常");
  }


  @PostMapping(value = "/update")
  public RestResult update(@RequestParam("name") String name,
      @RequestParam(value = "type", required = false) int type,
      @RequestParam(value = "remark", required = false) String remark,
      @RequestParam(value = "spuConf", required = false) String spuConf,
      @RequestParam(value = "configFile", required = false) MultipartFile file) {
    try {
      return clusterService
          .updateCluster(name, type, remark, spuConf, file);
    } catch (IOException e) {
      log.error(ExceptionUtils.getFullStackTrace(e));
    }

    return new RestResult(false, 500, "请求异常");
  }


  @DeleteMapping(value = "/{name}/")
  public RestResult delete(@PathVariable("name") String name,
      @RequestParam(value = "id", required = true) long id) {

    if (clusterService.removeById(id)) {
      String localConfigPath = Constant.CONFIG_DIR_BASE + File.separator + name;
      File localConfigDir = new File(localConfigPath);
      String hdfsConfigPath = Constant.STREAM_ENGINES_BASE_DIR + File.separator + name;
      try {
        FileUtils.deleteDirectory(localConfigDir);
        hadoopService.destroyCluster(name, hdfsConfigPath);
      } catch (IOException e) {
        log.error("", e);
        return new RestResult(false, 500, "删除失败");
      }

      return new RestResult(true, 200, "删除成功");
    }
    return new RestResult(false, 500, "删除失败");
  }

  @GetMapping(value = "/{name}/basic")
  public RestResult getBasic(@PathVariable("name") String name) {
    ClusterEntity cluster = clusterService
        .getOne(new QueryWrapper<ClusterEntity>().lambda().eq(ClusterEntity::getName, name));
    if (cluster == null) {
      return new RestResult(true, 404, "集群不存在");
    }

    return new DataResult<>(cluster);
  }

  @GetMapping(value = "/{name}/metrics")
  public RestResult getOverview(@PathVariable("name") String name) {
    ClusterEntity cluster = clusterService
        .getOne(new QueryWrapper<ClusterEntity>().lambda().eq(ClusterEntity::getName, name));
    if (cluster == null) {
      return new RestResult(true, 404, "集群不存在");
    }

//    if (clusterPO.getType() == ClusterTypeEnum.YARN.getCode()) {
    HadoopClient hadoopClient = hadoopService.getHadoopClient(name);
    if (hadoopClient == null) {
      return new RestResult(false, 404, "集群不存在或未上传集群配置文件");
    }

    try {
      YarnClusterPO yarnOverview = hadoopClient.getYarnOverview();
      if (cluster.getTotalCores() == null) {
        cluster.setTotalCores(yarnOverview.getCoresTotal());
        cluster.setTotalMemory((double) Math.round(yarnOverview.getMemTotal() / 1024 * 100) / 100);
        cluster.setTotalNodes(yarnOverview.getNodeList().size());
        clusterService.update(cluster,
            new QueryWrapper<ClusterEntity>().lambda().eq(ClusterEntity::getName, name));
      }
      return new DataResult<>(yarnOverview);
    } catch (Exception e) {
      log.error(ExceptionUtils.getFullStackTrace(e));
    }
    return new RestResult(false, 500, "请求失败");
  }

}

