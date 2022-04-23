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
 * @Description:
 * @author dgyu
 * @since 2022-04-12
 */
@ApiModel(value = "")
@Data
@Accessors(chain = true)
@TableName("yj_operation_status")
public class YjOperationStatusEntity implements Serializable{

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "")
    @TableId(value = "id", type = IdType.AUTO)
        private Integer id;
    @ApiModelProperty(value = "部门受案号")
    @TableField("bmsah")
    private String bmsah;
    @ApiModelProperty(value = "目录编码")
    @TableField("mlbh")
    private String mlbh;
    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @ApiModelProperty(value = "创建用户id")
    @TableField("create_user")
    private String createUser;




}
