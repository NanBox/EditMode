package com.southernbox.editlayout.sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.southernbox.editmode.EditAdapter;
import com.southernbox.editmode.EditLayout;
import com.southernbox.editmode.EditViewHolder;

import java.util.List;

/**
 * Created by SouthernBox on 2016/10/25 0025.
 * 主页面适配器
 */

public class MainAdapter extends EditAdapter<String> {

    MainAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public EditViewHolder onCreateEditViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindEditViewHolder(EditViewHolder holder, int position) {
        TextView tvName = (TextView) holder.vContent;
        tvName.setText(mList.get(position));
    }

    private static class ViewHolder extends EditViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public EditLayout setEditLayout(View itemView) {
            return itemView.findViewById(R.id.edit_layout);
        }

        @Override
        public View setContent(View itemView) {
            return itemView.findViewById(R.id.tv_name);
        }

        @Override
        public View setPreDelete(View itemView) {
            return itemView.findViewById(R.id.fl_pre_delete);
        }

        @Override
        public View setDelete(View itemView) {
            return itemView.findViewById(R.id.fl_delete);
        }

        @Override
        public View setSort(View itemView) {
            return itemView.findViewById(R.id.fl_sort);
        }
    }
}
