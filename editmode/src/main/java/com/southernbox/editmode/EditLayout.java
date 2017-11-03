package com.southernbox.editmode;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by SouthernBox on 2016/10/27 0027.
 * 侧滑删除控件
 */

public class EditLayout extends FrameLayout {

    private View mContentView;  //内容部分
    private int mWidth;         //内容部分宽度
    private int mHeight;        //内容部分高度

    private View mLeftView;     //左边圆形删除按键
    private int mLeftWidth;     //左边圆形删除按键宽度

    private View mRightView;    //右边删除按键
    private int mRightWidth;    //右边删除按键宽度

    private View mSortView;     //排序按键
    private int mSortWidth;     //排序按键宽度

    private ViewDragHelper mDragHelper;
    private boolean isEdit;     //是否为编辑状态

    public EditLayout(Context context) {
        this(context, null);
    }

    public EditLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {

            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return false;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                if (changedView == mContentView) {
                    mLeftView.offsetLeftAndRight(dx);
                    mSortView.offsetLeftAndRight(dx);
                }
                invalidate();
                //左侧展开时，修改 mContentView 宽度
                if (left == mLeftWidth) {
                    mContentView.layout(mLeftWidth, 0, mWidth, mHeight);
                    mSortView.layout(mWidth - mSortWidth, 0, mWidth, mHeight);
                    mSortView.setVisibility(VISIBLE);
                }
            }

        };

        mDragHelper = ViewDragHelper.create(this, mCallback);
    }

    /**
     * 状态改变监听器
     */
    public interface OnStateChangeListener {

        void onLeftOpen(EditLayout layout);

        void onRightOpen(EditLayout layout);

        void onClose(EditLayout layout);

    }

    private OnStateChangeListener mOnStateChangeListener;

    public void setOnDragStateChangeListener(OnStateChangeListener onStateChangeListener) {
        mOnStateChangeListener = onStateChangeListener;
    }

    /**
     * 排序监听器
     */
    public interface OnItemSortListener {

        void onStartDrags(RecyclerView.ViewHolder viewHolder);

        void onItemMove(int fromPosition, int toPosition);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mRightView = getChildAt(0);
        mContentView = getChildAt(1);
        mLeftView = getChildAt(2);
        mSortView = getChildAt(3);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mRightWidth = mRightView.getMeasuredWidth();
        mLeftWidth = mLeftView.getMeasuredWidth();
        mSortWidth = mSortView.getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //判断是否为编辑模式,摆放每个子View的位置
        mRightView.layout(mWidth - mRightWidth, 0, mWidth, mHeight);
        mSortView.layout(mWidth - mSortWidth, 0, mWidth, mHeight);
        if (isEdit) {
            mContentView.layout(mLeftWidth, 0, mWidth, mHeight);
            mLeftView.layout(0, 0, mLeftWidth, mHeight);
            mSortView.setVisibility(VISIBLE);
        } else {
            mContentView.layout(0, 0, mWidth, mHeight);
            mLeftView.layout(-mLeftWidth, 0, 0, mHeight);
            mSortView.setVisibility(INVISIBLE);
        }
    }

    @Override
    public void computeScroll() {
        invalidate();
        super.computeScroll();
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * 设置编辑状态
     *
     * @param isEdit 是否为编辑状态
     */
    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    /**
     * 展开左侧
     */
    public void openLeft() {
        if (mOnStateChangeListener != null) {
            mOnStateChangeListener.onLeftOpen(this);
        }
        //滑动到左侧展开位置
        mDragHelper.smoothSlideViewTo(mContentView, mLeftWidth, 0);
        invalidate();
    }

    /**
     * 展开右侧
     */
    public void openRight() {
        //左侧完全展开时，才能向右侧展开
        if (mContentView.getLeft() == mLeftWidth) {
            if (mOnStateChangeListener != null) {
                mOnStateChangeListener.onRightOpen(this);
            }
            //滑动到右侧展开位置
            mDragHelper.smoothSlideViewTo(mContentView, -(mRightWidth - mLeftWidth), 0);
            invalidate();
        }
    }

    /**
     * 关闭
     */
    public void close() {
        if (mOnStateChangeListener != null) {
            mOnStateChangeListener.onClose(this);
        }
        //将 mContentView 宽度复原
        if (mContentView.getLeft() > 0) {
            //左边展开复原
            mContentView.layout(mLeftWidth, 0, mWidth + mLeftWidth, mHeight);
            mLeftView.layout(0, 0, mLeftWidth, mHeight);
        } else {
            //右边展开复原
            mContentView.layout(-mRightWidth, 0, mWidth - mRightWidth, mHeight);
            mLeftView.layout(-mLeftWidth - mRightWidth, 0, -mRightWidth, mHeight);
        }
        mSortView.setVisibility(INVISIBLE);
        //滑动到关闭位置
        mDragHelper.smoothSlideViewTo(mContentView, 0, 0);
        invalidate();
    }

}
