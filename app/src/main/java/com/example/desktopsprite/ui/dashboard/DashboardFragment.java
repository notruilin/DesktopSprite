package com.example.desktopsprite.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.desktopsprite.R;
import com.example.desktopsprite.SharedPreferenceManager;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        Switch switchClock = (Switch) root.findViewById(R.id.switch_clock);
        Switch switchWeather = (Switch) root.findViewById(R.id.switch_weather);

        final SharedPreferenceManager spm = new SharedPreferenceManager(getContext());
        switchClock.setChecked(spm.getClockState());
        switchWeather.setChecked(spm.getWeatherState());

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