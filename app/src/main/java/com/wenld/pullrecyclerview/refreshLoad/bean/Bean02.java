package com.wenld.pullrecyclerview.refreshLoad.bean;

import android.support.annotation.DrawableRes;

import java.io.Serializable;

/**
 * <p/>
 * Author: 温利东 on 2017/6/14 11:47.
 * http://www.jianshu.com/u/99f514ea81b3
 * github: https://github.com/LidongWen
 */

public class Bean02 implements Serializable {
    public String title;
    public @DrawableRes int imgRes;
    public String imgUrl = "http://upload-images.jianshu.io/upload_images/1599843-876468433f5dfe91.jpg";

    public Bean02(String title) {
        this.title=title;
    }
}
