package com.example.bookworm;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * BookStorage is a utility class for persisting the user's book list.
 * It uses SharedPreferences for local storage and GSON to serialize/deserialize
 * the ArrayList of Book objects into a JSON string.
 */
public class BookStorage {

    /** Name of the SharedPreferences file. */
    private static final String PREFS_NAME = "bookworm_prefs";
    
    /** Key used to store and retrieve the list of books in the user's collection. */
    private static final String KEY_MY_LIST = "my_list_books";

    /**
     * Serializes the provided list of books to JSON and saves it to SharedPreferences.
     * 
     * @param context     The application context.
     * @param myListBooks The list of Book objects to persist.
     */
    public static void saveMyList(Context context, ArrayList<Book> myListBooks) {
        // Access the private SharedPreferences for this app
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Convert the list of books into a JSON string using GSON
        Gson gson = new Gson();
        String json = gson.toJson(myListBooks);
        
        // Store the JSON string and commit changes asynchronously
        editor.putString(KEY_MY_LIST, json);
        editor.apply();
    }

    /**
     * Retrieves the user's book list from SharedPreferences and deserializes it from JSON.
     * 
     * @param context The application context.
     * @return An ArrayList of Book objects, or an empty list if no data is found.
     */
    public static ArrayList<Book> loadMyList(Context context) {
        // Access the SharedPreferences file
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        
        // Retrieve the stored JSON string
        String json = sharedPreferences.getString(KEY_MY_LIST, null);

        // If no list has been saved yet, return a new empty list
        if (json == null) {
            return new ArrayList<>();
        }

        // Use GSON and TypeToken to reconstruct the ArrayList<Book> from the JSON string
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Book>>() {}.getType();
        ArrayList<Book> loadedList = gson.fromJson(json, type);

        // Ensure we never return a null list
        return (loadedList != null) ? loadedList : new ArrayList<>();
    }
}