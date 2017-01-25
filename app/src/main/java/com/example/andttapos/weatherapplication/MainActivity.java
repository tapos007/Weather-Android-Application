package com.example.andttapos.weatherapplication;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView resultText;
    public void getWeatherData(View view){
        InputMethodManager mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editText.getWindowToken(),0);
        String cityName = editText.getText().toString();

        GetWeatherInfo info = new GetWeatherInfo();
        try {
            String encodedCityName = URLEncoder.encode(cityName,"UTF-8");
            info.execute("http://api.openweathermap.org/data/2.5/weather?q="+encodedCityName+"&appid=978aff26eae6792808d93345a8f18efc").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public class GetWeatherInfo extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {

            URL url;
            String result = "";
            try {
                url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data!=-1){
                    char info = (char) data;
                    result +=info;
                    data = reader.read();

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("result",result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String msg = "";
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray weatherInfo = jsonObject.getJSONArray("weather");

                for (int i = 0; i < weatherInfo.length(); i++) {
                    JSONObject info = weatherInfo.getJSONObject(i);
                    msg = info.getString("main") + ": "+info.getString("description");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            resultText.setText(msg);


        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.cityEditText);
        editText.setVisibility(View.VISIBLE);
        resultText = (TextView) findViewById(R.id.resultTextView);

    }
}
