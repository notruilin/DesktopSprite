package com.example.desktopsprite.ui.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("startAlarm".equals(intent.getAction())) {
            Toast.makeText(context, "alarm notification", Toast.LENGTH_LONG).show();
            Log.w("myApp", "Alarm");
        }
    }
}
