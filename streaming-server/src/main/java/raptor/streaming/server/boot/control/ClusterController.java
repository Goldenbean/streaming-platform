package raptor.streaming.server.boot.control;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.io.IOException;
import java.util.List;
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
import raptor.streaming.hadoop.HadoopClient;
import raptor.streaming.hadoop.bean.YarnClusterPO;
import raptor.streaming.server.boot.bean.DataResult;
import raptor.streaming.server.boot.bean.RestResult;
import raptor.streaming.server.boot.constants.Constant;
import raptor.streaming.server.boot.dao.model.ClusterPO;
import raptor.streaming.server.boot.service.ClusterService;
import raptor.streaming.server.boot.service.HadoopService;

@RestController
@RequestMapping(value = Constant.API_PREFIX_URI +"/cluster")
public class ClusterController {

  private static final Logger log = LoggerFactory
      .getLogger(ClusterController.class);

  @Autowired
  private ClusterService clusterService;

  @Autowired
  private HadoopService hadoopService;


  @GetMapping(value = "/list")
  public DataResult list(
      @RequestParam(value = "curPage", required = false, defaultValue = "1") Integer pageNum,
      @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize
  ) {
    PageHelper.startPage(pageNum, pageSize, true);
    List<ClusterPO> ClusterPOList = clusterService.getAllCluster();
    return new DataResult<>(new PageInfo<>(ClusterPOList));
  }


  @PostMapping(value = "/")
  public RestResult add(@RequestParam("name") String name,
      @RequestParam("type") int type,
      @RequestParam(value = "description", required = false) String description,
      @RequestParam(value = "spuConf", required = false) String spuConf,
      @RequestParam(value = "configFile", required = false) MultipartFile configFile) {

    try {
      return clusterService.addCluster(name, type, description, spuConf, configFile);
    } catch (IOException e) {
      log.error(ExceptionUtils.getFullStackTrace(e));
    }

    return new RestResult(false, 500, "请求异常");
  }


  @PostMapping(value = "/update")
  public RestResult update(@RequestParam("name") String name,
      @RequestParam(value = "type", required = false) int type,
      @RequestParam(value = "description", required = false) String description,
      @RequestParam(value = "spuConf", required = false) String spuConf,
      @RequestParam(value = "configFile", required = false) MultipartFile file) {
    try {
      return clusterService
          .updateCluster(name, type, description, spuConf, file);
    } catch (IOException e) {
      log.error(ExceptionUtils.getFullStackTrace(e));
    }

    return new RestResult(false, 500, "请求异常");
  }

  @DeleteMapping(value = "/{name}/")
  public RestResult delete(@PathVariable("name") String name) {
    return clusterService.deleteCluster(name);
  }


  @GetMapping(value = "/{name}/basic")
  public RestResult getBasic(@PathVariable("name") String name) {
    ClusterPO clusterPO = clusterService.getCluster(name);
    if (clusterPO == null) {
      return new RestResult(true, 404, "集群不存在");
    }

    return new DataResult<>(clusterPO);
  }

  @GetMapping(value = "/{name}/metrics")
  public RestResult getOverview(@PathVariable("name") String name) {
    ClusterPO clusterPO = clusterService.getCluster(name);
    if (clusterPO == null) {
      return new RestResult(true, 404, "集群不存在");
    }

//    if (clusterPO.getType() == ClusterTypeEnum.YARN.getCode()) {
    HadoopClient hadoopClient = hadoopService.getHadoopClient(name);
    if (hadoopClient == null) {
      return new RestResult(false, 404, "集群不存在或未上传集群配置文件");
    }

    try {
      YarnClusterPO yarnOverview = hadoopService.getYarnOverview(name);

      if (clusterPO.getTotalCores() == 0) {
        clusterPO.setTotalCores(yarnOverview.getCoresTotal());
        clusterPO.setTotalMemory((double) Math.round(yarnOverview.getMemTotal()/1024*100)/100);
        clusterPO.setTotalNodes(yarnOverview.getNodeList().size());

        clusterService.updateCluster(clusterPO);
      }

      return new DataResult<>(yarnOverview);
    } catch (Exception e) {
      log.error(ExceptionUtils.getFullStackTrace(e));
    }
//    }

    return new RestResult(false, 500, "请求失败");
  }

}
