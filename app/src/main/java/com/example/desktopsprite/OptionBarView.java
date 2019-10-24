/*
 * Project - Desktop Sprite
 * COMP90018 Mobile Computing Systems Programming
 * Author - Yao Wang, Tong He, Dinghao Yong, Jianyu Yan, Ruilin Liu
 * Oct 2019, Semester 2
 */

package com.example.desktopsprite;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

/*
 * This class implements the view of the option bar
 * Including button listeners, updating positions
 */

public class OptionBarView extends LinearLayout {

    private DesktopSpriteManager desktopSpriteManager;
    private final WindowManager windowManager;
    private WindowManager.LayoutParams barParams;
    private int runCount = 0 ;

    public int barWidth, barHeight;

    public OptionBarView(Context context, DesktopSpriteManager desktopSpriteManager) {
        super(context);
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
        barParams.y = y - 70;
        windowManager.updateViewLayout(this, barParams);
    }

    void setButtonsListeners() {
        final Button eatBtn = findViewById(R.id.btn_eat);
        SharedPreferenceManager spm = new SharedPreferenceManager(getContext());
        final String petName = spm.getPet();

        eatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("myApp", "Eat!"+runCount);
                desktopSpriteManager.setQuestionNumber(1);
                desktopSpriteManager.showOptionDialog("Complementary or Milk?","Complementary","Milk",4000);
                desktopSpriteManager.hideOptionBar();
            }
        });

        final Button showerBtn = findViewById(R.id.btn_shower);
        showerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("myApp", "Shower!");
                desktopSpriteManager.shower();
                desktopSpriteManager.hideOptionBar();
            }
        });

        final Button sleepBtn = findViewById(R.id.btn_sleep);
        sleepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("myApp", "Sleep!");
                desktopSpriteManager.sleep();
                desktopSpriteManager.hideOptionBar();
            }
        });

        final Button outBtn = findViewById(R.id.btn_out);
        outBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desktopSpriteManager.showDialog("Loading the weather on your location...." , 1000);
                desktopSpriteManager.setQuestionNumber(4);
                desktopSpriteManager.getCurrentLocation();
                desktopSpriteManager.setOut(true);

                Log.w("myApp", "Weather!");
                desktopSpriteManager.hideOptionBar();

            }
        });

        final Button ballBtn = findViewById(R.id.btn_ball);
        ballBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("myApp", "Ball!");
                desktopSpriteManager.playBall();
                desktopSpriteManager.hideOptionBar();
            }
        });

        final Button alarmBtn = findViewById(R.id.btn_alarm);
        alarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("myApp", "Alarm!");
                desktopSpriteManager.showDialog("Alarm Alarm Alarm Alarm Alarm aaaaaa asdadsa!", 1000);
                desktopSpriteManager.hideOptionBar();
            }
        });

        final Button stepBtn = findViewById(R.id.btn_step);
        stepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int steps = desktopSpriteManager.getSteps();
                String comment;
                if (steps >= 1000)
                    comment = "(*^▽^*)";
                else
                    comment = "Please spend more time with me \n ╥﹏╥";
                desktopSpriteManager.showDialog("We walked " + steps + " steps together." + comment, 3000);
                desktopSpriteManager.hideOptionBar();
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
                desktopSpriteManager.hideOptionBar();
            }
        });

        final Button moreBtn = findViewById(R.id.btn_more);
        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("myApp", "More!");
                desktopSpriteManager.launchMainActivity();
            }
        });
    }


}
