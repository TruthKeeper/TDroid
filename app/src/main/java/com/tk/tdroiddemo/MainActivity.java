package com.tk.tdroiddemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tk.tdroid.base.BaseActivity;
import com.tk.tdroid.recycler.adapter.Entry;
import com.tk.tdroid.recycler.adapter.FasterAdapter;
import com.tk.tdroid.recycler.adapter.FasterHolder;
import com.tk.tdroid.recycler.adapter.Strategy;
import com.tk.tdroiddemo.sample.SampleAutoInjectActivity;
import com.tk.tdroiddemo.sample.SampleHttpActivity;
import com.tk.tdroiddemo.sample.SampleSaveRestoreActivity;
import com.tk.tdroiddemo.sample.SampleSpannableActivity;
import com.tk.tdroiddemo.sample.SampleToastActivity;
import com.tk.tdroiddemo.sample.SampleUIActivity;
import com.tk.tdroiddemo.sample.SampleViewLoaderActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements FasterAdapter.OnItemClickListener {
    private Toolbar toolbar;
    private RecyclerView recyclerview;

    private FasterAdapter<Item> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        List<Entry<Item>> list = new ArrayList<>();
        list.add(Entry.create(new Item(ComponentsActivity.class, "组件化")));
        list.add(Entry.create(new Item(SampleSpannableActivity.class, "Spannable")));
        list.add(Entry.create(new Item(SampleHttpActivity.class, "Http测试")));
        list.add(Entry.create(new Item(SampleViewLoaderActivity.class, "View视图加载")));
        list.add(Entry.create(new Item(SampleToastActivity.class, "Toast测试")));
        list.add(Entry.create(new Item(SampleUIActivity.class, "UI封装")));
        list.add(Entry.create(new Item(SampleSaveRestoreActivity.class, "APT_自动保存、恢复数据")));
        list.add(Entry.create(new Item(SampleAutoInjectActivity.class, "APT_自动注入携带数据", new Intent(MainActivity.this, SampleAutoInjectActivity.class)
                .putExtra("nickName", "张三")
                .putExtra("userId", 233L)
                .putExtra("sex", true)
                .putExtra("parent", new SampleAutoInjectActivity.Extra("李四", "王五")))));

        adapter = new FasterAdapter.Builder<Item>()
                .bind(Item.class, new MainStrategy())
                .data(list)
                .itemClickListener(this)
                .build();
        recyclerview.setAdapter(adapter);
    }

    /**
     * 单击
     *
     * @param adapter
     * @param view
     * @param listPosition
     */
    @Override
    public void onItemClick(FasterAdapter adapter, View view, int listPosition) {
        Item listItem = this.adapter.getListItem(listPosition);
        startActivity(listItem.intent == null ? new Intent(this, listItem.cls) : listItem.intent);
    }

    static class Item {
        public final Class<? extends Activity> cls;
        public final CharSequence item;
        public final Intent intent;

        public Item(Class<? extends Activity> cls, CharSequence item) {
            this.cls = cls;
            this.item = item;
            this.intent = null;
        }

        public Item(Class<? extends Activity> cls, CharSequence item, Intent intent) {
            this.cls = cls;
            this.item = item;
            this.intent = intent;
        }
    }

    static class MainStrategy extends Strategy<Item> {
        @Override
        public int layoutId() {
            return R.layout.item_main;
        }

        @Override
        public void onBindViewHolder(FasterHolder holder, Item data) {
            holder.setText(R.id.item, data.item);
        }
    }
}
