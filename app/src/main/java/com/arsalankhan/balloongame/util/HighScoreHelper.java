package com.arsalankhan.balloongame.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Arsalan khan on 9/17/2017.
 */

public class HighScoreHelper {

    private static final String glob_pref="glog_pref";
    private static final String score_pref="score";

    private static SharedPreferences getPreferences(Context context){
        return context.getSharedPreferences(glob_pref,context.MODE_PRIVATE);
    }


    public static boolean isTopScore(Context context,int newScore){

        int topScore = getPreferences(context).getInt(score_pref,0);

        return newScore > topScore;
    }

    public static int getTopScore(Context context){
        return getPreferences(context).getInt(score_pref,0);
    }

    public static void setTopScore(Context context, int newScore){

        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putInt(score_pref,newScore);
        editor.apply();
    }
}
