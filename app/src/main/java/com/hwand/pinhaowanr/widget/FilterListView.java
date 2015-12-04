package com.hwand.pinhaowanr.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hwand.pinhaowanr.R;
import com.hwand.pinhaowanr.adapter.FilterListAdapter;
import com.hwand.pinhaowanr.utils.AndTools;

import java.util.List;

/**
 * 企业OA消息点击筛选弹出的listview
 * Created by shiqi on 15/4/23.
 */
public class FilterListView extends DDPopupWindow{

    private Context mContext;
    private FilterListAdapter mFilterListAdapter;
    private ListView mListView;
    private View mRootView;

    private int mCount;

    private onShareDismissListener onDismissListener=null;

    private final static int FILTER_LIST_MAX_VALUE = 8;//筛选列表最多占用的高度是8行，多余8行滑动listview显示
    private final static int FILTER_ITEM_HEIGHT = 44;


    public FilterListView(Context context, int count) {
        super(context);
        mCount = count;
        init(context);
    }
    private void init(Context context){
        mContext = context;

        mRootView  = View.inflate(context, R.layout.view_filter_layout, null);
        findViews();

        setOutsideTouchable(true);
        setFocusable(true);

        setBackgroundDrawable(new BitmapDrawable());
//        setAnimationStyle(R.style.filter_list_animation);

        setContentView(mRootView);

        initPopupWindowSize();

        setWindowOnTouch();
    }

    private void initPopupWindowSize(){
        setWidth(AndTools.getScreenWidth(mContext));
        int line = mCount > FILTER_LIST_MAX_VALUE ? FILTER_LIST_MAX_VALUE : mCount;
        int lineHeight = AndTools.dp2px(mContext,FILTER_ITEM_HEIGHT);
        setHeight(line * lineHeight);
    }
    public void notifyDataChanged(){
        if(mFilterListAdapter != null){
            mFilterListAdapter.notifyDataSetChanged();
        }
    }
    private void findViews() {
        mListView = (ListView)mRootView.findViewById(R.id.listview);

        if(mCount > FILTER_LIST_MAX_VALUE){
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mListView.getLayoutParams();
            int lineHeight = AndTools.dp2px(mContext,44);
            layoutParams.height = FILTER_LIST_MAX_VALUE * lineHeight;
            mListView.setLayoutParams(layoutParams);
        }

    }

    public void setAdapter(FilterListAdapter filterListAdapter){
        mFilterListAdapter  = filterListAdapter;
        mListView.setAdapter(mFilterListAdapter);
    }

    private void setWindowOnTouch() {
        mRootView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = mRootView.getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }


    @Override
    public void dismiss() {
        super.dismiss();
        if (onDismissListener!=null) {
            onDismissListener.onDismiss();
        }
    }

    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onDismissListener1){
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(parent, view, position, id);
                }
                dismiss();
            }

        });
        mOnItemClickListener = onDismissListener1;

    }
    public interface OnItemClickListener{
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id);
    }

    public void setOnDismissListeren(onShareDismissListener ondDismissListener){
        this.onDismissListener=ondDismissListener;
    }

    public interface onShareDismissListener{
        void onDismiss();
    }


}
