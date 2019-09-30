package com.example.desktopsprite;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class DesktopSpriteView extends FrameLayout {
    public int spriteWidth, spriteHeight;
    public int screenWidth, screenHeight;

    private int spriteX, spriteY;
    private WindowManager.LayoutParams spriteParams;

    private final WindowManager windowManager;

    public DesktopSpriteView(Context context) {
        super(context);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.sprite_layout, this);
        FrameLayout view = findViewById(R.id.sprite_layout);
        spriteWidth = view.getLayoutParams().width;
        spriteHeight = view.getLayoutParams().height;
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Log.w("myApp", "touched!");
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                spriteX = (int) event.getRawX();
                spriteY = (int) event.getRawY();
                //Log.w("myApp", "down! " + "X: " + ((Integer)spriteX).toString() + " Y: " + ((Integer)spriteY).toString());
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) event.getRawX() - spriteX;
                int dy = (int) event.getRawY() - spriteY;
                spriteX += dx;
                spriteY += dy;
                //Log.w("myApp", "move! " + "dx: " + ((Integer)dx).toString() + " dy: " + ((Integer)dy).toString());
                if (event.getPointerCount() == 1) {
                    updateSpritePosition(dx, dy);
                }
                break;
        }
        return true;
    }

    public void setSpriteParams(WindowManager.LayoutParams spriteParams) {
        this.spriteParams = spriteParams;
    }

    void updateSpritePosition(int dx, int dy) {
        spriteParams.x += dx;
        spriteParams.y += dy;
        windowManager.updateViewLayout(this, spriteParams);
    }
}
