package org.iot.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView temp;
    TextView hmi;
    TextView isRain;
    TextView isPerson;
    String baseUrl = "http://172.20.10.7:8000/";
    static RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        temp = findViewById(R.id.temp);
        hmi = findViewById(R.id.hmi);
        isRain = findViewById(R.id.isRain);
        isPerson = findViewById(R.id.isPerson);
        getWindowInformation();

        Button openButton = findViewById(R.id.open);

        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("button", "press");
                setWindowOpen();
            }
        });

    }

    public void getWindowInformation(){
        String url = "window/inf/1/?format=json";
        StringRequest request = new StringRequest(Request.Method.GET, baseUrl + url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Log.d("WindowInfo", response);
                        processResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("WindowInfo", error.getMessage());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        request.setShouldCache(false);
        requestQueue.add(request);
    }

    public void setWindowOpen(){
        Log.d("WindowInfo", "hi");
        String url = "window/inf/1/open/";
        StringRequest request = new StringRequest(Request.Method.GET, baseUrl + url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("WindowOpen", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("WindowOpen", error.getMessage());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        request.setShouldCache(false);
        requestQueue.add(request);
    }

    public void processResponse(String response){
        Gson gson = new Gson();
        Window window = gson.fromJson(response, Window.class);
        Log.d("WindowInfo", window.temperature);
        Log.d("WindowInfo", window.humidity);
        Log.d("WindowInfo", window.is_rain);
        Log.d("WindowInfo", window.is_person);

        temp.setText(window.temperature.toString());
        hmi.setText(window.humidity.toString());
        isRain.setText(window.is_rain.toString());
        isPerson.setText(window.is_person.toString());
    }
}
