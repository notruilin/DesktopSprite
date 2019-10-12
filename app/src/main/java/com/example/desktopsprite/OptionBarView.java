package com.example.desktopsprite;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

public class OptionBarView extends LinearLayout {

    private Context context;
    private DesktopSpriteManager desktopSpriteManager;
    private final WindowManager windowManager;
    private WindowManager.LayoutParams barParams;

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
        setButtonsListeners();
    }

    public void setBarParams(WindowManager.LayoutParams barParams) {
        this.barParams = barParams;
    }

    public void setPosition(int x, int y) {
        if (!desktopSpriteManager.spriteShowing())  return;
        barParams.x = x;
        barParams.y = y - 100;
        windowManager.updateViewLayout(this, barParams);
    }

    void setButtonsListeners() {
        Log.w("myApp", "setButtonsListeners");

        final Button eatBtn = findViewById(R.id.btn_eat);
        eatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("myApp", "Eat!");
                desktopSpriteManager.feed();
            }
        });

        final Button showerBtn = findViewById(R.id.btn_shower);
        showerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("myApp", "Shower!");
            }
        });

        final Button sleepBtn = findViewById(R.id.btn_sleep);
        sleepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("myApp", "Sleep!");
            }
        });

        final Button outBtn = findViewById(R.id.btn_out);
        outBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("myApp", "Weather!");
                desktopSpriteManager.checkWeather();
            }
        });

        final Button homeBtn = findViewById(R.id.btn_home);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("myApp", "Go Home!");

            }
        });

        final Button stopBtn = findViewById(R.id.btn_stop);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("myApp", "Stop!");
            }
        });

        final Button moreBtn = findViewById(R.id.btn_more);
        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("myApp", "More!");
            }
        });
    }
}
