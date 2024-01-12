package com.example.ufcproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;

public class MapsFragment extends Fragment {


    String mapTitle;
    double lattitude,longitude;
    int mapsRandom = (int)(Math.random()*5);
    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            switch (mapsRandom){
                case 0: mapTitle = "Madison Square Garden"; lattitude = 40.7505129; longitude = -73.9935159; break;
                case 1: mapTitle = "T-MOBILE ARENA"; lattitude = 36.108465; longitude =  -115.173078; break;
                case 2: mapTitle = "UFC APEX"; lattitude = 36.0690085; longitude = -115.2288223; break;
                case 3: mapTitle = "Dome du Millenaire"; lattitude = 51.502213; longitude = 0.002244; break;
                case 4: mapTitle = "TOYOTA CENTER"; lattitude = 29.7519514; longitude = -95.3622851; break;
            }
            LatLng place = new LatLng(lattitude, longitude);
            googleMap.addMarker(new MarkerOptions().position(place).title(mapTitle));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(place));
            CameraPosition cameraPosition = new CameraPosition.Builder().target(place).zoom(11.5f).build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            googleMap.moveCamera(cameraUpdate);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}