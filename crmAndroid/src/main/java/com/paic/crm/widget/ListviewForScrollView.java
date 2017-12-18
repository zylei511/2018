package com.paic.crm.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by ex-zhangyuelei001 on 2017/11/14.
 */

public class ListviewForScrollView extends ListView {
    public ListviewForScrollView(Context context) {
        super(context);
    }

    public ListviewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListviewForScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
