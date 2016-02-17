package com.ark.component;

/**
 * Created by edwin on 14-9-10.
 */

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.RelativeLayout;
import com.ark.newark_vision1.R;

public class SlideBar extends RelativeLayout {

    private static final String TAG = "SlideBar";
    private static final boolean DEBUG = true;

    GradientView mGradientView;
    private int gradientViewStartX;
    private float mEventDownX;
    private float mGradientViewIndicateLeft;
    private OnTriggerListener mOnTriggerListener;
    private VelocityTracker mVelocityTracker = null;
    private int mMinVelocityXToUnlock;
    private int mMinDistanceToUnlock;
    private int mLeftAnimationDuration;
    private int mRightAnimationDuration;
    private ObjectAnimator animLeftMoveAnimator;
    private ObjectAnimator animRightMoveAnimator;
    private static final int MaxDistance = 400;
    private View gradientView;
    private int Slideiconwith;

    public interface OnTriggerListener {
        public void onTrigger();
    }

    public SlideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        gradientViewStartX = context.getResources().
                getDimensionPixelSize(R.dimen.gradient_view_margin_left);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlideBar);
        mMinVelocityXToUnlock = a.getInt(R.styleable.SlideBar_MinVelocityXToUnlock, 100);
        mMinDistanceToUnlock = a.getInt(R.styleable.SlideBar_MinDistanceToUnlock, 100);
        mLeftAnimationDuration = a.getInt(R.styleable.SlideBar_LeftAnimationDuratioin, 100);
        mRightAnimationDuration = a.getInt(R.styleable.SlideBar_RightAnimationDuratioin, 100);
        a.recycle();

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getActionMasked();
        boolean handled = false;

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }

        mVelocityTracker.addMovement(event);

        switch (action) {
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_DOWN:
                if (DEBUG) Log.v(TAG, "*** DOWN ***");
                handleDown(event);
                handled = true;
                break;

            case MotionEvent.ACTION_MOVE:
                if (DEBUG) Log.v(TAG, "*** MOVE ***");
                handleMove(event);
                handled = true;
                break;

            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                if (DEBUG) Log.v(TAG, "*** UP ***");
//                handleUp(event);
                resetControls();
                handled = true;
                break;

            case MotionEvent.ACTION_CANCEL:
                if (DEBUG) Log.v(TAG, "*** CANCEL ***");
                handled = true;
                break;

        }
        invalidate();
        return handled ? true : super.onTouchEvent(event);
    }


    private void handleUp(MotionEvent event) {

        Log.v(TAG, "handleUp,mIndicateLeft:" + mGradientViewIndicateLeft);
        //1. if user slide some distance, unlock
        if (mGradientViewIndicateLeft >= mMinDistanceToUnlock) {
            unlockSuccess();
            return;
        }
        //2. if user slide very fast, unlock
        if (velocityTrigUnlock()) {
            return;
        }
        //otherwise reset the controls
        resetControls();
    }

    /**
     * another way to unlock, if user slide very fast
     */
    private boolean velocityTrigUnlock() {
        final VelocityTracker velocityTracker = mVelocityTracker;
        velocityTracker.computeCurrentVelocity(1000);

        int velocityX = (int) velocityTracker.getXVelocity();

        Log.v(TAG, "velocityX:" + velocityX);

        if (velocityX > mMinVelocityXToUnlock) {
            unlockSuccess();
            return true;
        }

        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
        return false;
    }


    private void unlockSuccess() {
        mOnTriggerListener.onTrigger();
        animRightMoveAnimator = ObjectAnimator.ofFloat(mGradientView, "x", mGradientView.getX(), MaxDistance)
                .setDuration(mRightAnimationDuration);
        animRightMoveAnimator.start();
    }


    private void handleMove(MotionEvent event) {

        mGradientViewIndicateLeft = event.getX() - mEventDownX + gradientViewStartX;
        Log.d("", "-----------------d-----------------" + mGradientViewIndicateLeft);
        if (mGradientViewIndicateLeft <= gradientViewStartX) {
            mGradientViewIndicateLeft = gradientViewStartX;
        }
        if (mGradientViewIndicateLeft <= 180) {
            mGradientView.setX(mGradientViewIndicateLeft);
            if (this.getWidth() - mMinVelocityXToUnlock <= mGradientViewIndicateLeft) {
                if (mGradientViewIndicateLeft >= mMinDistanceToUnlock) {
                    unlockSuccess();
                    return;
                }
                //2. if user slide very fast, unlock
                if (velocityTrigUnlock()) {
                    return;
                }
                //otherwise reset the controls
                resetControls();
            }
        }

    }


    private void handleDown(MotionEvent event) {
        mEventDownX = event.getX();
        if (mGradientView == null) {
            mGradientView = (GradientView) findViewById(R.id.gradientView);
        }
        mGradientView.stopAnimatorAndChangeColor();

    }

    public void setOnTriggerListener(OnTriggerListener listener) {
        mOnTriggerListener = listener;
    }

    private void resetControls() {
        mGradientView.startAnimator();
        animLeftMoveAnimator = ObjectAnimator.ofFloat(mGradientView, "x",
                mGradientView.getX(), gradientViewStartX).setDuration(mLeftAnimationDuration);
        animLeftMoveAnimator.start();
    }


}
