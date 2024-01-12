package com.example.ufcproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MapActivity extends AppCompatActivity {

    private MapsFragment mapsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapsFragment = new MapsFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.FrameMap, mapsFragment).commit();


    }
}