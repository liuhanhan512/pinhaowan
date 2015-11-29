package com.hwand.pinhaowanr.widget.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.hwand.pinhaowanr.R;


/**
 * Created by clifford on 15/5/12.
 */
public class UniformGridView extends ViewGroup {

    public interface UniformGridViewAdapter {
        int getWidthCount();
        int getHeightCount();
        View getView(int px, int py, UniformGridView parent, View oldView);
    }

    public interface OnUniformGridViewItemClickListener {
        void onItemClick(int px, int py, UniformGridView parent, View v);
    }

    public static final int LAYOUT_MODE_SCALE_ALL = 0;
    public static final int LAYOUT_MODE_SCALE_ITEM = 1;
    public static final int LAYOUT_MODE_SCALE_SPACE = 2;
    public static final int LAYOUT_MODE_WRAP_SPACE = 3;

    private UniformGridViewAdapter mUniformGridViewAdapter;
    private int mWidthCount;
    private int mHeightCount;
    private View[] mViews;

    private int mHorizontalLayoutMode;
    private int mVerticalLayoutMode;
    private float mHorizontalSpaceProportion;
    private float mVerticalSpaceProportion;
    private float mHorizontalSpace;
    private float mVerticalSpace;
    private float mItemWidth;
    private float mItemHeight;

    private OnUniformGridViewItemClickListener mOnUniformGridViewItemClickListener;

    public UniformGridView(Context context) {
        super(context);
    }

    public UniformGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UniformGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.UniformGridView);
        mHorizontalLayoutMode = ta.getInt(R.styleable.UniformGridView_horizontal_layout_mode, LAYOUT_MODE_SCALE_ITEM);
        mVerticalLayoutMode = ta.getInt(R.styleable.UniformGridView_vertical_layout_mode, LAYOUT_MODE_SCALE_ITEM);
        mHorizontalSpaceProportion = ta.getFloat(R.styleable.UniformGridView_horizontal_space_proportion, 0);
        mVerticalSpaceProportion = ta.getFloat(R.styleable.UniformGridView_vertical_space_proportion, 0);
        mHorizontalSpace = ta.getDimension(R.styleable.UniformGridView_horizontal_space, 0);
        mVerticalSpace = ta.getDimension(R.styleable.UniformGridView_vertical_space, 0);
        mItemWidth = ta.getDimension(R.styleable.UniformGridView_item_width, 0);
        mItemHeight = ta.getDimension(R.styleable.UniformGridView_item_height, 0);
        ta.recycle();
    }

    public int getHorizontalLayoutMode() {
        return mHorizontalLayoutMode;
    }

    public void setHorizontalLayoutMode(int horizontalLayoutMode) {
        mHorizontalLayoutMode = horizontalLayoutMode;
    }

    public int getVerticalLayoutMode() {
        return mVerticalLayoutMode;
    }

    public void setVerticalLayoutMode(int verticalLayoutMode) {
        mVerticalLayoutMode = verticalLayoutMode;
    }

    public float getHorizontalSpaceProportion() {
        return mHorizontalSpaceProportion;
    }

    public void setHorizontalSpaceProportion(float horizontalSpaceProportion) {
        mHorizontalSpaceProportion = horizontalSpaceProportion;
    }

    public float getVerticalSpaceProportion() {
        return mVerticalSpaceProportion;
    }

    public void setVerticalSpaceProportion(float verticalSpaceProportion) {
        mVerticalSpaceProportion = verticalSpaceProportion;
    }

    public float getHorizontalSpace() {
        return mHorizontalSpace;
    }

    public void setHorizontalSpace(float horizontalSpace) {
        mHorizontalSpace = horizontalSpace;
    }

    public float getVerticalSpace() {
        return mVerticalSpace;
    }

    public void setVerticalSpace(float verticalSpace) {
        mVerticalSpace = verticalSpace;
    }

    public float getItemWidth() {
        return mItemWidth;
    }

    public void setItemWidth(float itemWidth) {
        mItemWidth = itemWidth;
    }

    public float getItemHeight() {
        return mItemHeight;
    }

    public void setItemHeight(float itemHeight) {
        mItemHeight = itemHeight;
    }

    public static Point calculateChildSizeAndSpace(int parentSize, int childCount,
                                                   int layoutMode, float spaceProportion,
                                                   float spaceSize, float itemSize) {
        Point point = new Point();
        if (childCount <= 0 || parentSize <= 0) {
            return point;
        }
        if (childCount == 1) {
            point.x = parentSize;
            point.y = 0;
            return point;
        }
        float space = 0;
        float item = 0;
        if (layoutMode == LAYOUT_MODE_SCALE_ALL) {
            if (spaceProportion < 0) {
                spaceProportion = 0;
            } else if (spaceProportion > 1) {
                spaceProportion = 1;
            }
            space = parentSize * spaceProportion;
            item = parentSize - space;
        } else if (layoutMode == LAYOUT_MODE_SCALE_ITEM) {
            if (spaceSize < 0) {
                spaceSize = 0;
            }
            space = spaceSize * (childCount - 1);
            if (space > parentSize) {
                space = parentSize;
            }
            item = parentSize - space;
        } else if (layoutMode == LAYOUT_MODE_SCALE_SPACE) {
            if (itemSize < 0) {
                itemSize = 0;
            }
            item = itemSize * childCount;
            if (item > parentSize) {
                item = parentSize;
            }
            space = parentSize - item;
        } else if (layoutMode == LAYOUT_MODE_WRAP_SPACE){
            if (itemSize < 0){
                itemSize = 0;
            }
            item = itemSize * childCount;
            if(item > parentSize){
                item = parentSize;
            }
            space = Math.min((parentSize - item), spaceSize * (childCount - 1));
        }
        space = space / (childCount - 1);
        item = item / childCount;
        point.x = (int) item;
        point.y = (int) space;
        return point;
    }

    public void setOnUniformGridViewItemClickListener(OnUniformGridViewItemClickListener listener) {
        mOnUniformGridViewItemClickListener = listener;
    }

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnUniformGridViewItemClickListener != null) {
                int resultX = -1;
                int resultY = -1;
                for (int h = 0; h < mHeightCount; h++) {
                    for (int w = 0; w < mWidthCount; w++) {
                        if (mViews[h * mWidthCount + w] == v) {
                            resultX = w;
                            resultY = h;
                        }
                    }
                }
                if (resultX >= 0 && resultY >= 0) {
                    mOnUniformGridViewItemClickListener.onItemClick(resultX, resultY, UniformGridView.this, v);
                }
            }
        }
    };

    public void notifyDataChanged() {
        if (mUniformGridViewAdapter != null) {
            if (mWidthCount == mUniformGridViewAdapter.getWidthCount() && mHeightCount == mUniformGridViewAdapter.getHeightCount()) {
                for (int h = 0; h < mHeightCount; h++) {
                    for (int w = 0; w < mWidthCount; w++) {
                        View v = mViews[h * mWidthCount + w];
                        View newV = mUniformGridViewAdapter.getView(w, h, this, v);
                        if (v != newV) {
                            mViews[h * mWidthCount + w] = newV;
                            if (v != null) {
                                removeView(v);
                            }
                            if (newV != null) {
                                LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                                newV.setLayoutParams(layoutParams);
                                newV.setOnClickListener(mOnClickListener);
                                addView(newV);
                            }
                        }
                    }
                }
            } else {
                removeAllViews();
                mWidthCount = mUniformGridViewAdapter.getWidthCount();
                mHeightCount = mUniformGridViewAdapter.getHeightCount();
                if (mWidthCount <= 0 || mHeightCount <= 0) {
                    mWidthCount = 0;
                    mHeightCount = 0;
                    mViews = null;
                } else {
                    mViews = new View[mWidthCount * mHeightCount];
                    for (int h = 0; h < mHeightCount; h++) {
                        for (int w = 0; w < mWidthCount; w++) {
                            View newV = mUniformGridViewAdapter.getView(w, h, this, null);
                            mViews[h * mWidthCount + w] = newV;
                            if (newV != null) {
                                LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                                newV.setLayoutParams(layoutParams);
                                newV.setOnClickListener(mOnClickListener);
                                addView(newV);
                            }
                        }
                    }
                }
            }
        } else {
            removeAllViews();
            mWidthCount = 0;
            mHeightCount = 0;
            mViews = null;
        }
        requestLayout();
    }

    public void notifyDataChanged(int px, int py) {
        if (mUniformGridViewAdapter != null) {
            if (mWidthCount == mUniformGridViewAdapter.getWidthCount() && mHeightCount == mUniformGridViewAdapter.getHeightCount()) {
                if (px >= 0 && px < mWidthCount && py >= 0 && py < mHeightCount) {
                    View v = mViews[py * mWidthCount + px];
                    View newV = mUniformGridViewAdapter.getView(px, py, this, v);
                    if (v != newV) {
                        mViews[py * mWidthCount + px] = newV;
                        if (v != null) {
                            removeView(v);
                        }
                        if (newV != null) {
                            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                            newV.setLayoutParams(layoutParams);
                            newV.setOnClickListener(mOnClickListener);
                            addView(newV);
                        }
                    }
                }
            }
        }
    }

    public void setUniformGridViewAdapter(UniformGridViewAdapter adapter) {
        if (mUniformGridViewAdapter != adapter) {
            mUniformGridViewAdapter = adapter;
            notifyDataChanged();
        }
    }

    public UniformGridViewAdapter getUniformGridViewAdapter() {
        return mUniformGridViewAdapter;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthResult = 0;
        int heightResult = 0;
        //只有明确指定宽高才行，其他方式不支持
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY && MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            widthResult = widthSize;
            heightResult = heightSize;
            if (mWidthCount > 0 && mHeightCount > 0) {
                int childWidth = calculateChildSizeAndSpace(widthResult - getPaddingLeft() - getPaddingRight(), mWidthCount, mHorizontalLayoutMode, mHorizontalSpaceProportion, mHorizontalSpace, mItemWidth).x;
                int childHeight = calculateChildSizeAndSpace(heightResult - getPaddingTop() - getPaddingBottom(), mHeightCount, mVerticalLayoutMode, mVerticalSpaceProportion, mVerticalSpace, mItemHeight).x;
                for (int i = 0; i < mViews.length; i++) {
                    View v = mViews[i];
                    if (v != null) {
                        v.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY));
                    }
                }
            }
        } else {
            widthResult = getPaddingLeft() + getPaddingRight();
            heightResult = getPaddingTop() + getPaddingBottom();
        }
        setMeasuredDimension(widthResult, heightResult);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mWidthCount > 0 && mHeightCount > 0) {
            int xPoint;
            int yPoint = getPaddingTop();
            Point childH = calculateChildSizeAndSpace(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), mWidthCount, mHorizontalLayoutMode, mHorizontalSpaceProportion, mHorizontalSpace, mItemWidth);
            Point childV = calculateChildSizeAndSpace(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), mHeightCount, mVerticalLayoutMode, mVerticalSpaceProportion, mVerticalSpace, mItemHeight);
            for (int h = 0; h < mHeightCount; h++) {
                xPoint = getPaddingLeft();
                for (int w = 0; w < mWidthCount; w++) {
                    View v = mViews[h * mWidthCount + w];
                    if (v != null) {
                        v.layout(xPoint, yPoint, xPoint + childH.x, yPoint + childV.x);
                    }
                    xPoint += childH.x + childH.y;
                }
                yPoint += childV.x + childV.y;
            }
        }
    }
}