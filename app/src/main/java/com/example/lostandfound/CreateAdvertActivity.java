package com.example.lostandfound;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CreateAdvertActivity extends AppCompatActivity {

    EditText name, phone, description, date, location;
    RadioGroup postTypeGroup;
    Button saveButton;
    MyDatabaseHelper myDB;

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

        myDB = new MyDatabaseHelper(this);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = postTypeGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedId);
                String postType = selectedRadioButton.getText().toString();

                boolean isInserted = myDB.insertItem(postType, name.getText().toString(), phone.getText().toString(),
                        description.getText().toString(), date.getText().toString(), location.getText().toString());

                if (isInserted)
                    Toast.makeText(CreateAdvertActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(CreateAdvertActivity.this, "Data not Inserted", Toast.LENGTH_SHORT).show();
            }
        });
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
