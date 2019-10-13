package com.example.desktopsprite;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Handler;

public class DesktopSpriteView extends LinearLayout {
    private final static float epsilon = 5;

    // If the sprite is showing
    public boolean showing = false;

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

    private boolean holding = false;
    public boolean optionBarShowing = false;

    private ImageView imageView;

    public DesktopSpriteView(Context context, DesktopSpriteManager desktopSpriteManager) {
        super(context);
        this.context = context;
        this.desktopSpriteManager = desktopSpriteManager;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.sprite_layout, this);
        imageView = findViewById(R.id.sprite);
        setToDefaultView();

        LinearLayout view = findViewById(R.id.sprite_layout);
        spriteWidth = view.getLayoutParams().width;
        spriteHeight = view.getLayoutParams().height;
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
    }

    public void initSpritePosition() {
        setSpritePosition(0, 0);
        fallToGround();
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
                desktopSpriteManager.hideOptionBar();
                if (!holding) {
                    holding = true;
                    showHolding();
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
                if (holding) {
                    holding = false;
                    if (isHorizontalEdge(event.getRawY())) {
                        showHorizontalHide();
                    }
                    else {
                        int verticalEdge = isVerticalEdge(event.getRawX());
                        if (verticalEdge == 0) {
                            playVerticalLeftHide();
                        }
                        else {
                            if (verticalEdge == 1) {
                                playVerticalRightHide();
                            }
                            else{
                                fallToGround();
                            }
                        }
                    }
                }
        }
        return true;
    }

    private boolean isHorizontalEdge(float y) {
        if (screenHeight - y < epsilon) {
            return true;
        }
        return false;
    }

    // Return 0 means left vertical edge, 1 right vertical edge, -1 not vertical edge
    private int isVerticalEdge(float x) {
        if (x < epsilon)    return 0;
        if (screenWidth - x < epsilon) return 1;
        return -1;
    }

    public void setSpriteParams(WindowManager.LayoutParams spriteParams) {
        this.spriteParams = spriteParams;
    }

    void updateSpritePosition(int dx, int dy) {
        if (!showing)   return;
        spriteParams.x += dx;
        spriteParams.y += dy;
        windowManager.updateViewLayout(this, spriteParams);
        desktopSpriteManager.setBarViewPosition(spriteParams.x, spriteParams.y);
    }

    void setSpritePosition(int x, int y) {
        if (!showing)   return;
        spriteParams.x = x;
        spriteParams.y = y;
        windowManager.updateViewLayout(this, spriteParams);
        desktopSpriteManager.setBarViewPosition(spriteParams.x, spriteParams.y);
    }

    void playVomitAnim() {
        imageView.setImageResource(R.drawable.vomit_anim);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
        default_when_animation_ends(animationDrawable);
    }

    void drinkMilk(){
        imageView.setImageResource(R.drawable.feed_milk);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.setOneShot(true);
        animationDrawable.start();
        default_when_animation_ends(animationDrawable);
    }

    void fallToGround() {
        final int[] location = new int[2];
        imageView.getLocationOnScreen(location);
        imageView.setImageResource(R.drawable.feed_milk_2);
        ValueAnimator animator = ValueAnimator.ofFloat(location[1], screenHeight - imageView.getHeight());
        animator.setDuration(screenHeight - imageView.getHeight()/2 - location[1]);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setSpritePosition(location[0],(int)(float)animation.getAnimatedValue());
            }
        });
        animator.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                setToDefaultView();
            }
        });
        animator.start();
    }

    void default_when_animation_ends(AnimationDrawable animationDrawable){
        int duration = 0;
        for(int i=0;i<animationDrawable.getNumberOfFrames();i++){
            duration += animationDrawable.getDuration(i);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setToDefaultView();
            }
        },duration);
    }

    void showHolding() {
        imageView.setImageResource(R.drawable.holding);
    }

    void showHorizontalHide() {
        imageView.setImageResource(R.drawable.horizontally_embed);
    }

    void playVerticalLeftHide() {
        imageView.setImageResource(R.drawable.vertically_embed_left_anim);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
    }

    void playVerticalRightHide() {
        imageView.setImageResource(R.drawable.vertically_embed_right_anim);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
    }

    //看向左边
    void setToDefaultView() {
        imageView.setImageResource(R.drawable.see_left);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
    }

    // 看向右边
    void setToDefaultViewRight() {
        imageView.setImageResource(R.drawable.see_right);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
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
        if (optionBarShowing) {
            desktopSpriteManager.hideOptionBar();
        }
        else {
            desktopSpriteManager.showOptionBar(5000);
        }
    }
}
