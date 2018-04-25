package com.tim.pollution.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tim.pollution.R;

public class WeatherDetailActivity extends AppCompatActivity {

    String regionid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);
        regionid=getIntent().getStringExtra("regionid");
    }
}
