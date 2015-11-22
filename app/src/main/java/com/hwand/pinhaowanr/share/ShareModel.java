package com.hwand.pinhaowanr.share;

import com.umeng.socialize.media.UMImage;

public class ShareModel {
    private String title;
    private String text;
    private String url;
    private UMImage umImage;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UMImage getUmImage() {
        return umImage;
    }

    public void setUmImage(UMImage umImage) {
        this.umImage = umImage;
    }
}
