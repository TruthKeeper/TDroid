package com.tk.tdroiddemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tk.tdroid.base.BaseActivity;
import com.tk.tdroiddemo.sample.SampleUIActivity;
import com.tk.widget.recycler.adapter.FasterAdapter;
import com.tk.widget.recycler.adapter.FasterHolder;
import com.tk.widget.recycler.adapter.Strategy;
import com.tk.tdroiddemo.sample.SampleHttpActivity;
import com.tk.tdroiddemo.sample.SampleViewLoaderActivity;
import com.tk.tdroiddemo.sample.SampleSpannableActivity;
import com.tk.tdroiddemo.sample.SampleToastActivity;


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
        adapter = new FasterAdapter.Builder<Item>()
                .fillBySingleStrategy(new Item[]{
                        new Item(SampleSpannableActivity.class, "Spannable"),
                        new Item(SampleHttpActivity.class, "Http测试"),
                        new Item(SampleViewLoaderActivity.class, "View视图加载"),
                        new Item(SampleToastActivity.class, "Toast测试 "),
                        new Item(SampleUIActivity.class, "UI封装 "),
                }, new MainStrategy())
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
        startActivity(new Intent(this, this.adapter.getListItem(listPosition).getCls()));
    }

    static class Item {
        private Class<? extends Activity> cls;
        private CharSequence item;

        public Item(Class<? extends Activity> cls, CharSequence item) {
            this.cls = cls;
            this.item = item;
        }

        public Class<? extends Activity> getCls() {
            return cls;
        }

        public CharSequence getItem() {
            return item;
        }
    }

    static class MainStrategy extends Strategy<Item> {
        @Override
        public int layoutId() {
            return R.layout.item_main;
        }

        @Override
        public void onBindViewHolder(FasterHolder holder, Item data) {
            holder.setText(R.id.item, data.getItem());
        }
    }
}
