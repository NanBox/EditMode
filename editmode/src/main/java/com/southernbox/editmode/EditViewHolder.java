package com.southernbox.editmode;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by nanquan.lin on 2017/11/3 0003.
 * 编辑项的 ViewHolder
 */

public abstract class EditViewHolder extends RecyclerView.ViewHolder {
    public EditLayout editLayout;
    public View vContent;
    public View vPreDelete;
    public View vDelete;
    public View vSort;

    public EditViewHolder(View itemView) {
        super(itemView);
        editLayout = setEditLayout(itemView);
        vContent = setContent(itemView);
        vPreDelete = setPreDelete(itemView);
        vDelete = setDelete(itemView);
        vSort = setSort(itemView);
    }

    public abstract EditLayout setEditLayout(View itemView);

    public abstract View setContent(View itemView);

    public abstract View setPreDelete(View itemView);

    public abstract View setDelete(View itemView);

    public abstract View setSort(View itemView);

}
