package com.example.bookworm;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * MyListActivity shows the user's saved reading/borrowing list.
 * Displays total fine and warns about overdue books.
 */
public class MyListActivity extends AppCompatActivity {

    private RecyclerView recyclerMyList;
    private TextView textTotalFine;

    private ArrayList<Book> myListBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);

        recyclerMyList = findViewById(R.id.recyclerMyList);
        textTotalFine = findViewById(R.id.textTotalFine);

        myListBooks = BookStorage.loadMyList(this);

        MyListAdapter adapter = new MyListAdapter(this, myListBooks, totalFine -> {
            textTotalFine.setText("Total fine: $" + totalFine);
        });

        recyclerMyList.setLayoutManager(new LinearLayoutManager(this));
        recyclerMyList.setAdapter(adapter);

        int startingFine = MyListAdapter.calculateTotalFine(myListBooks);
        textTotalFine.setText("Total fine: $" + startingFine);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Reload in case it changed
        myListBooks = BookStorage.loadMyList(this);

        // Show late warnings when user returns to this screen
//        MyListAdapter.showLateWarnings(this, myListBooks);

        int fine = MyListAdapter.calculateTotalFine(myListBooks);
        textTotalFine.setText("Total fine: $" + fine);
    }
}