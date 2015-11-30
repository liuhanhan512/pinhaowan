package com.hwand.pinhaowanr.utils;

import android.text.TextUtils;

import com.tencent.connect.common.Constants;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by duanjunlei on 2015/11/30.
 */
public class StrUtils {

    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isPhone(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    public static String encode(String str) {
        if (str == null) {
            return Constants.STR_EMPTY;
        } else {
            if (str.contains("/")) {
                str = str.replaceAll("/", "-");
            }
            if (str.contains(":")) {
                str = str.replaceAll(":", "-");
            }
            if (str.contains("<")) {
                str = str.replaceAll("<", "-");
            }
            if (str.contains(">")) {
                str = str.replaceAll(">", "-");
            }
            if (str.contains("?")) {
                str = str.replace("?", "-");
            }
            if (str.contains("&")) {
                str = str.replace("&", "-");
            }
            if (str.contains("#")) {
                str = str.replace("#", "-");
            }
            if (str.contains("\"")) {
                str = str.replace("\"", "-");
            }
            return str;
        }
    }

    public static String getMD5(String content) {
        if (!TextUtils.isEmpty(content)) {
            try {
                MessageDigest digest = MessageDigest.getInstance("MD5");
                digest.update(content.getBytes());
                return getHashString(digest);

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return Constants.STR_EMPTY;
        }
        return Constants.STR_EMPTY;
    }

    private static String getHashString(MessageDigest digest) {
        StringBuilder builder = new StringBuilder(Constants.STR_EMPTY);
        for (byte b : digest.digest()) {
            builder.append(Integer.toHexString((b >> 4) & 0xf));
            builder.append(Integer.toHexString(b & 0xf));
        }
        return builder.toString();
    }

    public static String getImageName(String url) {
        if (TextUtils.isEmpty(url)) {
            return Constants.STR_EMPTY;
        } else {
            String imageName = Constants.STR_EMPTY;
            if (url.contains("/")) {
                String[] strs = url.split("/");
                imageName = strs[strs.length - 1];
            }
            if (imageName.contains("?")) {
                String[] strs = imageName.split("[?]");
                imageName = strs[0];
            }
            return imageName;
        }
    }

    public static String markStr(String str) {
        if (TextUtils.isEmpty(str)) {
            str = Constants.STR_EMPTY;
        }
        return "\"" + str + "\"";
    }

    public static boolean emailCheck(String str) {
        String check = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*"
                + "@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*"
                + "((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(str);
        return matcher.matches();
    }

    public static boolean nickNameCheck(String str) {
        String strPattern = "^[0-9a-zA-Z\u4e00-\u9fa5-_]+$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static boolean phoneCheck(String str) {
        String check = "^1[3|4|5|7|8][0-9]" + "\\" + "d{4,8}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(str);
        return matcher.matches();
    }

    public static boolean checkFull(String str) {
        String checkFull = "[\uFF00-\uFFFF]";
        Pattern regexFull = Pattern.compile(checkFull);
        Matcher matcherFull = regexFull.matcher(str);
        return matcherFull.matches();
    }

    public static boolean checkChinese(String str) {
        String checkChinese = "[\u4e00-\u9fa5]";
        Pattern regexChinese = Pattern.compile(checkChinese);
        Matcher matcherChinese = regexChinese.matcher(str);
        return matcherChinese.matches();
    }

    public static String splitStr(String str, String subStr) {
        if (str.startsWith(subStr) && str.length() > subStr.length()) {
            str = str.substring(subStr.length() + 1);
        }
        return str;
    }

    public static String buildTransaction(final String type, final String id, final int scene) {
        return (TextUtils.isEmpty(type) || TextUtils.isEmpty(id)) ? String.valueOf(System.currentTimeMillis()) : type
                + "_" + id + "_" + scene + "_" + System.currentTimeMillis();
    }
}
