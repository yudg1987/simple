package com.dgyu.pure_design.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:最近翻阅表
 * @author dgyu
 * @since 2022-04-12
 */
@ApiModel(value = "最近翻阅表")
@Data
@Accessors(chain = true)
@TableName("recently_read")
public class RecentlyReadEntity implements Serializable{

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "")
    @TableId(value = "id", type = IdType.AUTO)
        private Integer id;
    @ApiModelProperty(value = "用户名")
    @TableField("username")
    private String username;
    @ApiModelProperty(value = "部门受案号")
    @TableField("bmsah")
    private String bmsah;
    @ApiModelProperty(value = "翻阅材料")
    @TableField("mlbh")
    private String mlbh;
    @ApiModelProperty(value = "材料名称")
    @TableField("mlxsmc")
    private String mlxsmc;
    @ApiModelProperty(value = "浏览图片id")
    @TableField("picture_id")
    private String pictureId;
    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @ApiModelProperty(value = "是否是案件速览图谱模块的最近浏览(0否，1是)")
    @TableField("is_map_recently_read")
    private Integer isMapRecentlyRead;
    @ApiModelProperty(value = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    @ApiModelProperty(value = "是否是打开状态")
    @TableField("is_active")
    private Integer isActive;
    @ApiModelProperty(value = "父类名称")
    @TableField("parent_file_name")
    private String parentFileName;
    @ApiModelProperty(value = "顶级目录编号")
    @TableField("top_mlbh")
    private String topMlbh;
    @ApiModelProperty(value = "浏览模式(0:图谱阅卷模式，1：基本阅卷模式)")
    @TableField("browse_mode")
    private Integer browseMode;




}
