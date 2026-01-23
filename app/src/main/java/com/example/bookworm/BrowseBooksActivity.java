package com.example.bookworm;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * BrowseBooksActivity shows available library books using RecyclerView.
 */
public class BrowseBooksActivity extends AppCompatActivity {

    private RecyclerView recyclerAvailableBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_books);

        recyclerAvailableBooks = findViewById(R.id.recyclerAvailableBooks);

        ArrayList<Book> availableBooks = BookRepository.getAvailableBooks();

        AvailableBooksAdapter adapter = new AvailableBooksAdapter(this, availableBooks);
        recyclerAvailableBooks.setLayoutManager(new LinearLayoutManager(this));
        recyclerAvailableBooks.setAdapter(adapter);
    }
}