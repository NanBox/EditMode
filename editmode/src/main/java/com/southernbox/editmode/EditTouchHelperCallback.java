package com.southernbox.editmode;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by nanquan.lin on 2017/5/22 0022.
 * 退拽排序回调
 */

public class EditTouchHelperCallback extends ItemTouchHelper.Callback {

    private EditLayout.OnItemSortListener mOnItemSortListener;

    public EditTouchHelperCallback(EditLayout.OnItemSortListener onItemSortListener) {
        this.mOnItemSortListener = onItemSortListener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // 允许上下拖拽
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        // 禁止左右拖拽
        int swipeFlags = 0;
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mOnItemSortListener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public boolean isLongPressDragEnabled() {
        //禁止长按触发拖拽
        return false;
    }
}
