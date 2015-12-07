package com.hwand.pinhaowanr.model;

/**
 * Created by hanhanliu on 15/12/5.
 */
public class UserInfoModel {

    // 0 失败（这个角色不存在） 1 成功
    private int result;
    private String url;
    private int focus;
    private String content;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getFocus() {
        return focus;
    }

    public void setFocus(int focus) {
        this.focus = focus;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
