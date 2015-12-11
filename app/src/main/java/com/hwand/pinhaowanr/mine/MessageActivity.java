package com.hwand.pinhaowanr.mine;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hwand.pinhaowanr.BaseActivity;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.model.MsgInfo;
import com.hwand.pinhaowanr.utils.LogUtil;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.DDAlertDialog;
import com.hwand.pinhaowanr.widget.MultiTypeAdapter;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dxz on 15/12/02.
 */
public class MessageActivity extends BaseActivity {

    public static final String KEY_INTENT_ID = "key_id";
    // UI references.
    private RecyclerView mRecyclerView;
    private EditText mMsgInput;
    private TextView mSend;

    private MultiTypeAdapter mAdapter;

    private int mID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_layout);
        setActionBarTtile("消息");

        mID = getIntent().getIntExtra(KEY_INTENT_ID, 0);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        mMsgInput = (EditText) findViewById(R.id.msg_input);
        mSend = (TextView) findViewById(R.id.btn_send);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MultiTypeAdapter(this, new ArrayList<MsgInfo>());
        mRecyclerView.setAdapter(mAdapter);
        request();

    }

    private void request() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("sendId", mID + "");
        String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_QUERY_MSG_DETAIL, params);
        LogUtil.d("dxz", url);
        NetworkRequest.get(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtil.d("dxz", response);
                if (!TextUtils.isEmpty(response) && response.contains("1")) {
                    // TODO:
                } else {
                    new DDAlertDialog.Builder(MessageActivity.this)
                            .setTitle("提示").setMessage("网络问题请重试！")
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
                LogUtil.d("dxz", error.toString());
                new DDAlertDialog.Builder(MessageActivity.this)
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

    private void senMsg() {
        String msg = mMsgInput.getText().toString();
        mMsgInput.setError(null);
        boolean cancel = false;
        View focusView = null;
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(msg)) {
            mMsgInput.setError(getString(R.string.error_field_required));
            focusView = mMsgInput;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            Map<String, String> params = new HashMap<String, String>();
            params.put("acceptId", mID + "");
            params.put("content", URLEncoder.encode(msg));
            String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_SEND_MSG, params);
            LogUtil.d("dxz", url);
            NetworkRequest.get(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    LogUtil.d("dxz", response);
                    //结果（result）0 失败(这个角色不存在) 1 成功
                    if (!TextUtils.isEmpty(response) && response.contains("1")) {

                        // TODO:
                    } else {
                        new DDAlertDialog.Builder(MessageActivity.this)
                                .setTitle("提示").setMessage("网络问题请重试！")
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
                    LogUtil.d("dxz", error.toString());
                    new DDAlertDialog.Builder(MessageActivity.this)
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
}

