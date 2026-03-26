import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class represents a library card holder.
 * It keeps track of who the reader is and what books
 * they currently have checked out.
 *
 * @author  Julian Mendoza
 * @version 1.0
 * @since   2026-03-26
 */

public class Reader {
    static final int CARD_NUMBER_ = 0;
    static final int NAME_ = 1;
    static final int PHONE_ = 2;
    static final int BOOK_COUNT_ = 3;
    static final int BOOK_START_ = 4;
    private int cardNumber;
    private String name;
    private String phone;
    private List<Book> books;

    public Reader(int cardNumber, String name, String phone) {
        this.cardNumber = cardNumber;
        this.name = name;
        this.phone = phone;
        this.books = new ArrayList<>();
    }
}
