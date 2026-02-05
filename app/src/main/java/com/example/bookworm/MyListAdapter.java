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
 * MyListAdapter renders the user's selected books.
 * Allows setting return date and showing reminder messages (Toast).
 */
public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.MyListViewHolder> {

    public interface FineChangedListener {
        void onFineChanged(int totalFineDollars);
    }

    private final Context context;
    private final ArrayList<Book> myListBooks;
    private final FineChangedListener fineChangedListener;

    public MyListAdapter(Context context, ArrayList<Book> myListBooks, FineChangedListener fineChangedListener) {
        this.context = context;
        this.myListBooks = myListBooks;
        this.fineChangedListener = fineChangedListener;
    }

    @NonNull
    @Override
    public MyListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(context).inflate(R.layout.item_book_my_list, parent, false);
        return new MyListViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyListViewHolder holder, int position) {
        Book currentBook = myListBooks.get(position);

        holder.textMyListTitle.setText(currentBook.getTitle());
        holder.textMyListAuthor.setText(currentBook.getAuthor());

        // If a return date was previously saved, initialize DatePicker to it
        if (currentBook.getReturnDateMillis() > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(currentBook.getReturnDateMillis());
            holder.datePickerReturn.updateDate(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            // Requirement 2: User can only set one date reminder per book.
            // If already set, we disable the ability to set it again.
            holder.buttonSetReminder.setEnabled(false);
            holder.buttonSetReminder.setText("Reminder Set");
            holder.datePickerReturn.setEnabled(false);
        } else {
            holder.buttonSetReminder.setEnabled(true);
            holder.buttonSetReminder.setText("Set up a reminder");
            holder.datePickerReturn.setEnabled(true);
        }

        holder.buttonSetReminder.setOnClickListener(v -> {
            long selectedReturnDateMillis = getDatePickerMillis(holder.datePickerReturn);
            currentBook.setReturnDateMillis(selectedReturnDateMillis);

            BookStorage.saveMyList(context, myListBooks);

            Toast.makeText(context, "Reminder saved. We'll warn you if you're late.", Toast.LENGTH_SHORT).show();

            int fine = calculateTotalFine(myListBooks);
            fineChangedListener.onFineChanged(fine);
            
            // Disable after setting
            notifyItemChanged(holder.getAdapterPosition());
        });

        holder.buttonReturnBook.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                // Requirement 1: Remove book from list and "permanent notification" (handled by removal)
                myListBooks.remove(currentPos);
                BookStorage.saveMyList(context, myListBooks);
                notifyItemRemoved(currentPos);
                
                // Update fine
                int fine = calculateTotalFine(myListBooks);
                fineChangedListener.onFineChanged(fine);

                Toast.makeText(context, "Book returned and removed from your list.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return myListBooks.size();
    }

    /**
     * Converts DatePicker selected date to epoch millis.
     * @param datePicker DatePicker widget
     * @return selected date as millis
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
     * Calculates total fine across all books.
     * Rule: if return date passed by 10+ days => $10 fine per late book.
     * @param books list of user books
     * @return total fine dollars
     */
    public static int calculateTotalFine(ArrayList<Book> books) {
        int totalFine = 0;
        long nowMillis = System.currentTimeMillis();

        for (Book book : books) {
            long returnMillis = book.getReturnDateMillis();
            if (returnMillis <= 0) continue; // no date set, no fine

            long diffMillis = nowMillis - returnMillis;
            long diffDays = diffMillis / (1000L * 60L * 60L * 24L);

            if (diffDays >= 10) {
                totalFine += 10;
            }
        }

        return totalFine;
    }

    /**
     * Shows a warning Toast for books whose return date already passed.
     * Used when user enters My List screen.
     * @param books list of user books
     */
    public static void showLateWarnings(Context context, ArrayList<Book> books) {
        long nowMillis = System.currentTimeMillis();

        for (Book book : books) {
            long returnMillis = book.getReturnDateMillis();
            if (returnMillis <= 0) continue;

            if (nowMillis > returnMillis) {
                Toast.makeText(context,
                        "Late return: " + book.getTitle(),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    static class MyListViewHolder extends RecyclerView.ViewHolder {

        TextView textMyListTitle;
        TextView textMyListAuthor;
        DatePicker datePickerReturn;
        Button buttonSetReminder;
        Button buttonReturnBook;

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
