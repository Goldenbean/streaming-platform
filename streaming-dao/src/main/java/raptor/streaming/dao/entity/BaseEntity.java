package raptor.streaming.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;


public class BaseEntity<T extends Model> extends Model {

  /**
   * 主键ID , 这里故意演示注解可以无
   */
  @TableId(value = "ID", type = IdType.AUTO)
  @ApiModelProperty("主键")
  private Long id;

  /**
   * 创建者
   */
  @TableField("creator")
  @ApiModelProperty("创建人")
  private String creator;

  /**
   * 更新者
   */
  @TableField("modifier")
  @ApiModelProperty("更新者")
  private String modifier;

  /**
   * 创建时间
   */
  @TableField(value = "gmt_create", fill = FieldFill.INSERT)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
  @ApiModelProperty(value = "创建时间", dataType = "java.util.Date")
  private Date gmtCreate;

  /**
   * 更新时间
   */
  @TableField(value = "gmt_modify", fill = FieldFill.INSERT)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
  @ApiModelProperty(value = "更新时间", dataType = "java.util.Date")
  private Date gmtModify;

  /**
   * 备注信息
   */
  @ApiModelProperty(value = "备注信息")
  private String remark;
//    /**
//     * 删除标记
//     */
//    @TableField(value = "DEL_FLAG", fill = FieldFill.INSERT)
//    @TableLogic(value = "0")
//    @ApiModelProperty(hidden = true)
//    private String delFlag;

  @Override
  protected Serializable pkVal() {
    return this.id;
  }

  public Long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  public String getModifier() {
    return modifier;
  }

  public void setModifier(String modifier) {
    this.modifier = modifier;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  public Date getGmtModify() {
    return gmtModify;
  }

  public void setGmtModify(Date gmtModify) {
    this.gmtModify = gmtModify;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }
}
