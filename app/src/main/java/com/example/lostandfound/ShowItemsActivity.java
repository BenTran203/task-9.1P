package com.example.lostandfound;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;
import android.view.MenuItem;

public class ShowItemsActivity extends AppCompatActivity {

    ListView listView;
    MyDatabaseHelper myDB;
    ArrayList<String> listItem;
    ArrayAdapter<String> adapter;
    ArrayList<Integer> itemIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_items);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        listView = findViewById(R.id.list_view);
        myDB = new MyDatabaseHelper(this);
        listItem = new ArrayList<>();
        itemIds = new ArrayList<>();

        viewData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemId = itemIds.get(position);
                String itemDetails = listItem.get(position);

                Intent intent = new Intent(ShowItemsActivity.this, RemoveItemActivity.class);
                intent.putExtra("ITEM_ID", itemId);
                intent.putExtra("ITEM_DETAILS", itemDetails);
                startActivity(intent);
            }
        });
    }

    private void viewData() {
        Cursor cursor = myDB.getAllItems();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data to show", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                itemIds.add(cursor.getInt(0)); // assuming first column is item ID
                listItem.add(cursor.getString(1) + " " + cursor.getString(2) + " " + cursor.getString(3) + " " + cursor.getString(4) + " " + cursor.getString(5));
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
            listView.setAdapter(adapter);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}