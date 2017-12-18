package com.paic.crm.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import com.paic.crm.android.R;
import com.paic.crm.utils.ActivityCollector;
import com.paic.crm.utils.CRMLog;

/**
 * Created by hanyh on 16/1/21.
 */
public class BaseActivity extends FragmentActivity {

    public int screenWidth;
    public int screenHeigh;
    private GestureDetector gestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        screenWidth = dm.widthPixels;

        screenHeigh = dm.heightPixels;
        gestureDetector = new GestureDetector(this,new MyGestureDetector());
        ActivityCollector.addActivity(this);
    }

    public void toActivity(Activity from, Class<?> toCls, Bundle bundle) {

        Intent intent = new Intent(from, toCls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        CRMLog.LogInfo("taotao", "dispatchTouchEvent");
        if(!"HomeActivity".equals(this.getClass().getSimpleName())){
            boolean b = gestureDetector.onTouchEvent(ev);
            if(b){
                finish();
                ActivityCollector.removeActivity(this);
                overridePendingTransition(0,R.anim.slide_out_left);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    protected void onResume() {
        CRMLog.LogInfo("base",this.getClass().getName());
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    public class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onContextClick(MotionEvent e) {
            return super.onContextClick(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return super.onDoubleTapEvent(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return super.onDown(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            long startTime = e2.getDownTime();
            //判断是否要finish掉当前activity
            if(e2.getX()-e1.getX()>screenWidth/2&&startTime>300){
                return true;
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public void onShowPress(MotionEvent e) {
            super.onShowPress(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }

    }


}
