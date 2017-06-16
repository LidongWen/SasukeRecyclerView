package com.wenld.sasukeRecyclerview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.wenld.multitypeadapter.wrapper.LoadMoreWrapper2;

/**
 * <p/>
 * Author: 温利东 on 2017/6/16 13:09.
 * blog: http://blog.csdn.net/sinat_15877283
 * github: https://github.com/LidongWen
 */

public class RefreshLoadRecyclerView extends RecyclerView implements IRefreshFunction, ILodMoreFunction {
    // 包裹了一层的头部底部Adapter
    private RefreshAdapter mWrapRecyclerAdapter;

    // 下拉刷新的辅助类
    private RefreshViewCreator mRefreshCreator;
    // 下拉刷新头部的高度
    private int mRefreshViewHeight = 0;
    // 下拉刷新的头部View
    private View mRefreshView;
    // 位移距离
    int disY = 0;
    // 手指拖拽的阻力指数
    protected float mDragIndex = 0.35f;
    // 当前是否正在拖动
    private boolean mCurrentDrag = false;
    // 当前的状态
    private int mCurrentRefreshStatus;
    // 默认状态
    private int REFRESH_STATUS_NORMAL = 0x0011;
    // 下拉刷新状态
    private int REFRESH_STATUS_PULL_DOWN_REFRESH = 0x0022;
    // 松开刷新状态
    private int REFRESH_STATUS_LOOSEN_REFRESHING = 0x0033;
    // 正在刷新状态
    private int REFRESH_STATUS_REFRESHING = 0x0044;


    private OnRefreshListener mListener;

    GestureDetector mGestureDetector;

    public RefreshLoadRecyclerView(Context context) {
        super(context);
    }

    public RefreshLoadRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshLoadRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mGestureDetector = new GestureDetector(getContext(), gestureListener);

        mWrapRecyclerAdapter = new RefreshAdapter(adapter);
        super.setAdapter(mWrapRecyclerAdapter);

        addRefreshView();
    }

    private void addRefreshView() {
        if (mWrapRecyclerAdapter != null && mRefreshCreator != null) {
            // 添加头部的刷新View
            View refreshView = mRefreshCreator.getRefreshView(getContext(), this);
            if (refreshView != null) {
                addHeaderView(refreshView);
                this.mRefreshView = refreshView;
            }
        }
    }

    private void addHeaderView(View view) {
        if (mWrapRecyclerAdapter == null)
            throw new NullPointerException("RefreshLoadRecyclerView adapter is null ");
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.addHeaderView(view);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                disY = 0;
                if (mCurrentDrag) {
                    restoreRefreshView();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 重置当前刷新状态状态
     */
    private void restoreRefreshView() {
        int currentTopMargin = ((ViewGroup.MarginLayoutParams) mRefreshView.getLayoutParams()).topMargin;
        int finalTopMargin = -mRefreshViewHeight + 1;
        if (mCurrentRefreshStatus == REFRESH_STATUS_LOOSEN_REFRESHING) {
            finalTopMargin = 0;
            mCurrentRefreshStatus = REFRESH_STATUS_REFRESHING;
            if (mRefreshCreator != null) {
                mRefreshCreator.onRefreshing();
                setLoadMoreEnble(false);
            }
            if (mListener != null) {
                mListener.onRefresh();
            }
        }

        int distance = currentTopMargin - finalTopMargin;

        // 回弹到指定位置
        ValueAnimator animator = ObjectAnimator.ofFloat(currentTopMargin, finalTopMargin).setDuration(distance);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentTopMargin = (float) animation.getAnimatedValue();
                setRefreshViewMarginTop((int) currentTopMargin);
            }
        });
        animator.start();
        mCurrentDrag = false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                // 如果是在最顶部才处理，否则不需要处理
                if (canScrollUp() || mCurrentRefreshStatus == REFRESH_STATUS_REFRESHING
                        || mRefreshView == null || mRefreshCreator == null || isRefreshing() || isLoading()) {
                    // 如果没有到达最顶端，也就是说还可以向上滚动就什么都不处理
                    return super.onTouchEvent(e);
                }

                // 解决下拉刷新自动滚动问题
                if (mCurrentDrag) {
                    scrollToPosition(0);
                }
                mGestureDetector.onTouchEvent(e);
                // 获取手指触摸拖拽的距离
                break;
        }

        return super.onTouchEvent(e);
    }

    /**
     * 更新刷新的状态
     */
    private void updateRefreshStatus(int distanceY) {
        if (distanceY <= 0) {
            mCurrentRefreshStatus = REFRESH_STATUS_NORMAL;
        } else if (distanceY < mRefreshViewHeight) {
            mCurrentRefreshStatus = REFRESH_STATUS_PULL_DOWN_REFRESH;
        } else {
            mCurrentRefreshStatus = REFRESH_STATUS_LOOSEN_REFRESHING;
        }

        if (mRefreshCreator != null) {
            mRefreshCreator.onPull(distanceY, mRefreshViewHeight, mCurrentRefreshStatus);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //if (changed) {
        if (mRefreshView != null && mRefreshViewHeight <= 0) {
            // 获取头部刷新View的高度
            mRefreshViewHeight = mRefreshView.getMeasuredHeight();
            if (mRefreshViewHeight > 0) {
                // 隐藏头部刷新的View  marginTop  多留出1px防止无法判断是不是滚动到头部问题
                setRefreshViewMarginTop(-mRefreshViewHeight + 1);
            }
        }
        // }
    }

    /**
     * 设置刷新View的marginTop
     */
    private void setRefreshViewMarginTop(int marginTop) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mRefreshView.getLayoutParams();
        if (marginTop < -mRefreshViewHeight + 1) {
            marginTop = -mRefreshViewHeight + 1;
        }
        params.topMargin = marginTop;
        mRefreshView.setLayoutParams(params);
    }


    @Override
    public void setRefreshEnble(boolean b) {

    }

    @Override
    public boolean isRefreshing() {
        return mCurrentRefreshStatus == REFRESH_STATUS_REFRESHING;
    }

    @Override
    public void stopRefresh() {
        setLoadMoreEnble(true);
        mCurrentRefreshStatus = REFRESH_STATUS_NORMAL;
        restoreRefreshView();
        if (mRefreshCreator != null) {
            mRefreshCreator.onStopRefresh();
        }
    }

    @Override
    public void setRefreshCreator(RefreshViewCreator view) {
        this.mRefreshCreator = view;
        addRefreshView();
    }


    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mListener = listener;
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

    public void setOnLoadMoreListener(LoadMoreWrapper2.OnLoadMoreListener loadMoreListener) {
        mWrapRecyclerAdapter.setOnLoadMoreListener(loadMoreListener);
    }

    @Override
    public void setLoadMoreEnble(boolean b) {
        mWrapRecyclerAdapter.setLoadMoreEnble(b);
    }

    @Override
    public boolean isLoading() {
        return mWrapRecyclerAdapter.isLoading();
    }

    @Override
    public void stopLoad() {
        mWrapRecyclerAdapter.stopLoad();
    }

    @Override
    public void setLoadMoreLayoutId(int layoutId) {
        mWrapRecyclerAdapter.setLoadMoreLayoutId(layoutId);
    }

    @Override
    public void setLoadMoreView(View view) {
        mWrapRecyclerAdapter.setLoadMoreView(view);
    }

    public void notifyDataSetChanged() {
        getAdapter().notifyDataSetChanged();
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is disY custom view.
     * 判断是不是滚动到了最顶部，这个是从SwipeRefreshLayout里面copy过来的源代码
     */
    private boolean canScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            return ViewCompat.canScrollVertically(this, -1) || this.getScrollY() > 0;
        } else {
            return ViewCompat.canScrollVertically(this, -1);
        }
    }

    public GestureDetector.OnGestureListener gestureListener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (e1 != null) Log.e("onScroll", "MotionEvent " + e1.getX() + "  " + e1.getY());
            Log.e("onScroll", "e2 " + e2.getX() + "  " + e2.getY() + " distanceX " + distanceX + "  distanceY " + distanceY);
            Log.e("onScroll aa", "disY "+ disY);
            if(disY ==0){
             disY = disY -1;
            }else {
                disY += distanceY;
            }

            int distaaanceY = (int) (-1 * disY * mDragIndex);
//             如果是已经到达头部，并且不断的向下拉，那么不断的改变refreshView的marginTop的值
            if (distaaanceY > 0) {
                int marginTop = distaaanceY - mRefreshViewHeight;
                setRefreshViewMarginTop(marginTop);
                updateRefreshStatus(distaaanceY);
                mCurrentDrag = true;
//                return false;
            }

            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    };

}
