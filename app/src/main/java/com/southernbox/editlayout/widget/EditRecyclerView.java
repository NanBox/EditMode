package com.southernbox.editlayout.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.southernbox.editlayout.adapter.EditAdapter;

/**
 * Created by SouthernBox on 2017/5/2 0002.
 * 自定义列表控件，判断是否可滑动
 */

public class EditRecyclerView extends RecyclerView {

    private boolean isEdit;             //是否为编辑状态
    private EditLayout rightOpenItem;   //向右展开项

    public EditRecyclerView(Context context) {
        super(context);
    }

    public EditRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EditRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (getAdapter() instanceof EditAdapter) {
                    EditAdapter adapter = (EditAdapter) getAdapter();
                    rightOpenItem = adapter.getRightOpenItem();
                    isEdit = adapter.isEdit();
                }
                if (isEdit && rightOpenItem != null) {
                    rightOpenItem.openLeft();
                }
        }
        return super.onTouchEvent(e);
    }
}
