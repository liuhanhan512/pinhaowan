package com.hwand.pinhaowanr.widget;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.hwand.pinhaowanr.R;


/**
 * 获取照片弹出的dialog
 */
public class FetchPhotoDialog extends DDDialog {

    private Context mContext;
    private View mRootView;

    private FetchPhotoDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    /**
     * 分享控件的构造器 默认是2行4列
     *
     * @param context
     * @param
     */
    public FetchPhotoDialog(Context context) {
        this(context, R.style.share_box_float);
        init(context);

    }

    private void init(Context context) {
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fetch_photo_layout);
        /**
         * @modified renxia 2014-6-25
         *
         * @keludeID 5105318
         * @场景描述 点击分享组件，其他空白地方没有取消功能，其他dialog都可以，交互也要求改掉
         * @解决说明 调用setCanceledOnTouchOutside实现，android:windowIsFloating 设为true，把宽度拉掉边上
         */
        getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        LayoutParams a = getWindow().getAttributes();
        a.gravity = Gravity.BOTTOM;
        setCanceledOnTouchOutside(true);
        findViews();
        initialize();
    }


    private void findViews() {
        mRootView = findViewById(R.id.root);
        TextView mTopText = (TextView) findViewById(R.id.fetch_ablum_text);
        TextView mMiddleText = (TextView) findViewById(R.id.take_photo_text);
        TextView mBottomText = (TextView) findViewById(R.id.cancel_text);

        mTopText.setText(R.string.fetch_from_ablum);
        mMiddleText.setText(R.string.fetch_from_camera);
        mBottomText.setText(R.string.cancel);
        findViewById(R.id.fetch_from_ablum_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFetchPhotoClickListener != null) {
                    mFetchPhotoClickListener.fetchFromAblumClick();
                }
                dismiss();
            }
        });
        findViewById(R.id.take_photo_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFetchPhotoClickListener != null) {
                    mFetchPhotoClickListener.fetchFromCameraClick();
                }
                dismiss();
            }
        });
        findViewById(R.id.cancel_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFetchPhotoClickListener != null) {
                    mFetchPhotoClickListener.cancelClick();
                }
                dismiss();
            }
        });
    }


    private void initialize() {
        setWindowOnTouch();
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
    }

    @Override
    public void show() {
        super.show();
    }

    private FetchPhotoClickListener mFetchPhotoClickListener;

    public void setFetchPhoteClickListener(FetchPhotoClickListener fetchPhotoClickListener) {
        mFetchPhotoClickListener = fetchPhotoClickListener;
    }

    public interface FetchPhotoClickListener {
        public void fetchFromAblumClick();

        public void fetchFromCameraClick();

        public void cancelClick();
    }

}