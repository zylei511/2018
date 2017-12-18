package com.paic.crm.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.graphics.drawable.Drawable;

/**
 * Created by yueshaojun on 16/2/18.
 */
public class XEditText extends EditText {
    private DrawableRightListener drawableRightListener;
    final int DRAWABLE_RIGHT = 2;

    public XEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public XEditText(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
    }

    public XEditText(Context context) {
        super(context);
    }

    public void setClickDrawableRightListener(DrawableRightListener drawableRightListener) {
        this.drawableRightListener = drawableRightListener;
    }

    public interface DrawableRightListener {
        void onDrawableRightClick(View view);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (drawableRightListener != null) {
                    Drawable drawableRight = getCompoundDrawables()[DRAWABLE_RIGHT];
                    if (drawableRight != null && event.getRawX() >= (getWidth() - drawableRight.getBounds().width())) {
                        drawableRightListener.onDrawableRightClick(this);
                        return false;
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

}
