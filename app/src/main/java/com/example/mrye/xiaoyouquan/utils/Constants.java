package com.example.mrye.xiaoyouquan.utils;

/**
 * 常量类
 */
public class Constants {
    /**
     * 教务系统登录界面网址
     */
    public static final String EDUCATION_SYSTEM_LOGIN_URL = "http://ids.wtu.edu.cn/amserver/UI/Login?goto=http://my.wtu.edu.cn/index.portal";

    // 请求头
    public static final String HEADER_NAME_HOST = "Host";
    public static final String HEADER_VALUE_HOST = "ids.wtu.edu.cn";
    public static final String HEADER_NAME_REFERER ="Referer";
    public static final String HEADER_VALUE_REFERER ="http://ids.wtu.edu.cn/amserver/UI/Login?goto=http://my.wtu.edu.cn/index.portal";
    public static final String HEADER_NAME_AGENT ="User-Agent";
    public static final String HEADER_VALUE_AGENT ="Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 UBrowser/6.1.2107.204 Safari/537.36";
    // 登录时的请求参数
    public static final String LOGIN_BODY_NAME_IDToken0 = "IDToken0";
    public static final String LOGIN_BODY_VALUE_IDToken0 = "";
    public static final String LOGIN_BODY_NAME_IDButton = "IDButton";
    public static final String LOGIN_BODY_VALUE_IDButton = "Submit";
    public static final String LOGIN_BODY_NAME_goto = "goto";
    public static final String LOGIN_BODY_VALUE_goto= "aHR0cDovL215Lnd0dS5lZHUuY24vaW5kZXgucG9ydGFs";
    public static final String LOGIN_BODY_NAME_encoded = "encoded";
    public static final String LOGIN_BODY_VALUE_encoded = "true";
    public static final String LOGIN_BODY_NAME_gx_charset = "gx_charset";
    public static final String LOGIN_BODY_VALUE_gx_charset = "UTF-8";
    public static final String LOGIN_BODY_NAME_USERNAME = "IDToken1";
    public static final String LOGIN_BODY_NAME_PASSWORD = "IDToken2";

    public static final String SCHEDULE_BODY_NAME_SCHOOLYEAR = "xnm";
    public static final String SCHEDULE_BODY_NAME_TERM = "xqm";

}
