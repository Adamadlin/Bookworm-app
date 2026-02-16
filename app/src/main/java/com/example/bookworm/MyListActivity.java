package com.example.bookworm;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * MyListActivity displays the collection of books the user has added to their personal list.
 * It provides features to set return reminders, view potential fines for overdue books,
 * and remove books from the list.
 */
public class MyListActivity extends AppCompatActivity {

    /** RecyclerView to display the user's personal book list. */
    private RecyclerView recyclerMyList;
    
    /** TextView to display the calculated total fine for late books. */
    private TextView textTotalFine;

    /** The data set containing the user's books. */
    private ArrayList<Book> myListBooks;

    /**
     * Initializes the activity and sets up the user's personal list view.
     * 
     * @param savedInstanceState A Bundle containing the activity's previously saved state, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);

        // Bind layout components
        recyclerMyList = findViewById(R.id.recyclerMyList);
        textTotalFine = findViewById(R.id.textTotalFine);

        // Load the user's books from storage
        myListBooks = BookStorage.loadMyList(this);

        // Initialize the adapter with a listener to update the fine display when items change
        MyListAdapter adapter = new MyListAdapter(this, myListBooks, totalFine -> {
            textTotalFine.setText("Total fine: $" + totalFine);
        });

        // Configure the RecyclerView
        recyclerMyList.setLayoutManager(new LinearLayoutManager(this));
        recyclerMyList.setAdapter(adapter);

        // Calculate and display the initial total fine
        int startingFine = MyListAdapter.calculateTotalFine(myListBooks);
        textTotalFine.setText("Total fine: $" + startingFine);
    }

    /**
     * Refresh data and check for overdue books whenever the activity returns to the foreground.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Reload the list in case it was modified in another part of the app
        myListBooks = BookStorage.loadMyList(this);

        // Display late warnings for any books that have passed their return date
        MyListAdapter.showLateWarnings(this, myListBooks);

        // Re-calculate the total fine
        int fine = MyListAdapter.calculateTotalFine(myListBooks);
        textTotalFine.setText("Total fine: $" + fine);
    }
}
