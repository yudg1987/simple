package com.dgyu.pure_design.common.enums;

/**
 * 描述内容
 *
 * @author yychen3
 * @date 2019-12-11
 */
public enum CommitStatusEnum {

    //报告模版代码
    NO(1, "未提交"),
    COMMITTING(2, "编辑中"),
    DONE(3, "已提交");

    private Integer code;

    private String desc;

    CommitStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static CommitStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (CommitStatusEnum anEnum : values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
