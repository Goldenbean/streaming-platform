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
 * 
 * </p>
 *
 * @author azhe
 * @since 2020-12-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_project")
@ApiModel(value="ProjectEntity对象", description="")
public class ProjectEntity extends BaseEntity<ProjectEntity> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "项目名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "项目代码")
    @TableField("code")
    private String code;

    @ApiModelProperty(value = "项目管理员")
    @TableField("admin_user_name")
    private String adminUserName;

    @ApiModelProperty(value = "项目成员")
    @TableField("users")
    private String users;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
