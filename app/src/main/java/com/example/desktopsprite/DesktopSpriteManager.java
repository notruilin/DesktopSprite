package com.example.desktopsprite;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class DesktopSpriteManager {
    private WindowManager windowManager;
    private DesktopSpriteView spriteView;
    private OptionBarView optionBarView;
    private DialogView dialogView;

    private long lastShowOptionBarTime;
    private long lastShowDialogTime;

    // SilenceMode == 0, no limit
    // SilenceMode == 1, disable dialog
    // SilenceMode == 2, disable both dialog, option bar
    private int silenceMode = 0;

    public void showSprite(Context context) {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        spriteView = new DesktopSpriteView(context, this);
        WindowManager.LayoutParams spriteParams = setParams(spriteView.spriteWidth, spriteView.spriteHeight);
        spriteView.setSpriteParams(spriteParams);
        windowManager.addView(spriteView, spriteParams);
        spriteView.initSpritePosition();
        spriteView.showing = true;
        MainActivity.getInstance().updateButton("Dismiss");
    }

    public void createOptionBar(Context context) {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        optionBarView = new OptionBarView(context, this);
        WindowManager.LayoutParams optionBarParams = setParams(optionBarView.barWidth, optionBarView.barHeight);
        optionBarView.setBarParams(optionBarParams);
        windowManager.addView(optionBarView, optionBarParams);
        optionBarView.setVisibility(View.INVISIBLE);
    }

    public void createDialog(Context context) {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        dialogView = new DialogView(context, this);
        WindowManager.LayoutParams dialogParams = setParams(dialogView.dialogWidth, dialogView.dialogHeight);
        dialogView.setDialogParams(dialogParams);
        windowManager.addView(dialogView, dialogParams);
        dialogView.setVisibility(View.INVISIBLE);
    }

    private WindowManager.LayoutParams setParams(int width, int height) {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        params.format = PixelFormat.RGBA_8888;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.width = width;
        params.height = height;
        return params;
    }

    public void showOptionBar(final int duration) {
        if (silenceMode == 2)    return;
        lastShowOptionBarTime = System.currentTimeMillis();
        spriteView.optionBarShowing = true;
        optionBarView.setVisibility(View.VISIBLE);

        optionBarView.postDelayed(new Runnable() {
            @Override
            public void run() {
                // If show option bar again during duration
                if (System.currentTimeMillis() - lastShowOptionBarTime < duration)  return;
                optionBarView.setVisibility(View.GONE);
                spriteView.optionBarShowing = false;
            }
        }, duration);
    }

    public void showDialog(String txt, final int duration) {
        if (silenceMode >= 1)    return;
        lastShowDialogTime = System.currentTimeMillis();
        dialogView.setTxt(txt);
        dialogView.setVisibility(View.VISIBLE);

        dialogView.postDelayed(new Runnable() {
            @Override
            public void run() {
                // If show dialog again during duration
                if (System.currentTimeMillis() - lastShowDialogTime < duration)  return;
                dialogView.setVisibility(View.GONE);
            }
        }, duration);
    }

    public void showDialog2(String txt, final int duration) {
        if (silenceMode >= 1)    return;
        lastShowDialogTime = System.currentTimeMillis();
        dialogView.setTxt(txt);
        dialogView.setVisibility(View.VISIBLE);

        dialogView.postDelayed(new Runnable() {
            @Override
            public void run() {
                // If show dialog again during duration
                if (System.currentTimeMillis() - lastShowDialogTime < duration)  return;
                dialogView.setVisibility(View.GONE);
            }
        }, duration);
    }


    public void setSilenceMode(int silenceMode) {
        this.silenceMode = silenceMode;
    }

    public int getSilenceMode() {
        return silenceMode;
    }

    public void setBarViewPosition(int x, int y) {
        optionBarView.setPosition(x, y);
    }

    // If left == true, show the dialog on the left side of sprite
    public void setDialogViewPosition(int x, int y, boolean left) {
        dialogView.setPosition(x, y, left);
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
        windowManager.removeView(dialogView);
    }

    public boolean spriteShowing() {
        return spriteView.showing;
    }

    public void checkLight() {
        float light  = SensorsManager.getInstance().getLight();
        showDialog("The light is " + light + " lx", 3000);
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

    public boolean random_crawl() {
        boolean crawl_left = Math.random() < 0.5;
        spriteView.play_crawl(crawl_left);


        return true;
    }
}
