package com.arsalankhan.balloongame;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arsalankhan.balloongame.util.Constant;

import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements Balloon.BalloonListener {

    ViewGroup mContentView;
    private int mBalloonColor[] = new int[4];
    private int mNextColor,mScreenWidth,mScreenHeight;
    private int mLevel;
    private int mScore;
    private TextView mScoreDisplay,mLevelDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawableResource(R.drawable.modern_background);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_main);

        //setting the Color
        mBalloonColor[0] = Color.argb(255,255,0,0);
        mBalloonColor[1] = Color.argb(255,0,255,0);
        mBalloonColor[2] = Color.argb(255,0,0,255);
        mBalloonColor[3] = Color.argb(255, 190, 80, 20);

        mContentView= (ViewGroup) findViewById(R.id.activity_main);
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setFullScreen();
            }
        });

        setFullScreen();
        //getting the Screen Width and Height
        ViewTreeObserver viewTreeObserver = mContentView.getViewTreeObserver();
        if(viewTreeObserver.isAlive()){

            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                        mContentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        mScreenWidth= mContentView.getWidth();
                        mScreenHeight = mContentView.getHeight();
                    }
                }
            });

        }

        mScoreDisplay = (TextView) findViewById(R.id.display_score);
        mLevelDisplay = (TextView) findViewById(R.id.display_level);
        updateDisplay();
    }

    //start button click listener
    public void StartGameButton(View view){

        startLevel();
    }
    //hiding the system UI
    private void setFullScreen(){

        RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.activity_main);

        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        rootLayout.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setFullScreen();
    }


    private void startLevel(){

        mLevel++;

        updateDisplay();

        BalloonLauncher balloonLauncher = new BalloonLauncher();
        balloonLauncher.execute(mLevel);
    }

    @Override
    public void poppedBalloon(Balloon balloon, boolean userTouch) {

        mContentView.removeView(balloon);
        if(userTouch){
            mScore++;
        }

        updateDisplay();
    }

    //Update the score and level
    private void updateDisplay() {

        mLevelDisplay.setText(String.valueOf(mLevel));
        mScoreDisplay.setText(String.valueOf(mScore));

    }


    class BalloonLauncher extends AsyncTask<Integer,Integer,Void>{

        @Override
        protected Void doInBackground(Integer... params) {

            int level = params[0];

            int maxDelay = Math.max(Constant.MIN_ANIMATION_DELAY,
                                   (Constant.MAX_ANIMATION_DELAY-((level-1)) * 500));

            int minDelay = maxDelay/2;

            int balloonsLauncher = 0;

            while(balloonsLauncher < 3){

                //Random Number Generator
                Random random = new Random(new Date().getTime());

                // Get a random horizontal position for the next balloon
                int xPosition = random.nextInt(mScreenWidth - 200);

                publishProgress(xPosition);

                balloonsLauncher++;

               // Wait a random number of milliseconds before looping
                int delay = random.nextInt(minDelay) + minDelay;
                try {

                    Thread.sleep(delay);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {

            int xPosition = values[0];
            LaunchBalloon(xPosition);

        }

        private void LaunchBalloon(int x) {

            Balloon balloon = new Balloon(MainActivity.this,mBalloonColor[mNextColor],150);

            balloon.setX(x);
            balloon.setY(mScreenHeight + balloon.getHeight());

            mContentView.addView(balloon);

            if(mNextColor + 1 ==mBalloonColor.length){
                mNextColor = 0;
            }
            else{
                mNextColor++;
            }


            int duration = Math.max(Constant.MIN_ANIMATION_DURATION,
                                     Constant.MAX_ANIMATION_DURATION- (mLevel * 1000));

            balloon.releasBalloon(mScreenHeight,duration);
        }
    }
}



