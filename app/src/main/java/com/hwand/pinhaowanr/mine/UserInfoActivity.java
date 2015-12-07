package com.hwand.pinhaowanr.mine;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hwand.pinhaowanr.BaseActivity;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.model.UserInfoModel;
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.utils.LogUtil;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.CircleImageView;
import com.hwand.pinhaowanr.widget.DDAlertDialog;
import com.hwand.pinhaowanr.widget.DDProgressDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserInfoActivity extends BaseActivity {

    public static final String KEY_INTENT_ID = "key_id";
    public static final String KEY_INTENT_NAME = "key_name";

    private int mID;
    private String mName;

    // UI references.
    private CircleImageView mHeadImageView;
    private TextView mBtnSendMsg;
    private TextView mBtnFocus;
    private TextView mFocusCount;
    private TextView mAbout;

    private DDProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        mHeadImageView = (CircleImageView) findViewById(R.id.head);
        mBtnSendMsg = (TextView) findViewById(R.id.btn_msg);
        mBtnFocus = (TextView) findViewById(R.id.btn_focus);
        mFocusCount = (TextView) findViewById(R.id.focus_count);
        mAbout = (TextView) findViewById(R.id.about);

        mID = getIntent().getIntExtra(KEY_INTENT_ID, 0);
        mName = getIntent().getStringExtra(KEY_INTENT_NAME);

        setActionBarTtile(mName);
        mDialog = DDProgressDialog.show(this, "提示", "正在努力加载...", true);
        request();
        mBtnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
            }
        });
        mBtnFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                focus();
            }
        });
    }

    private void request() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", mID + "");
        String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_USER_INFO, params);
        LogUtil.d("dxz", url);
        NetworkRequest.get(url, UserInfoModel.class, new Response.Listener<UserInfoModel>() {
            @Override
            public void onResponse(UserInfoModel response) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                LogUtil.d("dxz", response.toString());
                // 0 失败（这个角色不存在） 1 成功
                if (response != null && response.getResult() == 1) {
                    try {
                        String url = response.getUrl();
                        LogUtil.d("dxz", url);
                        ImageLoader.getInstance().displayImage(url, mHeadImageView);
                    } catch (Exception e) {
                    }

                    mFocusCount.setText(response.getFocus() + "人");
                    mAbout.setText("        " + response.getContent());
                } else {
                    new DDAlertDialog.Builder(UserInfoActivity.this)
                            .setTitle("提示").setMessage("这个角色不存在")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            }).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                new DDAlertDialog.Builder(UserInfoActivity.this)
                        .setTitle("提示").setMessage("网络问题请重试！")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        }).show();
            }
        });
    }

    private void focus() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", mID + "");
        String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_FOCUS_SOMEONE, params);
        LogUtil.d("dxz", url);
        NetworkRequest.get(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                LogUtil.d("dxz", response);
                // 0 失败（这个角色不存在） 1 成功
                if (!TextUtils.isEmpty(response) && response.contains("1")) {
                    try {
                        JSONObject json= new JSONObject(response);
                        int focus = json.optInt("focus");
                        if (focus != 0) {
                            mFocusCount.setText(focus+"人");
                            AndTools.showToast("关注成功");
                        } else {
                            AndTools.showToast("关注失败");
                        }
                    } catch (Exception e){
                        AndTools.showToast("关注失败");
                    }

                } else {
                    AndTools.showToast("关注失败");
                    new DDAlertDialog.Builder(UserInfoActivity.this)
                            .setTitle("提示").setMessage("这个角色不存在")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                AndTools.showToast("关注失败");
                new DDAlertDialog.Builder(UserInfoActivity.this)
                        .setTitle("提示").setMessage("网络问题请重试！")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }
}

