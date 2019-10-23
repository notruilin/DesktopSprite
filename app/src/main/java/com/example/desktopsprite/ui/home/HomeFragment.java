package com.example.desktopsprite.ui.home;

import com.example.desktopsprite.*;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final SharedPreferenceManager spm = new SharedPreferenceManager(getContext());
        spm.checkLogin();
        String pet = spm.getPet();
        HashMap<String, String> user = spm.getUser();
        final EditText edUserName = root.findViewById(R.id.home_user_name);
        final EditText edUserEmail = root.findViewById(R.id.home_user_email);
        edUserName.setText(user.get(spm.NAME));
        edUserEmail.setText(user.get(spm.EMAIL));

        final EditText edPetName = root.findViewById(R.id.home_pet_name);
        edPetName.setText(pet);

        final Button button = root.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("myApp", "Click Button!");
                boolean flag = false;
                String newName = edPetName.getText().toString();

                if (button.getText() == "Summon") {
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

        Button btn_save = root.findViewById(R.id.btn_save_user);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Save", Toast.LENGTH_SHORT).show();

                String userName = edUserName.getText().toString();
                String userEmail = edUserEmail.getText().toString();


                spm.saveUser(userName, userEmail);
            }
        });

        Button btn_logout = root.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "logout", Toast.LENGTH_SHORT).show();

                spm.logout();
            }
        });

        return root;
    }
}