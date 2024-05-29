package com.example.lostandfound;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MyDatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_display);

        myDB = new MyDatabaseHelper(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        loadAllMarkers();
    }

    private void loadAllMarkers() {
        Cursor cursor = myDB.getAllLocations();
        if (cursor != null && cursor.moveToFirst()) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            do {
                int latIndex = cursor.getColumnIndex(MyDatabaseHelper.COLUMN_LATITUDE);
                int lonIndex = cursor.getColumnIndex(MyDatabaseHelper.COLUMN_LONGITUDE);
                int locIndex = cursor.getColumnIndex(MyDatabaseHelper.COLUMN_LOCATION);

                if (latIndex != -1 && lonIndex != -1 && locIndex != -1) {
                    double latitude = cursor.getDouble(latIndex);
                    double longitude = cursor.getDouble(lonIndex);
                    String location = cursor.getString(locIndex);

                    if (latitude != 0 && longitude != 0) {
                        LatLng latLng = new LatLng(latitude, longitude);
                        mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                        builder.include(latLng);
                    }
                } else {
                    Log.e("MapActivity", "Column index not found.");
                }
            } while (cursor.moveToNext());

            cursor.close();

            // Move the camera to show all markers
            LatLngBounds bounds = builder.build();
            int padding = 100; // offset from edges of the map in pixels
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
        }
    }
}
