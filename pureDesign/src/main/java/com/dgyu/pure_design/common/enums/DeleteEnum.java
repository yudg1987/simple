package com.dgyu.pure_design.common.enums;

import lombok.Getter;

@Getter
public enum DeleteEnum {
    //1删除，0 未删除
    NOT_DELETE_ENUM(0, "未删除"),
    DELETE_ENUM(1, "删除")
    ;


    private Integer code;

    private String desc;

    DeleteEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static DeleteEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }

        for (DeleteEnum anEnum : values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }

        return null;
    }
}
