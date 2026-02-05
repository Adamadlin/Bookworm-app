package com.example.bookworm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * MainActivity is the entry screen.
 * Lets user navigate to browse books or view their list.
 */
public class MainActivity extends AppCompatActivity {

    private Button buttonBrowseBooks;
    private Button buttonMyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonBrowseBooks = findViewById(R.id.buttonBrowseBooks);
        buttonMyList = findViewById(R.id.buttonMyList);

        buttonBrowseBooks.setOnClickListener(v -> {
          //  Toast.makeText(this, "Opening available books...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, BrowseBooksActivity.class);
            startActivity(intent);
        });

        buttonMyList.setOnClickListener(v -> {
        //    Toast.makeText(this, "Opening your list...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, MyListActivity.class);
            startActivity(intent);
        });
    }
}