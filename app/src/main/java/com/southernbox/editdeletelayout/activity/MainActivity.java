package com.southernbox.editdeletelayout.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.southernbox.editdeletelayout.R;
import com.southernbox.editdeletelayout.adapter.MainAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by SouthernBox on 2016/10/25 0025.
 * 主页面
 */

public class MainActivity extends AppCompatActivity {

    private MainAdapter mAdapter;
    private TextView tvEdit;
    private boolean isEdit;

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
                    mAdapter.closeAll();
                } else {
                    tvEdit.setText("完成");
                    mAdapter.openLeftAll();
                }
                isEdit = !isEdit;
                mAdapter.setEdit(isEdit);
            }
        });
    }

    private void initRecyclerView() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        String[] names = getResources().getStringArray(R.array.query_suggestions);
        List<String> mList = new ArrayList<>();
        Collections.addAll(mList, names);
        mAdapter = new MainAdapter(this, mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
