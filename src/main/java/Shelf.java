
import java.util.HashMap;
import java.util.Objects;

/**
 * This class represents a shelf in the library.
 * It keeps track of what subject the shelf holds and
 * how many copies of each book are stored on it.
 *
 * @author  Julian Mendoza
 * @version 1.0
 * @since   2026-03-26
 */
public class Shelf {

    static final int SHELF_NUMBER_ = 0;

    static final int SUBJECT_ = 1;

    private HashMap<Book, Integer> books;

    private int shelfNumber;

    private String subject;


    @Deprecated
    public Shelf() {
    }

    /**
     * Creates a new Shelf with a number and a subject.
     *
     * @param shelfNumber the number assigned to this shelf
     * @param subject     the subject category for this shelf
     */
    public Shelf(int shelfNumber, String subject) {
        this.shelfNumber = shelfNumber;
        this.subject = subject;
        this.books = new HashMap<>();
    }


    public int getShelfNumber() {
        return shelfNumber;
    }

    public void setShelfNumber(int shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    public String getSubject() {
        return subject;
    }


    public void setSubject(String subject) {
        this.subject = subject;
    }


    public HashMap<Book, Integer> getBooks() {
        return books;
    }

    public void setBooks(HashMap<Book, Integer> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shelf shelf = (Shelf) o;
        return shelfNumber == shelf.shelfNumber &&
                Objects.equals(subject, shelf.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shelfNumber, subject);
    }

    @Override
    public String toString() {
        return shelfNumber + " : " + subject;
    }

    public int getBookCount(Book book) {
        if (!books.containsKey(book)) {
            return -1;
        }
        return books.get(book);
    }


    public Code addBook(Book book) {
        if (books.containsKey(book)) {
            books.put(book, books.get(book) + 1);
            System.out.println(book + " added to shelf " + this);
            return Code.SUCCESS;
        }
        if (book.getSubject().equals(subject)) {
            books.put(book, 1);
            System.out.println(book + " added to shelf " + this);
            return Code.SUCCESS;
        }
        return Code.SHELF_SUBJECT_MISMATCH_ERROR;
    }


    public Code removeBook(Book book) {
        if (!books.containsKey(book)) {
            System.out.println(book.getTitle() + " is not on shelf " + subject);
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }
        if (books.get(book) <= 0) {
            System.out.println("No copies of " + book.getTitle() + " remain on shelf " + subject);
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }
        books.put(book, books.get(book) - 1);
        System.out.println(book.getTitle() + " successfully removed from shelf " + subject);
        return Code.SUCCESS;
    }


    public String listBooks() {
        int total = 0;
        StringBuilder sb = new StringBuilder();
        for (int count : books.values()) {
            total += count;
        }
        String bookWord = total == 1 ? "book" : "books";
        sb.append(total).append(" ").append(bookWord).append(" on shelf: ").append(this).append("\n");
        for (Book book : books.keySet()) {
            sb.append(book).append(" ").append(books.get(book)).append("\n");
        }
        return sb.toString();
    }

}