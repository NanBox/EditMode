package com.southernbox.editlayout.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.southernbox.editmode.EditAdapter;
import com.southernbox.editmode.EditRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by SouthernBox on 2016/10/25 0025.
 * 主页面
 */

public class MainActivity extends AppCompatActivity {

    private EditAdapter mAdapter;
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
        tvEdit = findViewById(R.id.tv_edit);
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
        EditRecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        String[] names = getResources().getStringArray(R.array.query_suggestions);
        List<String> mList = new ArrayList<>();
        Collections.addAll(mList, names);
        mAdapter = new MainAdapter(this, mList);
        mRecyclerView.setAdapter(mAdapter);
    }
}
