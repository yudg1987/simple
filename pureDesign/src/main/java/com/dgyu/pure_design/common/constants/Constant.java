package com.dgyu.pure_design.common.constants;

public class Constant {
    public final static String SUCCESS = "1";
    public final static String FAILURE = "0";
    public final static String DUP_EMPHASES = "2";//重点文书已存在
    public final static String CASE_TYPE = "11";

    public static final String URL_LOGIN = "/es/login";
    public static final String URL_CASE_ID_ALL = "/es/case/caseIdAll";
    public static final String URL_ALLTREE = "/es/archive/allTree/";
    public static final String URL_CASE_LIST = "/police/cases/list";
    public static final String URL_ATTACH = "/es/archive/attach/WjxhList";
    public static final String AHSJ_URL = "ahsj_url";//安徽省检接口地址
    public static final String AHSJ_USERNAME = "ahsj_username";// 安徽省检登录用户名
    public static final String AHSJ_PASSWORD = "ahsj_password";//安徽省检登录密码
    public static final String AHSJ_ALL_BMSAH = "ahsj_all_bmsah";//安徽省检所有的caseId
    public static final String CASE_SYNC_SET = "case_sync_set";//记录已同步案件
    public static final String CASE_STATE_SET = "case_state_set";//记录案件状态信息
    public static final String CASE_PROCESS_EVI_RELATION = "case_process_evi_relation";//记录发破案经过对应的卷宗

    /**************菜单目录类型start**************/
    public static final String MEAN_TYPE_ONE = "1";
    public static final String MEAN_TYPE_TWO = "2";
    public static final String MEAN_TYPE_THRTEE = "3";

    public static String SUSPECTNAME = "犯罪主体";
    public static String SUSPECFILE = "犯罪嫌疑人基本信息";
    public static String CASESOLVE_PROCESS_EVIDENCE_RELATIONS = "发破案经过关联证据";
    public static String CASESOLVEPROCESSNAME = "发破案经过";
    public static String FACTNAME = "犯罪主观方面";
    public static String FACT2NAME = "犯罪客观方面";
    public static String REACTNAME = "罪前罪后情节";
    public static String REACTFILE = "罪前罪后表现及其他量刑情节";
    public static String FZSSFILE = "犯罪事实";
    /**************-**************/

    /**
     * 审查元素文件类型
     */
    public interface REVIEW_INFO_FILE_TYPE {
        //鉴定报告
        int REPORT = 0;
        //有律师
        int LAYWER = 1;
        //疑似营运车辆
        int DRIVER = 2;
        //酒精模块
        int ALCOHOL = 3;
        //认罪认罚
        int PLEADGUILTY = 4;
        //量刑建议书
        int SENTENCE = 5;
    }
}
