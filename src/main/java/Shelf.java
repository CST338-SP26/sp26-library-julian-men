
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
}