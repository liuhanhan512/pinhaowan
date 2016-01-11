package com.hwand.pinhaowanr.utils;

import java.util.Map;

/**
 * Created by dxz on 2015/11/28.
 */
public class UrlConfig {

    public static final String HOST_PATH = "http://119.29.26.188/server/";

    public static final String URL_LOGIN = HOST_PATH + "Login?";
    public static final String URL_GET_PWD = HOST_PATH + "GetPassword?";
    public static final String URL_GET_CODE = HOST_PATH + "GetVerifyCode?";
    public static final String URL_PUSH_CODE = HOST_PATH + "ConfirmVerifyCode?";
    public static final String URL_REGISTER = HOST_PATH + "Register?";
    public static final String URL_CONFIG = HOST_PATH + "GetConfig?";
    public static final String URL_HOME_PAGE = HOST_PATH + "GetHomePageInfo?";
    public static final String URL_SEARCH_MORE = HOST_PATH + "SearchAndMore?";
    public static final String URL_CLASS_DETAIL = HOST_PATH + "CheckClassDetail?";
    public static final String URL_APPLY_TIMES = HOST_PATH + "GetSubscribeTime?";
    public static final String URL_APPLY_CLASS = HOST_PATH + "SubscribeClass?";
    public static final String URL_BUDDY_INFO = HOST_PATH + "GetPartnerInfo?";
    public static final String URL_SUPER_MOMMYS = HOST_PATH + "GetRegisterUsers?";
    public static final String URL_ACTIVITY_DETAIL = HOST_PATH + "ActivityDetail?";
    public static final String URL_ACTIVITY_MSG = HOST_PATH + "ActivityMessageInfo?";
    public static final String URL_DISCUSS_ACTIVITY = HOST_PATH + "ActivityDiscuss?";
    public static final String URL_APPLY_ACTIVITY = HOST_PATH + "ActivitySignUp?";
    public static final String URL_PINPIN_INFO = HOST_PATH + "GetPinPinInfo?";
    public static final String URL_PIN_CLASS = HOST_PATH + "GetPinClass?";
    public static final String URL_PIN_CLASS_DETAIL = HOST_PATH + "GetPinClassDetail?";
    public static final String URL_PIN_CLASS_BY_ID = HOST_PATH + "GetPinClassByClassId?";
    public static final String URL_CREAT_PIN_CLASS = HOST_PATH + "CreatPinClass?";
    public static final String URL_JOIN_PIN_CLASS = HOST_PATH + "UserPinClass?";
    public static final String URL_USER_INFO = HOST_PATH + "OtherUserInfo?";
    public static final String URL_FOCUS_SOMEONE = HOST_PATH + "FocusForOther?";
    public static final String URL_SEND_MSG = HOST_PATH + "SendMessage?";
    public static final String URL_QUERY_MY_ORDERS = HOST_PATH + "CheckMySubscribe?";
    public static final String URL_DEL_MY_ORDER = HOST_PATH + "DeleteMySubscribe?";
    public static final String URL_QUERY_MSGS = HOST_PATH + "CheckMyAllMessage?";
    public static final String URL_QUERY_MSG_DETAIL = HOST_PATH + "CheckMessageDetail?";
    public static final String URL_DEL_MSG = HOST_PATH + "DeleteMessage?";
    public static final String URL_MODIFY_USER_INFO = HOST_PATH + "ChangeMyInfo?";
    public static final String URL_QUERY_MY_PIN_CLASSES = HOST_PATH + "CheckMyPinClass?";
    public static final String URL_MODIFY_HEAD = HOST_PATH + "ChangeHeadPicture?";
    public static final String URL_MODIFY_PWD = HOST_PATH + "ChangePassword?";
    public static final String URL_LOGOUT = HOST_PATH + "Logout?";

    /**
     * map转字符串 拼接参数
     *
     * @param hashMap
     * @return
     */
    public static String getHttpGetUrl(String url, Map<String, String> hashMap) {
        String paramStr = url;
        if (paramStr.endsWith("?")) {
            paramStr = paramStr.replace("?", "");
        }
        for (String key : hashMap.keySet()) {
            paramStr += "&" + key + "=" + hashMap.get(key);
        }

        if (!paramStr.contains("?") && paramStr.contains("&")) {
            paramStr = paramStr.replaceFirst("&", "?");
        }
        return paramStr;
    }
}
