package com.paic.crm.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by yueshaojun on 16/9/12.
 */
public class SplashAdapter extends PagerAdapter {
    List<ImageView> imageViews;
    @Override
    public int getCount() {
        return imageViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imageViews.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(imageViews.get(position));
        return imageViews.get(position);
    }

    public SplashAdapter(List<ImageView> imageViews) {
        this.imageViews = imageViews;
    }

    public Object getItemView(int position){
        return imageViews.get(position);
    }

}
