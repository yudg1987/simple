package com.dgyu.pure_design.common.enums;

/**
 * 报告模版编码
 */
public enum TemplateCodeEnums {

    //报告模版代码
    STEAL(1, "盗窃罪"),
    DANGER_DRIVE(2, "危险驾驶罪"),
    TRAFFIC_ACCIDENT(3, "交通肇事罪"),
    REVIEW_REPORT(4, "审查报告"),
    ARREST_OPINION(5, "逮捕意见书"),
    REVIEW_REPORT_ANHUI(6, "审查报告(安徽省检)"),
    STANDARD(7, "标准文书"),
    COMMENT(8, "本地文书"),
    //99 专门为法院判决书生成服务
    PJS_QUICKCUT(99, "危险驾驶罪（速裁）判决书"),
    PJS_SIMPLE(100, "危险驾驶罪（简易）判决书"),
    PJS_CRIMINAL(101, "危险驾驶罪（普通 仅刑事）判决书"),
    PJS_CRIMINAL_INCIDENTAL_CIVIL(102, "危险驾驶罪（普通 刑事附带民事）判决书"),
    IMPORT(139, "导入文书"),
    //广德市域社会治理,2xx
    GD_TJJZFM(201, "调解卷宗封面"),
    GD_FD(202, "封底"),
    GD_JNML(203, "卷内目录"),
    GD_JZQKSM(204, "卷宗情况说明"),
    GD_RMTJDCJL(205, "人民调解调查记录"),
    GD_RMTJHFJL(206, "人民调解回访记录"),
    GD_RMTJJL(207, "人民调解记录"),
    GD_RMTJKTXYDJB(208, "人民调解口头协议登记表"),
    GD_RMTJSQS(209, "人民调解申请书"),
    GD_RMTJSLDJB(210, "人民调解受理登记表"),
    GD_RMTJXYS(211, "人民调解协议书"),
    GD_SFQRSQSGRYDW(212, "司法确认申请书-个人与单位"),
    GD_SFQRSQSGRYGR(213, "司法确认申请书-个人与个人"),
    ;
    private Integer code;

    private String desc;

    TemplateCodeEnums(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static TemplateCodeEnums getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (TemplateCodeEnums anEnum : values()) {
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
