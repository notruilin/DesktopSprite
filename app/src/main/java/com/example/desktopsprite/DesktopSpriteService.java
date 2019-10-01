package com.example.desktopsprite;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class DesktopSpriteService extends Service {

    private boolean spriteExist = false;

    private DesktopSpriteManager spriteManager;

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
            spriteManager = new DesktopSpriteManager();
            spriteManager.showSprite(getApplicationContext());
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
        spriteManager.destroySprite();
        super.onDestroy();
    }
}
