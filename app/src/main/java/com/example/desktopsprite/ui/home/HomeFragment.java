package com.example.desktopsprite.ui.home;

import com.example.desktopsprite.*;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.desktopsprite.R;
import com.qmuiteam.qmui.widget.QMUIProgressBar;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.setting, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_name) {

            Intent intent = new Intent();
            intent.setClass(getContext(), SetActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);

        setHasOptionsMenu(true);

        View root = inflater.inflate(R.layout.fragment_home, container, false);


        final SharedPreferenceManager spm = new SharedPreferenceManager(getContext());
        spm.checkLogin();
        String pet = spm.getPet();

        final EditText edPetName = root.findViewById(R.id.home_pet_name);
        edPetName.setText(pet);

        final Button button = root.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("myApp", "Click Button!");
                boolean flag = false;
                String newName = edPetName.getText().toString();

                if (button.getText() == "Hello") {
                    flag = false;
                } else {
                    flag = true;
                }

                spm.togglePetShowState(flag);
                spm.savePet(newName);

                Intent intent = new Intent(getActivity(), DesktopSpriteService.class);
                getActivity().startService(intent);
            }
        });

        QMUIProgressBar petHunger= root.findViewById(R.id.pet_hunger);
        QMUIProgressBar petSteps = root.findViewById(R.id.pet_sport);
        QMUIProgressBar petSleep = root.findViewById(R.id.pet_sleep);

        petHunger.setMaxValue(100);
        petHunger.setQMUIProgressBarTextGenerator(new QMUIProgressBar.QMUIProgressBarTextGenerator() {
            @Override
            public String generateText(QMUIProgressBar progressBar, int value, int maxValue) {
                return value + "/" + maxValue;
            }
        });
        petSteps.setMaxValue(10000);
        petSteps.setQMUIProgressBarTextGenerator(new QMUIProgressBar.QMUIProgressBarTextGenerator() {
            @Override
            public String generateText(QMUIProgressBar progressBar, int value, int maxValue) {
                return value + "/" + maxValue + " steps";
            }
        });
        petSleep.setMaxValue(100);
        petSleep.setQMUIProgressBarTextGenerator(new QMUIProgressBar.QMUIProgressBarTextGenerator() {
            @Override
            public String generateText(QMUIProgressBar progressBar, int value, int maxValue) {
                return value + "/" + maxValue;
            }
        });

        petHunger.setProgress(80);
        petSteps.setProgress(0);
        petSleep.setProgress(90);

        return root;
    }
}