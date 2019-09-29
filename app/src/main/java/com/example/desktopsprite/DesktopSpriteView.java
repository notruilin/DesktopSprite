package com.example.desktopsprite;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class DesktopSpriteView extends FrameLayout {
    private final WindowManager windowManager;

    public DesktopSpriteView(Context context) {
        super(context);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.sprite_layout, this);
        FrameLayout view = findViewById(R.id.sprite_layout);
        ImageView spriteView = findViewById(R.id.sprite);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
    }
}
