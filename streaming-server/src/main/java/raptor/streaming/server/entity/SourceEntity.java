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
 * 数据源列表
 * </p>
 *
 * @author azhe
 * @since 2020-12-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dw_source")
@ApiModel(value="SourceEntity对象", description="数据源列表")
public class SourceEntity extends BaseEntity<SourceEntity> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "数据源名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "数据源类型, 0 mysql 1 kafka 2 hive 3 clickhouse")
    @TableField("type")
    private Integer type;

    @ApiModelProperty(value = "IP地址")
    @TableField("ip")
    private String ip;

    @ApiModelProperty(value = "配置信息")
    @TableField("config")
    private String config;

    @ApiModelProperty(value = "项目id")
    @TableField("app_key")
    private Long appKey;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
