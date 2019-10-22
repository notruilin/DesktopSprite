package com.example.desktopsprite;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

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


public class DesktopSpriteManager {
    private WindowManager windowManager;
    private DesktopSpriteView spriteView;
    private OptionBarView optionBarView;
    private DialogView dialogView;
    private AlertDialogView alertDialogView;


    private long lastShowOptionBarTime;
    private long lastShowDialogTime;

    // SilenceMode == 0, no limit
    // SilenceMode == 1, disable dialog
    // SilenceMode == 2, disable both dialog, option bar
    private int silenceMode = 0;

    private int response_from_alert_dialog = 1;
    private boolean if_response = false;

    Context context;

    public void showSprite(Context context) {
        this.context = context;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        spriteView = new DesktopSpriteView(context, this);
        WindowManager.LayoutParams spriteParams = setParams(spriteView.spriteWidth, spriteView.spriteHeight);
        spriteView.setSpriteParams(spriteParams);
        windowManager.addView(spriteView, spriteParams);
        spriteView.initSpritePosition();
        spriteView.showing = true;
        MainActivity.getInstance().updateButton("Dismiss");
    }

    public void createOptionBar(Context context) {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        optionBarView = new OptionBarView(context, this);
        WindowManager.LayoutParams optionBarParams = setParams(optionBarView.barWidth, optionBarView.barHeight);
        optionBarView.setBarParams(optionBarParams);
        windowManager.addView(optionBarView, optionBarParams);
        optionBarView.setVisibility(View.INVISIBLE);
    }

    public void createDialog(Context context) {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        dialogView = new DialogView(context, this);
        WindowManager.LayoutParams dialogParams = setParams(dialogView.dialogWidth, dialogView.dialogHeight);
        dialogView.setDialogParams(dialogParams);
        windowManager.addView(dialogView, dialogParams);
        dialogView.setVisibility(View.INVISIBLE);
    }

    public void createAlertDialog(Context context) {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        alertDialogView = new AlertDialogView(context, this);
        WindowManager.LayoutParams alertDialogParams = setParams(alertDialogView.dialogWidth, alertDialogView.dialogHeight);
        alertDialogView.setAlertDialogParams(alertDialogParams);
        windowManager.addView(alertDialogView, alertDialogParams);
        alertDialogView.setVisibility(View.INVISIBLE);
    }

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

    public void showOptionBar(final int duration) {
        if (silenceMode == 2)    return;
        lastShowOptionBarTime = System.currentTimeMillis();
        spriteView.optionBarShowing = true;

        optionBarView.setVisibility(View.VISIBLE);

        optionBarView.postDelayed(new Runnable() {
            @Override
            public void run() {
                // If show option bar again during duration
                if (System.currentTimeMillis() - lastShowOptionBarTime < duration)  return;
                optionBarView.setVisibility(View.GONE);
                spriteView.optionBarShowing = false;
            }
        }, duration);
    }

    public void showDialog(String txt, final int duration) {
        if (silenceMode >= 1)    return;
        lastShowDialogTime = System.currentTimeMillis();
        dialogView.setTxt(txt);
        dialogView.setVisibility(View.VISIBLE);

        dialogView.postDelayed(new Runnable() {
            @Override
            public void run() {
                // If show dialog again during duration
                if (System.currentTimeMillis() - lastShowDialogTime < duration)  return;
                dialogView.setVisibility(View.GONE);
            }
        }, duration);
    }

    public void showAlertDialog(String txt1, String txt2, String txt3, final int duration) {
        if (silenceMode >= 1)    return;
        lastShowDialogTime = System.currentTimeMillis();
        alertDialogView.setTxt(txt1);
        alertDialogView.setVisibility(View.VISIBLE);

        alertDialogView.setFirstButton(txt2);
        alertDialogView.setSecondButton(txt3);
        alertDialogView.postDelayed(new Runnable() {
            @Override
            public void run() {
                // If show dialog again during duration
                if (System.currentTimeMillis() - lastShowDialogTime < duration)  return;
                alertDialogView.setVisibility(View.GONE);
            }
        }, duration);
    }

    public void showDialog2(String txt, final int duration) {
        if (silenceMode >= 1)    return;
        lastShowDialogTime = System.currentTimeMillis();
        dialogView.setTxt(txt);
        dialogView.setVisibility(View.VISIBLE);

        dialogView.postDelayed(new Runnable() {
            @Override
            public void run() {
                // If show dialog again during duration
                if (System.currentTimeMillis() - lastShowDialogTime < duration)  return;
                dialogView.setVisibility(View.GONE);
            }
        }, duration);
    }


    public void setSilenceMode(int silenceMode) {
        this.silenceMode = silenceMode;
    }

    public int getSilenceMode() {
        return silenceMode;
    }

    public void setBarViewPosition(int x, int y) {
        optionBarView.setPosition(x, y);
    }

    // If left == true, show the dialog on the left side of sprite
    public void setDialogViewPosition(int x, int y, boolean left) {
        dialogView.setPosition(x, y, left);
    }

    public void setAlertDialogViewPosition(int x, int y, boolean left){
        alertDialogView.setPosition(x, y, left);
    }

    public void hideOptionBar() {
        spriteView.optionBarShowing = false;
        optionBarView.setVisibility(View.INVISIBLE);
    }

    public void hideAlertDialogView(){
        alertDialogView.setVisibility(View.INVISIBLE);
    }

    public void destroySprite() {
        Log.w("myApp", "destroy");
        spriteView.showing = false;
        windowManager.removeView(spriteView);
        windowManager.removeView(optionBarView);
        windowManager.removeView(dialogView);
    }

    public boolean spriteShowing() {
        return spriteView.showing;
    }

    public void checkLight() {
        float light  = SensorsManager.getInstance().getLight();
        showDialog("The light is " + light + " lx", 3000);
    }

    public void feed_milk(){
        spriteView.drinkMilk();
    }

    public void feed_complementary(){
        spriteView.eatComplementary();
    }

    public void shower() { spriteView.play_shower();}

    public void sleep() {spriteView.play_aeolian();}

    public void startVomit() {
        spriteView.playVomitAnim();
    }

    public boolean random_crawl() {
        boolean crawl_left = Math.random() < 0.5;
        spriteView.play_crawl(crawl_left);


        return true;
    }


    public void showWeather(String str){
        Log.w("myApp", "Have get Weather"+str);
        String mainWeather = "";
        String description = "";
        try {
            JSONObject json = new JSONObject(str);
            String data = json.getString("weather");
            String weather=data.replace("[","").replace("]","");
            Log.w("myApp", weather);
            JSONObject json2 = new JSONObject(weather);
            mainWeather = json2.getString("main");
            description = json2.getString("description");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(mainWeather.equals("Clear")){
            Log.w("myApp", description);
            spriteView.sleep_after_play_aeolian();
        }
        else if(mainWeather.equals("Rain")||mainWeather.equals("Snow")||mainWeather.equals("Drizzle")){
            Log.w("myApp", description);
            spriteView.sleep_after_play_aeolian();
        }
        else if(mainWeather.equals("Thunderstorm")){
            Log.w("myApp", description);
            spriteView.sleep_after_play_aeolian();
        }
        else if(mainWeather.equals("Clouds")){
            Log.w("myApp", description);
            spriteView.sleep_after_play_aeolian();
        }
        else{
        }

    }


    public void getCurrentLocation() {

        double locationNow[] = LocationGPSManager.getInstance().getLocation();
        Log.w("myApp", "lat="+locationNow[0]+"&lon="+locationNow[1]);

        new GetData().execute("lat="+locationNow[0]+"&lon="+locationNow[1]);
    }


    //get weather data
    class GetData extends AsyncTask<String,Integer,String> {

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
            }
            finally {
                if(conn!=null)
                    conn.disconnect();
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... progresses) {
            Log.w("myApp", "on progress");
            showDialog("Searching for Weather...",1000);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            showWeather(result);

        }

    }

    public void backHome(){
        spriteView.setToDefaultView();
    }


    public int getSteps() {
        int steps  = SensorsManager.getInstance().getStepCounter();
        return steps;

    }

    public void setResponse(int response) {
        this.response_from_alert_dialog = response;
    }

    public int getResponse(){
        return this.response_from_alert_dialog;
    }

//    public void set_if_response(boolean boo){
//        this.if_response = boo;
//    }
//
//    public boolean get_if_response(){
//        return if_response;
//    }

    public void showDefault(){
        spriteView.setToDefaultView();
    }

    public void launchMainActivity() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
