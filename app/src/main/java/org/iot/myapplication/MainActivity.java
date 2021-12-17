package org.iot.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    TextView wishTemp;
    TextView wishHum;
    TextView openState;
    TextView lockState;

    EditText wishTempEdit;
    EditText wishHumEdit;

    Button openButton;
    Button closeButton;
    Button adjustButton;
    Button syncButton;
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
        wishTempEdit = findViewById(R.id.wishTempEdit);
        wishHumEdit = findViewById(R.id.wishHumEdit);
        openState = findViewById(R.id.openState);
        lockState = findViewById(R.id.lockState);

        wishTemp = findViewById(R.id.wishTemp);
        wishHum = findViewById(R.id.wishHum);

        getWindowInformation();

        openButton = findViewById(R.id.open);
        closeButton = findViewById(R.id.close);
        adjustButton = findViewById(R.id.adjust);
        syncButton = findViewById(R.id.sync);

        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("openButton", "press");
                setWindowOpen();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("closeButton", "press");
                setWindowClose();
            }
        });


        adjustButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("adjustButton", "press");
                adjustWindowInfo();
            }
        });

        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("syncButton", "press");
                getWindowInformation();
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
        String url = "window/inf/1/open/";
        StringRequest request = new StringRequest(Request.Method.GET, baseUrl + url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("WindowOpen", response);
                        Toast.makeText(getApplicationContext(),
                                "창문을 열었습니다.",
                                Toast.LENGTH_SHORT).show();
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
        openState.setText("true");
    }

    public void setWindowClose(){
        String url = "window/inf/1/close/";
        StringRequest request = new StringRequest(Request.Method.GET, baseUrl + url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("WindowOpen", response);
                        Toast.makeText(getApplicationContext(),
                                "창문을 닫았습니다.",
                                Toast.LENGTH_SHORT).show();
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
        openState.setText("false");
    }

    public void adjustWindowInfo(){
        String wt = wishTempEdit.getText().toString();
        String wh = wishHumEdit.getText().toString();
        String url = "window/inf/1/";
        StringRequest request = new StringRequest(Request.Method.GET,
                baseUrl + url + wt + "/" + wh + "/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("WindowOpen", response);
                        Toast.makeText(getApplicationContext(),
                                "온 습도를 수정했습니다",
                                Toast.LENGTH_SHORT).show();
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
        wishTemp.setText(wt + ".0");
        wishHum.setText(wh + ".0");
    }

    public void processResponse(String response){
        Gson gson = new Gson();
        Window window = gson.fromJson(
                response, Window.class
        );
        Log.d("API REQ", "temperature : " + window.temperature.toString());
        Log.d("API REQ", "humidity : " + window.humidity.toString());
        Log.d("API REQ", "is_rain : " + window.is_rain.toString());
        Log.d("API REQ", "is_person : " + window.is_person.toString());
        Log.d("API REQ", "wishing_temp : " + window.wishing_temp.toString());
        Log.d("API REQ", "wishing_hum : " + window.wishing_hum.toString());
        Log.d("API REQ", "is_open : " + window.is_open.toString());
        Log.d("API REQ", "is_lock : " + window.is_lock.toString());

        temp.setText(window.temperature.toString());
        hmi.setText(window.humidity.toString());
        isRain.setText(window.is_rain.toString());
        isPerson.setText(window.is_person.toString());
        wishTemp.setText(window.wishing_temp.toString());
        wishHum.setText(window.wishing_hum.toString());
        openState.setText(window.is_open.toString());
        lockState.setText(window.is_lock.toString());
    }
}
