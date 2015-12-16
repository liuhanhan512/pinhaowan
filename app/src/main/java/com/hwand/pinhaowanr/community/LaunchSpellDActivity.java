package com.hwand.pinhaowanr.community;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.hwand.pinhaowanr.BaseActivity;
import com.hwand.pinhaowanr.DataCacheHelper;
import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.model.SpellDClassModel;
import com.hwand.pinhaowanr.model.SpellDClassStageModel;
import com.hwand.pinhaowanr.model.SpellDModel;
import com.hwand.pinhaowanr.utils.AndTools;
import com.hwand.pinhaowanr.utils.Constant;
import com.hwand.pinhaowanr.utils.DateUtil;
import com.hwand.pinhaowanr.widget.CircleImageView;

import java.util.List;

/**
 * 发起拼拼页面
 * Created by hanhanliu on 15/12/12.
 */
public class LaunchSpellDActivity extends BaseActivity {

    private SpellDModel mSpellDModel;
    private SpellDClassModel mSpellDClassModel;
    private int mType;
    private int mCurrentType;
    private String mCurrentTime;

    private static final String SPELL_D_KEY = "SPELL_D_KEY";
    private static final String SPELL_D_CLASS_KEY = "SPELL_D_CLASS_KEY";
    private static final String SPELL_TYPE_KEY = "SPELL_TYPE_KEY";

    public static void launch(Context context){
        Intent intent = new Intent();
        intent.setClass(context , LaunchSpellDActivity.class);
        context.startActivity(intent);
    }

    public static void launch(Context context , int type , SpellDModel spellDModel , SpellDClassModel spellDClassModel){
        Intent intent = new Intent();
        intent.setClass(context , LaunchSpellDActivity.class);
        intent.putExtra(SPELL_D_KEY, spellDModel);
        intent.putExtra(SPELL_TYPE_KEY, type);
        intent.putExtra(SPELL_D_CLASS_KEY , spellDClassModel);
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

    private void initIntentValues(){
        mSpellDModel = (SpellDModel) getIntent().getSerializableExtra(SPELL_D_KEY);
        mSpellDClassModel = (SpellDClassModel)getIntent().getSerializableExtra(SPELL_D_CLASS_KEY);
        mType = getIntent().getIntExtra(SPELL_TYPE_KEY , -1);
        mCurrentType = mType;
    }

    private void initTitle(){
        if(mSpellDModel != null){
            setActionBarTtile(mSpellDModel.getClassName());
        }
    }

    private TextView peopleCost;
    private EditText mAddress , mPeople;
    private Spinner mCategorySpinner , mTimeSpinner;
    private void initViews(){

        RelativeLayout headerLayout = (RelativeLayout)findViewById(R.id.header_layout);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) headerLayout.getLayoutParams();
        layoutParams.height = AndTools.getScreenWidth(this) * 9 / 16 + AndTools.dp2px(this , 40);
        headerLayout.setLayoutParams(layoutParams);

        ImageView imageView = (ImageView)findViewById(R.id.pin_image);
        AndTools.displayImage(null , mSpellDModel.getPictureUrl() , imageView);

        CircleImageView avatar = (CircleImageView)findViewById(R.id.avatar);
        AndTools.displayImage(null , DataCacheHelper.getInstance().getUserInfo().getUrl() , avatar);

        mAddress = (EditText)findViewById(R.id.address);
        mPeople = (EditText)findViewById(R.id.people_count);

        mCategorySpinner = (Spinner)findViewById(R.id.category_spinner);
        // 建立数据源
        String[] items = getCategorys();
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> _Adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, items);
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

        mTimeSpinner = (Spinner)findViewById(R.id.time_spinner);
        // 建立数据源
        String[] timeItems = getTimes(mType);
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> timeAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, timeItems);
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

        peopleCost = (TextView)findViewById(R.id.people_cost);
        updatePeopleCostView();

    }

    private void onCategoryClick(int position){
        int type =  Constant.SPELL_D_CLASS_ONE;
        switch (mType){
            case Constant.SPELL_D_CLASS_ONE:

                if(position == 0){
                    type = Constant.SPELL_D_CLASS_ONE;
                } else if(position == 1) {
                    type = Constant.SPELL_D_CLASS_STAGE;
                } else {
                    type = Constant.SPELL_D_CLASS_ALL;
                }


                break;
            case Constant.SPELL_D_CLASS_STAGE:
                if(position == 0){
                    type = Constant.SPELL_D_CLASS_STAGE;
                } else if(position == 1) {
                    type = Constant.SPELL_D_CLASS_ONE;
                } else {
                    type = Constant.SPELL_D_CLASS_ALL;
                }
                break;
            case Constant.SPELL_D_CLASS_ALL:
                if(position == 0){
                    type = Constant.SPELL_D_CLASS_ALL;
                } else if(position == 1) {
                    type = Constant.SPELL_D_CLASS_ONE;
                } else {
                    type = Constant.SPELL_D_CLASS_STAGE;
                }
                break;
            default:
                if(position == 0){
                    type = Constant.SPELL_D_CLASS_ONE;
                } else if(position == 1) {
                    type = Constant.SPELL_D_CLASS_STAGE;
                } else {
                    type = Constant.SPELL_D_CLASS_ALL;
                }
                break;
        }
        mCurrentType = type;
        String[] timeItems = getTimes(type);
        mTimeSpinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, timeItems));
    }

    private String[] getCategorys(){
        switch (mType){
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

    private String[] getTimes(int type){
        switch (type){
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

    private String[] getStateTime(){

        List<SpellDClassStageModel> spellDClassStageModels =  mSpellDClassModel.getStageTimeList();
        if(spellDClassStageModels != null){

            int size = spellDClassStageModels.size();
            String[] s = new String[size];
            for(int i = 0 ;i < size ; i++){
                SpellDClassStageModel spellDClassStageModel = spellDClassStageModels.get(i);
                s[i] = getString(R.string.reservation_time , spellDClassStageModel.getStartTime(),
                        spellDClassStageModel.getEndTime());
            }

            return s;
        }
        return new String[0];
    }

    private void updatePeopleCostView(){
        switch (mType){
            case Constant.SPELL_D_CLASS_ONE:
                peopleCost.setText(getString(R.string.people_cost , mSpellDClassModel.getOncePrice()));
                break;
            case Constant.SPELL_D_CLASS_STAGE:
                peopleCost.setText(getString(R.string.people_cost , mSpellDClassModel.getStagePrice()));
                break;
            case Constant.SPELL_D_CLASS_ALL:
                peopleCost.setText(getString(R.string.people_cost , mSpellDClassModel.getYearPrice()));
                break;
        }

    }
}
