package com.arsalankhan.balloongame.util;

import android.content.Context;
import android.media.MediaPlayer;

import com.arsalankhan.balloongame.R;

/**
 * Created by Arsalan khan on 9/17/2017.
 */

public class SoundHelper {
    private MediaPlayer mMediaPlayer;

    public SoundHelper() {
    }

    public void prepareMusicPlayer(Context context){

        mMediaPlayer = MediaPlayer.create(context, R.raw.pleasant_music);
        mMediaPlayer.setVolume(.5f,.5f);
        mMediaPlayer.setLooping(true);
    }

    public void playMusic(){

        if(mMediaPlayer!= null){

            mMediaPlayer.start();
        }
    }

    public void pauseMusic(){

        if(mMediaPlayer!=null && mMediaPlayer.isPlaying()){
            mMediaPlayer.pause();
        }
    }
}
