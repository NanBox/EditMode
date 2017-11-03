package com.southernbox.editmode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.Collections;

/**
 * Created by SouthernBox on 2017/5/2 0002.
 * 自定义 RecyclerView，判断是否可滑动
 */

public class EditRecyclerView extends RecyclerView implements EditLayout.OnItemSortListener {

    private boolean isEdit;             //是否为编辑状态
    private EditLayout rightOpenItem;   //向右展开项
    private ItemTouchHelper mItemTouchHelper;

    public EditRecyclerView(Context context) {
        this(context, null);
    }

    public EditRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setItemAnimator(new DefaultItemAnimator());
        mItemTouchHelper = new ItemTouchHelper(new EditTouchHelperCallback(this));
        mItemTouchHelper.attachToRecyclerView(this);
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
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

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter instanceof EditAdapter) {
            ((EditAdapter) adapter).setOnItemSortListener(this);
        }
    }

    @Override
    public void onStartDrags(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        EditAdapter mAdapter = (EditAdapter) getAdapter();
        Collections.swap(mAdapter.getList(), fromPosition, toPosition);
        mAdapter.notifyItemMoved(fromPosition, toPosition);
    }
}
