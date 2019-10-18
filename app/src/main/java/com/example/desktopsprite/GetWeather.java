package com.example.desktopsprite;

import android.os.AsyncTask;
import android.widget.Toast;
import android.content.Context;
import android.app.Service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

class GetWeather extends AsyncTask <String,Void,String> {

    @Override
    protected String doInBackground(String... params) {
        // TODO Auto-generated method stub
        String result = "";
        HttpURLConnection conn = null;
        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+ URLEncoder.encode(params[0], "UTF-8")+"&APPID=a8545160b5cc4954f46ca20570aef7a6");
            conn = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(conn.getInputStream());
            if (in != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                String line = "";

                while ((line = bufferedReader.readLine()) != null)
                    result += line;
            }
            in.close();
            return result;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            if(conn!=null)
                conn.disconnect();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
    }

}

