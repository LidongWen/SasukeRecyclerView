package com.wenld.sasukeRecyclerview;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.wenld.multitypeadapter.wrapper.LoadMoreWrapper2;


/**
 * <p/>
 * Author: 温利东 on 2017/6/16 13:10.
 * blog: http://blog.csdn.net/sinat_15877283
 * github: https://github.com/LidongWen
 */

public interface ILodMoreFunction {

    /**
     * set Enble
     *
     * @param b
     */
    void setLoadMoreEnble(boolean b);

    boolean isLoading();

    void stopLoad();

    void setLoadMoreLayoutId(@LayoutRes int layoutId);

    void setLoadMoreView(View view);
    void setOnLoadMoreListener(LoadMoreWrapper2.OnLoadMoreListener loadMoreListener);
}
