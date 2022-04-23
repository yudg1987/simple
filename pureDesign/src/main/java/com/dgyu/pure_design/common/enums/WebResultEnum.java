package com.dgyu.pure_design.common.enums;

public enum WebResultEnum {
    /** 成功 */
    SUCCESS(1, "成功"),

    /** 失败 */
    FAILED(0, "失败"),

    /** 参数错误（客户端原因造成） */
    PARAMETER_ERROR(210, "参数错误"),
    /** 参数转换异常（客户端原因造成） */
    PARAMETER_ERROR_CONVERT(211, "参数转换异常，请确认参数类型"),
    /** 参数缺失（客户端原因造成） */
    PARAMETER_ERROR_MISSING(212, "参数缺失"),
    /** 不支持该参数值, 比如: 方法有一个枚举入参, 但内部实现只支持部分枚举值 */
    PARAMETER_VALUE_UNSUPPORTED(213, "不支持该参数值"),
    PARAMETER_COLLECTION_IS_EMPTY(214, "集合参数不能为空"),
    PARAMETER_COLLECTION_SIZE_OVER_LIMIT(215, "集合参数长度超过上限"),
    PLATM_ENGINE_RESULT_ERROR(216, "PLATM引擎返回结果异常"),

    /** 服务器内部异常（服务端原因造成） */
    ERROR(250, "服务器内部异常"),
    /** 依赖的底层服务异常, 如: db, mq, redis等不可用或异常（服务端原因造成） */
    RELIANCE_SUPPORT_ERROR(251, "依赖的底层服务异常"),

    /** DB执行抛出异常 */
    DB_EXECUTE_EXCEPTION(260, "数据库执行抛出异常"),
    /** 数据库执行结果不符合预期, 如: select by id 返回2条数据 */
    UNEXPECTED_DB_RESULT_SIZE(261, "数据库执行结果不符合预期"),

    FILE_UPLOAD_FAILURE(262,"文件保存失败！"),

    THIRD_PARTY_SERVICE_FAILURE(263,"第三方系统接口状态异常！"),
    ELLE_SERVICE_FAILURE(264,"文书要素抽取引擎接口状态异常！"),
    DELETE_DEFAULT_FAILURE(265,"默认标签页无法删除！"),
    PYTHON_ENGINE_FAILURE(266,"python推荐接口异常！"),
    /*----------- 21XX 案件 -----------*/
    /**
     * 存在重复案件编号
     */
    CASE_NO_EXIST(2201, "存在重复案件编号"),

    /*----------- 22XX 标签 -----------*/

    /*----------- 23XX 智能文件夹 -----------*/

    /*----------- 24XX 统计指标 -----------*/
    STAT_INDICATOR_INVALID(2401, "不支持该统计指标"),

    /*----------- 25XX 主机 -----------*/
    HOST_NOT_EXIST(2501, "主机不存在"),

    /*----------- 26XX 仓库 -----------*/
    NO_ACTIVE_REPO(2601, "没有可用文档库"),

    /*----------- 27XX 用户 -----------*/
    USER_NOT_LOGIN(2791, "用户未登录"),
    USER_NO_EXIST(2792, "用户已存在"),
    USER_ERROR_LOGIN(2793, "用户名或密码错误"),
    USER_ERROE_UPDATE_PASSWORD(2794, "修改密码失败"),
    USER_UPDATE_PASSWORD(2795, "修改密码成功"),

    EDS_ERROR(10002, "上传编目失败"),
    ;

    private Integer code;
    private String desc;

    WebResultEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static WebResultEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }

        for (WebResultEnum anEnum : values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }

        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
