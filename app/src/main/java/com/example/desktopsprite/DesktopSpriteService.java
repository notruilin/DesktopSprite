package com.example.desktopsprite;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class DesktopSpriteService extends Service {

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

        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DesktopSpriteView spriteView = new DesktopSpriteView(getApplicationContext());
        LayoutParams spriteParams = new LayoutParams();
        spriteParams.type = LayoutParams.TYPE_APPLICATION_OVERLAY;
        spriteParams.format = PixelFormat.RGBA_8888;
        spriteParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
        spriteParams.gravity = Gravity.LEFT | Gravity.TOP;
        spriteParams.width = 800;
        spriteParams.height = 800;
        spriteParams.x = windowManager.getDefaultDisplay().getWidth();
        spriteParams.y = windowManager.getDefaultDisplay().getHeight() / 2;
        Log.w("myApp", spriteParams.x + " " + spriteParams.y + " " + spriteParams.width + " " + spriteParams.height);
        windowManager.addView(spriteView, spriteParams);
        Log.w("myApp", "end onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
