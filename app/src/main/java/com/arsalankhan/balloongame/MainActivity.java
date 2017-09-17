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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arsalankhan.balloongame.util.Constant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements Balloon.BalloonListener {

    ViewGroup mContentView;
    private int mBalloonColor[] = new int[4];
    private int mNextColor, mScreenWidth, mScreenHeight;
    private int mLevel, mScore, mPinUsed;
    private TextView mScoreDisplay,mLevelDisplay;
    private List<ImageView> mPinImages = new ArrayList<>();
    private List<Balloon> mBalloons = new ArrayList<>();
    private boolean mPlaying;
    private boolean mStopedGame= true;
    private int mPoppedBalloons;
    private Button mGoButton;

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

        mPinImages.add((ImageView) findViewById(R.id.pushpin1));
        mPinImages.add((ImageView) findViewById(R.id.pushpin2));
        mPinImages.add((ImageView) findViewById(R.id.pushpin3));
        mPinImages.add((ImageView) findViewById(R.id.pushpin4));
        mPinImages.add((ImageView) findViewById(R.id.pushpin5));

        mGoButton = (Button) findViewById(R.id.goButton);


        updateDisplay();
    }

    //start button click listener
    public void StartGameButton(View view){

        if(mPlaying){
            gameOver(false);
        }
        else if(mStopedGame){
            startGame();
        }
        else{

            startLevel();
        }

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

private void startGame(){
    mLevel= 0;
    mPinUsed= 0;
    mScore= 0;

    for(ImageView pin : mPinImages){
        pin.setImageResource(R.drawable.pin);
    }
    mStopedGame = false;
    startLevel();

}
    private void startLevel(){
        setFullScreen();
        mLevel++;
        mPlaying = true;
        mPoppedBalloons =0;
        mGoButton.setText("Stop Game");

        updateDisplay();

        BalloonLauncher balloonLauncher = new BalloonLauncher();
        balloonLauncher.execute(mLevel);
    }

    private void finishLevel(){

        Toast.makeText(this, "You Finished Level: "+mLevel, Toast.LENGTH_SHORT).show();
        mPlaying = false;
        mGoButton.setText("Start Level "+mLevel+1);
    }
    @Override
    public void poppedBalloon(Balloon balloon, boolean userTouch) {

        mContentView.removeView(balloon);

        mPoppedBalloons++;

        //remove the balloon from the Arraylist
        mBalloons.remove(balloon);

        if(userTouch){
            mScore++;
        }
        else{

            mPinUsed++;

            if(mPinUsed <= mPinImages.size()){

                mPinImages.get(mPinUsed -1).setImageResource(R.drawable.pin_off);
            }

            if(mPinUsed == Constant.NUMBER_OF_PINS){
                gameOver(true);
            }
            else{

                Toast.makeText(this, "Missed that one!", Toast.LENGTH_SHORT).show();
            }

        }

        updateDisplay();

        if(mPoppedBalloons == Constant.BALLOONS_PER_LEVEL){

            finishLevel();
        }
    }

    private void gameOver(boolean b) {


            for(Balloon balloons : mBalloons){

                mContentView.removeView(balloons);
                balloons.setPopped(true);

            }

            mBalloons.clear();
            mPlaying =false;
            mStopedGame = true;
            mGoButton.setText("Start Game");
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

            while(mPlaying && balloonsLauncher < Constant.BALLOONS_PER_LEVEL){

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

            mBalloons.add(balloon);

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



