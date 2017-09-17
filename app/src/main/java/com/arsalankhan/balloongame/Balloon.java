package com.arsalankhan.balloongame;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.arsalankhan.balloongame.util.PixelHelper;


/**
 * Created by Arsalan khan on 9/16/2017.
 */

public class Balloon extends AppCompatImageView implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {

    private ValueAnimator valueAnimator;
    private boolean mPopped;
    private BalloonListener mListener;

    public Balloon(Context context) {
        super(context);
    }

    public Balloon(Context context,int color,int rawHeight) {
        super(context);

        mListener = (BalloonListener) context;

        this.setImageResource(R.drawable.balloon);
        this.setColorFilter(color);

        int rawWidth = rawHeight/2;

        int dpWidth = PixelHelper.pixelToDp(rawWidth,context);
        int dpHeight = PixelHelper.pixelToDp(rawHeight,context);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(dpWidth,dpHeight);
        setLayoutParams(params);
    }

    //Balloon Animation
    public void releasBalloon(int sreenHeight, int duration){

        valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(duration);
        valueAnimator.setFloatValues(sreenHeight,0f);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setTarget(this);
        valueAnimator.addListener(this);
        valueAnimator.addUpdateListener(this);
        valueAnimator.start();
    }

    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animator) {

        if(!mPopped){

            mListener.poppedBalloon(this,false);
        }
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {

        setY((float)valueAnimator.getAnimatedValue());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(!mPopped && event.getAction() == MotionEvent.ACTION_DOWN){

            mListener.poppedBalloon(this,true);
            mPopped = true;
            valueAnimator.cancel();
        }
        return super.onTouchEvent(event);
    }

    //This is called from MainActivity when the gameOver and remove the Balloons from screen so also cancel it animation
    public void setPopped(boolean popped) {

        mPopped = popped;

        if(mPopped){

            valueAnimator.cancel();
        }
    }

    public interface BalloonListener {
      void poppedBalloon(Balloon balloon, boolean userTouch);
    }
}
