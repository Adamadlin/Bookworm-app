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
 * AvailableBooksAdapter renders the available books list.
 * Allows adding a book into the user's "My List" (saved with SharedPreferences).
 */
public class AvailableBooksAdapter extends RecyclerView.Adapter<AvailableBooksAdapter.BookViewHolder> {

    private final Context context;
    private final ArrayList<Book> availableBooks;

    /**
     * Creates the adapter for displaying the available books.
     * @param context Activity/Fragment context used for inflating and Glide.
     * @param availableBooks list of books to render in the RecyclerView.
     */
    public AvailableBooksAdapter(Context context, ArrayList<Book> availableBooks) {
        this.context = context;
        this.availableBooks = availableBooks;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(context).inflate(R.layout.item_book_browse, parent, false);
        return new BookViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book currentBook = availableBooks.get(position);

        holder.textBookTitle.setText(currentBook.getTitle());
        holder.textBookAuthor.setText(currentBook.getAuthor());

        // Load LOCAL drawable cover image (res/drawable) instead of URL.
        Glide.with(context)
                .load(currentBook.getCoverImageResId())
                .into(holder.imageBookCover);

        holder.buttonAddToList.setOnClickListener(v -> addBookToMyList(currentBook));
    }

    @Override
    public int getItemCount() {
        return availableBooks.size();
    }

    /**
     * Adds a selected book into "My List" and persists it via SharedPreferences.
     * Prevents duplicates by book title.
     * @param book selected book to add.
     */
    private void addBookToMyList(Book book) {
        ArrayList<Book> myList = BookStorage.loadMyList(context);

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

        myList.add(book);
        BookStorage.saveMyList(context, myList);

        Toast.makeText(context, "Added to My List!", Toast.LENGTH_SHORT).show();
    }

    /**
     * ViewHolder holds the UI references for one card row.
     */
    static class BookViewHolder extends RecyclerView.ViewHolder {

        ImageView imageBookCover;
        TextView textBookTitle;
        TextView textBookAuthor;
        Button buttonAddToList;

        /**
         * Binds the row views for a single RecyclerView item.
         * @param itemView the inflated row view.
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