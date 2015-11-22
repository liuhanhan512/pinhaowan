package com.hwand.pinhaowanr.share;


import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.hwand.pinhaowanr.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;


/**
 * TODO<分享工具>
 *
 * @data: 2014-7-21 下午2:45:38
 * @version: V1.0
 */

public class SharePopupWindow extends PopupWindow {

    private Context context;
    private final UMSocialService mController = UMServiceFactory
            .getUMSocialService("com.umeng.share");

    public SharePopupWindow(Context cx) {
        this.context = cx;
    }

    public void showShareWindow() {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_share, null);
        GridView gridView = (GridView) view.findViewById(R.id.share_gridview);
        ShareAdapter adapter = new ShareAdapter(context);
        gridView.setAdapter(adapter);

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        // 取消按钮
        btn_cancel.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // 销毁弹出框
                dismiss();
            }
        });

        // 设置SelectPicPopupWindow的View
        this.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(context.getResources().getColor(R.color.white));
//        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setFocusable(true);
        this.setOutsideTouchable(true);

        gridView.setOnItemClickListener(new ShareItemClickListener(this));

    }

    private class ShareItemClickListener implements OnItemClickListener {
        private PopupWindow pop;

        public ShareItemClickListener(PopupWindow pop) {
            this.pop = pop;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            share(position);
            pop.dismiss();

        }
    }

    /**
     * 分享
     *
     * @param position
     */
    private void share(int position) {
        switch (position){
            case 0:
                wechat();
                break;
            case 1:
                wechatmoment();
                break;
            case 2:
                sina();
                break;
            case 3:
                qq();
                break;
            case 4:
                qzone();
                break;
        }

    }

    /**
     * 根据不同的平台设置不同的分享内容</br>
     */
    public void setShareContent(ShareModel shareModel) {

        // 配置SSO
        mController.getConfig().setSsoHandler(new SinaSsoHandler());

        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent
                .setShareContent(shareModel.getText());
        weixinContent.setTitle(shareModel.getTitle());
        weixinContent.setTargetUrl(shareModel.getUrl());
        weixinContent.setShareMedia(shareModel.getUmImage());
        mController.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia
                .setShareContent(shareModel.getText());
        circleMedia.setTitle(shareModel.getTitle());
        circleMedia.setShareMedia(shareModel.getUmImage());
        // circleMedia.setShareMedia(uMusic);
        // circleMedia.setShareMedia(video);
        circleMedia.setTargetUrl(shareModel.getUrl());
        mController.setShareMedia(circleMedia);


        // 设置QQ空间分享内容
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(shareModel.getText());
        qzone.setTargetUrl(shareModel.getUrl());
        qzone.setTitle(shareModel.getTitle());
        qzone.setShareMedia(shareModel.getUmImage());
        // qzone.setShareMedia(uMusic);
        mController.setShareMedia(qzone);


        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(shareModel.getText());
        qqShareContent.setTitle(shareModel.getTitle());
        qqShareContent.setShareMedia(shareModel.getUmImage());
        qqShareContent.setTargetUrl(shareModel.getUrl());
        mController.setShareMedia(qqShareContent);

        SinaShareContent sinaContent = new SinaShareContent();
        sinaContent
                .setShareContent(shareModel.getText()+shareModel.getUrl());
        sinaContent.setTitle(shareModel.getTitle());
        sinaContent.setTargetUrl(shareModel.getUrl());
        sinaContent.setShareImage(shareModel.getUmImage());
        mController.setShareMedia(sinaContent);

    }

    private void wechat() {
        mController.postShare(context, SHARE_MEDIA.WEIXIN,
                new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA arg0, int eCode,
                                           SocializeEntity arg2) {
                    }

                });
    }

    private void wechatmoment() {
        mController.postShare(context, SHARE_MEDIA.WEIXIN_CIRCLE,
                new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA arg0, int eCode,
                                           SocializeEntity arg2) {
                    }

                });
    }

    private void sina() {
        mController.postShare(context, SHARE_MEDIA.SINA,
                new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA arg0, int eCode,
                                           SocializeEntity arg2) {
                    }

                });
    }

    private void qq() {
        mController.postShare(context, SHARE_MEDIA.QQ,
                new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA arg0, int eCode,
                                           SocializeEntity arg2) {
                    }

                });
    }

    private void qzone() {
        mController.postShare(context, SHARE_MEDIA.QZONE,
                new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA arg0, int eCode,
                                           SocializeEntity arg2) {
                    }

                });
    }

}
