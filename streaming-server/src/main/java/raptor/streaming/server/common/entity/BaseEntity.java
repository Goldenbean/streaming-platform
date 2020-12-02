/**
 * projectName: mybatis-plus
 * fileName: BaseEntity.java
 * packageName: com.fendo.mybatis.plus.common.persistent
 * date: 2018-03-24 18:12
 * copyright(c) 2017-2020 xxx公司
 */
package raptor.streaming.server.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author: azhe
 * @className: BaseEntity
 * @description: 实体类基类
 * @data: 2020-12-01 15:58
 **/
@Data
public class BaseEntity<T extends Model> extends Model {

    /**
     * 主键ID , 这里故意演示注解可以无
     */
    @TableId(value="ID",type = IdType.AUTO)
    @ApiModelProperty("主键")
    private long id;

    /**
     * 创建者
     */
    @TableField("creater")
    @ApiModelProperty("创建人")
    private String creater;

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


}
