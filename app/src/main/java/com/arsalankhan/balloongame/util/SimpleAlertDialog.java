package com.arsalankhan.balloongame.util;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * Created by Arsalan khan on 9/17/2017.
 */

public class SimpleAlertDialog extends DialogFragment {

    private static final String titleKey ="title";
    private static final String messageKey = "message";

    public static SimpleAlertDialog newInstance(String title, String message){

        Bundle args = new Bundle();
        args.putString(titleKey,title);
        args.putString(messageKey,message);

        SimpleAlertDialog fragment = new SimpleAlertDialog();
        fragment.setArguments(args);

        return fragment;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();

        String title = args.getString(titleKey);
        String message = args.getString(messageKey);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false);
        builder.setPositiveButton("Ok", null);

        return builder.create();
    }
}
