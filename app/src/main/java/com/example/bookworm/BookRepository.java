package com.example.bookworm;

import java.util.ArrayList;

/**
 * BookRepository provides the available library books (hardcoded list for this project).
 * Uses local drawable images (res/drawable) instead of URLs for covers.
 */
public class BookRepository {

    /**
     * Returns a list of available programming books for browsing.
     * Each book includes: title, author, local cover drawable, and a website link.
     *
     * @return ArrayList of Book objects
     */
    public static ArrayList<Book> getAvailableBooks() {
        ArrayList<Book> books = new ArrayList<>();

        books.add(new Book(
                "Clean Code",
                "Robert C. Martin",
                R.drawable.clean_code,
                "https://www.oreilly.com/library/view/clean-code/9780136083238/"
        ));

        books.add(new Book(
                "Effective Java (3rd Edition)",
                "Joshua Bloch",
                R.drawable.effective_java,
                "https://www.oreilly.com/library/view/effective-java-3rd/9780134686097/"
        ));

        books.add(new Book(
                "Design Patterns: Elements of Reusable Object-Oriented Software",
                "Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides",
                R.drawable.design_patterns,
                "https://www.oreilly.com/library/view/design-patterns-elements/0201633612/"
        ));

        books.add(new Book(
                "Black Hat Python (2nd Edition)",
                "Justin Seitz, Tim Arnold",
                R.drawable.blackhatpy,
                "https://nostarch.com/black-hat-python2E"
        ));

        books.add(new Book(
                "The Pragmatic Programmer (20th Anniversary Edition)",
                "Andrew Hunt, David Thomas",
                R.drawable.thepragm,
                "https://pragprog.com/titles/tpp20/the-pragmatic-programmer-20th-anniversary-edition/"
        ));

        return books;
    }
}

/*
BookRepository.java:
This class is a simple data provider for the app.
It builds and returns an ArrayList<Book> that is used by BrowseBooksActivity to display
the available books in a RecyclerView. Each book uses a local drawable image from res/drawable.
*/