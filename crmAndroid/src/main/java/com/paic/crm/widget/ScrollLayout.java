package com.paic.crm.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.paic.crm.utils.CRMLog;

/**
 * Created by yueshaojun988 on 2017/9/12.
 */

public class ScrollLayout extends LinearLayout implements
        ViewTreeObserver.OnGlobalLayoutListener{
    private static final String TAG = "ScrollLayout";

    private int originHeight = 0;
    private int currentKeyboardHeight = 0;
    private int keyboardHeight = 0;
    private boolean isLayoutOver = false;
    private int lastViewBottom = 0;

    private Scroller mScroller;
    public ScrollLayout(Context context) {
        super(context);
        init(context);
    }

    public ScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        getViewTreeObserver().addOnGlobalLayoutListener(this);
        mScroller = new Scroller(context);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        isLayoutOver = false;
        if(changed){
            if(originHeight==0){
                originHeight = b - t;
                lastViewBottom = getChildAt(getChildCount()-1).getBottom();
                CRMLog.LogDebg(TAG,"onLayout:originHeight-->>"+ originHeight +"-->lastViewBottom"+lastViewBottom+":"+changed );
            }else {
                //TODO 这时候重布局已经完成
                currentKeyboardHeight = originHeight - (b-t);
                if(currentKeyboardHeight !=0){
                    keyboardHeight = currentKeyboardHeight;
                }
                CRMLog.LogDebg(TAG,"onLayout over:"+ currentKeyboardHeight +":+ "+changed);
                isLayoutOver = true ;
            }
        }
    }

    @Override
    public void onGlobalLayout() {

        if(isLayoutOver) {
            CRMLog.LogDebg(TAG,"onGlobalLayout"+ currentKeyboardHeight);
            int leftHeightAfterKeyboard = originHeight - currentKeyboardHeight;
            int dert = keyboardHeight +lastViewBottom-originHeight;
            //当前键盘高度为0，则表示关闭
            if(currentKeyboardHeight ==0) {
                scrollTo(0,-dert);
            }else if(lastViewBottom>leftHeightAfterKeyboard){
                scrollTo(0,dert);
            }
        }

    }

    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            invalidate();
        }
        super.computeScroll();
    }

    /**
     * 获取键盘高度
     * @return
     */
    public int getKeyboardHeight() {
        return keyboardHeight;
    }
}
