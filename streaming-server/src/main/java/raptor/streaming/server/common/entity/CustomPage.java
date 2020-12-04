package raptor.streaming.server.common.entity;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * @author: azhe
 * @className: CustomPage
 * @description: 自定义分页结果
 * @data: 2020-12-01 15:58
 **/
@ApiModel
@Data
public class CustomPage<T> {

  //当前页数
  @ApiModelProperty(value = "当前页数")
  private int curPage;

  //每页显示数量
  @ApiModelProperty(value = "每页显示数量")
  private int pageSize;

  @ApiModelProperty(value = "总条数")
  private int total;

  //数据列表
  @ApiModelProperty(value = "数据列表")
  private List<T> list;

  //总页数
  @ApiModelProperty(value = "总页数")
  private int totalPage;


  public CustomPage() {
  }

  @SuppressWarnings("deprecation")
  public CustomPage(Page<T> page) {
    this.curPage = (int) page.getCurrent();
    this.pageSize = (int) page.getSize();
    this.total = (int) page.getTotal();
    this.list = page.getRecords();
    this.totalPage = (int) page.getPages();
  }

}
