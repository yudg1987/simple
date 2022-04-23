package com.dgyu.pure_design.common.response;

import com.dgyu.pure_design.common.enums.WebResultEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * web 层接口响应
 */
@Data
@ApiModel(description = "统一响应对象")
public class ResponseVO<T> {

    @ApiModelProperty(value = "响应状态code", notes = "200:成功; 其他: 失败", example = "200")
    private Integer code;

    @ApiModelProperty(value = "响应结果描述", example = "成功")
    private String message;

    @ApiModelProperty("响应数据")
    private T rows;

    private ResponseVO() {
        super();
    }

    public static <T> ResponseVO<T> fail() {
        return fail(WebResultEnum.FAILED);
    }

    public static <T> ResponseVO<T> fail(WebResultEnum resultEnum) {
        return fail(resultEnum, resultEnum.getDesc());
    }

    public static <T> ResponseVO<T> fail(WebResultEnum resultEnum, String message) {
        ResponseVO<T> vo = new ResponseVO<>();
        vo.setCode(resultEnum.getCode());
        vo.setMessage(message);
        return vo;
    }

    public static <T> ResponseVO<T> success() {
        return instance(WebResultEnum.SUCCESS, null);
    }

    public static <T> ResponseVO<T> success(T data) {
        return instance(WebResultEnum.SUCCESS, data);
    }

    public static <T> ResponseVO<T> instance(WebResultEnum resultEnum, T data) {
        ResponseVO<T> vo = new ResponseVO<>();
        vo.setCode(resultEnum.getCode());
        vo.setMessage(resultEnum.getDesc());
        vo.setRows(data);
        return vo;
    }



}
