package com.example.desktopsprite;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class DesktopSpriteService extends Service {

    private boolean spriteExist = false;

    private DesktopSpriteManager spriteManager;

    private SensorsManager sensorsManager;
    private LocationGPSManager locationGPSManager;

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
            if (sensorsManager == null)
                sensorsManager = new SensorsManager(this);
            if (locationGPSManager == null)
                locationGPSManager = new LocationGPSManager();
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

    // Called by SensorsManger when the proximity is less than 3 cm, only called one time if the user stays closer than 3 cm
    public void tooClose(float proximity) {
        Log.w("myApp", "Too close!!!!!!!!!!!!!!!!!");
    }

    // Called by SensorsManger when the user keeps shaking the phone, "times" presents how many times did the user shake
    public void keepShaking(int times) {
        Log.w("myApp", "Keep Shaking!!!!!!!!!!!!!!!!!");
    }

    // Just example, can delete
    public void testGetLocation() {
        double[] location = locationGPSManager.getLocation();
        Log.w("myApp", "longitude: " + location[0] + " latitude: " + location[1]);
    }
}
