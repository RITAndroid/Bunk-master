package com.rit.vishwajeet.bunkmanager;

import android.content.Context;
import android.view.View;
import android.widget.Toast;


public class message {
    public static void Message(Context context, String message ){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
