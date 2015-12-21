package com.hwand.pinhaowanr.community;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hwand.pinhaowanr.BaseActivity;
import com.hwand.pinhaowanr.DataCacheHelper;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.model.SpellDClassModel;
import com.hwand.pinhaowanr.model.SpellDClassStageModel;
import com.hwand.pinhaowanr.model.SpellDModel;
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.utils.Constant;
import com.hwand.pinhaowanr.utils.LogUtil;
import com.hwand.pinhaowanr.utils.NetworkRequest;
import com.hwand.pinhaowanr.utils.UrlConfig;
import com.hwand.pinhaowanr.widget.CircleImageView;
import com.hwand.pinhaowanr.widget.DDAlertDialog;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发起拼拼页面
 * Created by hanhanliu on 15/12/12.
 */
public class LaunchSpellDActivity extends BaseActivity {

    private SpellDModel mSpellDModel;
    private SpellDClassModel mSpellDClassModel;
    private int mType;
    private int mCurrentType;
    private double mCurrentPrice;
    private String mCurrentTime;

    private LinearLayout mOnceLayout;
    private Spinner mSpinnerYear;
    private Spinner mSpinnerMonth;
    private Spinner mSpinnerDay;
    private ArrayList<String> dataYear = new ArrayList<String>();
    private ArrayList<String> dataMonth = new ArrayList<String>();
    private ArrayList<String> dataDay = new ArrayList<String>();
    private ArrayAdapter<String> adapterSpinnerYear;
    private ArrayAdapter<String> adapterSpinnerMonth;
    private ArrayAdapter<String> adapterSpinnerDay;


    private static final String SPELL_D_KEY = "SPELL_D_KEY";
    private static final String SPELL_D_CLASS_KEY = "SPELL_D_CLASS_KEY";
    private static final String SPELL_TYPE_KEY = "SPELL_TYPE_KEY";

    public static void launch(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, LaunchSpellDActivity.class);
        context.startActivity(intent);
    }

    public static void launch(Context context, int type, SpellDModel spellDModel, SpellDClassModel spellDClassModel) {
        Intent intent = new Intent();
        intent.setClass(context, LaunchSpellDActivity.class);
        intent.putExtra(SPELL_D_KEY, spellDModel);
        intent.putExtra(SPELL_TYPE_KEY, type);
        intent.putExtra(SPELL_D_CLASS_KEY, spellDClassModel);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_spell_d_layout);
        initIntentValues();
        initTitle();
        initViews();
    }

    private void initIntentValues() {
        mSpellDModel = (SpellDModel) getIntent().getSerializableExtra(SPELL_D_KEY);
        mSpellDClassModel = (SpellDClassModel) getIntent().getSerializableExtra(SPELL_D_CLASS_KEY);
        mType = getIntent().getIntExtra(SPELL_TYPE_KEY, -1);
        mCurrentType = mType;
    }

    private void initTitle() {
        if (mSpellDModel != null) {
            setActionBarTtile(mSpellDModel.getClassName());
        }
    }

    private TextView peopleCost;
    private EditText mAddress, mPeople;
    private Spinner mCategorySpinner, mTimeSpinner;
    private TextView create;

    private void initViews() {

        RelativeLayout headerLayout = (RelativeLayout) findViewById(R.id.header_layout);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) headerLayout.getLayoutParams();
        layoutParams.height = AndTools.getScreenWidth(this) * 9 / 16 + AndTools.dp2px(this, 40);
        headerLayout.setLayoutParams(layoutParams);

        ImageView imageView = (ImageView) findViewById(R.id.pin_image);
        AndTools.displayImage(null, mSpellDModel.getPictureUrl(), imageView);

        CircleImageView avatar = (CircleImageView) findViewById(R.id.avatar);
        AndTools.displayImage(null, DataCacheHelper.getInstance().getUserInfo().getUrl(), avatar);

        mOnceLayout = (LinearLayout)findViewById(R.id.once_layout);
        mSpinnerYear = (Spinner) findViewById(R.id.spinner_year);
        mSpinnerMonth = (Spinner) findViewById(R.id.spinner_month);
        mSpinnerDay = (Spinner) findViewById(R.id.spinner_day);
        // 年份设定为当年的前后16年
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < 32; i++) {
            dataYear.add("" + (cal.get(Calendar.YEAR) - 16 + i));
        }
        adapterSpinnerYear = new ArrayAdapter<String>(this, R.layout.spinner_item_layout, dataYear);
        adapterSpinnerYear.setDropDownViewResource(R.layout.spinner_item_layout);
        mSpinnerYear.setAdapter(adapterSpinnerYear);
        mSpinnerYear.setSelection(12);// 默认选中4年前

        // 12个月
        for (int i = 1; i <= 12; i++) {
            dataMonth.add("" + (i < 10 ? "0" + i : i));
        }
        adapterSpinnerMonth = new ArrayAdapter<String>(this, R.layout.spinner_item_layout, dataMonth);
        adapterSpinnerMonth.setDropDownViewResource(R.layout.spinner_item_layout);
        mSpinnerMonth.setAdapter(adapterSpinnerMonth);

        adapterSpinnerDay = new ArrayAdapter<String>(this, R.layout.spinner_item_layout, dataDay);
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


        mAddress = (EditText) findViewById(R.id.address);
        mPeople = (EditText) findViewById(R.id.people_count);
        mPeople.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String countStr = mPeople.getText().toString().trim();
                updatePeopleCostView(countStr);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mCategorySpinner = (Spinner) findViewById(R.id.category_spinner);
        // 建立数据源
        String[] items = getCategorys();
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> _Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        //绑定 Adapter到控件
        mCategorySpinner.setAdapter(_Adapter);
        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onCategoryClick(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mTimeSpinner = (Spinner) findViewById(R.id.time_spinner);
        // 建立数据源
        String[] timeItems = getTimes(mType);
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, timeItems);
        //绑定 Adapter到控件
        mTimeSpinner.setAdapter(timeAdapter);
        mTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] timeItems = getTimes(mCurrentType);
                mCurrentTime = timeItems[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        create = (TextView) findViewById(R.id.launch_spell_d);
        peopleCost = (TextView) findViewById(R.id.people_cost);
        String count = mPeople.getText().toString().trim();
        updatePeopleCostView(count);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request();

            }
        });

    }

    private void request() {
        final String count = mPeople.getText().toString().trim();
        final String address = mAddress.getText().toString().trim();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(address)) {
            mAddress.setError(getString(R.string.error_field_required));
            focusView = mAddress;
            cancel = true;
        } else if (TextUtils.isEmpty(count)) {
            mPeople.setError(getString(R.string.error_field_required));
            focusView = mPeople;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            Map<String, String> params = new HashMap<String, String>();
            params.put("id", mSpellDModel.getId() + "");
            params.put("type", mCurrentType + "");
            if(mType == Constant.SPELL_D_CLASS_ONE){
                StringBuilder sb = new StringBuilder("");
                sb.append(mSpinnerYear.getSelectedItem().toString() + "-");
                sb.append(mSpinnerMonth.getSelectedItem().toString() + "-");
                sb.append(mSpinnerDay.getSelectedItem().toString());
                final String pinTime = sb.toString();
                params.put("pinTime", URLEncoder.encode(pinTime));
            } else {

                params.put("pinTime", URLEncoder.encode(mCurrentTime));
            }
            params.put("roles", count);
            params.put("detailAddress", URLEncoder.encode(address));
            params.put("money", new DecimalFormat("0.00").format(mCurrentPrice));
            String url = UrlConfig.getHttpGetUrl(UrlConfig.URL_CREAT_PIN_CLASS, params);
            LogUtil.d("dxz", url);
            NetworkRequest.get(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // TODO:
                    LogUtil.d("dxz", response);
                    // 结果（result）0 失败（ConfirmVerifyCode 接口验证没有通过） 1 成功 2 密码不合法
                    if (!TextUtils.isEmpty(response) && response.contains("1")) {
                        AndTools.showToast("成功发起拼课！");
                        hideImm();
                        finish();
                    } else {
                        new DDAlertDialog.Builder(LaunchSpellDActivity.this)
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
                    new DDAlertDialog.Builder(LaunchSpellDActivity.this)
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

    private void onCategoryClick(int position) {
        int type = Constant.SPELL_D_CLASS_ONE;
        switch (mType) {
            case Constant.SPELL_D_CLASS_ONE:

                if (position == 0) {
                    type = Constant.SPELL_D_CLASS_ONE;
                } else if (position == 1) {
                    type = Constant.SPELL_D_CLASS_STAGE;
                } else {
                    type = Constant.SPELL_D_CLASS_ALL;
                }


                break;
            case Constant.SPELL_D_CLASS_STAGE:
                if (position == 0) {
                    type = Constant.SPELL_D_CLASS_STAGE;
                } else if (position == 1) {
                    type = Constant.SPELL_D_CLASS_ONE;
                } else {
                    type = Constant.SPELL_D_CLASS_ALL;
                }
                break;
            case Constant.SPELL_D_CLASS_ALL:
                if (position == 0) {
                    type = Constant.SPELL_D_CLASS_ALL;
                } else if (position == 1) {
                    type = Constant.SPELL_D_CLASS_ONE;
                } else {
                    type = Constant.SPELL_D_CLASS_STAGE;
                }
                break;
            default:
                if (position == 0) {
                    type = Constant.SPELL_D_CLASS_ONE;
                } else if (position == 1) {
                    type = Constant.SPELL_D_CLASS_STAGE;
                } else {
                    type = Constant.SPELL_D_CLASS_ALL;
                }
                break;
        }
        mCurrentType = type;
        if(mCurrentType == Constant.SPELL_D_CLASS_ONE){
            mTimeSpinner.setVisibility(View.GONE);
            mOnceLayout.setVisibility(View.VISIBLE);
        } else {
            mOnceLayout.setVisibility(View.GONE);
            mTimeSpinner.setVisibility(View.VISIBLE);
            String[] timeItems = getTimes(type);
            mTimeSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, timeItems));
        }

    }

    private String[] getCategorys() {
        switch (mType) {
            case Constant.SPELL_D_CLASS_ONE:
                return getResources().getStringArray(R.array.once_first_array);
            case Constant.SPELL_D_CLASS_STAGE:
                return getResources().getStringArray(R.array.stage_first_array);
            case Constant.SPELL_D_CLASS_ALL:
                return getResources().getStringArray(R.array.all_first_array);
            default:
                return getResources().getStringArray(R.array.once_first_array);
        }
    }

    private String[] getTimes(int type) {
        switch (type) {
            case Constant.SPELL_D_CLASS_ONE:
                return getResources().getStringArray(R.array.once_time_array);
            case Constant.SPELL_D_CLASS_STAGE:
                return getStateTime();
            case Constant.SPELL_D_CLASS_ALL:
                return getResources().getStringArray(R.array.all_time_array);
            default:
                return getResources().getStringArray(R.array.once_time_array);
        }
    }

    private String[] getStateTime() {

        List<SpellDClassStageModel> spellDClassStageModels = mSpellDClassModel.getStageTimeList();
        if (spellDClassStageModels != null) {

            int size = spellDClassStageModels.size();
            String[] s = new String[size];
            for (int i = 0; i < size; i++) {
                SpellDClassStageModel spellDClassStageModel = spellDClassStageModels.get(i);
                s[i] = getString(R.string.reservation_time, spellDClassStageModel.getStartTime(),
                        spellDClassStageModel.getEndTime());
            }

            return s;
        }
        return new String[0];
    }

    private void updatePeopleCostView(String countStr) {
        double count = 1.00;
        if (!TextUtils.isEmpty(countStr)) {
            count = Double.valueOf(countStr);
        }
        switch (mType) {
            case Constant.SPELL_D_CLASS_ONE:
                mCurrentPrice = mSpellDClassModel.getOncePrice() / count;
                break;
            case Constant.SPELL_D_CLASS_STAGE:
                mCurrentPrice = mSpellDClassModel.getStagePrice() / count;
                break;
            case Constant.SPELL_D_CLASS_ALL:
                mCurrentPrice = mSpellDClassModel.getYearPrice() / count;
                break;
        }
        peopleCost.setText(getString(R.string.people_cost, mCurrentPrice));

    }

    private void hideImm() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(create.getApplicationWindowToken(), 0);

        }
    }
}
