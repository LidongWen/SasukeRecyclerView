package com.wenld.sasukeRecyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.wenld.orecyclerview.R;


/**
 * <p/>
 * Author: 温利东 on 2017/6/16 10:33.
 * http://www.jianshu.com/u/99f514ea81b3
 * github: https://github.com/LidongWen
 */

public class DefaultRefreshCreator extends RefreshViewCreator {
    // 加载数据的ImageView
    private View mRefreshIv;
    private TextView tv;

    @Override
    public View getRefreshView(Context context, ViewGroup parent) {
        View refreshView = LayoutInflater.from(context).inflate(R.layout.layout_refresh_header_view, parent, false);
        mRefreshIv = refreshView.findViewById(R.id.refresh_iv);
        tv = (TextView) refreshView.findViewById(R.id.refresh_tv);
        return refreshView;
    }

    @Override
    public void onPull(int currentDragHeight, int refreshViewHeight, int currentRefreshStatus) {

        // 默认状态
        if (currentRefreshStatus == REFRESH_STATUS_PULL_DOWN_REFRESH) {
            tv.setText("向下拉进化血轮眼");
        }
        if (currentRefreshStatus == REFRESH_STATUS_LOOSEN_REFRESHING) {
            tv.setText("松开手指进行进化");
        } else {
            tv.setText("向下拉进化血轮眼");
        }

        float rotate = ((float) currentDragHeight) / refreshViewHeight;
        mRefreshIv.setRotation(rotate * 360);
    }

    @Override
    public void onRefreshing() {
        // 刷新的时候不断旋转
        RotateAnimation animation = new RotateAnimation(0, 720,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setRepeatCount(-1);
        animation.setDuration(1000);
        tv.setText("血轮眼进化中...");
        mRefreshIv.startAnimation(animation);
    }

    @Override
    public void onStopRefresh() {
        // 停止加载的时候清除动画
        mRefreshIv.setRotation(0);
        tv.setText("血轮眼进化完成");
        mRefreshIv.clearAnimation();
    }
}
