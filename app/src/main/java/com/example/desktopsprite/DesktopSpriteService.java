/*
 * Project - Desktop Sprite
 * COMP90018 Mobile Computing Systems Programming
 * Author - Yao Wang, Tong He, Dinghao Yong, Jianyu Yan, Ruilin Liu
 * Oct 2019, Semester 2
 */

package com.example.desktopsprite;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/*
 * This service implements the agent of the sprite
 * It will automatically trigger some actions, for example, crawl on the screen
 */

public class DesktopSpriteService extends Service {

    private boolean spriteExist = false;

    private DesktopSpriteManager spriteManager;

    private SensorsManager sensorsManager;
    private LocationGPSManager locationGPSManager;

    private Timer timer;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Log.w("WY", "handelMessage: 1");
                    spriteManager.random_crawl();
                    break;
            }
        }
    };

    private Timer timer2;    // timer for light/dark reminder in sensor manager
    private Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    Log.w("myApp", "handelMessage: 2");
                    sensorsManager.resetLightReminder();
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
        super.onCreate();
    }

    /*
     * Method to initialize all required classes and views for a sprite or destroy them
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w("myApp", "onStartCommand");
        // If the sprite doesn't exist, create one
        if (!spriteExist) {
            if (sensorsManager == null)
                sensorsManager = new SensorsManager(this);
            if (locationGPSManager == null)
                locationGPSManager = new LocationGPSManager(this);
            spriteExist = true;
            spriteManager = new DesktopSpriteManager(handler);
            spriteManager.showSprite(getApplicationContext());
            spriteManager.createOptionBar(getApplicationContext());
            spriteManager.createDialog(getApplicationContext());
            spriteManager.createOptionDialog(getApplicationContext());
        } else {
            // If the sprite exist, destroy all relevant instances
            spriteExist = false;
            MainActivity.getInstance().updateButton("Summon");
            onDestroy();
        }

        // start circulation timer
        if (timer == null) {
            timer = new Timer();
//            timer.schedule(new RefreshTask(), 3000);
            timer.scheduleAtFixedRate(new RefreshTask(), 5000, 10000);
        }

        if (timer2 == null) {
            timer2 = new Timer();
            timer2.scheduleAtFixedRate(new RefreshTask2(), 4000, 1200000);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        spriteManager.destroySprite();
        super.onDestroy();
    }

    /*
     * Called by SensorsManger when the proximity is less than 3 cm, only called one time if the user stays closer than 3 cm
     */
    public void tooClose(float proximity) {
        Log.w("myApp", "Too close!!!!!!!!!!!!!!!!!");
    }

    public void tooDark(float light){
        Log.w("myApp", "Too Dark");
        spriteManager.remindDark(light);

    }

    public void tooLightful(float light){
        Log.w("myApp", "Too Dark");
        spriteManager.remindLightful(light);
    }

    public void normalLight(float light){
        spriteManager.showDialog("Ambient brightness is "+light+" lux now. " +
                "Comfortable for eyes!", 4000);
        spriteManager.setBabyDeaultView();
    }

    public void remindMelbourne(){
        Log.w("myApp", "Near Melbourne");
        spriteManager.showLandmarkBuilding();
    }



    /*
     * Called by SensorsManger when the user keeps shaking the phone, "times" presents how many times did the user shake
     * @param   times   how many times does the user shake the phone
     */
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
            if(handler.hasMessages(1)) return;
            if(spriteManager.is_crawling()) return;
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
            //spriteManager.random_crawl();
        }
    }

    // this is the timer task to refresh the light&dark reminder in sensorManager
    class RefreshTask2 extends TimerTask {

        @Override
        public void run() {
            Log.w("myApp", "TIMETASK RUNNING: ");
            Message message = new Message();
            message.what = 2;
            handler.sendMessage(message);
        }
    }


}
