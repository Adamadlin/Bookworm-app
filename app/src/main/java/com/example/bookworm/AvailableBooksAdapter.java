package com.example.bookworm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * AvailableBooksAdapter renders the available books list in a RecyclerView.
 * This adapter is responsible for displaying the library's catalog and allowing
 * users to add books to their personal list, which is persisted using SharedPreferences.
 */
public class AvailableBooksAdapter extends RecyclerView.Adapter<AvailableBooksAdapter.BookViewHolder> {

    /** Context used for inflating layouts, displaying Toasts, and loading images with Glide. */
    private final Context context;
    
    /** The list of books available for browsing. */
    private final ArrayList<Book> availableBooks;

    /**
     * Constructs a new AvailableBooksAdapter.
     * 
     * @param context        The Activity or Fragment context.
     * @param availableBooks The list of Book objects to be displayed.
     */
    public AvailableBooksAdapter(Context context, ArrayList<Book> availableBooks) {
        this.context = context;
        this.availableBooks = availableBooks;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     * 
     * @param parent   The ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return A new BookViewHolder that holds the View for each book item.
     */
    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout (CardView) for each book in the browse list
        View rowView = LayoutInflater.from(context).inflate(R.layout.item_book_browse, parent, false);
        return new BookViewHolder(rowView);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * 
     * @param holder   The ViewHolder which should be updated to represent the contents of the item.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        // Get the book data for the current position
        Book currentBook = availableBooks.get(position);

        // Set the title and author text views
        holder.textBookTitle.setText(currentBook.getTitle());
        holder.textBookAuthor.setText(currentBook.getAuthor());

        // Use Glide to efficiently load the local drawable image into the ImageView
        Glide.with(context)
                .load(currentBook.getCoverImageResId())
                .into(holder.imageBookCover);

        // Set up the click listener for the "Add to List" button
        holder.buttonAddToList.setOnClickListener(v -> addBookToMyList(currentBook));
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     * 
     * @return The size of the availableBooks list.
     */
    @Override
    public int getItemCount() {
        return availableBooks.size();
    }

    /**
     * Adds a selected book to the user's personal list and saves it to SharedPreferences.
     * This method performs a check to ensure that duplicate books are not added.
     * 
     * @param book The book object selected by the user.
     */
    private void addBookToMyList(Book book) {
        // Load the current list from storage
        ArrayList<Book> myList = BookStorage.loadMyList(context);

        // Check if the book is already in the list (comparison by title)
        boolean alreadyExists = false;
        for (Book b : myList) {
            if (b.getTitle().equalsIgnoreCase(book.getTitle())) {
                alreadyExists = true;
                break;
            }
        }

        if (alreadyExists) {
            Toast.makeText(context, "This book is already in your list.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add the new book and persist the updated list
        myList.add(book);
        BookStorage.saveMyList(context, myList);

        Toast.makeText(context, "Added to My List!", Toast.LENGTH_SHORT).show();
    }

    /**
     * BookViewHolder provides a reference to the views for each data item.
     * It caches the View references to avoid frequent findViewById calls.
     */
    public static class BookViewHolder extends RecyclerView.ViewHolder {

        /** ImageView for the book cover image. */
        ImageView imageBookCover;
        
        /** TextView for the book title. */
        TextView textBookTitle;
        
        /** TextView for the book author. */
        TextView textBookAuthor;
        
        /** Button to add the book to the user's list. */
        Button buttonAddToList;

        /**
         * Initializes the ViewHolder by finding and storing references to the UI components.
         * 
         * @param itemView The root view of the item layout.
         */
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            imageBookCover = itemView.findViewById(R.id.imageBookCover);
            textBookTitle = itemView.findViewById(R.id.textBookTitle);
            textBookAuthor = itemView.findViewById(R.id.textBookAuthor);
            buttonAddToList = itemView.findViewById(R.id.buttonAddToList);
        }
    }
}