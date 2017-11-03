package com.southernbox.editmode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SouthernBox on 2016/10/25 0025.
 * 列表适配器
 */

public abstract class EditAdapter<T> extends RecyclerView.Adapter {

    protected Context mContext;
    protected List<T> mList;

    private boolean isEdit;  //是否处于编辑状态
    private List<EditLayout> allItems = new ArrayList<>();
    private EditLayout mRightOpenItem;  //向右展开的删除项，只会存在一项

    public EditAdapter(Context context, List<T> List) {
        this.mContext = context;
        this.mList = List;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateEditViewHolder(parent, viewType);
    }

    public abstract EditViewHolder onCreateEditViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final EditViewHolder viewHolder = (EditViewHolder) holder;
        final EditLayout editLayout = viewHolder.editLayout;

        if (!allItems.contains(editLayout)) {
            allItems.add(editLayout);
        }

        editLayout.setEdit(isEdit);

        onBindEditViewHolder(viewHolder, position);

        viewHolder.vPreDelete.setOnTouchListener(new View.OnTouchListener() {

            @Override
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (isEdit && mRightOpenItem != null) {
                            mRightOpenItem.openLeft();
                        } else {
                            editLayout.openRight();
                        }
                }
                return true;
            }
        });

        viewHolder.vDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                if (position >= 0) {
                    mList.remove(position);
                    mRightOpenItem = null;
                    notifyItemRemoved(position);
                    if (position != mList.size()) {
                        notifyItemRangeChanged(position, mList.size() - position);
                    }
                }
            }
        });

        viewHolder.vSort.setOnTouchListener(new View.OnTouchListener() {

            @Override
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (isEdit && mRightOpenItem != null) {
                    mRightOpenItem.openLeft();
                } else {
                    mOnItemSortListener.onStartDrags(viewHolder);
                }
                return false;
            }
        });

        editLayout.setOnDragStateChangeListener(new EditLayout.OnStateChangeListener() {

            @Override
            public void onLeftOpen(EditLayout layout) {
                if (mRightOpenItem == layout) {
                    mRightOpenItem = null;
                }
            }

            @Override
            public void onRightOpen(EditLayout layout) {
                if (mRightOpenItem != layout) {
                    mRightOpenItem = layout;
                }
            }

            @Override
            public void onClose(EditLayout layout) {
                if (mRightOpenItem == layout) {
                    mRightOpenItem = null;
                }
            }
        });

    }

    public abstract void onBindEditViewHolder(EditViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * 设置编辑状态
     *
     * @param isEdit 是否为编辑状态
     */
    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
        if (isEdit) {
            openLeftAll();
        } else {
            closeAll();
        }
        for (EditLayout editLayout : allItems) {
            editLayout.setEdit(isEdit);
        }
    }

    private EditLayout.OnItemSortListener mOnItemSortListener;

    public void setOnItemSortListener(EditLayout.OnItemSortListener onItemSortListener) {
        mOnItemSortListener = onItemSortListener;
    }

    /**
     * 关闭所有 item
     */
    private void closeAll() {
        for (EditLayout editLayout : allItems) {
            editLayout.close();
        }
    }

    /**
     * 将所有 item 向左展开
     */
    private void openLeftAll() {
        for (EditLayout editLayout : allItems) {
            editLayout.openLeft();
        }
    }

    /**
     * 获取编辑状态
     *
     * @return 是否为编辑状态
     */
    public boolean isEdit() {
        return isEdit;
    }

    /**
     * 获取向右展开的 item
     *
     * @return 向右展开的 item
     */
    public EditLayout getRightOpenItem() {
        return mRightOpenItem;
    }

    public List<T> getList() {
        return mList;
    }
}
