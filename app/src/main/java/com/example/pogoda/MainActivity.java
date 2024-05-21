package com.example.pogoda;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    EditText etCity;
    ImageView imageView;
    TextView tx1,tx2,tx3,tx4,tx5, dane;
    private final String url = "https://api.openweathermap.org/data/2.5/forecast";
    private final String appid = "94e43de29fe647389ba802cda8eefdde";
    DecimalFormat df = new DecimalFormat("#.##");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etCity = findViewById(R.id.editText);
        tx1 = findViewById(R.id.textView4);
        tx2 = findViewById(R.id.textView5);
        tx3 = findViewById(R.id.textView6);
        tx4 = findViewById(R.id.textView7);
        tx5 = findViewById(R.id.textView8);
        dane = findViewById(R.id.textView3);
        imageView = (ImageView) findViewById(R.id.imageView);

    }

    public void getWeatherDetails(View view)
    {
        String napis = ((Button) view).getText().toString();
        String tempUrl ="";
        String city = etCity.getText().toString().trim();
        if (city.equals(""))
        {
            dane.setText("Miasto nie może być puste");
        }
        else
        {
            tempUrl = url + "?q=" + city + "," + "" + "&cnt=9" + "&units=metric" + "&appid=" + appid;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JSONObject jsonResponse = null;
                    try {

                        jsonResponse = new JSONObject(response);
                        JSONArray jsonArrayList = jsonResponse.getJSONArray("list");
                        JSONObject jsonObjectList;
                       if (napis.equals("Dzisiaj"))
                       {
                           jsonObjectList = jsonArrayList.getJSONObject(0);
                           dane.setText("Dzisiejsza tempertatura w " + city);
                           Log.d("dzisiaj", response);
                       }
                       else
                       {
                           jsonObjectList = jsonArrayList.getJSONObject(8);
                           dane.setText("Tempertatura w " + city + " jutro");
                           Log.d("jutro", response);
                       }
                        JSONArray jsonArray = jsonObjectList.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        String condition = jsonObjectWeather.getString("main");
                        JSONObject jsonObjectMain = jsonObjectList.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp");
                        double feelsLike = jsonObjectMain.getDouble("feels_like");
                        int humidity = jsonObjectMain.getInt("humidity");
                        float pressure = jsonObjectMain.getInt("pressure");
                        JSONObject jsonObjectWind = jsonObjectList.getJSONObject("wind");
                        String wind = jsonObjectWind.getString("speed");
                        if (condition.equals("Clouds"))
                        {
                        imageView.setBackgroundResource(R.drawable.cloud);
                        }
                        else if (condition.equals("Thunderstorm"))
                        {
                            imageView.setBackgroundResource(R.drawable.thunderstorm);
                        }
                        else if (condition.equals("Drizzle"))
                        {
                            imageView.setBackgroundResource(R.drawable.drizzle);
                        }
                        else if (condition.equals("Rain"))
                        {
                            imageView.setBackgroundResource(R.drawable.raining);
                        }
                        else if (condition.equals("Snow"))
                        {
                            imageView.setBackgroundResource(R.drawable.snowicon);
                        }
                        else if (condition.equals("Mist"))
                        {
                            imageView.setBackgroundResource(R.drawable.mist);
                        }
                        else if (condition.equals("Fog"))
                        {
                            imageView.setBackgroundResource(R.drawable.fog);
                        }
                        else if (condition.equals("Clear"))
                        {
                            imageView.setBackgroundResource(R.drawable.sun);
                        }
                        tx1.setText(String.valueOf("Temperatura: " + temp + " ℃"));
                        tx2.setText(String.valueOf("Odczuwalna: " + feelsLike + " ℃"));
                        tx3.setText(String.valueOf("Wilgotność: " + humidity + "%"));
                        tx4.setText(String.valueOf("Prędkość wiatru: " + wind  + " m/s"));
                        tx5.setText(String.valueOf("Ciśnienie: " + pressure + " hPa"));



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}