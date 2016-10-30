package com.southernbox.swipedeletelayout.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by SouthernBox on 2016/10/27 0027.
 */

public class SwipeDeleteLayout extends FrameLayout {

    private ViewDragHelper mDragHelper;
    private View mBackView;
    private View mFrontView;
    private View mLeftView;
    private int mWidth;
    private int mHeight;
    private int mBackWidth;
    private int mLeftWidth;

    public final static int STATE_CLOSE = 1;      //关闭状态
    public final static int STATE_OPEN_LEFT = 2;  //左边打开
    public final static int STATE_OPEN_RIGHT = 3; //右边打开

    public SwipeDeleteLayout(Context context) {
        this(context, null);
    }

    public SwipeDeleteLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeDeleteLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        //限定移动范围
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == mFrontView) {
                if (left < -mBackWidth) {
                    left = -mBackWidth;
                } else if (left > mLeftWidth) {
                    left = mLeftWidth;
                }
            } else if (child == mBackView) {
                if (left < mWidth - mBackWidth) {
                    left = mWidth - mBackWidth;
                } else if (left > mWidth) {
                    left = mWidth;
                }
            } else if (child == mLeftView) {
                if (left < mWidth - mBackWidth) {
                    left = mWidth - mBackWidth;
                } else if (left > -mLeftWidth) {
                    left = 0 - mLeftWidth;
                }
            }
            return left;
        }

        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if (changedView == mFrontView) {
                mBackView.offsetLeftAndRight(dx);
                mLeftView.offsetLeftAndRight(dx);
            } else if (changedView == mBackView) {
                mFrontView.offsetLeftAndRight(dx);
                mLeftView.offsetLeftAndRight(dx);
            } else if (changedView == mLeftView) {
                mFrontView.offsetLeftAndRight(dx);
                mBackView.offsetLeftAndRight(dx);
            }
            dispatchDragState(mFrontView.getLeft());
            invalidate();
        }
    };

    public enum State {
        CLOSE, OPENLEFT, OPENRIGHT, DRAGGING
    }

    private State mState = State.CLOSE;

    public interface OnDragStateChangeListener {

        void onPreExecuted(SwipeDeleteLayout layout);

        void onClose(SwipeDeleteLayout layout);

        void onLeftOpen(SwipeDeleteLayout layout);

        void onRightOpen(SwipeDeleteLayout layout);

        void onDragging();

        void onStartRightOpen(SwipeDeleteLayout layout);
    }

    private OnDragStateChangeListener mOnDragStateChangeListener;

    public State getState() {
        return mState;
    }

    protected void dispatchDragState(int left) {
        State preState = mState;
        mState = updateState(left);
        if (mOnDragStateChangeListener != null) {
            if (mState != preState) {
                if (mState == State.OPENLEFT) {
                    mOnDragStateChangeListener.onLeftOpen(this);
                } else if (mState == State.OPENRIGHT) {
                    mOnDragStateChangeListener.onRightOpen(this);
                } else if (mState == State.CLOSE) {
                    mOnDragStateChangeListener.onClose(this);
                } else if (mState == State.DRAGGING) {
                    if (preState == State.OPENLEFT) {
                        mOnDragStateChangeListener.onStartRightOpen(this);
                    }
                }
            } else {
                mOnDragStateChangeListener.onDragging();
            }
        }
    }

    private State updateState(int left) {
        if (left == mLeftWidth) {
            return State.OPENLEFT;
        } else if (left == -mBackWidth) {
            return State.OPENRIGHT;
        } else if (left == 0) {
            return State.CLOSE;
        } else {
            return State.DRAGGING;
        }
    }

    public void setState(State state) {
        mState = state;
    }

    public OnDragStateChangeListener getOnDragStateChangeListener() {
        return mOnDragStateChangeListener;
    }

    public void setOnDragStateChangeListener(OnDragStateChangeListener onDragStateChangeListener) {
        mOnDragStateChangeListener = onDragStateChangeListener;
        mOnDragStateChangeListener.onPreExecuted(this);
    }

    public void rightOpen() {
        rightOpen(true);
    }

    public void rightOpen(boolean isSmooth) {
        if (isSmooth) {
            mDragHelper.smoothSlideViewTo(mFrontView, -mBackWidth, 0);
            invalidate();
        } else {
            layoutContent(STATE_OPEN_RIGHT);
        }
    }

    public void close() {
        close(true);
    }

    public void leftOpen(boolean isSmooth) {
        if (isSmooth) {
            mDragHelper.smoothSlideViewTo(mFrontView, mLeftWidth, 0);
            invalidate();
        } else {
            layoutContent(STATE_OPEN_LEFT);
        }
    }

    public void close(boolean isSmooth) {
        if (isSmooth) {
            mDragHelper.smoothSlideViewTo(mFrontView, 0, 0);
            invalidate();
        } else {
            layoutContent(STATE_CLOSE);
        }
    }

    private void layoutContent(int state) {
        Rect frontRect = computeFrontRect(state);
        Rect backRect = computeBackRectFromFront(frontRect);
        Rect leftRect = computeLeftRectFromFront(frontRect);
        mFrontView.layout(frontRect.left, frontRect.top, frontRect.right, frontRect.bottom);
        mBackView.layout(backRect.left, backRect.top, backRect.right, backRect.bottom);
        mLeftView.layout(leftRect.left, leftRect.top, leftRect.right, leftRect.bottom);
    }

    private Rect computeBackRectFromFront(Rect frontRect) {
        return new Rect(frontRect.right, frontRect.top, frontRect.right + mBackWidth,
                frontRect.bottom);
    }

    private Rect computeLeftRectFromFront(Rect frontRect) {
        return new Rect(frontRect.left - mLeftWidth, frontRect.top, frontRect.left, frontRect.bottom);
    }

    private Rect computeFrontRect(int state) {
        int left = 0;
        if (state == STATE_OPEN_LEFT) {
            left = mLeftWidth;
        } else if (state == STATE_OPEN_RIGHT) {
            left = -mBackWidth;
        }
        return new Rect(left, 0, left + mWidth, mHeight);
    }

    private void init() {
        mDragHelper = ViewDragHelper.create(this, mCallback);
    }

    //在onLayout中"摆放"每个子View的位置
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //判断是否为编辑模式
        if (isEdit) {
            mFrontView.layout(mLeftWidth, 0, mLeftWidth + mWidth, mHeight);
            mBackView.layout(mWidth + mLeftWidth, 0, mBackWidth + mWidth + mLeftWidth, mHeight);
            mLeftView.layout(0, 0, mLeftWidth, mHeight);
        } else {
            mFrontView.layout(0, 0, mWidth, mHeight);
            mBackView.layout(mWidth, 0, mBackWidth + mWidth, mHeight);
            mLeftView.layout(-mLeftWidth, 0, 0, mHeight);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mBackView = getChildAt(0);
        mFrontView = getChildAt(1);
        mLeftView = getChildAt(2);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mBackWidth = mBackView.getMeasuredWidth();
        mLeftWidth = mLeftView.getMeasuredWidth();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private boolean isEdit;

    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
        mDragHelper.abort();
    }
}
