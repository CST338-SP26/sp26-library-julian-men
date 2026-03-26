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

    public Book(String isbn, String title, String subject, int pageCount, String author, LocalDate dueDate) {
        this.isbn = isbn;
        this.title = title;
        this.subject = subject;
        this.pageCount = pageCount;
        this.author = author;
        this.dueDate = dueDate;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
