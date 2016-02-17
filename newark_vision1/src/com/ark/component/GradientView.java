package com.ark.component;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.widget.TextView;
import com.ark.newark_vision1.R;

public class GradientView extends TextView {


    private static final String TAG = "GradientView";
    private static final boolean DEBUG = false;
    private float mIndex = 0;
    private Shader mShader;
    private int mTextSize;
    private static final int mUpdateStep = 15;
    private static final int mMaxWidth = 40 * mUpdateStep; // 26*25
    //    private static final int mMaxWidth = 220; // 26*25
    private static final int mMinWidth = 6 * mUpdateStep;  // 5*25
    int mDefaultColor;
    int mSlideColor;
    private ValueAnimator animator;
    private int mWidth, mHeight;
    private String mStringToShow;
    private Paint mTextPaint;
    private float mTextHeight;
    private Drawable mSlideIcon;
    private int mSlideIconHeight;
    private static final int mSlideIconOffSetTop = 2;
    private float SidleIcon_with;
    private float SidleIcon_height;
    private float textMargin_left;
    private Typeface face;


    private AnimatorUpdateListener mAnimatorUpdateListener = new AnimatorUpdateListener() {

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            mIndex = Float.parseFloat(animation.getAnimatedValue().toString());
            // RadialGradient SweepGradient
            mShader = new LinearGradient(mIndex - 20 * mUpdateStep, 100,
                    mIndex, 100, new int[]{mDefaultColor, mDefaultColor, mDefaultColor, mSlideColor,
                    mSlideColor, mDefaultColor, mDefaultColor, mDefaultColor}, null,
                    Shader.TileMode.CLAMP);

            postInvalidate();

        }
    };

    public GradientView(Context context) {
        super(context);
    }


    public GradientView(Context context, AttributeSet attrs) {
        super(context, attrs);
        face = Typeface.createFromAsset(this.getResources().getAssets(), "Transformers.ttf");
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GradientView);
        mStringToShow = a.getString(R.styleable.GradientView_StringToShow);
        mTextSize = (int) a.getDimension(R.styleable.GradientView_TextSize, 40);
        mDefaultColor = a.getColor(R.styleable.GradientView_TextColor, Color.GRAY);
        mSlideColor = a.getColor(R.styleable.GradientView_SlideColor, Color.WHITE);

        SidleIcon_with = a.getFloat(R.styleable.GradientView_Slideiconwith, 100);
        SidleIcon_height = a.getFloat(R.styleable.GradientView_Slideiconheight, 100);
        textMargin_left = a.getFloat(R.styleable.GradientView_TextMarginleft, 300);

        mSlideIcon = a.getDrawable(R.styleable.GradientView_Slideicon);


        mSlideIconHeight = mSlideIcon.getMinimumHeight();
        a.recycle();

        animator = ValueAnimator.ofFloat(mMinWidth, mMaxWidth);
        animator.setDuration(1800);
        animator.addUpdateListener(mAnimatorUpdateListener);
        animator.setRepeatCount(Animation.INFINITE);//repeat animation
        animator.start();


        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mSlideColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mTextHeight = mTextPaint.ascent();

        setFocusable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (DEBUG)
            Log.w(TAG, "b onDraw()");
        mTextPaint.setShader(mShader);
        Rect src = new Rect();
        src.bottom = (int) SidleIcon_height * 2;
        src.left = 0;
        src.top = 0;
        src.right = (int) SidleIcon_with * 2;
        RectF rtf = new RectF();

        rtf.bottom = (int) SidleIcon_height;
        rtf.left = 0;
        rtf.top = 0;
        rtf.right = (int) SidleIcon_with;


//        canvas.drawBitmap(((BitmapDrawable) mSlideIcon).getBitmap(), 20, mHeight
//                / 2 - mSlideIconHeight / 2 + mSlideIconOffSetTop, null);

        Bitmap bt = ((BitmapDrawable) mSlideIcon).getBitmap();


        canvas.drawBitmap(bt, src, rtf, null);


        //canvas.drawBitmap(((BitmapDrawable) mSlideIcon).getBitmap(), src, rtf, null);
        canvas.drawText(mStringToShow, textMargin_left, mHeight / 2 - mTextHeight
                / 2 - mSlideIconOffSetTop, mTextPaint); // slide_unlock
    }


    public void stopAnimatorAndChangeColor() {
        //if(DEBUG)
        Log.w(TAG, "stopGradient");
        animator.cancel();
        //reset
        mShader = new LinearGradient(0, 100, mIndex, 100,
                new int[]{mSlideColor, mSlideColor},
                null, Shader.TileMode.CLAMP);
        invalidate();
    }

    public void startAnimator() {
        if (DEBUG)
            Log.w(TAG, "startGradient");
        animator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    public void resetControl() {
        animator.start();
        this.setX(0);
        invalidate();
    }
}

