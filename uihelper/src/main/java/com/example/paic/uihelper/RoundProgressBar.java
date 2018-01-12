package com.example.paic.uihelper;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.paic.R;

/**
 *
 * @author ex-zhangyuelei001
 * @date 2018/1/3
 */

public class RoundProgressBar extends View {

    /**
     * 画笔对象的引用
     */
    private Paint paint;

    /**
     * 圆环的颜色
     */
    private int roundColor;

    /**
     * 圆环进度的颜色
     */
    private int roundProgressColor;

    /**
     * 中间进度百分比的字符串的颜色
     */
    private int textColor;

    /**
     * 中间进度百分比的字符串的字体
     */
    private float textSize;

    /**
     * 圆环的宽度
     */
    private float roundWidth;

    /**
     * 最大进度
     */
    private int max;

    /**
     * 当前进度
     */
    private float progress;
    /**
     * 是否显示中间的进度
     */
    private boolean textIsDisplayable;

    /**
     * 进度的风格，实心或者空心
     */
    private int style;

    public static final int STROKE = 0;
    public static final int FILL = 1;

    public RoundProgressBar(Context context) {
        super(context);
        init(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RoundProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        paint = new Paint();
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.RoundProgressBar);
        //获取自定义属性和默认值
        roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.TRANSPARENT);
        roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.WHITE);
        textColor = mTypedArray.getColor(R.styleable.RoundProgressBar_textColor, Color.WHITE);
        textSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_textSize, 15);
        roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 5);
        max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100000);
        textIsDisplayable = mTypedArray.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable, true);
        style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);
        mTypedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        long before = System.currentTimeMillis();
        Log.e("onDraw", "start draw : " + before);
        //获取圆心的x坐标
        int centre = getWidth() / 2;
        //圆环的半径
        int radius = (int) (centre - roundWidth / 2);
        //消除锯齿
        paint.setAntiAlias(true);

        if (roundColor == Color.TRANSPARENT) {
            //设置圆环的颜色
            paint.setColor(roundColor);
            //设置空心
            paint.setStyle(Paint.Style.STROKE);
            //设置圆环的宽度
            paint.setStrokeWidth(roundWidth);
            //画出圆环
            canvas.drawCircle(centre, centre, radius, paint);
        }


        Log.e("log", "centre" + centre);

        //画进度百分比
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        //设置字体
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        //中间的进度百分比，先转换成float在进行除法运算，不然都为0
        int percent = (int)(progress  * 100);
        //测量字体宽度，我们需要根据字体的宽度设置在圆环中间
        float textWidth = paint.measureText(percent + "%");

        //画出进度百分比
        if(textIsDisplayable && percent != 0 && style == STROKE){
            canvas.drawText(percent + "%", centre - textWidth / 2, centre + textSize/2, paint);
        }

        //画圆弧 ，画圆环的进度
        //设置进度是实心还是空心
        //设置圆环的宽度
        paint.setStrokeWidth(roundWidth);
        //设置进度的颜色
        paint.setColor(roundProgressColor);
        //用于定义的圆弧的形状和大小的界限
        RectF oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius);

        switch (style) {
            case STROKE:
                paint.setStyle(Paint.Style.STROKE);
                //根据进度画圆弧
                canvas.drawArc(oval, -90, 360 * progress, false, paint);
                break;

            case FILL:
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                //根据进度画圆弧
                if (progress != 0) {
                    canvas.drawArc(oval, -90, 360 * progress, true, paint);
                }
                break;
            default:
                break;

        }

        Log.e("onDraw", "end draw : " + System.currentTimeMillis());
        Log.e("onDraw", "end draw : " + (System.currentTimeMillis()-before));

    }

    public synchronized int getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     *
     * @param max
     */
    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    /**
     * 获取进度.需要同步
     *
     * @return
     */
    public synchronized float getProgress() {
        return progress;
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     *
     * @param progress
     */
    public synchronized void setProgress(float progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress = progress;
            postInvalidate();
        }

    }


    public int getCricleColor() {
        return roundColor;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return roundProgressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }


}
