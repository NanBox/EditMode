package com.southernbox.editlayout.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by SouthernBox on 2016/10/27 0027.
 * 侧滑删除控件
 */

public class EditLayout extends FrameLayout {

    private View mContentView;  //内容部分
    private View mLeftView;     //左边圆形删除按键
    private View mRightView;    //右边删除按键
    private int mWidth;         //内容部分宽度
    private int mHeight;        //内容部分高度
    private int mLeftWidth;     //左边部分宽度
    private int mRightWidth;    //右边部分宽度
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
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (child == mContentView) {
                    if (left < -mRightWidth) {
                        left = -mRightWidth;
                    } else if (left > mLeftWidth) {
                        left = mLeftWidth;
                    }
                } else if (child == mRightView) {
                    if (left < mWidth - mRightWidth) {
                        left = mWidth - mRightWidth;
                    } else if (left > mWidth) {
                        left = mWidth;
                    }
                } else if (child == mLeftView) {
                    if (left < mWidth - mRightWidth) {
                        left = mWidth - mRightWidth;
                    } else if (left > -mLeftWidth) {
                        left = 0 - mLeftWidth;
                    }
                }
                return left;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                if (changedView == mContentView) {
                    mRightView.offsetLeftAndRight(dx);
                    mLeftView.offsetLeftAndRight(dx);
                } else if (changedView == mRightView) {
                    mContentView.offsetLeftAndRight(dx);
                    mLeftView.offsetLeftAndRight(dx);
                } else if (changedView == mLeftView) {
                    mContentView.offsetLeftAndRight(dx);
                    mRightView.offsetLeftAndRight(dx);
                }
                invalidate();
            }
        };

        mDragHelper = ViewDragHelper.create(this, mCallback);
    }

    public interface OnStateChangeListener {

        void onLeftOpen(EditLayout layout);

        void onRightOpen(EditLayout layout);

        void onClose(EditLayout layout);

    }

    private OnStateChangeListener mOnStateChangeListener;

    public void setOnDragStateChangeListener(OnStateChangeListener onStateChangeListener) {
        mOnStateChangeListener = onStateChangeListener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mLeftView = getChildAt(0);
        mContentView = getChildAt(1);
        mRightView = getChildAt(2);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mRightWidth = mRightView.getMeasuredWidth();
        mLeftWidth = mLeftView.getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //判断是否为编辑模式,摆放每个子View的位置
        if (isEdit) {
            mContentView.layout(mLeftWidth, 0, mLeftWidth + mWidth, mHeight);
            mRightView.layout(mWidth + mLeftWidth, 0, mRightWidth + mWidth + mLeftWidth, mHeight);
            mLeftView.layout(0, 0, mLeftWidth, mHeight);
        } else {
            mContentView.layout(0, 0, mWidth, mHeight);
            mRightView.layout(mWidth, 0, mRightWidth + mWidth, mHeight);
            mLeftView.layout(-mLeftWidth, 0, 0, mHeight);
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
        mDragHelper.smoothSlideViewTo(mContentView, mLeftWidth, 0);
        invalidate();
    }

    /**
     * 展开右侧
     */
    public void openRight() {
        if (mOnStateChangeListener != null) {
            mOnStateChangeListener.onRightOpen(this);
        }
        mDragHelper.smoothSlideViewTo(mContentView, -mRightWidth, 0);
        invalidate();
    }

    /**
     * 关闭
     */
    public void close() {
        if (mOnStateChangeListener != null) {
            mOnStateChangeListener.onClose(this);
        }
        mDragHelper.smoothSlideViewTo(mContentView, 0, 0);
        invalidate();
    }

}
