package com.example.desktopsprite;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class OptionBarView extends LinearLayout {

    private Context context;
    private DesktopSpriteManager desktopSpriteManager;
    private final WindowManager windowManager;

    public int barWidth, barHeight;

    public OptionBarView(Context context, DesktopSpriteManager desktopSpriteManager) {
        super(context);
        this.context = context;
        this.desktopSpriteManager = desktopSpriteManager;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.option_bar_layout, this);

        LinearLayout view = findViewById(R.id.option_bar_layout);
        barWidth = view.getLayoutParams().width;
        barHeight = view.getLayoutParams().height;
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
    }
}
