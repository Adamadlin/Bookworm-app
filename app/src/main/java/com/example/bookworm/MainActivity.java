package com.example.bookworm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * MainActivity is the primary entry point of the Bookworm application.
 * It provides a simple dashboard with navigation buttons to either browse 
 * the library's catalog or view the user's personal list of saved books.
 */
public class MainActivity extends AppCompatActivity {

    /** Button that navigates to the book browsing screen. */
    private Button buttonBrowseBooks;
    
    /** Button that navigates to the user's personal book list. */
    private Button buttonMyList;

    /**
     * Called when the activity is first created.
     * Sets up the UI components and navigation logic.
     * 
     * @param savedInstanceState A Bundle containing the activity's previously saved state, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Map UI components to their respective layout IDs
        buttonBrowseBooks = findViewById(R.id.buttonBrowseBooks);
        buttonMyList = findViewById(R.id.buttonMyList);

        // Navigation to BrowseBooksActivity
        buttonBrowseBooks.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BrowseBooksActivity.class);
            startActivity(intent);
        });

        // Navigation to MyListActivity
        buttonMyList.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MyListActivity.class);
            startActivity(intent);
        });
    }
}