package com.example.desktopsprite;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DesktopSpriteView extends FrameLayout {
    public int spriteWidth, spriteHeight;
    public int screenWidth, screenHeight;

    private int spriteX, spriteY;
    private WindowManager.LayoutParams spriteParams;
    private AnimationDrawable animationDrawable;

    private Context context;
    private final WindowManager windowManager;

    public DesktopSpriteView(Context context) {
        super(context);
        this.context = context;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.sprite_layout, this);
        ImageView imageView = findViewById(R.id.sprite);
        imageView.setImageResource(R.drawable.stand);
        RelativeLayout view = findViewById(R.id.sprite_layout);
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
                playHangAnim();
                showDialog("Put me down!!!! Test Github sync. Lalalalala.???", 1000);
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
            case MotionEvent.ACTION_UP:
                stopHangAnim();
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
            this.addView(dialogLayout);
        }
        else {
            dialogLayout.setVisibility(View.VISIBLE);
        }

        TextView textView = findViewById(R.id.dialog_txt);
        textView.setText(txt);

        if (spriteX < screenWidth / 2) {
            findViewById(R.id.dialog_left_triangle).setVisibility(View.VISIBLE);
            findViewById(R.id.dialog_right_triangle).setVisibility(View.GONE);
        }
        else {
            findViewById(R.id.dialog_right_triangle).setVisibility(View.VISIBLE);
            findViewById(R.id.dialog_left_triangle).setVisibility(View.GONE);
        }

        dialogLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.dialog_layout).setVisibility(View.GONE);
            }
        }, duration);

        findViewById(R.id.sprite_layout).forceLayout();
    }
}
