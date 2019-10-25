/*
 * Project - Desktop Sprite
 * COMP90018 Mobile Computing Systems Programming
 * Author - Yao Wang, Tong He, Dinghao Yong, Jianyu Yan, Ruilin Liu
 * Oct 2019, Semester 2
 */

package com.example.desktopsprite;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/*
 * This class implements the specific actions of the sprite
 * In other words, how to perform an action
 */

public class DesktopSpriteManager {
    private Handler handler;
    private WindowManager windowManager;
    private DesktopSpriteView spriteView;
    private OptionBarView optionBarView;
    private DialogView dialogView;
    private OptionDialogView optionDialogView;
    private BallView ballView;

    private long lastShowOptionBarTime;
    private long lastShowDialogTime;

    /*
     * SilenceMode == 0, no limit
     * SilenceMode == 1, disable dialog
     * SilenceMode == 2, disable both dialog and option bar
     */
    private int silenceMode = 0;
    private boolean out = false;

    private int response_from_alert_dialog = 1;
    private boolean if_response = false;
    private int questionNumber = 0;
    private String weather = "Clear";
    private boolean tooDark = false;
    public boolean playingBall = false;

    Context context;

    public DesktopSpriteManager(Handler handler) {
        this.handler = handler;
    }

    // Initialize the sprite view
    public void showSprite(Context context) {
        this.context = context;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        spriteView = new DesktopSpriteView(context, this,handler);
        WindowManager.LayoutParams spriteParams = setParams(spriteView.spriteWidth, spriteView.spriteHeight);
        spriteView.setSpriteParams(spriteParams);
        windowManager.addView(spriteView, spriteParams);
        spriteView.initSpritePosition();
        spriteView.showing = true;
        MainActivity.getInstance().updateButton("See You.");
    }

    // Initialize the option bar view
    public void createOptionBar(Context context) {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        optionBarView = new OptionBarView(context, this);
        WindowManager.LayoutParams optionBarParams = setParams(optionBarView.barWidth, optionBarView.barHeight);
        optionBarView.setBarParams(optionBarParams);
        windowManager.addView(optionBarView, optionBarParams);
        optionBarView.setVisibility(View.INVISIBLE);
    }

    // Initialize the dialog view
    public void createDialog(Context context) {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        dialogView = new DialogView(context, this);
        WindowManager.LayoutParams dialogParams = setParams(dialogView.dialogWidth, dialogView.dialogHeight);
        dialogView.setDialogParams(dialogParams);
        windowManager.addView(dialogView, dialogParams);
        dialogView.setVisibility(View.INVISIBLE);
    }

    // Initialize the option dialog view
    public void createOptionDialog(Context context) {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        optionDialogView = new OptionDialogView(context, this);
        WindowManager.LayoutParams alertDialogParams = setParams(optionDialogView.dialogWidth, optionDialogView.dialogHeight);
        optionDialogView.setOptionDialogParams(alertDialogParams);
        windowManager.addView(optionDialogView, alertDialogParams);
        optionDialogView.setVisibility(View.INVISIBLE);
    }

    // Initialize the toy ball
    public void createBall(Context context) {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        ballView = new BallView(context, this, spriteView.screenWidth, spriteView.screenHeight);
        WindowManager.LayoutParams ballParams = setParams(ballView.ballWidth, ballView.ballHeight);
        ballView.setBallParams(ballParams);
        windowManager.addView(ballView, ballParams);
        ballView.setVisibility(View.GONE);
    }

    // Set layout parameters
    private WindowManager.LayoutParams setParams(int width, int height) {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        params.format = PixelFormat.RGBA_8888;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.width = width;
        params.height = height;
        return params;
    }

    /*
     * Show the option bar view, then hide it after specific seconds
     * @param    duration    hide the option bar after how many seconds
     */
    public void showOptionBar(final int duration) {
        if (silenceMode == 2) return;
        lastShowOptionBarTime = System.currentTimeMillis();
        spriteView.optionBarShowing = true;

        optionBarView.setVisibility(View.VISIBLE);

        optionBarView.postDelayed(new Runnable() {
            @Override
            public void run() {
                // If show option bar again during duration
                if (System.currentTimeMillis() - lastShowOptionBarTime < duration) return;
                optionBarView.setVisibility(View.GONE);
                spriteView.optionBarShowing = false;
            }
        }, duration);
    }

    /*
     * Show the dialog view, then hide it after specific seconds
     * @param    txt         the text to show on the dialog
     * @param    duration    hide the option bar after how many seconds
     */
    public void showDialog(String txt, final int duration) {
        if (silenceMode >= 1) return;
        lastShowDialogTime = System.currentTimeMillis();
        dialogView.setTxt(txt);
        dialogView.setVisibility(View.VISIBLE);

        dialogView.postDelayed(new Runnable() {
            @Override
            public void run() {
                // If show dialog again during duration
                if (System.currentTimeMillis() - lastShowDialogTime < duration) return;
                dialogView.setVisibility(View.GONE);
            }
        }, duration);
    }

    /*
     * Show the option dialog view, then hide it after specific seconds
     * @param    txt1        the description of the option
     * @param    txt2        option1
     * @param    txt3        option2
     * @param    duration    hide the option bar after how many seconds
     */
    public void showOptionDialog(String txt1, String txt2, String txt3, final int duration) {
        if (silenceMode >= 1) return;
        lastShowDialogTime = System.currentTimeMillis();
        optionDialogView.setTxt(txt1);
        optionDialogView.setVisibility(View.VISIBLE);

        optionDialogView.setFirstButton(txt2);
        optionDialogView.setSecondButton(txt3);
        optionDialogView.postDelayed(new Runnable() {
            @Override
            public void run() {
                // If show dialog again during duration
                if (System.currentTimeMillis() - lastShowDialogTime < duration) return;
                optionDialogView.setVisibility(View.GONE);
            }
        }, duration);
    }

    public void showDialog2(String txt, final int duration) {
        if (silenceMode >= 1) return;
        lastShowDialogTime = System.currentTimeMillis();
        dialogView.setTxt(txt);
        dialogView.setVisibility(View.VISIBLE);

        dialogView.postDelayed(new Runnable() {
            @Override
            public void run() {
                // If show dialog again during duration
                if (System.currentTimeMillis() - lastShowDialogTime < duration) return;
                dialogView.setVisibility(View.GONE);
            }
        }, duration);
    }

    /*
     * Set the sprite to silence mode to avoid interruption
     * @param   silenceMode    the silence level to set, 0 no limitation, 1 disable the dialog, 2 disable the option bar
     */
    public void setSilenceMode(int silenceMode) {
        this.silenceMode = silenceMode;
    }

    // Get the current silence level, 0 no limitation, 1 disable the dialog, 2 disable the option bar
    public int getSilenceMode() {
        return silenceMode;
    }

    // Set the option bar position to (x,y)
    public void setBarViewPosition(int x, int y) {
        optionBarView.setPosition(x, y);
    }

    /*
     * Set the option bar position
     * @param   x       the x coordinate of sprite (if left == true, left edge, if left == false, right edge)
     * @param   y       the y coordinate of sprite (top edge)
     * @param   left    if left == true, show the dialog on the left side of sprite
     */
    public void setDialogViewPosition(int x, int y, boolean left) {
        dialogView.setPosition(x, y, left);
    }

    public void setAlertDialogViewPosition(int x, int y, boolean left) {
        optionDialogView.setPosition(x, y, left);
    }

    public void hideOptionBar() {
        spriteView.optionBarShowing = false;
        optionBarView.setVisibility(View.INVISIBLE);
    }

    public void hideDialogView() {
        dialogView.setVisibility(View.INVISIBLE);
    }

    public void hideOptionDialogView() {
        optionDialogView.setVisibility(View.INVISIBLE);
    }

    public void destroySprite() {
        spriteView.showing = false;
        windowManager.removeView(spriteView);
        windowManager.removeView(optionBarView);
        windowManager.removeView(dialogView);
    }

    public boolean spriteShowing() {
        return spriteView.showing;
    }

    public void feed_milk() {
        spriteView.drinkMilk();
    }

    public void feed_complementary() {
        spriteView.eatComplementary();
    }

    public void shower() {
        spriteView.play_shower();
    }

    public void sleep() {
        spriteView.play_aeolian();
    }

    public void startVomit() {
        spriteView.playVomitAnim();
    }

    public boolean random_crawl() {
        spriteView.play_crawl();
        return true;
    }

    public void showLandmarkBuilding(){
        if (this.out){
            showDialog("You are near the St.Paul Church!!!", 3000);
            spriteView.play_landmark();
        }else{}
    }

    public void setOut(boolean boo){
        this.out = boo;
    }

    public void showWeather(String str) {
        Log.w("myApp", "Have get Weather" + str);
        String mainWeather = "";
        String description = "";
        try {
            JSONObject json = new JSONObject(str);
            String data = json.getString("weather");
            String weather = data.replace("[", "").replace("]", "");
            Log.w("myApp", weather);
            JSONObject json2 = new JSONObject(weather);
            mainWeather = json2.getString("main");
            description = json2.getString("description");
            this.weather = mainWeather;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //mainWeather = "Thunderstorm";
        //this.weather = mainWeather;

        if (mainWeather.equals("Clear")) {
            Log.w("myApp", description);
            //for test
            hideDialogView();
            spriteView.play_sunny();
            showOptionDialog(description + " now", "set a beach background", "cancel", 10000);
        } else if (mainWeather.equals("Rain") || mainWeather.equals("Snow") || mainWeather.equals("Drizzle") || mainWeather.equals("Clouds")) {
            Log.w("myApp", description);
            hideDialogView();

            spriteView.play_rain();

            showOptionDialog(description + " now", "give baby an umbrella", "cancel", 4000);

        } else if (mainWeather.equals("Thunderstorm")) {
            Log.w("myApp", description);
            hideDialogView();

            //for test
            spriteView.play_thunder();

            showOptionDialog(description + " now", "give baby an earphone", "cancel", 4000);

        } else {
            hideDialogView();
        }

    }


    public void getCurrentLocation() {
        LocationGPSManager.getInstance().setReminderLocation(false);
        double locationNow[] = LocationGPSManager.getInstance().getLocation();
        Log.w("myApp", "lat=" + locationNow[0] + "&lon=" + locationNow[1]);
        new GetData().execute("lat=" + locationNow[0] + "&lon=" + locationNow[1]);
    }

    public boolean is_crawling() {
        return spriteView.is_crawling;
    }


    //get weather data
    class GetData extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String result = "";
            HttpURLConnection conn = null;
            try {
                URL url = new URL("https://api.openweathermap.org/data/2.5/weather?lon=144.96&lat=-37.81&APPID=a8545160b5cc4954f46ca20570aef7a6");

//              URL url = new URL("https://api.openweathermap.org/data/2.5/weather?"+ URLEncoder.encode(params[0], "UTF-8")+"&APPID=a8545160b5cc4954f46ca20570aef7a6");
                conn = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(conn.getInputStream());
                if (in != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null)
                        result += line;
                }
                in.close();
                return result;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (conn != null)
                    conn.disconnect();
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... progresses) {
            Log.w("myApp", "on progress");
            showDialog("Searching for Weather...", 1000);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            showWeather(result);

        }

    }


    public int getSteps() {
        int steps = SensorsManager.getInstance().getStepCounter();
        return steps;

    }

    public void remindDark(float light){
        showDialog("The Environment is too Dark! I'll fall into sleep", 3000);
        spriteView.sleep_after_play_aeolian();
    }

    public void remindLightful(float light){
        showDialog("The ambient light is "+light+" lux now, too bright! Stop Reading", 3000);
    }

    public void normalLight(float light) {
        showDialog("Ambient brightness is "+light+" lux now. " + "Comfortable for eyes!", 4000);
    }

    public void showLight() {
        float light = SensorsManager.getInstance().getLight();
        if (light < 100){
            remindDark(light);
        }
        if (light > 400){
            remindLightful(light);
        }
        if (light>=100 & light<=400){
            normalLight(light);
        }
    }

    public void launchMainActivity() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void setQuestionNumber(int i) {
        this.questionNumber = i;
    }

    public int getQuestionNumber() {
        return this.questionNumber;
    }

    public void showActionOnWeather() {
        if (this.weather.equals("Clear")) {
            spriteView.play_sunny_after();
            showDialog("Playing on beach!", 2000);
        } else if (this.weather.equals("Rain") || this.weather.equals("Snow") || this.weather.equals("Drizzle") || this.weather.equals("Clouds")) {
            spriteView.play_rain_after();
            showDialog("Lovely Umbrella!", 2000);

        } else if (this.weather.equals("Thunderstorm")) {
            spriteView.play_thunder_after();
            showDialog("Wonderful Songs!", 2000);

        } else {
        }
    }

    public void setBabyDeaultView() {
        spriteView.setToDefaultView();
    }

    public void playBall() {
        if (!playingBall) {
            spriteView.setCurrent_state(1);
            ballView.setVisibility(View.VISIBLE);
            playingBall = true;
        }
        else {
            spriteView.setCurrent_state(0);
            ballView.setVisibility(View.GONE);
            playingBall = false;
            spriteView.setToDefaultView();
        }
    }

    public void moveBall(float dx, float dy) {
        Log.w("myApp", "Playing");
        if (playingBall)
            ballView.updatePosition(dx, dy);
    }

    public void changeToSeeLeft() {
        spriteView.setToSeeLeft();
    }

    public void changeToSeeRight() {
        spriteView.setToSeeRight();
    }

}
