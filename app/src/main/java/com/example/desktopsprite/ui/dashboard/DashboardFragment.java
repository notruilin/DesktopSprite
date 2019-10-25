package com.example.desktopsprite.ui.dashboard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.desktopsprite.HttpCall;
import com.example.desktopsprite.HttpRequest;
import com.example.desktopsprite.R;
import com.example.desktopsprite.SharedPreferenceManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        Switch switchClock = (Switch) root.findViewById(R.id.switch_clock);
        Switch switchWeather = (Switch) root.findViewById(R.id.switch_weather);
        final ImageView imageView = (ImageView) root.findViewById(R.id.imageView);
        final TextView weatherDate = (TextView) root.findViewById(R.id.weather_datetime);
        final TextView weatherDeg = (TextView) root.findViewById(R.id.weather_degree);
        final TextView weatherView =  (TextView) root.findViewById(R.id.weather_wind);

        final SharedPreferenceManager spm = new SharedPreferenceManager(getContext());
        switchClock.setChecked(spm.getClockState());
        switchWeather.setChecked(spm.getWeatherState());

        if (spm.getWeatherState()) {
            ConstraintLayout cv = root.findViewById(R.id.weather_layout);
            cv.setVisibility(View.VISIBLE);
        }

        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.GET);
        httpCall.setUrl("https://mobile.xiyunkey.com/weather.php");
        HashMap<String, String> params = new HashMap<>();
        params.put("city", "Melbourne");
        httpCall.setParams(params);
        new HttpRequest() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Log.w("weather", "response from server: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONObject wind = obj.getJSONObject("wind");
                    JSONArray weathers = obj.getJSONArray("weather");
                    JSONObject weather = (JSONObject) weathers.get(0);
                    JSONObject main = obj.getJSONObject("main");
                    String des = weather.getString("description");

                    Calendar rightNow = Calendar.getInstance(TimeZone.getTimeZone("Australia/Melbourne"));
                    String weekday = rightNow.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
                    int Hour = rightNow.get(Calendar.HOUR_OF_DAY);
                    DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
                    String formattedDate = df.format(rightNow.getTime());
                    String dt = weekday + " " + Hour +"\n"+ formattedDate + "\n" + des;
                    weatherDate.setText(dt);

                    Double temp_min = (main.getDouble("temp_min") - 273.15);
                    Double temp_max = (main.getDouble("temp_max") - 273.15);
                    String temp = String.format("%.1f - %.1f \u2103", temp_min, temp_max);
                    weatherDeg.setText(temp);

                    String windspeed = wind.getString("speed");
                    String deg = wind.getString("deg");
                    String hum = main.getString("humidity");
                    String text="Humidity: " + hum + " %\nWind: " + windspeed + " km/h";
                    weatherView.setText(text);

                    String icon_name = "weather_" + weather.getString("icon");
                    int resID = getContext().getResources().getIdentifier(icon_name, "drawable", getContext().getPackageName());
                    Log.w("IMAGE", "onResponse: " + icon_name + ", " + getActivity().getPackageName() + ", " + resID);

                    imageView.setImageResource(resID);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute(httpCall);

        switchClock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                spm.setClockState(isChecked);
            }
        });

        switchWeather.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                spm.setWeatherState(isChecked);

                if (isChecked) {
                    ConstraintLayout cv = root.findViewById(R.id.weather_layout);
                    cv.setVisibility(View.VISIBLE);
                } else {
                    ConstraintLayout cv = root.findViewById(R.id.weather_layout);
                    cv.setVisibility(View.INVISIBLE);
                }
            }
        });

        return root;
    }
}