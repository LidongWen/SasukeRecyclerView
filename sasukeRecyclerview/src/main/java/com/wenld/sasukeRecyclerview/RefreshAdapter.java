package com.wenld.sasukeRecyclerview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.wenld.multitypeadapter.utils.WrapperUtils;
import com.wenld.multitypeadapter.wrapper.LoadMoreWrapper2;

/**
 * <p/>
 * Author: 温利东 on 2017/6/16 10:33.
 * http://www.jianshu.com/u/99f514ea81b3
 * github: https://github.com/LidongWen
 */
final class RefreshAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ILodMoreFunction {
    private final static String TAG = "RefreshAdapter";
    private View mHeaderView;

    // 基本的头部类型开始位置  用于viewType
    private static int BASE_ITEM_TYPE_HEADER = 10000000;

    // 列表的Adapter
    private LoadMoreWrapper2 loadMoreWrapper2;

    public RefreshAdapter(RecyclerView.Adapter adapter) {
        this.loadMoreWrapper2 = new LoadMoreWrapper2(adapter);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (isHeaderViewType(viewType)) {
            return createHeaderFooterViewHolder(mHeaderView);
        }

        return loadMoreWrapper2.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        WrapperUtils.onAttachedToRecyclerView(loadMoreWrapper2, recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {

                if (isHeaderPosition(position)) {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null) {
                    return oldLookup.getSpanSize(calculationPosition(position));
                }
                return 1;
            }
        });
    }

    /**
     * 创建头部或者底部的ViewHolder
     */
    private RecyclerView.ViewHolder createHeaderFooterViewHolder(View view) {
        return new RecyclerView.ViewHolder(view) {
        };
    }

    /**
     * 是不是头部类型
     */
    private boolean isHeaderViewType(int viewType) {
        return BASE_ITEM_TYPE_HEADER == viewType;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderPosition(position)) {
            return;
        }

        int adapterPosition = calculationPosition(position);
        loadMoreWrapper2.onBindViewHolder(holder, adapterPosition);

    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderPosition(position)) {
            // 直接返回position位置的key
            return BASE_ITEM_TYPE_HEADER;
        }

        return loadMoreWrapper2.getItemViewType(calculationPosition(position));
    }

    private boolean isHeaderPosition(int position) {
        return mHeaderView != null && position < 1;
    }

    @Override
    public int getItemCount() {
        return loadMoreWrapper2.getItemCount() + 1;
    }

    public int calculationPosition(int position) {
        return position - 1;
    }

    private LoadMoreWrapper2 getAdapter() {
        return loadMoreWrapper2;
    }

    public void addHeaderView(View view) {
        mHeaderView = view;
        notifyDataSetChanged();
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
        int position = viewHolder.getLayoutPosition();
        if (isHeaderPosition(position)) {
            WrapperUtils.setFullSpan(viewHolder);
            return;
        }
        loadMoreWrapper2.onViewAttachedToWindow(viewHolder, calculationPosition(position));
    }

    @Override
    public void setLoadMoreEnble(boolean b) {
        loadMoreWrapper2.setLoadMore(b);
    }

    @Override
    public boolean isLoading() {
        return loadMoreWrapper2.isLoading();
    }

    @Override
    public void stopLoad() {
        loadMoreWrapper2.loadingComplete();
    }

    @Override
    public void setLoadMoreLayoutId(int layoutId) {
        loadMoreWrapper2.setLoadMoreView(layoutId);
    }

    @Override
    public void setLoadMoreView(View view) {
        loadMoreWrapper2.setLoadMoreView(view);
    }

    @Override
    public void setOnLoadMoreListener(LoadMoreWrapper2.OnLoadMoreListener loadMoreListener) {
        loadMoreWrapper2.setOnLoadMoreListener(loadMoreListener);
    }
}
