package com.example.desktopsprite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.desktopsprite.ui.home.HomeFragment;

import java.util.HashMap;

public class SetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        final EditText edUserName = findViewById(R.id.home_user_name);
        final EditText edUserEmail = findViewById(R.id.home_user_email);
        final SharedPreferenceManager spm = new SharedPreferenceManager(this);

        HashMap<String, String> user = spm.getUser();
        edUserName.setText(user.get(spm.NAME));
        edUserEmail.setText(user.get(spm.EMAIL));

        Button btn_save = findViewById(R.id.btn_save_user);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SetActivity.this, "Save", Toast.LENGTH_SHORT).show();

                String userName = edUserName.getText().toString();
                String userEmail = edUserEmail.getText().toString();

                spm.saveUser(userName, userEmail);

                Intent intent = new Intent(SetActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SetActivity.this, "logout", Toast.LENGTH_SHORT).show();
                spm.logout();
            }
        });
    }
}
