package com.wenld.pullrecyclerview.refreshLoad.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wenld.multitypeadapter.base.MultiItemView;
import com.wenld.multitypeadapter.base.ViewHolder;
import com.wenld.pullrecyclerview.R;
import com.wenld.pullrecyclerview.refreshLoad.bean.Bean02;

/**
 * <p/>
 * Author: 温利东 on 2017/6/14 11:52.
 * http://www.jianshu.com/u/99f514ea81b3
 * github: https://github.com/LidongWen
 */

public class ItemVIew02 extends MultiItemView<Bean02, ViewHolder> {
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_two, parent, false);
        return new ViewHolder(inflater.getContext(), view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Bean02 item, int position) {
        holder.setText(R.id.tv_item02, item.title);
        ImageView iv = holder.getView(R.id.iv_item02);


        Glide.with(iv.getContext())
                .load(item.imgUrl)
//                .placeholder(R.mipmap.ic_launcher) // can also be a drawable
//                .error(R.mipmap.cheese) // will be displayed if the image cannot be loaded
                .centerCrop()
                .into(iv);

    }

}
