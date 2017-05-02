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
    public static SwipeDeleteLayout mRightOpenItem;

    private static final long delayClickTime = 500;
    private long lastClickTime;

    public MainAdapter(Context context, List<String> List) {
        this.mContext = context;
        this.mList = List;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_swipe, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.swipeDeleteLayout = (SwipeDeleteLayout) view.findViewById(R.id.sdl);
        holder.tvName = (TextView) view.findViewById(R.id.tv_name);
        holder.vPreDelete = view.findViewById(R.id.fl_predelete);
        holder.vDelete = view.findViewById(R.id.fl_delete);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;

        final SwipeDeleteLayout layout = viewHolder.swipeDeleteLayout;

        viewHolder.tvName.setText(mList.get(position));

        viewHolder.vPreDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRightOpenItem == null) {
                    layout.rightOpen();
                } else {
                    openLeftAll();
                }
            }
        });

        viewHolder.vDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastClickTime < delayClickTime) {
                    return;
                }
                int position = viewHolder.getAdapterPosition();
                mList.remove(position);
                mRightOpenItem = null;
                notifyItemRemoved(position);
                if (position != mList.size()) {
                    notifyItemRangeChanged(position, mList.size() - position);
                }
                lastClickTime = currentTime;
            }
        });

        layout.setOnDragStateChangeListener(new SwipeDeleteLayout.OnDragStateChangeListener() {

            @Override
            public void onPreExecuted(SwipeDeleteLayout layout) {
                allItems.add(layout);
            }

            @Override
            public void onDragging() {

            }

            @Override
            public void onStartRightOpen(SwipeDeleteLayout layout) {
                mRightOpenItem = layout;
            }

            @Override
            public void onClose(SwipeDeleteLayout layout) {
                mRightOpenItem = null;
            }

            @Override
            public void onLeftOpen(SwipeDeleteLayout layout) {
                mRightOpenItem = null;
            }

            @Override
            public void onRightOpen(SwipeDeleteLayout layout) {
                mRightOpenItem = layout;
            }
        });

    }

    public void closeAll() {
        for (SwipeDeleteLayout layout : allItems) {
            layout.close(true);
        }
    }

    public static void openLeftAll() {
        for (SwipeDeleteLayout sl : allItems) {
            sl.leftOpen(true);
        }
    }

    public void setEdit(boolean isEdit) {
        MainAdapter.isEdit = isEdit;
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
        }
    }
}
