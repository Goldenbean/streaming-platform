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
 * 集群列表
 * </p>
 *
 * @author azhe
 * @since 2020-12-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_cluster")
@ApiModel(value="ClusterEntity对象", description="集群列表")
public class ClusterEntity extends BaseEntity<ClusterEntity> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "集群名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "集群类型, 0 standalone 1 yarn 2 k8s")
    @TableField("type")
    private Integer type;

    @ApiModelProperty(value = "yarn集群配置文件，zip压缩包")
    @TableField("config_file")
    private byte[] configFile;

    @ApiModelProperty(value = "yarn集群配置文件md5值")
    @TableField("config_file_md5")
    private String configFileMd5;

    @ApiModelProperty(value = "配置文件名称")
    @TableField("config_file_name")
    private String configFileName;

    @ApiModelProperty(value = "集群计算单元的配置")
    @TableField("spu_conf")
    private String spuConf;

    @ApiModelProperty(value = "集群配置信息")
    @TableField("cluster_conf")
    private String clusterConf;

    @ApiModelProperty(value = "计算节点数量")
    @TableField("total_nodes")
    private Integer totalNodes;

    @ApiModelProperty(value = "集群内存总量(GB)")
    @TableField("total_memory")
    private Double totalMemory;

    @ApiModelProperty(value = "集群核数总量")
    @TableField("total_cores")
    private Integer totalCores;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
