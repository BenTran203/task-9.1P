package com.example.lostandfound;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class RemoveItemActivity extends AppCompatActivity {

    TextView itemDetails;
    Button removeButton;
    MyDatabaseHelper myDB;
    int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_item);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        itemDetails = findViewById(R.id.item_details);
        removeButton = findViewById(R.id.remove_button);
        myDB = new MyDatabaseHelper(this);

        // Retrieve itemId and itemDetails from Intent extras
        itemId = getIntent().getIntExtra("ITEM_ID", -1);
        String itemDetailsText = getIntent().getStringExtra("ITEM_DETAILS");
        itemDetails.setText(itemDetailsText);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer deletedRows = myDB.deleteItem(String.valueOf(itemId));
                if (deletedRows > 0) {
                    Toast.makeText(RemoveItemActivity.this, "Item Removed", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RemoveItemActivity.this, "Item not Removed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Close this activity and return to the previous activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
