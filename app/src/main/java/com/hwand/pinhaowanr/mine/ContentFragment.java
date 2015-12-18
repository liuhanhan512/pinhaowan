package com.hwand.pinhaowanr.mine;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.hwand.pinhaowanr.BaseFragment;
import com.hwand.pinhaowanr.DataCacheHelper;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.event.InfoChangeEvent;
import com.hwand.pinhaowanr.main.MineFragment;
import com.hwand.pinhaowanr.model.UserInfo;
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.utils.LogUtil;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.DDAlertDialog;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by dxz on 15/12/01.
 */
public class ContentFragment extends BaseFragment {

    public static ContentFragment newInstance() {
        ContentFragment fragment = new ContentFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    // UI references.
    private EditText mContent;
    private TextView mNext;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_content_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();
        initView();
        setTitleBarTtile("个人介绍");
    }

    @Override
    public void onResume() {
        super.onResume();
        MineFragment.setNoExit(true);
    }

    private void initView() {
        mContent = (EditText) mFragmentView.findViewById(R.id.content_input);
        mContent.setHint("请填写个人介绍");
        mContent.requestFocus();
        mNext = (TextView) mFragmentView.findViewById(R.id.btn_next);
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content = mContent.getText().toString();
                boolean cancel = false;
                View focusView = null;
                if (TextUtils.isEmpty(content)) {
                    mContent.setError(getString(R.string.error_field_required));
                    focusView = mContent;
                    cancel = true;
                }
                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("type", "7");
                    params.put("value", URLEncoder.encode(content));
                    String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_MODIFY_USER_INFO, params);
                    LogUtil.d("dxz", url);
                    NetworkRequest.get(url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            LogUtil.d("dxz", response);
                            // 结果（result）0 失败 1 成功
                            if (!TextUtils.isEmpty(response) && response.contains("1")) {
                                AndTools.showToast("修改成功！");
                                try {
                                    UserInfo info = DataCacheHelper.getInstance().getUserInfo();
                                    info.setContent(content);
                                    Gson gson = new Gson();
                                    String str = gson.toJson(info, UserInfo.class);
                                    DataCacheHelper.getInstance().saveUserInfo(str);
                                    EventBus.getDefault().post(new InfoChangeEvent());
                                } catch (Exception e) {
                                }
                                hideImm();
                                getFragmentManager().popBackStack();
                            } else {
                                new DDAlertDialog.Builder(getActivity())
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
                            new DDAlertDialog.Builder(getActivity())
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
        });
    }

    private void hideImm() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(mNext.getApplicationWindowToken(), 0);

        }
    }
}
