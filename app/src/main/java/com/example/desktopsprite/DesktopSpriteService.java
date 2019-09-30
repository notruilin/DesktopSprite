package com.example.desktopsprite;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

public class DesktopSpriteService extends Service {

    private boolean spriteExist = false;
    private WindowManager windowManager;
    private DesktopSpriteView spriteView;

    public DesktopSpriteService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.w("myApp", "onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w("myApp", "onStartCommand");

        if (!spriteExist) {
            spriteExist = true;
            windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            spriteView = new DesktopSpriteView(getApplicationContext());
            LayoutParams spriteParams = new LayoutParams();
            spriteParams.type = LayoutParams.TYPE_APPLICATION_OVERLAY;
            spriteParams.format = PixelFormat.RGBA_8888;
            spriteParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
            spriteParams.gravity = Gravity.LEFT | Gravity.TOP;
            spriteParams.width = spriteView.spriteWidth;
            spriteParams.height = spriteView.spriteHeight;
            spriteParams.x = spriteView.screenWidth / 2;
            spriteParams.y = spriteView.screenHeight / 2;
            spriteView.setSpriteParams(spriteParams);
            windowManager.addView(spriteView, spriteParams);
            MainActivity.getInstance().updateButton("Dismiss");
            Log.w("myApp", "end onStartCommand");
        }
        else {
            spriteExist = false;
            MainActivity.getInstance().updateButton("Summon");
            onDestroy();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.w("myApp", "destroy");
        windowManager.removeView(spriteView);
        super.onDestroy();
    }
}
