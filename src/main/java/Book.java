/**
 * This class represents a book in the library.
 * It stores information about the book and lets us
 * compare, retrieve, and display that information.
 *
 * @author  Julian Mendoza
 * @version 1.0
 * @since   2026-03-26
 */

import java.time.LocalDate;

public class Book {
    static final int ISBN_ = 0;
    static final int TITLE_ = 1;
    static final int SUBJECT_ = 2;
    static final int PAGE_COUNT_ = 3;
    static final int AUTHOR_ = 4;
    static final int DUE_DATE_ = 5;

    private String isbn;
    private String title;
    private String subject;
    private int pageCount;
    private String author;
    private LocalDate dueDate;

}
