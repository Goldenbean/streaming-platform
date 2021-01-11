package raptor.streaming.common.domain;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 * @author: azhe
 * @className: CustomPage
 * @description: 自定义分页结果
 * @data: 2020-12-01 15:58
 **/
@ApiModel
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

  public int getCurPage() {
    return curPage;
  }

  public void setCurPage(int curPage) {
    this.curPage = curPage;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public List<T> getList() {
    return list;
  }

  public void setList(List<T> list) {
    this.list = list;
  }

  public int getTotalPage() {
    return totalPage;
  }

  public void setTotalPage(int totalPage) {
    this.totalPage = totalPage;
  }
}
