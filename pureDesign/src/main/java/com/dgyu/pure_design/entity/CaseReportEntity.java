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
 * @since 2022-04-08
 */
@ApiModel(value = "")
@Data
@Accessors(chain = true)
@TableName("case_report")
public class CaseReportEntity implements Serializable{

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "")
    @TableId(value = "id", type = IdType.AUTO)
        private Integer id;
    @ApiModelProperty(value = "案件id")
    @TableField("case_id")
    private String caseId;
    @ApiModelProperty(value = "模型编号")
    @TableField("mxbm")
    private String mxbm;
    @ApiModelProperty(value = "文书模板编号")
    @TableField("wsmbbh")
    private String wsmbbh;
    @ApiModelProperty(value = "文书实例编号")
    @TableField("wsslbh")
    private String wsslbh;
    @ApiModelProperty(value = "报告标题，用于导出word名称")
    @TableField("report_title")
    private String reportTitle;
    @ApiModelProperty(value = "报告内容")
    @TableField("report_text")
    private String reportText;
    @ApiModelProperty(value = "报告状态1: 编辑  2:审核")
    @TableField("report_status")
    private Integer reportStatus;
    @ApiModelProperty(value = "报告路径")
    @TableField("report_path")
    private String reportPath;
    @ApiModelProperty(value = "模版代码，前台约定")
    @TableField("template_code")
    private Integer templateCode;
    @ApiModelProperty(value = "提交状态")
    @TableField("commit_status")
    private Integer commitStatus;
    @ApiModelProperty(value = "提交时间")
    @TableField("commit_time")
    private Date commitTime;
    @ApiModelProperty(value = "")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @ApiModelProperty(value = "")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    @ApiModelProperty(value = "东软一级类别CODE")
    @TableField("dryjlb")
    private String dryjlb;
    @ApiModelProperty(value = "东软二级类别CODE")
    @TableField("drejlb")
    private String drejlb;
    @ApiModelProperty(value = "word标准文书的存储报告路径")
    @TableField("standard_report_path")
    private String standardReportPath;




}
