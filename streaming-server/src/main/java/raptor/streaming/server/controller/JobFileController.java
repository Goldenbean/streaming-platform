package raptor.streaming.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
import raptor.streaming.common.utils.http.DataResult;
import raptor.streaming.common.utils.http.RestResult;
import raptor.streaming.dao.entity.JobFile;
import raptor.streaming.server.repository.JobFileRepository;

/**
 * <p>
 * 数仓管理 前端控制器
 * </p>
 *
 * @author azhe Created by azhe on 2020-12-16 17:27
 */
@RestController
@RequestMapping(value = Constant.API_PREFIX_URI + "/jobFile")
@Api(tags = "作业文件")
public class JobFileController {

  @Autowired
  private JobFileRepository jobFileRepository;

  @ApiOperation(value = "获取作业文件")
  @GetMapping(value = "/get")
  public RestResult get(
      @RequestParam(value = "fileId") Integer fileId) {

    JobFile jobFile = jobFileRepository
        .getOne(new QueryWrapper<JobFile>().lambda().eq(JobFile::getFileId, fileId));

    if (jobFile != null) {
      return new DataResult<>(jobFile);
    } else {
      return RestResult.getFailed();
    }
  }

  @ApiOperation(value = "保存作业文件")
  @PostMapping(value = "/save")
  public RestResult save(@RequestBody JobFile jobFile) {
    if (jobFileRepository.saveOrUpdate(jobFile, Wrappers.<JobFile>lambdaUpdate()
        .eq(JobFile::getFileId, jobFile.getFileId()))) {
      return RestResult.getSuccess();
    } else {
      return RestResult.getFailed();
    }
  }

  @DeleteMapping(value = "/{name}/")
  public RestResult delete(@PathVariable("name") String name,
      @RequestParam(value = "id", required = true) long id) {
    if (jobFileRepository.removeById(id)) {
      return RestResult.getSuccess();
    } else {
      return RestResult.getFailed();
    }
  }

  @PostMapping(value = "/update")
  public RestResult update(@RequestBody JobFile jobFile) {
    if (jobFileRepository.updateById(jobFile)) {
      return RestResult.getSuccess();
    }
    return RestResult.getFailed();
  }

}
