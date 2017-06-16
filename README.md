# sasuke-Recyclerview
轻量级 下拉刷新-上拉加载 RecyclerView

· [总览](#总览)
· [特性](#特性)
· [基础](#基础用法)
　　· [简单使用](#简单使用)
　　· [一些配置](#配置)
· [高级用法](#高级用法)
　　· [自定义刷新头部样式](#自定义刷新头部样式)
　　· [配合MultiType-Adapter](#MultiType-Adapter)
· [Thrank](#Thrank)
· [一些说明](#一些说明)
# 总览
![loading.gif](https://github.com/LidongWen/SasukeRecyclerView/blob/master/doc/refresh-pull.gif)


# 特性
- 轻盈、整个类库只有6个文件
- 扩展、支持自定义下拉刷新样式、京东、淘宝样式任你玩
- 还可配合[MultiType-Adapter](https://github.com/LidongWen/MultiTypeAdapter)使用
- 周到、支持网格布局、瀑布流布局。

# 基础
当然是引用啦
```
repositories {
    jcenter()
    maven { url "https://www.jitpack.io" }
}
// yout project build.gradle
dependencies {
        compile 'com.github.LidongWen:SasukeRecyclerView:0.0.1'
}
```
# 简单使用
1、xml内使用 `RefreshLoadRecyclerView`
2、java内 初始化  RefreshLoadRecyclerView
3、设置各种
```
        recyclerView.setAdapter(adapter);
        //注意 这边一定要先设置 适配器;
        recyclerView.setRefreshCreator(new DefaultRefreshCreator());    //设置刷新头部信息
        recyclerView.setOnRefreshListener(new RefreshLoadRecyclerView.OnRefreshListener()...);     //设置监听
        recyclerView.setLoadMoreLayoutId(R.layout.default_loading);     //设置上拉加载 view
        recyclerView.setOnLoadMoreListener(new LoadMoreWrapper2.OnLoadMoreListener);  //设置加载监听
```
# 配置
```
// 一些方法
    void setLoadMoreEnble(boolean b);
    boolean isLoading();
    void stopLoad();
    void setLoadMoreLayoutId(@LayoutRes int layoutId);
    void setLoadMoreView(View view);
    void setOnLoadMoreListener(LoadMoreWrapper2.OnLoadMoreListener loadMoreListener);
    void setRefreshEnble(boolean b);
    boolean isRefreshing();
    void stopRefresh();
    void setRefreshCreator(RefreshViewCreator view);

```

# 高级用法
# 自定义刷新头部样式
一般公司都会要求我们根据UI做的设计图来实现功能，这一点 wenld 早就帮你想到了，我们可以自定义下拉刷新的样式，以及上拉加载的样式；

```
// 下拉刷新 只要继承我们的   RefreshViewCreator  就好了  ，这是默认样式

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

```
# MultiType-Adapter

[MultiType-Adapter](http://www.jianshu.com/p/032a6773620b)：这是一个 让你从复杂布局中解脱出来的库。


# Thrank
- [红橙](http://www.jianshu.com/u/35083fcb7747): 向他学习

# 一些说明

> ##  V 0.0.1
> 下拉刷新，上拉加载功能
> 可自定义刷新头部
> 支持网格、瀑布流布局
> 配合[MultiType-Adapter](http://www.jianshu.com/p/032a6773620b)使用让你爽翻天


> **带完善**
> 手势需要完善

代码传送门：[戳我!!!](https://github.com/LidongWen/SasukeRecyclerView)

-----

希望我的文章不会误导在观看的你，如果有异议的地方欢迎讨论和指正。
如果能给观看的你带来收获，那就是最好不过了。

##### 人生得意须尽欢, 桃花坞里桃花庵