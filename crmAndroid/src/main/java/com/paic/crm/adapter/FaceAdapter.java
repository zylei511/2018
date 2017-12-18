package com.paic.crm.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by pingan001 on 16/2/2.
 */
public class FaceAdapter extends PagerAdapter {

    private List<View> viewContainter;

    public FaceAdapter(List<View> viewContainter) {
        this.viewContainter=viewContainter;
    }

    @Override
    public int getCount() {
        return viewContainter.size();
    }
    //滑动切换的时候销毁当前的组件
    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        ((ViewPager) container).removeView(viewContainter.get(position));
    }
    //每次滑动的时候生成的组件
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(viewContainter.get(position));
        return viewContainter.get(position);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        return titleContainer.get(position);
//    }
}
