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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

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
        final SharedPreferenceManager spm = new SharedPreferenceManager(this);
        HashMap<String, String> user = spm.getUser();

        if (userEmail.equals("admin@unimelb.edu.au") && userPswd.equals("123456")) {
            String uid = "1";
            spm.saveUser(user.get(spm.NAME),"admin@unimelb.edu.au", "1");
            loginProcess();
        } else {
            HttpCall httpCallPost = new HttpCall();
            httpCallPost.setMethodtype(HttpCall.POST);
            httpCallPost.setUrl("https://mobile.xiyunkey.com/login.php");
            HashMap<String,String> paramsPost = new HashMap<>();
            paramsPost.put("email",userEmail);
            paramsPost.put("pswd", userPswd);
            httpCallPost.setParams(paramsPost);
            new HttpRequest(){
                @Override
                public void onResponse(String response) {
                    super.onResponse(response);
                    Log.w("REGISTER", response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        int registerFlag = obj.getInt("type");
                        if (registerFlag == 1) {
                            JSONObject res = obj.getJSONObject("res");
                            String sName = res.getString("name");
                            String sEmail = res.getString("email");
                            String sID = res.getString("id");
                            spm.saveUser(sName, sEmail, sID);
                            loginProcess();
                        } else {
                            counter --;

                            Info.setText("you still can try " + String.valueOf(counter) + " times");
                            Info.setVisibility(TextView.VISIBLE);
                            if (counter <= 0) {
                                btn_login.setEnabled(false);
                                Info.setText("No more try.");
                            }
                            Toast.makeText(LoginActivity.this, "Fail to register", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.execute(httpCallPost);
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
