package com.example.bookworm;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * BrowseBooksActivity displays a catalog of all available books in the library.
 * Users can scroll through the list and select books to add to their personal "My List".
 */
public class BrowseBooksActivity extends AppCompatActivity {

    /** The RecyclerView that holds the list of available books. */
    private RecyclerView recyclerAvailableBooks;

    /**
     * Initializes the activity, sets the content view, and configures the RecyclerView.
     * 
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_books);

        // Find the RecyclerView in the layout
        recyclerAvailableBooks = findViewById(R.id.recyclerAvailableBooks);

        // Fetch the list of available books from the repository
        ArrayList<Book> availableBooks = BookRepository.getAvailableBooks();

        // Initialize the adapter with the book data
        AvailableBooksAdapter adapter = new AvailableBooksAdapter(this, availableBooks);
        
        // Use a LinearLayoutManager to arrange items in a vertical list
        recyclerAvailableBooks.setLayoutManager(new LinearLayoutManager(this));
        
        // Attach the adapter to the RecyclerView to populate the list
        recyclerAvailableBooks.setAdapter(adapter);
    }
}