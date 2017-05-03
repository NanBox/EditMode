package com.southernbox.swipedeletelayout.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.southernbox.swipedeletelayout.R;
import com.southernbox.swipedeletelayout.widget.SwipeDeleteLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SouthernBox on 2016/10/25 0025.
 * 主页面适配器
 */

public class MainAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<String> mList;
    public static boolean isEdit;

    private static ArrayList<SwipeDeleteLayout> allItems = new ArrayList<>();
    public static SwipeDeleteLayout mRightOpenItem;  //向右展开的删除项，只会存在一项

    public MainAdapter(Context context, List<String> List) {
        this.mContext = context;
        this.mList = List;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_swipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final SwipeDeleteLayout layout = viewHolder.swipeDeleteLayout;

        viewHolder.tvName.setText(mList.get(position));

        viewHolder.vPreDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRightOpenItem != null) {
                    mRightOpenItem.openLeft();
                } else {
                    layout.openRight();
                }
            }
        });

        viewHolder.vDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                mList.remove(position);
                mRightOpenItem = null;
                notifyItemRemoved(position);
                if (position != mList.size()) {
                    notifyItemRangeChanged(position, mList.size() - position);
                }
            }
        });

        layout.setOnDragStateChangeListener(new SwipeDeleteLayout.OnStateChangeListener() {

            @Override
            public void onPreExecuted(SwipeDeleteLayout layout) {
                if (!allItems.contains(layout)) {
                    allItems.add(layout);
                }
            }

            @Override
            public void onClose(SwipeDeleteLayout layout) {
                if (mRightOpenItem == layout) {
                    mRightOpenItem = null;
                }
            }

            @Override
            public void onLeftOpen(SwipeDeleteLayout layout) {
                if (mRightOpenItem == layout) {
                    mRightOpenItem = null;
                }
            }

            @Override
            public void onRightOpen(SwipeDeleteLayout layout) {
                if (mRightOpenItem != layout) {
                    mRightOpenItem = layout;
                }
            }
        });

    }

    /**
     * 设置编辑状态
     *
     * @param isEdit 是否为编辑状态
     */
    public void setEdit(boolean isEdit) {
        MainAdapter.isEdit = isEdit;
    }

    /**
     * 关闭所有 item
     */
    public void closeAll() {
        for (SwipeDeleteLayout layout : allItems) {
            layout.close();
        }
    }

    /**
     * 将所有 item 向左展开
     */
    public void openLeftAll() {
        for (SwipeDeleteLayout layout : allItems) {
            layout.openLeft();
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        SwipeDeleteLayout swipeDeleteLayout;
        TextView tvName;
        View vPreDelete;
        View vDelete;

        ViewHolder(View itemView) {
            super(itemView);
            swipeDeleteLayout = (SwipeDeleteLayout) itemView.findViewById(R.id.swipe_delete_layout);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            vPreDelete = itemView.findViewById(R.id.fl_pre_delete);
            vDelete = itemView.findViewById(R.id.fl_delete);
        }
    }
}
