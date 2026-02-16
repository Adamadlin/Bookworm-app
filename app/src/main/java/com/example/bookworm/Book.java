package com.example.bookworm;

import java.io.Serializable;

/**
 * Book is the primary data model for representing a single library book.
 * It contains static information about the book (title, author, cover image)
 * and dynamic user state, specifically the selected return date.
 * Implements Serializable to allow storage and transmission between app components.
 */
public class Book implements Serializable {

    /** The title of the book. */
    private final String title;
    
    /** The author of the book. */
    private final String author;

    /** 
     * The resource ID for the cover image (R.drawable.*). 
     * This allows loading book covers locally from the app's resources.
     */
    private final int coverImageResId;

    /** 
     * An optional URL for more information about the book. 
     * This can be used to open a web browser for further details.
     */
    private final String websiteUrl;

    /** 
     * The return date stored as milliseconds since the Unix epoch (0 means not set). 
     * This tracks when the user plans to return the book.
     */
    private long returnDateMillis;

    /**
     * Constructs a new Book object with essential information.
     * 
     * @param title            The title of the book.
     * @param author           The author of the book.
     * @param coverImageResId  The drawable resource ID for the cover image.
     * @param websiteUrl       A link for additional information about the book.
     */
    public Book(String title, String author, int coverImageResId, String websiteUrl) {
        this.title = title;
        this.author = author;
        this.coverImageResId = coverImageResId;
        this.websiteUrl = websiteUrl;
        this.returnDateMillis = 0L; // Default return date is unset
    }

    /**
     * Gets the title of the book.
     * 
     * @return The book's title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the author of the book.
     * 
     * @return The book's author.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Gets the drawable resource ID for the cover image.
     * 
     * @return The cover image resource ID (R.drawable.*).
     */
    public int getCoverImageResId() {
        return coverImageResId;
    }

    /**
     * Legacy method for retrieving a cover URL. 
     * Since the app now uses local resources, this method is maintained for 
     * compatibility but returns an empty string.
     * 
     * @return An empty string.
     */
    public String getCoverImageUrl() {
        return "";
    }

    /**
     * Gets the website URL for further book details.
     * 
     * @return The book's website URL.
     */
    public String getWebsiteUrl() {
        return websiteUrl;
    }

    /**
     * Gets the user-specified return date in milliseconds.
     * 
     * @return The return date (epoch millis), or 0 if unset.
     */
    public long getReturnDateMillis() {
        return returnDateMillis;
    }

    /**
     * Sets the return date for the book in milliseconds.
     * 
     * @param returnDateMillis The new return date in epoch milliseconds.
     */
    public void setReturnDateMillis(long returnDateMillis) {
        this.returnDateMillis = returnDateMillis;
    }
}