package com.hwand.pinhaowanr.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hwand.pinhaowanr.MainApplication;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.utils.LogUtil;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.UrlConfig;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hanhanliu on 15/12/12.
 */
public class ActivityDetailSignUpView extends LinearLayout {

    public void setId(int id) {
        this.mId = id;
    }

    private int mId;
    private Context mContext;
    private TextView mTime, mTickets;
    private TextView mSignUp;

    public ActivityDetailSignUpView(Context context) {
        super(context);
        mContext = context;
        initViews(context);
    }

    public ActivityDetailSignUpView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public ActivityDetailSignUpView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context) {
        LayoutInflater.from(context).inflate(R.layout.activity_detail_sign_up_layout, this);
        mTime = (TextView) findViewById(R.id.time);
        mTickets = (TextView) findViewById(R.id.tickets);
        mSignUp = (TextView) findViewById(R.id.sign_up);
        mSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainApplication.getInstance().isLogin()) {
                    signUp();
                } else{
                    AndTools.showToast("报名需要登录");
                }
            }
        });
    }

    public void isSignUped(boolean is) {
        if (is) {
            mSignUp.setText("已报名");
            mSignUp.setEnabled(false);
        } else {
            mSignUp.setText("报名");
            mSignUp.setEnabled(true);
        }
    }

    public void setTimeText(String text) {
        mTime.setText(text);
    }

    public void setTicketsText(String text) {
        mTickets.setText(text);
    }

    private void signUp() {
        Map<String, String> params = new HashMap<String, String>();

        params.put("id", mId + "");

        String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_APPLY_ACTIVITY, params);

        NetworkRequest.get(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AndTools.showToast(mContext.getString(R.string.sign_up_success_tips));
                // 结果（result）0 已经报过名了 1 成功
                if (!TextUtils.isEmpty(response) && response.contains("1")) {
                    mSignUp.setText("已报名");
                    mSignUp.setEnabled(false);
                    try {
                        JSONObject json = new JSONObject(response);
                        int id = json.optInt("id");
                        int ticket = json.optInt("remainTicket");
                        LogUtil.d("dxz", id + "    " + ticket);
                        if (id == mId) {
                            mTickets.setText(mContext.getString(R.string.remainder_tickets, ticket));
                        }
                    } catch (Exception e) {
                    }

                } else {
                    new DDAlertDialog.Builder(mContext)
                            .setTitle("提示").setMessage("已经报过名了")
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
                AndTools.showToast(mContext.getString(R.string.sign_up_fail_tips));
            }
        });
    }
}
