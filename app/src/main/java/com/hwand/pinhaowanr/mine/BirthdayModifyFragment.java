package com.hwand.pinhaowanr.mine;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dxz on 15/12/01.
 */
public class BirthdayModifyFragment extends BaseFragment implements View.OnClickListener {

    public static BirthdayModifyFragment newInstance() {
        BirthdayModifyFragment fragment = new BirthdayModifyFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    // UI references.
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
        return R.layout.fragment_child_birthday_layout;
    }

    @Override
    protected void initViews() {
        super.initViews();
        initView();
        setTitleBarTtile("选择宝宝出生");
    }

    @Override
    public void onResume() {
        super.onResume();
        MineFragment.setNoExit(true);
    }

    private void initView() {
        mSpinnerYear = (Spinner) mFragmentView.findViewById(R.id.spinner_year);
        mSpinnerMonth = (Spinner) mFragmentView.findViewById(R.id.spinner_month);
        mSpinnerDay = (Spinner) mFragmentView.findViewById(R.id.spinner_day);

        // 年份设定为当年的前后16年
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < 32; i++) {
            dataYear.add("" + (cal.get(Calendar.YEAR) - 16 + i));
        }
        adapterSpinnerYear = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_item_layout, dataYear);
        adapterSpinnerYear.setDropDownViewResource(R.layout.spinner_item_layout);
        mSpinnerYear.setAdapter(adapterSpinnerYear);
        mSpinnerYear.setSelection(12);// 默认选中4年前

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
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modify();
            }
        });
    }

    private void modify() {
        StringBuilder sb = new StringBuilder("");
        sb.append(mSpinnerYear.getSelectedItem().toString() + "-");
        sb.append(mSpinnerMonth.getSelectedItem().toString() + "-");
        sb.append(mSpinnerDay.getSelectedItem().toString());
        final String birthday = sb.toString();
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "4");
        params.put("value", birthday);
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
                        info.setBirthday(birthday);
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
