package com.zy.lucis.widget;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.zy.lucis.utils.SizeUtils;

/**
 * Created by zhangyang on 2017/12/29.
 * 绘制波浪线的视图
 */
public class WaveView extends View {

    private final int defaultWidth = SizeUtils.dp2px(100), defaultHeight = SizeUtils.dp2px(100);

    private int realWidth, realHeight;

    //波浪线控制点离中轴高度
    private int controlHeight = SizeUtils.dp2px(50);

    private int waveWidth = controlHeight * 4;

    private Paint wavePaint;

    private Path wavePath,circlePath;

    private ObjectAnimator animator;

    private int pathBeginX = -waveWidth;

    public WaveView(Context context) {
        this(context, null, 0);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        wavePath = new Path();

        wavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        wavePaint.setStyle(Paint.Style.FILL);

        animator = ObjectAnimator.ofInt(this,"pathBeginX",-waveWidth,0);
        animator.setDuration(3000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());

    }

    public int getPathBeginX() {
        return pathBeginX;
    }

    public void setPathBeginX(int pathBeginX) {
        this.pathBeginX = pathBeginX;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //对wrap_content的处理
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultWidth, defaultHeight);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultWidth, heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, defaultHeight);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        realWidth = getMeasuredWidth();
        realHeight = getMeasuredHeight();
        circlePath = new Path();
        circlePath.addCircle(realWidth/2,realHeight/2,Math.min(realHeight/2,realWidth/2), Path.Direction.CCW);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        animator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        animator.end();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        wavePath.reset();
        int i = realWidth / waveWidth + 1;
        wavePath.moveTo(pathBeginX,realHeight/2);
        for (int j=0;j<i;j++){
            wavePath.rQuadTo(waveWidth/4,-controlHeight,waveWidth/2,0);
            wavePath.rQuadTo(waveWidth/4,controlHeight,waveWidth/2,0);
        }
        wavePath.lineTo(realWidth,realHeight);
        wavePath.lineTo(0,realHeight);
        wavePath.close();

        canvas.save();
        canvas.clipPath(circlePath);
        canvas.drawPath(wavePath,wavePaint);
        canvas.restore();
    }
}
