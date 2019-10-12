package com.example.desktopsprite;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

public class DesktopSpriteManager {
    private WindowManager windowManager;
    private DesktopSpriteView spriteView;
    private OptionBarView optionBarView;

    public void showSprite(Context context) {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        spriteView = new DesktopSpriteView(context, this);
        WindowManager.LayoutParams spriteParams = new WindowManager.LayoutParams();
        spriteParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        spriteParams.format = PixelFormat.RGBA_8888;
        spriteParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        spriteParams.gravity = Gravity.LEFT | Gravity.TOP;
        spriteParams.width = spriteView.spriteWidth;
        spriteParams.height = spriteView.spriteHeight;
        spriteView.setSpriteParams(spriteParams);
        windowManager.addView(spriteView, spriteParams);
        spriteView.initSpritePosition();
        spriteView.showing = true;
        MainActivity.getInstance().updateButton("Dismiss");
        Log.w("myApp", "end onStartCommand");
    }

    public void createOptionBar(Context context) {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        optionBarView = new OptionBarView(context, this);
        WindowManager.LayoutParams optionBarParams = new WindowManager.LayoutParams();
        optionBarParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        optionBarParams.format = PixelFormat.RGBA_8888;
        optionBarParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        optionBarParams.gravity = Gravity.LEFT | Gravity.TOP;
        optionBarParams.width = optionBarView.barWidth;
        optionBarParams.height = optionBarView.barHeight;
        optionBarView.setBarParams(optionBarParams);
        windowManager.addView(optionBarView, optionBarParams);
        optionBarView.setVisibility(View.INVISIBLE);
    }

    public void showOptionBar(int duration) {
        spriteView.optionBarShowing = true;
        optionBarView.setVisibility(View.VISIBLE);

        optionBarView.postDelayed(new Runnable() {
            @Override
            public void run() {
                optionBarView.setVisibility(View.GONE);
                spriteView.optionBarShowing = false;
            }
        }, duration);
    }

    public void setBarViewPosition(int x, int y) {
        optionBarView.setPosition(x, y);
    }

    public void hideOptionBar() {
        spriteView.optionBarShowing = false;
        optionBarView.setVisibility(View.INVISIBLE);
    }

    public void destroySprite() {
        Log.w("myApp", "destroy");
        spriteView.showing = false;
        windowManager.removeView(spriteView);
        windowManager.removeView(optionBarView);
    }

    public boolean spriteShowing() {
        return spriteView.showing;
    }

    public void checkLight() {
        float light  = SensorsManager.getInstance().getLight();
        spriteView.showDialog("The light is " + light + " lx", 3000);
    }

    public void feed(){
        spriteView.drinkMilk();
    }


    public void checkWeather(){
        LocationGPSManager gpsManager = new LocationGPSManager();
        double[] location = gpsManager.getLocation();

    }


    public void startVomit() {
        spriteView.playVomitAnim();
    }
}
