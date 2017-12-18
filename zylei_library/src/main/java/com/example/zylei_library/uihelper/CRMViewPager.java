package com.example.zylei_library.uihelper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.example.zylei_library.R;

/**
 * Created by ex-zhangyuelei001 on 2017/12/14.
 */

public class CRMViewPager extends ViewPager {
    Paint paint = new Paint();

    public CRMViewPager(Context context) {
        super(context);
    }

    public CRMViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //在原来的高度上添加10dp
//        widthMeasureSpec += getResources().getDimension(R.dimen.margin_bottom_10dp);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
