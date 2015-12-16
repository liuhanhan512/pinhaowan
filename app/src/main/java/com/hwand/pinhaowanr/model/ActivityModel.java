package com.hwand.pinhaowanr.model;

import java.io.Serializable;
import java.util.List;

/**
 * 主页--小伙伴--活动详情model
 * Created by hanhanliu on 15/12/12.
 */
public class ActivityModel implements Serializable {

    private String detailAddress;
    private long stratTime;
    private long endTime;
    private int maxRoles;
    private String content;
    private String url;
    private List<ActivitySignModel> signTimeList;
    private List<ActivityDiscussModel> messageList;

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public long getStratTime() {
        return stratTime;
    }

    public void setStratTime(long stratTime) {
        this.stratTime = stratTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getMaxRoles() {
        return maxRoles;
    }

    public void setMaxRoles(int maxRoles) {
        this.maxRoles = maxRoles;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<ActivitySignModel> getSignTimeList() {
        return signTimeList;
    }

    public void setSignTimeList(List<ActivitySignModel> signTimeList) {
        this.signTimeList = signTimeList;
    }

    public List<ActivityDiscussModel> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<ActivityDiscussModel> messageList) {
        this.messageList = messageList;
    }
}
