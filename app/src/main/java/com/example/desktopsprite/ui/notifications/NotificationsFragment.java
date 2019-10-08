package com.example.desktopsprite.ui.notifications;

import android.app.AlarmManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.desktopsprite.DesktopSpriteService;
import com.example.desktopsprite.R;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_notifications, container, false);


        Button button = root.findViewById(R.id.button_alarm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("myApp", "Click Alarm Clock Button!");
                TimePicker timePicker = root.findViewById(R.id.clock_time_picker);
                int hour = timePicker.getHour();
                int min = timePicker.getMinute();
                Log.w("myApp", "hour = " + hour);
                // TODO new clock alarm

                // AlarmManager alarmManager = (AlarmManager)getSystemService(Service.ALARM_SERVICE);
                // alarmManager.set();
            }
        });


        return root;
    }


}