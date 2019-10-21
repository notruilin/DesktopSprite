package com.example.desktopsprite;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class DesktopSpriteService extends Service {

    private boolean spriteExist = false;

    private DesktopSpriteManager spriteManager;

    private SensorsManager sensorsManager;
    private LocationGPSManager locationGPSManager;

    private Timer timer;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case 1:
                    Log.w("WY", "handelMessage: 1" );
                    spriteManager.random_crawl();
                    break;
            }
        }
    };

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
                locationGPSManager = new LocationGPSManager(this);
            spriteExist = true;
            spriteManager = new DesktopSpriteManager();
            spriteManager.showSprite(getApplicationContext());
            spriteManager.createOptionBar(getApplicationContext());
            spriteManager.createDialog(getApplicationContext());
            spriteManager.createAlertDialog(getApplicationContext());
        }
        else {
            spriteExist = false;
            MainActivity.getInstance().updateButton("Summon");
            onDestroy();
        }

        // start circulation timer
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new RefreshTask(), 3000, 5000);
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
        spriteManager.startVomit();
    }


    //test
//    public void getLocation() {
//        double[] current_location = locationGPSManager.getI;
//        Log.w("myApp", "longitude: " + current_location[0] + " latitude: " + current_location[1]);
//
//    }


    class RefreshTask extends TimerTask {

        @Override
        public void run() {
            Log.w("wy", "TIMETASK RUNNING: ");
            Message message = new Message( );
            message.what = 1;
            handler.sendMessage(message);
            //spriteManager.random_crawl();
        }
    }


}
