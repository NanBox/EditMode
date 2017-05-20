package com.southernbox.editlayout.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.southernbox.editlayout.R;
import com.southernbox.editlayout.adapter.EditAdapter;
import com.southernbox.editlayout.widget.EditLayout;
import com.southernbox.editlayout.widget.EditRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by SouthernBox on 2016/10/25 0025.
 * 主页面
 */

public class MainActivity extends AppCompatActivity implements EditLayout.OnItemSortListener {

    private EditAdapter mAdapter;
    private TextView tvEdit;
    private boolean isEdit;
    private ItemTouchHelper mItemTouchHelper;
    private String[] names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTitle();
        initRecyclerView();
    }

    private void initTitle() {
        tvEdit = (TextView) findViewById(R.id.tv_edit);
        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    tvEdit.setText("编辑");
                } else {
                    tvEdit.setText("完成");
                }
                isEdit = !isEdit;
                mAdapter.setEdit(isEdit);
            }
        });
    }

    private void initRecyclerView() {
        EditRecyclerView mRecyclerView = (EditRecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        names = getResources().getStringArray(R.array.query_suggestions);
        List<String> mList = new ArrayList<>();
        Collections.addAll(mList, names);
        mAdapter = new EditAdapter(this, mList);
        mAdapter.setOnItemSortListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mItemTouchHelper = new ItemTouchHelper(new EditItemTouchHelperCallback());
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onStartDrags(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        //1、交换数据
//        Collections.swap(mData, fromPosition, toPosition);
        String temp = names[fromPosition];
        names[fromPosition] = names[toPosition];
        names[toPosition] = temp;
        //2、刷新
        mAdapter.notifyItemMoved(fromPosition, toPosition);
    }

    public class EditItemTouchHelperCallback extends ItemTouchHelper.Callback {

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            // 如果你不想上下拖动，可以将 dragFlags = 0
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;

            // 如果你不想左右滑动，可以将 swipeFlags = 0
            int swipeFlags = 0;

            //最终的动作标识（flags）必须要用makeMovementFlags()方法生成
            int flags = makeMovementFlags(dragFlags, swipeFlags);
            return flags;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public boolean isLongPressDragEnabled() {
            //禁止长按拖拽
            return false;
        }
    }
}
