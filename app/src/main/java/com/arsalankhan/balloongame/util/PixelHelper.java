package com.arsalankhan.balloongame.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by Arsalan khan on 9/16/2017.
 */

public class PixelHelper {

    public static int pixelToDp(int px, Context context){

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,px,
                context.getResources().getDisplayMetrics());
    }
}
