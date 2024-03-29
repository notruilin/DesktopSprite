package com.example.desktopsprite.ui.notifications;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlarmManager;
import java.util.Calendar;
import java.util.Date;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextClock;
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
import com.example.desktopsprite.SetActivity;

import static android.content.Context.ALARM_SERVICE;

public class NotificationsFragment extends Fragment {

    private TimePicker alarm;
    private AlarmManager am;
    private PendingIntent pi;

    private NotificationsViewModel notificationsViewModel;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.btn_add) {
            Log.w("test", "onOptionsItemSelected: ");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        setHasOptionsMenu(true);
        alarm = root.findViewById(R.id.clock_time_picker);
        alarm.setIs24HourView(Boolean.TRUE);
        initAlarm();
        Button button = root.findViewById(R.id.button_alarm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("myApp", "Click Alarm Clock Button!");
                Integer hour = alarm.getHour();
                Integer minute = alarm.getMinute();
                Log.w("myApp", "Hour = " + hour + " ,Minute = " + minute);

                Calendar rightNow = Calendar.getInstance();
                Integer currentH = rightNow.get(Calendar.HOUR_OF_DAY);
                Integer currentM = rightNow.get(Calendar.MINUTE);
                Log.w("myApp", "Hour = " + currentH + " ,Minute = " + currentM);
                // TODO new clock alarm
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                setAlarm(calendar);

                // AlarmManager alarmManager = (AlarmManager)getSystemService(Service.ALARM_SERVICE);
                // alarmManager.set();
            }
        });


        return root;
    }

    private void initAlarm() {
        am = (AlarmManager)getActivity().getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlarmBroadcast.class);
        intent.setAction("startAlarm");
        pi = PendingIntent.getBroadcast(getContext(), 110, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private void setAlarm(Calendar calendar){
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
        Toast.makeText(getContext(), "successful", Toast.LENGTH_SHORT).show();
    }


}