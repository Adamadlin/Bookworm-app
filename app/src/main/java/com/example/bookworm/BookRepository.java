package com.example.bookworm;

import java.util.ArrayList;

/**
 * BookRepository acts as a data provider for the library's catalog.
 * For this project, it provides a hardcoded list of programming books,
 * each associated with local cover images and information URLs.
 */
public class BookRepository {

    /**
     * Generates and returns a static list of available library books.
     * This simulates a database or network call by returning a predefined set of Book objects.
     * Each book includes its title, author, local resource ID for the cover, and a website URL.
     *
     * @return An ArrayList containing the catalog of available books.
     */
    public static ArrayList<Book> getAvailableBooks() {
        ArrayList<Book> books = new ArrayList<>();

        // Add "Clean Code" to the catalog
        books.add(new Book(
                "Clean Code",
                "Robert C. Martin",
                R.drawable.clean_code,
                "https://www.oreilly.com/library/view/clean-code/9780136083238/"
        ));

        // Add "Effective Java" to the catalog
        books.add(new Book(
                "Effective Java (3rd Edition)",
                "Joshua Bloch",
                R.drawable.effective_java,
                "https://www.oreilly.com/library/view/effective-java-3rd/9780134686097/"
        ));

        // Add "Design Patterns" to the catalog
        books.add(new Book(
                "Design Patterns: Elements of Reusable Object-Oriented Software",
                "Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides",
                R.drawable.design_patterns,
                "https://www.oreilly.com/library/view/design-patterns-elements/0201633612/"
        ));

        // Add "Black Hat Python" to the catalog
        books.add(new Book(
                "Black Hat Python (2nd Edition)",
                "Justin Seitz, Tim Arnold",
                R.drawable.blackhatpy,
                "https://nostarch.com/black-hat-python2E"
        ));

        // Add "The Pragmatic Programmer" to the catalog
        books.add(new Book(
                "The Pragmatic Programmer (20th Anniversary Edition)",
                "Andrew Hunt, David Thomas",
                R.drawable.thepragm,
                "https://pragprog.com/titles/tpp20/the-pragmatic-programmer-20th-anniversary-edition/"
        ));

        return books;
    }
}