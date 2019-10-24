/*
 * Project - Desktop Sprite
 * COMP90018 Mobile Computing Systems Programming
 * Author - Yao Wang, Tong He, Dinghao Yong, Jianyu Yan, Ruilin Liu
 * Oct 2019, Semester 2
 */

package com.example.desktopsprite;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.SoundPool;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.os.Handler;
import android.content.res.AssetFileDescriptor;

import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/*
 * This class implements the view of sprite
 * Including action animations, updating positions
 */

public class DesktopSpriteView extends LinearLayout {
    private final static float epsilon = 50;

    // If the sprite is showing
    public boolean showing = false;
    public boolean is_crawling = false;
    /*
     * 0 for default
     * 1 for some events is running
     */
    private int current_state = 0;
    private SoundPool spool;
    private Map<String,Integer> sound_dict = new HashMap<String,Integer> ();
    private boolean activate_media = false;
    private Handler myhander;
    private boolean crawl_left = false;


    public int spriteWidth, spriteHeight;
    public int screenWidth, screenHeight;

    private int defaultImageHeight, defaultImageWidth;

    public int spriteX, spriteY;
    private int statusBarHeight;
    private WindowManager.LayoutParams spriteParams;
    private AnimationDrawable animationDrawable;

    private final WindowManager windowManager;
    private DesktopSpriteManager desktopSpriteManager;

    // The threshold to detect a double click
    private static final long DOUBLE_CLICK_TIME = 300;
    // The last time the user touches the sprite
    private long lastTouchTime;

    private boolean holding = false;
    private boolean vomiting = false;
    public boolean optionBarShowing = false;

    private ImageView imageView;


    public DesktopSpriteView(Context context, DesktopSpriteManager desktopSpriteManager,Handler handler) {
        super(context);
        this.myhander = myhander;
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

        //prepare media content
//        init_sound_pool();
//        load_media(context);

    }

    public void initSpritePosition() {
        setSpritePosition(0, 0);
        fallToGround();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
            Log.w("myApp","outside");
            return true;
        }
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                current_state = 1;
                long touchTime = System.currentTimeMillis();
                // Detect double click
                if (touchTime - lastTouchTime <= DOUBLE_CLICK_TIME) {
                    onDoubleClick();
                }
                lastTouchTime = touchTime;
                spriteX = (int) event.getRawX();
                spriteY = (int) event.getRawY();
//                desktopSpriteManager.showDialog("XY: "+spriteX+spriteY, 4000);
//                int tmp = spriteX + defaultImageWidth / 2;
////                if (spriteX <= tmp) {
////                    setToDefaultView();
////                } else {
////                    setToDefaultViewRightSee();
////                }
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
                // Adjust the coordinate to make sure it is inside the screen
                if (spriteX < 0) spriteX = 0;
                if (spriteY < 0) spriteY = 0;
                if (spriteX > screenWidth) spriteX = screenWidth;
                if (spriteY > screenHeight) spriteY = screenHeight;
                if (event.getPointerCount() == 1) {
                    updateSpritePosition(dx, dy);
                }
                break;
            case MotionEvent.ACTION_UP:
                if(current_state != 2)
                    current_state = 0;
//                Log.w("WY","MotionEvent.ACTION_UP");
//                Log.w("WY","current_state == "+current_state);
                if (holding) {
                    holding = false;
                    // Embed the sprite to the bottom edge
                    if (isHorizontalEdge(event.getRawY())) {
                        desktopSpriteManager.setSilenceMode(2);
                        showHorizontalHide();
                    } else {
                        // Embed the sprite to the left edge
                        int verticalEdge = isVerticalEdge(event.getRawX());
                        if (verticalEdge == 0) {
                            desktopSpriteManager.setSilenceMode(2);
                            playVerticalLeftHide();
                        } else {
                            // Embed the sprite to the right edge
                            if (verticalEdge == 1) {
                                desktopSpriteManager.setSilenceMode(2);
                                playVerticalRightHide();
                            } else {
                                // Let to sprite fall down to the ground
                                fallToGround();
                                desktopSpriteManager.setSilenceMode(0);
                            }
                        }
                    }
                }
        }
        return true;
    }


    // Check if the sprite is on the bottom edge
    private boolean isHorizontalEdge(float y) {
        if (screenHeight - y < epsilon) {
            return true;
        }
        return false;
    }

    /*
     * Check if the sprite is on the left or right edge
     * @param   x   the x coordinate of sprite
     * @return  0 means left vertical edge, 1 right vertical edge, -1 not vertical edge
     */
    private int isVerticalEdge(float x) {
        if (x < epsilon) return 0;
        if (screenWidth - x < epsilon) return 1;
        return -1;
    }

    public void setSpriteParams(WindowManager.LayoutParams spriteParams) {
        this.spriteParams = spriteParams;
    }

    void updateSpritePosition(int dx, int dy) {
        if (!showing) return;
        spriteParams.x += dx;
        spriteParams.y += dy;
        windowManager.updateViewLayout(this, spriteParams);
        // Move the optionBar with the sprite, set the option bar to the top center of the sprite
        desktopSpriteManager.setBarViewPosition(spriteParams.x + defaultImageWidth / 2, spriteParams.y);
        // Move the dialog with the sprite
        moveDialogWithSprite(spriteParams.x, spriteParams.y);
        moveAlertDialogWithSprite(spriteParams.x, spriteParams.y);
    }

    void setSpritePosition(int x, int y) {
        if (!showing) return;
        spriteParams.x = x;
        spriteParams.y = y;
        windowManager.updateViewLayout(this, spriteParams);
        // Move the optionBar with the sprite, set the option bar to the top center of the sprite
        desktopSpriteManager.setBarViewPosition(spriteParams.x + defaultImageWidth / 2, spriteParams.y);
        // Move the dialog with the sprite
        moveDialogWithSprite(spriteParams.x, spriteParams.y);
        moveAlertDialogWithSprite(spriteParams.x, spriteParams.y);
    }

    void moveDialogWithSprite(int x, int y) {
        boolean left = true;
        int toRight = 0;
        if (x < screenWidth / 2) {
            left = false;
            toRight = defaultImageWidth;
        }
        desktopSpriteManager.setDialogViewPosition(x + toRight, y, left);
    }

    void moveAlertDialogWithSprite(int x, int y) {
        boolean left = true;
        int toRight = 0;
        if (x < screenWidth / 2) {
            left = false;
            toRight = defaultImageWidth;
        }
        desktopSpriteManager.setAlertDialogViewPosition(x + toRight, y, left);
    }

    void playVomitAnim() {
        imageView.setImageResource(R.drawable.vomit_anim);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
        this.current_state = 1;
        default_when_animation_ends(animationDrawable);
    }

    void eatComplementary() {
        imageView.setImageResource(R.drawable.feed_complementary);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.setOneShot(true);
        animationDrawable.start();
        this.current_state = 1;
        default_when_animation_ends(animationDrawable);
    }

    void drinkMilk() {
        imageView.setImageResource(R.drawable.feed_milk);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.setOneShot(true);
        animationDrawable.start();
        this.current_state = 1;
        default_when_animation_ends(animationDrawable);
    }

    void play_shower() {
        imageView.setImageResource(R.drawable.shower);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.setOneShot(true);
        animationDrawable.start();
        this.current_state = 1;
        default_when_animation_ends(animationDrawable);
    }

    void play_aeolian() {
        imageView.setImageResource(R.drawable.sleep_play_aeolian_bell);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.setOneShot(true);
        animationDrawable.start();
        this.current_state = 1;
        int duration = 0;
        for (int i = 0; i < animationDrawable.getNumberOfFrames(); i++) {
            duration += animationDrawable.getDuration(i);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sleep_after_play_aeolian();
            }
        }, duration);
    }

    void sleep_after_play_aeolian() {
        imageView.setImageResource(R.drawable.sleep);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
        this.current_state = 1;

    }


    void fallToGround() {
        final int[] location = new int[2];
        this.current_state = 1;
        imageView.getLocationOnScreen(location);
        imageView.setImageResource(R.drawable.free_fall);
        // Set the animation duration
        ValueAnimator animator = ValueAnimator.ofFloat(location[1], screenHeight - defaultImageHeight - statusBarHeight);
        int duration = screenHeight - defaultImageHeight - statusBarHeight - location[1];
        if (duration < 0) duration = 1;
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setSpritePosition(location[0], (int) (float) animation.getAnimatedValue());
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setToGround();
            }
        });
        animator.start();
    }

    // Set the sprite to default view
    void default_when_animation_ends(AnimationDrawable animationDrawable) {
        int duration = 0;
        for (int i = 0; i < animationDrawable.getNumberOfFrames(); i++) {
            duration += animationDrawable.getDuration(i);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setToDefaultView();
                if (vomiting = true)    vomiting = false;
            }
        }, duration);

        Log.w("WY","default_when_animation_ends");
        sent_message(1);
    }

    void showHolding() {
        imageView.setImageResource(R.drawable.holding);
        this.current_state = 1;
    }

    void showHorizontalHide() {
        imageView.setImageResource(R.drawable.horizontally_embed);
        this.current_state = 1;
    }

    void playVerticalLeftHide() {
        imageView.setImageResource(R.drawable.vertically_embed_left_anim);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
        this.current_state = 1;
    }

    void playVerticalRightHide() {
        imageView.setImageResource(R.drawable.vertically_embed_right_anim);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
        this.current_state = 1;
    }

    void setToDefaultView() {
        imageView.setImageResource(R.drawable.see_you);
        defaultImageHeight = imageView.getDrawable().getIntrinsicHeight();
        defaultImageWidth = imageView.getDrawable().getIntrinsicWidth();
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
        this.current_state = 0;

        sent_message(1);
    }

    void setToSeeLeft() {
        if (vomiting || !desktopSpriteManager.playingBall)   return;
        imageView.setImageResource(R.drawable.see_left);
        defaultImageHeight = imageView.getDrawable().getIntrinsicHeight();
        defaultImageWidth = imageView.getDrawable().getIntrinsicWidth();
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
    }

    void setToSeeRight() {
        if (vomiting || !desktopSpriteManager.playingBall)   return;
        imageView.setImageResource(R.drawable.see_right);
        defaultImageHeight = imageView.getDrawable().getIntrinsicHeight();
        defaultImageWidth = imageView.getDrawable().getIntrinsicWidth();
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
    }

    void setToDefaultViewRightSee() {
        imageView.setImageResource(R.drawable.see_right);
        defaultImageHeight = imageView.getDrawable().getIntrinsicHeight();
        defaultImageWidth = imageView.getDrawable().getIntrinsicWidth();
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
        sent_message(1);

    }

    void setToGround() {
        imageView.setImageResource(R.drawable.to_ground_anim);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.setOneShot(true);
        animationDrawable.start();
        default_when_animation_ends(animationDrawable);
    }

    void play_sunny() {
        imageView.setImageResource(R.drawable.sun_before);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
        this.current_state=1;
    }

    void play_sunny_after() {
        imageView.setImageResource(R.drawable.sun_after);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
        this.current_state=1;

    }

    void play_rain(){
        imageView.setImageResource(R.drawable.rain_before);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
        this.current_state=1;
    }

    void play_rain_after(){
        imageView.setImageResource(R.drawable.rain_after);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
        this.current_state=1;
    }

    void play_thunder(){
        imageView.setImageResource(R.drawable.thunder_before);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
        this.current_state=1;
    }

    void play_thunder_after(){
        imageView.setImageResource(R.drawable.thunder_afer);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
        this.current_state=1;
    }

    void onDoubleClick() {
        if (optionBarShowing) {
            current_state = 0;
            desktopSpriteManager.hideOptionBar();
        } else {
            // 2 for show OptionBar
            setToDefaultView();
            current_state = 2;
            imageView.clearAnimation();
            this.clearAnimation();
            Log.w("WY","onDoubleClick() \ncurrent_state == "+current_state);
            desktopSpriteManager.showOptionBar(5000);
        }
    }


    public boolean play_crawl() {
        is_crawling = true;
        //check current state
        if (current_state != 0) {
            return false;
        }
        Log.w("WY","play_crawl() \ncurrent_state == "+current_state);
        if(spriteParams.x > 10){
            crawl_left = true;
        }
        else {
            crawl_left = false;
        }

//        SoundPool media = SoundPool()

        //epsilon
        int dx;
        int dy = 0;
        int duration = 1500;
        float speed = 200/1500;



        if (crawl_left ) {
            dx = 0 - spriteParams.x;
            imageView.setImageResource(R.drawable.crawl_anim_left);

        }
        else {
            dx = (screenWidth - 336) - spriteParams.x;
            //dx = screenHeight -  - spriteParams.x;
            imageView.setImageResource(R.drawable.crawl_anim);
        }


//        ObjectAnimator.ofFloat(imageView,"translationX",spriteX,200F).setDuration(duration).start();
//        if(activate_media){
//            spool.play(sound_dict.get("crawl"),1,1,1,1,1);
//        }

        final int[] location = new int[2];
        imageView.getLocationOnScreen(location);



        Log.w("WY", "spriteX = " + spriteParams.x);
        Log.w("WY", "get_X = " + location[0]);
        //Log.w("WY", "spriteY = " + spriteParams.y);
        Log.w("WY", "dx = " + dx);
        Log.w("WY", "screenWidth = " + screenWidth);
        Log.w("WY", "screenHeight = " + screenHeight);
        Log.w("WY", "crawl_left = " + crawl_left);

        final ValueAnimator animator = ValueAnimator.ofFloat(spriteParams.x, spriteParams.x + dx).setDuration(4000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if(current_state==0){
                    setSpritePosition((int) (float) animation.getAnimatedValue(), spriteParams.y);
                }
                else {
                    animator.cancel();
                }
            }
        });
        animator.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                if(current_state==0)
                    play_crawl();

            }
        });
        animator.start();


        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();

        return true;
    }

    public void setCurrent_state(int i) {
        this.current_state = i;
    }
    private SoundPool init_sound_pool(){
        //设置描述音频流信息的属性

//        AudioAttributes abs = new AudioAttributes.Builder()
//                .setUsage(AudioAttributes.USAGE_MEDIA)
//                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                .build() ;
        spool =  new SoundPool.Builder()
                .setMaxStreams(100)   //设置允许同时播放的流的最大值
//                .setAudioAttributes(null)   //完全可以设置为null
                .build() ;
        return spool;
    }

    private boolean load_media(Context context){
        AssetFileDescriptor fd = null;
        try {
            fd = context.getAssets().openFd("crawl.map3");
//            sound_dict.put("crawl",spool.load(fd, fd.getStartOffset(), fd.getLength()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;

    }

    private void sent_message(int i){
        is_crawling = false;
//        Timer timer = new Timer();
//        timer.schedule(new RefreshTask(), 3000);
    }

    class RefreshTask extends TimerTask {

        @Override
        public void run() {
            Log.w("wy", "TIMETASK RUNNING: ");
            Message message = new Message();
            message.what = 1;
            myhander.sendMessage(message);
            //spriteManager.random_crawl();
        }
    }
}
