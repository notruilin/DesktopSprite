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


        final Button lightBtn = findViewById(R.id.btn_light);
        lightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("myApp", "Light!");
                desktopSpriteManager.checkLight();
            }
        });

        final Button weatherBtn = findViewById(R.id.btn_weather);
        weatherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("myApp", "Weather!");
                desktopSpriteManager.checkWeather();
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
