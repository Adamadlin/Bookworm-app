package com.example.bookworm;

import java.io.Serializable;

/**
 * Book represents one library book item.
 * Holds display info and user state (selected return date).
 */
public class Book implements Serializable {

    private final String title;
    private final String author;

    // Local drawable resource id for the cover image (R.drawable.*)
    private final int coverImageResId;

    // Optional website link for the book (can be opened later if you add that feature)
    private final String websiteUrl;

    // Return date stored as epoch millis (0 means not set yet)
    private long returnDateMillis;

    /**
     * Creates a Book object.
     * @param title book title
     * @param author book author
     * @param coverImageResId drawable resource id (R.drawable.*)
     * @param websiteUrl website link for the book
     */
    public Book(String title, String author, int coverImageResId, String websiteUrl) {
        this.title = title;
        this.author = author;
        this.coverImageResId = coverImageResId;
        this.websiteUrl = websiteUrl;
        this.returnDateMillis = 0L;
    }

    /**
     * @return the book title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the book author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Returns the drawable resource id (R.drawable.*) for the cover image.
     * @return cover drawable resource id
     */
    public int getCoverImageResId() {
        return coverImageResId;
    }

    /**
     * Compatibility method (older code might still call this).
     * Since we use local images now, this returns an empty string.
     * @return empty string (no URL used)
     */
    public String getCoverImageUrl() {
        return "";
    }

    /**
     * @return website URL for more info about the book
     */
    public String getWebsiteUrl() {
        return websiteUrl;
    }

    /**
     * @return return date in epoch millis (0 if not set)
     */
    public long getReturnDateMillis() {
        return returnDateMillis;
    }

    /**
     * Sets the return date in milliseconds since epoch.
     * @param returnDateMillis epoch millis
     */
    public void setReturnDateMillis(long returnDateMillis) {
        this.returnDateMillis = returnDateMillis;
    }
}

/*
Book.java:
This class defines the Book object used across the app.
It stores title, author, cover image (local drawable resource id), website URL,
and the user's selected return date (epoch millis).
Used by RecyclerView adapters and saved/loaded using SharedPreferences (via BookStorage).
*/