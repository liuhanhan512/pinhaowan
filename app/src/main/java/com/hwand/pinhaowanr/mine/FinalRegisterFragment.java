package com.hwand.pinhaowanr.mine;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
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

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hanhanliu on 15/11/20.
 */
public class FinalRegisterFragment extends BaseFragment implements View.OnClickListener {

    public static FinalRegisterFragment newInstance() {
        FinalRegisterFragment fragment = new FinalRegisterFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    // UI references.
    private View mRootView;
    private EditText mName;
    private EditText mChildName;

    private RadioButton mCheckBoxMan;
    private RadioButton mCheckBoxWoman;

    private RadioButton mCheckBoxDaddy;
    private RadioButton mCheckBoxMommy;
    private RadioButton mCheckBoxElse;

    private EditText mPwd;
    private EditText mCommitPwd;

    private TextView mNext;

    private Spinner mSpinnerYear;
    private Spinner mSpinnerMonth;
    private Spinner mSpinnerDay;
    private ArrayList<String> dataYear = new ArrayList<String>();
    private ArrayList<String> dataMonth = new ArrayList<String>();
    private ArrayList<String> dataDay = new ArrayList<String>();
    private ArrayAdapter<String> adapterSpinnerYear;
    private ArrayAdapter<String> adapterSpinnerMonth;
    private ArrayAdapter<String> adapterSpinnerDay;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_final_register_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();
        initView();
        setTitleBarTtile("注册");
    }

    @Override
    public void onResume() {
        super.onResume();
        MineFragment.setNoExit(true);
    }

    private void initView() {
        mRootView = mFragmentView.findViewById(R.id.root_linear);
        mName = (EditText) mFragmentView.findViewById(R.id.name_input);
        mChildName = (EditText) mFragmentView.findViewById(R.id.child_input);
        mPwd = (EditText) mFragmentView.findViewById(R.id.pwd_input);
        mCommitPwd = (EditText) mFragmentView.findViewById(R.id.commit_pwd_input);
        mCheckBoxMan = (RadioButton) mFragmentView.findViewById(R.id.checkbox_man);
        mCheckBoxWoman = (RadioButton) mFragmentView.findViewById(R.id.checkbox_woman);
        mCheckBoxDaddy = (RadioButton) mFragmentView.findViewById(R.id.checkbox_daddy);
        mCheckBoxMommy = (RadioButton) mFragmentView.findViewById(R.id.checkbox_mommy);
        mCheckBoxElse = (RadioButton) mFragmentView.findViewById(R.id.checkbox_else);
        mSpinnerYear = (Spinner) mFragmentView.findViewById(R.id.spinner_year);
        mSpinnerMonth = (Spinner) mFragmentView.findViewById(R.id.spinner_month);
        mSpinnerDay = (Spinner) mFragmentView.findViewById(R.id.spinner_day);

        // 年份设定为当年的前后20年
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < 40; i++) {
            dataYear.add("" + (cal.get(Calendar.YEAR) - 20 + i));
        }
        adapterSpinnerYear = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_item_layout, dataYear);
        adapterSpinnerYear.setDropDownViewResource(R.layout.spinner_item_layout);
        mSpinnerYear.setAdapter(adapterSpinnerYear);
        mSpinnerYear.setSelection(20);// 默认选中今年

        // 12个月
        for (int i = 1; i <= 12; i++) {
            dataMonth.add("" + (i < 10 ? "0" + i : i));
        }
        adapterSpinnerMonth = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_item_layout, dataMonth);
        adapterSpinnerMonth.setDropDownViewResource(R.layout.spinner_item_layout);
        mSpinnerMonth.setAdapter(adapterSpinnerMonth);

        adapterSpinnerDay = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_item_layout, dataDay);
        adapterSpinnerDay.setDropDownViewResource(R.layout.spinner_item_layout);
        mSpinnerDay.setAdapter(adapterSpinnerDay);

        mSpinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                dataDay.clear();
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, Integer.valueOf(mSpinnerYear.getSelectedItem().toString()));
                cal.set(Calendar.MONTH, arg2);
                int dayofm = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                for (int i = 1; i <= dayofm; i++) {
                    dataDay.add("" + (i < 10 ? "0" + i : i));
                }
                adapterSpinnerDay.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        mNext = (TextView) mFragmentView.findViewById(R.id.btn_next);
        mNext.setOnClickListener(this);
        mName.requestFocus();
    }

    @Override
    public void onClick(View v) {
        register();
    }

    private void register() {
        final String name = mName.getText().toString();
        final String childName = mChildName.getText().toString();
        String pwd = mPwd.getText().toString();
        String cPwd = mCommitPwd.getText().toString();
        final int childSex = mCheckBoxMan.isChecked() ? 1 : 0;
        int relation = 1;
        if (mCheckBoxMommy.isChecked()) {
            relation = 2;
        } else if (mCheckBoxElse.isChecked()) {
            relation = 3;
        }
        StringBuilder sb = new StringBuilder("");
        sb.append(mSpinnerYear.getSelectedItem().toString() + "-");
        sb.append(mSpinnerMonth.getSelectedItem().toString() + "-");
        sb.append(mSpinnerDay.getSelectedItem().toString());
        final String birthday = sb.toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(name)) {
            mName.setError(getString(R.string.error_field_required));
            focusView = mName;
            cancel = true;
        } else if (TextUtils.isEmpty(childName)) {
            mChildName.setError(getString(R.string.error_field_required));
            focusView = mChildName;
            cancel = true;
        } else if (TextUtils.isEmpty(pwd)) {
            mPwd.setError(getString(R.string.error_field_required));
            focusView = mPwd;
            cancel = true;
        } else if (TextUtils.isEmpty(cPwd)) {
            mCommitPwd.setError(getString(R.string.error_field_required));
            focusView = mCommitPwd;
            cancel = true;
        } else if (!TextUtils.equals(pwd, cPwd)) {
            mCommitPwd.setError(getString(R.string.error_inconsistent_password));
            focusView = mCommitPwd;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            final int rela = relation;
            Map<String, String> params = new HashMap<String, String>();
            params.put("name", URLEncoder.encode(name));
            params.put("childName", URLEncoder.encode(childName));
            params.put("childSex", childSex + "");
            params.put("birthday", birthday);
            params.put("relation", rela + "");
            params.put("passward", pwd);
            String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_REGISTER, params);
            LogUtil.d("dxz", url);
            NetworkRequest.get(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // TODO:
                    LogUtil.d("dxz", response);
                    if (!TextUtils.isEmpty(response) && response.contains("1")) {
                        Gson gson = new Gson();
                        UserInfo info = gson.fromJson(response, UserInfo.class);
                        info.setName(name);
                        info.setChildName(childName);
                        info.setChildSex(childSex);
                        info.setBirthday(birthday);
                        info.setRelation(rela);
                        String str = gson.toJson(info, UserInfo.class);
                        DataCacheHelper.getInstance().saveUserInfo(str);
                        AndTools.showToast("注册成功");
                        MineNaviFragment fragment = MineNaviFragment.newInstance();
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction tx = fm.beginTransaction();
                        tx.hide(FinalRegisterFragment.this);
                        tx.add(R.id.fragment_container, fragment, "MineNaviFragment");
                        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        tx.commit();
                    } else {
                        String msg = "网络问题请重试！";
                        if (TextUtils.isEmpty(response)) {

                        } else if (response.contains("0")) {
                            msg = "验证码验证失败！";
                        }
                        new DDAlertDialog.Builder(getActivity())
                                .setTitle("提示").setMessage(msg)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        getFragmentManager().popBackStack();
                                    }
                                }).show();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    LogUtil.d("dxz", error.toString());
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

}
