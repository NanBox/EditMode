package com.southernbox.editdeletelayout.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.southernbox.editdeletelayout.adapter.MainAdapter;

/**
 * Created by SouthernBox on 2017/5/2 0002.
 * 自定义列表控件，判断是否可滑动
 */

public class MyRecyclerView extends RecyclerView {

    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        EditDeleteLayout rightOpenItem = ((MainAdapter) getAdapter()).getRightOpenItem();
        if (MainAdapter.isEdit && rightOpenItem != null) {
            rightOpenItem.openLeft();
            return false;
        }
        return super.onTouchEvent(e);
    }
}
