package com.hwand.pinhaowanr.model;

/**
 * Created by hanhanliu on 15/12/6.
 */
public class SpellDModel {
    private long createTime;
    private String pictureUrl;
    private String subtitle;
    private String detailAddress;
    private String className;
    private int id;
    private int isPinClass;
    private String title;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsPinClass() {
        return isPinClass;
    }

    public void setIsPinClass(int isPinClass) {
        this.isPinClass = isPinClass;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
