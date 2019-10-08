package com.example.desktopsprite;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

public class DesktopSpriteManager {
    private WindowManager windowManager;
    private DesktopSpriteView spriteView;

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
        spriteParams.x = spriteView.screenWidth / 2;
        spriteParams.y = spriteView.screenHeight / 2;
        spriteView.setSpriteParams(spriteParams);
        windowManager.addView(spriteView, spriteParams);
        MainActivity.getInstance().updateButton("Dismiss");
        Log.w("myApp", "end onStartCommand");
    }

    public void destroySprite() {
        Log.w("myApp", "destroy");
        windowManager.removeView(spriteView);
    }

    public void checkLight() {
        float light  = SensorsManager.getInstance().getLight();
        spriteView.showDialog("The light is " + light + " lx", 3000);
    }

    public void startVomit() {
        spriteView.playVomitAnim();
    }
}
