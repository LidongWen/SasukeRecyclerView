package com.wenld.sasukeRecyclerview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * <p/>
 * Author: 温利东 on 2017/6/16 10:33.
 * http://www.jianshu.com/u/99f514ea81b3
 * github: https://github.com/LidongWen
 * Description: 下拉刷新的辅助类为了匹配所有效果
 */

public abstract class RefreshViewCreator {
    // 默认状态
    protected int REFRESH_STATUS_NORMAL = 0x0011;
    // 下拉刷新状态
    protected int REFRESH_STATUS_PULL_DOWN_REFRESH = 0x0022;
    // 松开刷新状态
    protected int REFRESH_STATUS_LOOSEN_REFRESHING = 0x0033;
    // 正在刷新状态
    protected int REFRESH_STATUS_REFRESHING = 0x0044;
    /**
     * 获取下拉刷新的View
     *
     * @param context 上下文
     * @param parent  RecyclerView
     */
    public abstract View getRefreshView(Context context, ViewGroup parent);

    /**
     * 正在下拉
     * @param currentDragHeight   当前拖动的高度
     * @param refreshViewHeight  总的刷新高度
     * @param currentRefreshStatus 当前状态
     */
    public abstract void onPull(int currentDragHeight, int refreshViewHeight, int currentRefreshStatus);

    /**
     * 正在刷新中
     */
    public abstract void onRefreshing();

    /**
     * 停止刷新
     */
    public abstract void onStopRefresh();
}
