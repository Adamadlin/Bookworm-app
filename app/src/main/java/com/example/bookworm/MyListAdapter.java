package com.example.bookworm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * MyListAdapter is responsible for rendering the books in the user's personal collection.
 * It provides the interface for setting return dates, triggering reminders, 
 * and removing books from the list. It also includes logic for fine calculation.
 */
public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.MyListViewHolder> {

    /**
     * Interface for communicating fine changes back to the hosting activity.
     */
    public interface FineChangedListener {
        /**
         * Triggered whenever a change in the book list affects the total fine.
         * 
         * @param totalFineDollars The updated total fine amount.
         */
        void onFineChanged(int totalFineDollars);
    }

    /** Context for inflating layouts and displaying UI notifications. */
    private final Context context;
    
    /** The list of books in the user's personal collection. */
    private final ArrayList<Book> myListBooks;
    
    /** Listener for updating the UI when the fine calculation changes. */
    private final FineChangedListener fineChangedListener;

    /**
     * Constructs the MyListAdapter.
     * 
     * @param context             The application context.
     * @param myListBooks         The user's personal book list.
     * @param fineChangedListener A listener to handle fine updates.
     */
    public MyListAdapter(Context context, ArrayList<Book> myListBooks, FineChangedListener fineChangedListener) {
        this.context = context;
        this.myListBooks = myListBooks;
        this.fineChangedListener = fineChangedListener;
    }

    /**
     * Inflates the layout for individual book items in the user's list.
     * 
     * @param parent   The parent ViewGroup.
     * @param viewType The view type ID.
     * @return A new MyListViewHolder instance.
     */
    @NonNull
    @Override
    public MyListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(context).inflate(R.layout.item_book_my_list, parent, false);
        return new MyListViewHolder(rowView);
    }

    /**
     * Binds book data to the ViewHolder and sets up user interactions.
     * 
     * @param holder   The ViewHolder to be updated.
     * @param position The position of the item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull MyListViewHolder holder, int position) {
        Book currentBook = myListBooks.get(position);

        // Update basic book information
        holder.textMyListTitle.setText(currentBook.getTitle());
        holder.textMyListAuthor.setText(currentBook.getAuthor());

        // Initialize the DatePicker if a return date has already been set
        if (currentBook.getReturnDateMillis() > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(currentBook.getReturnDateMillis());
            holder.datePickerReturn.updateDate(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            
            // To prevent multiple conflicting reminders, the UI is locked once a date is set
            holder.buttonSetReminder.setEnabled(false);
            holder.buttonSetReminder.setText(R.string.reminder_set);
            holder.datePickerReturn.setEnabled(false);
        } else {
            // Allow setting a reminder if one hasn't been established yet
            holder.buttonSetReminder.setEnabled(true);
            holder.buttonSetReminder.setText(R.string.set_up_a_reminder);
            holder.datePickerReturn.setEnabled(true);
        }

        // Logic for setting a return date reminder
        holder.buttonSetReminder.setOnClickListener(v -> {
            // Convert DatePicker selection into milliseconds
            long selectedReturnDateMillis = getDatePickerMillis(holder.datePickerReturn);
            currentBook.setReturnDateMillis(selectedReturnDateMillis);

            // Persist the updated book list
            BookStorage.saveMyList(context, myListBooks);

            Toast.makeText(context, "Reminder saved. We'll warn you if you're late.", Toast.LENGTH_SHORT).show();

            // Notify the activity that the total fine might have changed
            int fine = calculateTotalFine(myListBooks);
            fineChangedListener.onFineChanged(fine);
            
            // Refresh this item to reflect the "Reminder Set" state
            notifyItemChanged(holder.getAbsoluteAdapterPosition());
        });

        // Logic for returning a book (removing it from the list)
        holder.buttonReturnBook.setOnClickListener(v -> {
            int currentPos = holder.getAbsoluteAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                // Remove the book from the collection and update storage
                myListBooks.remove(currentPos);
                BookStorage.saveMyList(context, myListBooks);
                notifyItemRemoved(currentPos);
                
                // Recalculate total fine after book removal
                int fine = calculateTotalFine(myListBooks);
                fineChangedListener.onFineChanged(fine);

                Toast.makeText(context, "Book returned and removed from your list.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Returns the size of the user's book list.
     * 
     * @return Item count.
     */
    @Override
    public int getItemCount() {
        return myListBooks.size();
    }

    /**
     * Converts the current selection in a DatePicker into epoch milliseconds.
     * Time components (hours, minutes, seconds) are reset to zero for consistent date comparison.
     * 
     * @param datePicker The DatePicker instance.
     * @return Epoch milliseconds representing the selected date.
     */
    private long getDatePickerMillis(DatePicker datePicker) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, datePicker.getYear());
        calendar.set(Calendar.MONTH, datePicker.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * Static utility for calculating the total late fine across all books.
     * Rule: Any book overdue by 10 days or more incurs a flat $10 fine.
     * 
     * @param books The list of user books.
     * @return The total fine in dollars.
     */
    public static int calculateTotalFine(ArrayList<Book> books) {
        int totalFine = 0;
        long nowMillis = System.currentTimeMillis();

        for (Book book : books) {
            long returnMillis = book.getReturnDateMillis();
            if (returnMillis <= 0) continue; // Skip books without an established return date

            // Calculate the difference in time and convert it to days
            long diffMillis = nowMillis - returnMillis;
            long diffDays = diffMillis / (1000L * 60L * 60L * 24L);

            // Apply fine if overdue by at least 10 full days
            if (diffDays >= 10) {
                totalFine += 10;
            }
        }

        return totalFine;
    }

    /**
     * Static utility that displays Toast notifications for books that are past their return date.
     * 
     * @param context The application context.
     * @param books   The user's book collection.
     */
    public static void showLateWarnings(Context context, ArrayList<Book> books) {
        long nowMillis = System.currentTimeMillis();

        for (Book book : books) {
            long returnMillis = book.getReturnDateMillis();
            if (returnMillis <= 0) continue;

            // Check if current system time has passed the return date
            if (nowMillis > returnMillis) {
                Toast.makeText(context,
                        "Late return: " + book.getTitle(),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * ViewHolder for user list items. Caches view references for performance.
     */
    public static class MyListViewHolder extends RecyclerView.ViewHolder {

        /** Title of the book. */
        TextView textMyListTitle;
        
        /** Author of the book. */
        TextView textMyListAuthor;
        
        /** DatePicker for selecting the return date. */
        DatePicker datePickerReturn;
        
        /** Button to confirm the return date reminder. */
        Button buttonSetReminder;
        
        /** Button to return (remove) the book. */
        Button buttonReturnBook;

        /**
         * Initializes the ViewHolder by mapping layout IDs to members.
         * 
         * @param itemView The root view of the item layout.
         */
        public MyListViewHolder(@NonNull View itemView) {
            super(itemView);

            textMyListTitle = itemView.findViewById(R.id.textMyListTitle);
            textMyListAuthor = itemView.findViewById(R.id.textMyListAuthor);
            datePickerReturn = itemView.findViewById(R.id.datePickerReturn);
            buttonSetReminder = itemView.findViewById(R.id.buttonSetReminder);
            buttonReturnBook = itemView.findViewById(R.id.buttonReturnBook);
        }
    }
}
