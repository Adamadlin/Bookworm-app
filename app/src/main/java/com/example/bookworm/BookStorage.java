package com.example.bookworm;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * BookStorage handles saving and loading the user's "My List" using SharedPreferences.
 */
public class BookStorage {

    private static final String PREFS_NAME = "bookworm_prefs";
    private static final String KEY_MY_LIST = "my_list_books";

    /**
     * Saves the user's selected books list into SharedPreferences.
     * @param context app context
     * @param myListBooks list of books to save
     */
    public static void saveMyList(Context context, ArrayList<Book> myListBooks) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(myListBooks);
        editor.putString(KEY_MY_LIST, json);
        editor.apply();
    }

    /**
     * Loads the user's selected books list from SharedPreferences.
     * @param context app context
     * @return ArrayList of books (empty if none saved)
     */
    public static ArrayList<Book> loadMyList(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(KEY_MY_LIST, null);

        if (json == null) {
            return new ArrayList<>();
        }

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Book>>() {}.getType();
        ArrayList<Book> loadedList = gson.fromJson(json, type);

        return (loadedList != null) ? loadedList : new ArrayList<>();
    }
}