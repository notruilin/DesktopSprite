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
import android.util.Log;

public class DesktopSpriteView extends LinearLayout {
    private final static float epsilon = 5;

    // If the sprite is showing
    public boolean showing = false;

    public int spriteWidth, spriteHeight;
    public int screenWidth, screenHeight;

    private int defaultImageHeight, defaultImageWidth;

    private int spriteX, spriteY;
    private int statusBarHeight;
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

        statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
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
                int tmp = spriteX+ defaultImageWidth/2;
                Log.w("myApp", "down! " + "X: " + ((Integer)spriteX).toString());
                Log.w("myApp", "down! " + "X: " + ((Integer)tmp).toString());
                if (spriteX <= tmp){
                    setToDefaultView();
                }else{
                    setToDefaultViewRightSee();
                }
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
                if (spriteX < 0)    spriteX = 0;
                if (spriteY < 0)    spriteY = 0;
                if (spriteX > screenWidth)    spriteX = screenWidth;
                if (spriteY > screenHeight)    spriteY = screenHeight;
                //Log.w("myApp", "move! " + "dx: " + ((Integer)dx).toString() + " dy: " + ((Integer)dy).toString());
                if (event.getPointerCount() == 1) {
                    updateSpritePosition(dx, dy);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (holding) {
                    holding = false;
                    if (isHorizontalEdge(event.getRawY())) {
                        desktopSpriteManager.setSilenceMode(2);
                        showHorizontalHide();
                    }
                    else {
                        int verticalEdge = isVerticalEdge(event.getRawX());
                        if (verticalEdge == 0) {
                            desktopSpriteManager.setSilenceMode(2);
                            playVerticalLeftHide();
                        }
                        else {
                            if (verticalEdge == 1) {
                                desktopSpriteManager.setSilenceMode(2);
                                playVerticalRightHide();
                            }
                            else{
                                fallToGround();
                                desktopSpriteManager.setSilenceMode(0);
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
        //Log.w("myPos", spriteParams.x + " " + spriteParams.y);
        windowManager.updateViewLayout(this, spriteParams);
        // Move the optionBar with the sprite, set the option bar to the top center of the sprite
        desktopSpriteManager.setBarViewPosition(spriteParams.x + defaultImageWidth/2, spriteParams.y);
        // Move the dialog with the sprite
        moveDialogWithSprite(spriteParams.x, spriteParams.y);
    }

    void setSpritePosition(int x, int y) {
        if (!showing)   return;
        spriteParams.x = x;
        spriteParams.y = y;
        //Log.w("myPos", spriteParams.x + " " + spriteParams.y);
        windowManager.updateViewLayout(this, spriteParams);
        // Move the optionBar with the sprite, set the option bar to the top center of the sprite
        desktopSpriteManager.setBarViewPosition(spriteParams.x + defaultImageWidth/2, spriteParams.y);
        // Move the dialog with the sprite
        moveDialogWithSprite(spriteParams.x, spriteParams.y);
    }

    void moveDialogWithSprite(int x, int y) {
        boolean left = true;
        int toRight = 0;
        if (x < screenWidth/2) {
            left = false;
            toRight = defaultImageWidth;
        }
        desktopSpriteManager.setDialogViewPosition(x + toRight, y, left);
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
        imageView.setImageResource(R.drawable.free_fall);
        ValueAnimator animator = ValueAnimator.ofFloat(location[1], screenHeight - defaultImageHeight - statusBarHeight);
        int duration = screenHeight - defaultImageHeight - statusBarHeight - location[1];
        if (duration < 0)   duration = 1;
        animator.setDuration(duration);
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
                setToGround();
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

    void setToDefaultView() {
        imageView.setImageResource(R.drawable.see_you);
        defaultImageHeight = imageView.getDrawable().getIntrinsicHeight();
        defaultImageWidth = imageView.getDrawable().getIntrinsicWidth();
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
    }

    void setToDefaultViewRightSee(){
        imageView.setImageResource(R.drawable.see_right);
        defaultImageHeight = imageView.getDrawable().getIntrinsicHeight();
        defaultImageWidth = imageView.getDrawable().getIntrinsicWidth();
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
    }

    void setToGround() {
        imageView.setImageResource(R.drawable.to_ground_anim);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.setOneShot(true);
        animationDrawable.start();
        default_when_animation_ends(animationDrawable);
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
