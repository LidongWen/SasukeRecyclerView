package com.wenld.pullrecyclerview.refreshLoad;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.wenld.multitypeadapter.MultiTypeAdapter;
import com.wenld.multitypeadapter.wrapper.LoadMoreWrapper2;
import com.wenld.pullrecyclerview.R;
import com.wenld.pullrecyclerview.refreshLoad.adapter.ItemVIew01;
import com.wenld.pullrecyclerview.refreshLoad.adapter.ItemVIew02;
import com.wenld.pullrecyclerview.refreshLoad.adapter.ItemVIew03;
import com.wenld.pullrecyclerview.refreshLoad.adapter.ItemVIewNormal;
import com.wenld.pullrecyclerview.refreshLoad.bean.Bean01;
import com.wenld.pullrecyclerview.refreshLoad.bean.Bean02;
import com.wenld.pullrecyclerview.refreshLoad.bean.Bean03;
import com.wenld.sasukeRecyclerview.DefaultRefreshCreator;
import com.wenld.sasukeRecyclerview.RefreshLoadRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * Author: 温利东 on 2017/6/16 10:33.
 * http://www.jianshu.com/u/99f514ea81b3
 * github: https://github.com/LidongWen
 */
public class RefreshLoadActivity extends AppCompatActivity implements RefreshLoadRecyclerView.OnRefreshListener,
        LoadMoreWrapper2.OnLoadMoreListener {
    private RefreshLoadRecyclerView recyclerView;
    private MultiTypeAdapter adapter;
    //    private LoadMoreWrapper2 loadMoreWrapper2;
    List<Object> items;
    int SPAN_COUNT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refreshload);

        adapter = new MultiTypeAdapter();
        recyclerView = (RefreshLoadRecyclerView) findViewById(R.id.rlv_refresh_load);


        initRegister();

        setLayoutManager(recyclerView);

        int space = getResources().getDimensionPixelSize(R.dimen.normal_space);
        recyclerView.addItemDecoration(new ItemDecoration(space));
//        loadMoreWrapper2=new LoadMoreWrapper2(adapter);
//        loadMoreWrapper2.setLoadMoreView(R.layout.default_loading);
        recyclerView.setAdapter(adapter);
        recyclerView.setRefreshCreator(new DefaultRefreshCreator());
        recyclerView.setOnRefreshListener(this);
        recyclerView.setLoadMoreLayoutId(R.layout.default_loading);
        recyclerView.setOnLoadMoreListener(this);

        initData();

        adapter.setItems(items);
        adapter.notifyDataSetChanged();
    }

    private void initRegister() {
        adapter.register(String.class, new ItemVIewNormal());
        adapter.register(Bean01.class, new ItemVIew01());
        adapter.register(Bean02.class, new ItemVIew02());
        adapter.register(Bean03.class, new ItemVIew03());
    }

    private void setLayoutManager(RecyclerView recyclerView) {
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                Object item = items.get(position);
                if (item instanceof Bean01) {
                    return 1;
                }
                if (item instanceof Bean02) {
                    return 1;
                }
                if (item instanceof Bean03) {
                    return SPAN_COUNT;
                }
                if (item instanceof String) {
                    return SPAN_COUNT;
                }
                return 1;
            }
        };
//        layoutManager.setSpanSizeLookup(spanSizeLookup);
//        layoutManager.setSpanCount(2);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void initData() {
        items = new ArrayList<>();
        for (int j = 0; j < 1; j++) {
//            items.add("混合式 瀑布流 \n 多数据 -> 多类型  单数据 -> 多类型");

            for (int i = 0; i < 1; i++) {
                items.add(new Bean02("bean02_" + i));
            }
            for (int i = 0; i < 2; i++) {
                items.add(new Bean01("bean01_" + i));
            }
            for (int i = 0; i < 1; i++) {
                items.add(new Bean02("bean02_" + i));
            }
            for (int i = 0; i < 2; i++) {
                items.add(new Bean01("bean01_" + i));
            }
            for (int i = 0; i < 1; i++) {
                items.add(new Bean03("bean03_" + i));
            }
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.stopRefresh();
                    }
                }, 2000
        );
    }

    @Override
    public void onLoadMoreRequested() {
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        items.add("加载数据");
                        items.add("加载数据");
                        recyclerView.stopLoad();
//                        recyclerView.setLoadMoreEnble(false);
                        recyclerView.notifyDataSetChanged();
                    }
                }, 2000
        );
    }
}
