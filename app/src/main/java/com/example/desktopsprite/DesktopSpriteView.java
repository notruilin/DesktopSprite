package com.example.desktopsprite;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DesktopSpriteView extends LinearLayout {
    public int spriteWidth, spriteHeight;
    public int screenWidth, screenHeight;

    private int spriteX, spriteY;
    private WindowManager.LayoutParams spriteParams;
    private AnimationDrawable animationDrawable;

    private Context context;
    private final WindowManager windowManager;
    private DesktopSpriteManager desktopSpriteManager;

    private static final long DOUBLE_CLICK_TIME = 300;
    private long lastTouchTime;

    private boolean hanging = false;
    private boolean optionBarShowing = false;

    public DesktopSpriteView(Context context, DesktopSpriteManager desktopSpriteManager) {
        super(context);
        this.context = context;
        this.desktopSpriteManager = desktopSpriteManager;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.sprite_layout, this);
        ImageView imageView = findViewById(R.id.sprite);
        imageView.setImageResource(R.drawable.stand);
        LinearLayout view = findViewById(R.id.sprite_layout);
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
                long touchTime = System.currentTimeMillis();
                if (touchTime - lastTouchTime <= DOUBLE_CLICK_TIME) {
                    onDoubleClick();
                }
                lastTouchTime = touchTime;
                spriteX = (int) event.getRawX();
                spriteY = (int) event.getRawY();
                //Log.w("myApp", "down! " + "X: " + ((Integer)spriteX).toString() + " Y: " + ((Integer)spriteY).toString());
                break;
            case MotionEvent.ACTION_MOVE:
                if (!hanging) {
                    hanging = true;
                    playHangAnim();
                    showDialog("Put me down!!!! Test Github sync. Lalalalala.???", 1000);
                }
                int dx = (int) event.getRawX() - spriteX;
                int dy = (int) event.getRawY() - spriteY;
                spriteX += dx;
                spriteY += dy;
                //Log.w("myApp", "move! " + "dx: " + ((Integer)dx).toString() + " dy: " + ((Integer)dy).toString());
                if (event.getPointerCount() == 1) {
                    updateSpritePosition(dx, dy);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (hanging) {
                    hanging = false;
                    stopHangAnim();
                }
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

    void playHangAnim() {
        ImageView imageView = findViewById(R.id.sprite);
        imageView.setImageResource(R.drawable.hang_anim);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
    }

    void stopHangAnim() {
        animationDrawable.stop();
        ImageView imageView = findViewById(R.id.sprite);
        imageView.setImageResource(R.drawable.stand);
    }

    void showDialog(String txt, int duration) {
        View dialogLayout = findViewById(R.id.dialog_layout);
        if (dialogLayout == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View spriteLayout = inflater.inflate(R.layout.sprite_layout, null);
            dialogLayout = inflater.inflate(R.layout.dialog_layout, (ViewGroup) spriteLayout, false);
            this.addView(dialogLayout, -1);
        }
        else {
            dialogLayout.setVisibility(View.VISIBLE);
        }

        TextView textView = findViewById(R.id.dialog_txt);
        textView.setText(txt);

        dialogLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.dialog_layout).setVisibility(View.GONE);
            }
        }, duration);

    }

    void onDoubleClick() {
        Log.w("myApp", "Double Click");
        if (optionBarShowing) {
            hideOptionBar();
        }
        else {
            showOptionBar(5000);
        }
    }

    void showOptionBar(int duration) {
        Log.w("myApp", "1");
        View optionLayout = findViewById(R.id.option_bar_layout);
        if (optionLayout == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View spriteLayout = inflater.inflate(R.layout.option_bar_layout, null);
            optionLayout = inflater.inflate(R.layout.option_bar_layout, (ViewGroup) spriteLayout, false);
            this.addView(optionLayout, -1);
            Log.w("myApp", "2");
            setButtonsListeners();
            Log.w("myApp", "3");
        }
        else {
            optionLayout.setVisibility(View.VISIBLE);
        }

        optionBarShowing = true;

        optionLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.option_bar_layout).setVisibility(View.GONE);
                optionBarShowing = false;
            }
        }, duration);
    }

    public void hideOptionBar() {
        findViewById(R.id.option_bar_layout).setVisibility(View.GONE);
        optionBarShowing = false;
    }

    void setButtonsListeners() {
        Log.w("myApp", "setButtonsListeners");
        final Button eatBtn = findViewById(R.id.btn_eat);
        eatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("myApp", "Eat!");
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
