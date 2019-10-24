package com.example.desktopsprite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText edName = findViewById(R.id.reg_name);
        final EditText edEmail = findViewById(R.id.reg_email);
        final EditText edPswd = findViewById(R.id.reg_pswd);
        Button btnReg = findViewById(R.id.reg_btn);



        final SharedPreferenceManager spm = new SharedPreferenceManager(this);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String name = edName.getText().toString();
                final String email = edEmail.getText().toString();
                final String pswd = edPswd.getText().toString();

                HttpCall httpCallPost = new HttpCall();
                httpCallPost.setMethodtype(HttpCall.POST);
                httpCallPost.setUrl("https://mobile.xiyunkey.com/register.php");
                HashMap<String,String> paramsPost = new HashMap<>();
                paramsPost.put("name", name);
                paramsPost.put("email",email);
                paramsPost.put("pswd", pswd);
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
                                spm.saveUser(name, email, obj.getJSONObject("res").getString("id"));
                                loginProcess();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Fail to register", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.execute(httpCallPost);

            }
        });

    }

    private void loginProcess() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
    }
}
