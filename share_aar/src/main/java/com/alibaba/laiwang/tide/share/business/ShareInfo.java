package com.alibaba.laiwang.tide.share.business;

import java.util.Map;

/**
 * Created by zengchan.lzc on 2015/1/17.
 */
public class ShareInfo {
    private String title;
    private String content;
    private String pictureUrl;
    private String linkUrl;

    private String shareKey;
    private int    defaultPictrueSrcId;

    public static final int TEXT_TYPE = 1;
    public static final int LOCAL_IMAGE_TYPE = 2;
    public static final int NET_IMAGE_TYPE = 3;
    public static final int LINK_TYPE = 4;
    public static final int MUSIC_TYPE = 5;
    public static final int VIDEO_TYPE = 6;

    private int shareType = -1;

    public int getShareType() {
        return shareType;
    }

    public void setShareType(int shareType) {
        this.shareType = shareType;
    }

    private Map<String,String> extention;

    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public int getDefaultPictrueSrcId() {
        return defaultPictrueSrcId;
    }

    public void setDefaultPictrueSrcId(int defaultPictrueSrcId) {
        this.defaultPictrueSrcId = defaultPictrueSrcId;
    }
    public String getShareKey() {
        return shareKey;
    }

    public void setShareKey(String shareKey) {
        this.shareKey = shareKey;
    }


    public Map<String, String> getExtention() {
        return extention;
    }

    public void setExtention(Map<String, String> extention) {
        this.extention = extention;
    }

}
