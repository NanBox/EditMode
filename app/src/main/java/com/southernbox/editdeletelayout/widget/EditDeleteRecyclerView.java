package com.southernbox.editdeletelayout.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.southernbox.editdeletelayout.adapter.EditDeleteAdapter;

/**
 * Created by SouthernBox on 2017/5/2 0002.
 * 自定义列表控件，判断是否可滑动
 */

public class EditDeleteRecyclerView extends RecyclerView {

    private EditDeleteLayout rightOpenItem;

    public EditDeleteRecyclerView(Context context) {
        super(context);
    }

    public EditDeleteRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EditDeleteRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (getAdapter() instanceof EditDeleteAdapter) {
            rightOpenItem = ((EditDeleteAdapter) getAdapter()).getRightOpenItem();
        }
        if (EditDeleteAdapter.isEdit && rightOpenItem != null) {
            rightOpenItem.openLeft();
            return false;
        }
        return super.onTouchEvent(e);
    }
}
