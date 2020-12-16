package raptor.streaming.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import raptor.streaming.server.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 数仓表
 * </p>
 *
 * @author azhe
 * @since 2020-12-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dw_table")
@ApiModel(value="TableEntity对象", description="数仓表")
public class TableEntity extends BaseEntity<TableEntity> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "维度表表名")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "表类型, 0 实时数仓表 1 维度数仓表 2 离线数仓表")
    @TableField("type")
    private Integer type;

    @ApiModelProperty(value = "数据源")
    @TableField("source_name")
    private String sourceName;

    @ApiModelProperty(value = "项目id")
    @TableField("app_key")
    private Long appKey;

    @ApiModelProperty(value = "表结构")
    @TableField("table_schema")
    private String tableSchema;

    @ApiModelProperty(value = "其他配置")
    @TableField("config_json")
    private String configJson;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
