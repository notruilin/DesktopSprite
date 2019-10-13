package com.example.desktopsprite;

import android.content.Context;
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
        setButtonsListeners();
    }

    public void setBarParams(WindowManager.LayoutParams barParams) {
        this.barParams = barParams;
    }

    public void setPosition(int x, int y) {
        if (!desktopSpriteManager.spriteShowing())  return;
        barParams.x = x - this.getWidth()/2;
        barParams.y = y;
        windowManager.updateViewLayout(this, barParams);
    }

    void setButtonsListeners() {
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
                desktopSpriteManager.showDialog("Shower!", 1000);
            }
        });

        final Button sleepBtn = findViewById(R.id.btn_sleep);
        sleepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("myApp", "Sleep!");
                desktopSpriteManager.showDialog("Sleep!", 1000);
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
                desktopSpriteManager.showDialog("Go Home!", 1000);
            }
        });

        final Button alarmBtn = findViewById(R.id.btn_alarm);
        alarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("myApp", "Alarm!");
                desktopSpriteManager.showDialog("Alarm Alarm Alarm Alarm Alarm aaaaaa asdadsa!", 1000);
            }
        });

        final Button stepBtn = findViewById(R.id.btn_step);
        stepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("myApp", "Step!");
                desktopSpriteManager.showDialog("Step Step Step Step Step Step Step Step Step", 1000);
            }
        });

        final Button stopBtn = findViewById(R.id.btn_stop);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int silenceMode = desktopSpriteManager.getSilenceMode();
                if (silenceMode == 0)   {
                    silenceMode = 1;
                    stopBtn.setBackgroundResource(R.drawable.btn_stop);
                }
                else {
                    silenceMode = 0;
                    stopBtn.setBackgroundResource(R.drawable.btn_on);
                }
                desktopSpriteManager.setSilenceMode(silenceMode);
                desktopSpriteManager.showDialog("Silence Mode: Off", 1000);
                Log.w("myApp", "Set silence mode to: " + silenceMode);
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
