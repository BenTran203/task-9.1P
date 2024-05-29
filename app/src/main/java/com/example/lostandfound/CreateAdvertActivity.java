package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class CreateAdvertActivity extends AppCompatActivity {

    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    EditText name, phone, description, date, location;
    RadioGroup postTypeGroup;
    Button saveButton, pinLocationButton;
    MyDatabaseHelper myDB;
    private LatLng selectedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        description = findViewById(R.id.description);
        date = findViewById(R.id.date);
        location = findViewById(R.id.location);
        postTypeGroup = findViewById(R.id.post_type_group);
        saveButton = findViewById(R.id.save_button);
        pinLocationButton = findViewById(R.id.pin_location_button);

        myDB = new MyDatabaseHelper(this);

        // Initialize Places API
        Places.initialize(getApplicationContext(), "AIzaSyBZLOCRrMbEv445HOuAayng2I3LPj_DQAI");

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start autocomplete intent
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(CreateAdvertActivity.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });

        pinLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedLocation != null) {
                    // Pass coordinates to MapActivity
                    Intent intent = new Intent(CreateAdvertActivity.this, MapActivity.class);
                    intent.putExtra("latitude", selectedLocation.latitude);
                    intent.putExtra("longitude", selectedLocation.longitude);
                    intent.putExtra("location_name", location.getText().toString()); // Pass location name
                    startActivity(intent);
                } else {
                    Toast.makeText(CreateAdvertActivity.this, "Please select a location first.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = postTypeGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedId);
                String postType = selectedRadioButton.getText().toString();

                double latitude = selectedLocation != null ? selectedLocation.latitude : 0.0;
                double longitude = selectedLocation != null ? selectedLocation.longitude : 0.0;

                boolean isInserted = myDB.insertItem(postType, name.getText().toString(), phone.getText().toString(),
                        description.getText().toString(), date.getText().toString(), location.getText().toString(), latitude, longitude);

                if (isInserted)
                    Toast.makeText(CreateAdvertActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(CreateAdvertActivity.this, "Data not Inserted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                selectedLocation = place.getLatLng();
                location.setText(place.getName());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.e("CreateAdvertActivity", "An error occurred: " + status.getStatusMessage());
                Toast.makeText(this, "Error: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                Log.i("CreateAdvertActivity", "Autocomplete canceled.");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Close this activity and return to the previous activity (MainActivity)
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
