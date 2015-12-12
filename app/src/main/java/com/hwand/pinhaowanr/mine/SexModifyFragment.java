package com.hwand.pinhaowanr.mine;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.hwand.pinhaowanr.BaseFragment;
import com.hwand.pinhaowanr.DataCacheHelper;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.main.MineFragment;
import com.hwand.pinhaowanr.model.UserInfo;
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.utils.LogUtil;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.DDAlertDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dxz on 15/12/01.
 */
public class SexModifyFragment extends BaseFragment implements View.OnClickListener {

    public static SexModifyFragment newInstance() {
        SexModifyFragment fragment = new SexModifyFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    // UI references.
    private TextView mNext;

    private RadioButton mCheckBoxMan;
    private RadioButton mCheckBoxWoman;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_child_sex_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();
        initView();
        setTitleBarTtile("选择宝宝性别");
    }

    @Override
    public void onResume() {
        super.onResume();
        MineFragment.setNoExit(true);
    }

    private void initView() {
        mCheckBoxMan = (RadioButton) mFragmentView.findViewById(R.id.checkbox_man);
        mCheckBoxWoman = (RadioButton) mFragmentView.findViewById(R.id.checkbox_woman);

        mNext = (TextView) mFragmentView.findViewById(R.id.btn_next);
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modify();
            }
        });
    }

    private void modify() {
        StringBuilder sb = new StringBuilder("");
        final int childSex = mCheckBoxMan.isChecked() ? 1 : 0;
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "3");
        params.put("value", childSex+"");
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
                        info.setChildSex(childSex);
                        Gson gson = new Gson();
                        String str = gson.toJson(info, UserInfo.class);
                        DataCacheHelper.getInstance().saveUserInfo(str);
                    } catch (Exception e) {
                    }
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
