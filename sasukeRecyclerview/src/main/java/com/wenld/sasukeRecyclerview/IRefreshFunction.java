package com.wenld.sasukeRecyclerview;

/**
 * <p/>
 * Author: 温利东 on 2017/6/16 13:10.
 * blog: http://blog.csdn.net/sinat_15877283
 * github: https://github.com/LidongWen
 */

public interface IRefreshFunction {

    /**
     * set Enble
     *
     * @param b
     */
    void setRefreshEnble(boolean b);

    boolean isRefreshing();

    void stopRefresh();

    void setRefreshCreator(RefreshViewCreator view);
}
