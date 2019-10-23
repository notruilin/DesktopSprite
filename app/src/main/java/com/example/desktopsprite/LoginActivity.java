package com.example.desktopsprite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private EditText Email;
    private EditText Pswd;
    private TextView Info;
    private Button btn_login;
    private Button btn_signup;
    private Integer counter = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferenceManager spm = new SharedPreferenceManager(this);
        if (spm.isLogin()) {
            loginProcess();
        }

        Email = (EditText)findViewById(R.id.edEmail);
        Pswd = (EditText)findViewById(R.id.edPswd);
        Info = (TextView) findViewById(R.id.loginInfo);
        btn_login = (Button) findViewById(R.id.btnLogin);
        btn_signup = (Button) findViewById(R.id.btnSignup);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Email.getText().toString(), Pswd.getText().toString());
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

    }

    private void validate(String userEmail, String userPswd) {
        Log.w("validate", "validate: " + userEmail + " ," + userPswd);
        SharedPreferenceManager spm = new SharedPreferenceManager(this);
        HashMap<String, String> user = spm.getUser();

        //pswd
        if (userEmail.equals("admin@unimelb.edu.au") && userPswd.equals("123456")) {
            String uid = "1";
            spm.saveUser(user.get(spm.NAME),"admin@unimelb.edu.au", "1");
            loginProcess();
        } else if (Boolean.FALSE) {
            Log.w("validate", "validate: " + userEmail + " ," + userPswd);
        } else {
            counter --;

            Info.setText("you still can try " + String.valueOf(counter) + " times");
            Info.setVisibility(TextView.VISIBLE);
            if (counter <= 0) {
                btn_login.setEnabled(false);
                Info.setText("No more try.");
            }
        }
    }

    private void signUp() {
        Intent intent = new Intent();
        intent.setClass(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void loginProcess() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
    }
}
