package com.dgyu.pure_design.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 简单返回结果状态码
 * @author Administrator
 *
 */
@Getter
@AllArgsConstructor
public enum ResultEnum {
    //
    SUCCESS("1", "请求成功"),
    FAILED("0", "请求失败"),

    NO_CONTENT("204", "请求无内容"),
    USER_NOT_LOGIN("401", "用户未登录"),
    USER_LOGIN_FAIL("-2", "用户登录失败"),
    USER_ACCESS_FORBIDDEN("403", "用户授权失败"),
    NOT_FOUND("404", "资源不存在"),
    INTERNAL_ERROR("500", "服务器错误"),
    SYSTEM_ERROR("501", "系统繁忙"),
    UNKNOWN("-1", "系统异常"),
    BUSINESS_FAILURE("-2", "业务异常"),
    PARAMETER_MISSING("502", "参数缺失"),
    INVALID_PARAMETER("501", "参数不合法"),
            ;
    private String code;
    private String msg;


    public static ResultEnum getEnumByCode(String code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists!"));
    }

}
