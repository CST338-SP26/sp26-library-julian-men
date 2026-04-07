import Utilities.Code;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @version 1.3.1.
 */
class LibraryTest {

    Library csumb = null;

    String library00 = "Library00.csv";
    String library01 = "Library01.csv";
    String badBooks0 = "badBooks0.csv";
    String badBooks1 = "badBooks1.csv";
    String badShelves0 = "badShelves0.csv";
    String badShelves1 = "badShelves1.csv";
    String badReader0 = "badReader0.csv";
    String badReader1 = "badReader1.csv";

    @BeforeEach
    void setUp() {
        csumb = new Library("CSUMB");
    }

    @AfterEach
    void tearDown() {
        csumb = null;
    }

    @Test
    void init_test() {
        assertEquals(Code.FILE_NOT_FOUND_ERROR, csumb.init("nope.csv"));
        assertEquals(Code.BOOK_COUNT_ERROR, csumb.init(badBooks0));
        assertEquals(Code.BOOK_RECORD_COUNT_ERROR, csumb.init(badBooks1));
        assertEquals(Code.SHELF_COUNT_ERROR, csumb.init(badShelves0));
        assertEquals(Code.SHELF_NUMBER_PARSE_ERROR, csumb.init(badShelves1));
    }

    @Test
    void init_goodFile_test() {
        assertEquals(Code.SUCCESS, csumb.init(library00));
    }

    @Test
    void addBook() {
        Book book = new Book("1337", "Headfirst Java", "education", 1337,
                "Grady Booch", LocalDate.of(1970, 1, 1));
        // no shelf exists yet — should get SHELF_EXISTS_ERROR
        assertEquals(Code.SHELF_EXISTS_ERROR, csumb.addBook(book));
        // add a shelf and try again
        csumb.addShelf("education");
        assertEquals(Code.SUCCESS, csumb.addBook(book));
    }

    @Test
    void returnBook() {
        csumb.init(library00);
        Reader reader = csumb.getReaderByCard(1);
        Book book = csumb.getBookByISBN("42-w-87");
        assertNotNull(reader);
        assertNotNull(book);
        csumb.checkOutBook(reader, book);
        assertEquals(Code.SUCCESS, csumb.returnBook(reader, book));
    }

    @Test
    void testReturnBook() {
        csumb.init(library00);
        Book book = csumb.getBookByISBN("42-w-87");
        assertNotNull(book);
        assertEquals(Code.SUCCESS, csumb.returnBook(book));
    }

    @Test
    void listBooks() {
        csumb.init(library00);
        // library00 has 9 total books (4+3+1+1)
        assertEquals(9, csumb.listBooks());
    }

    @Test
    void checkOutBook() {
        csumb.init(library00);
        Reader reader = csumb.getReaderByCard(1);
        Book book = csumb.getBookByISBN("42-w-87");
        assertNotNull(reader);
        assertNotNull(book);
        assertEquals(Code.SUCCESS, csumb.checkOutBook(reader, book));
        // reader not in library
        Reader fakeReader = new Reader(99, "Fake Person", "000-000-0000");
        assertEquals(Code.READER_NOT_IN_LIBRARY_ERROR, csumb.checkOutBook(fakeReader, book));
        // book not in library
        Book fakeBook = new Book("0000", "Fake Book", "sci-fi", 100,
                "Nobody", LocalDate.of(1970, 1, 1));
        assertEquals(Code.BOOK_NOT_IN_INVENTORY_ERROR, csumb.checkOutBook(reader, fakeBook));
    }

    @Test
    void getBookByISBN() {
        csumb.init(library00);
        assertNotNull(csumb.getBookByISBN("42-w-87"));
        assertNull(csumb.getBookByISBN("0000"));
    }

    @Test
    void listShelves() {
        csumb.init(library00);
        // library00 has 3 shelves
        assertEquals(3, csumb.listShelves());
    }

    @Test
    void addShelf() {
        // add by string
        assertEquals(Code.SUCCESS, csumb.addShelf("sci-fi"));
        // adding the same shelf again returns error
        assertEquals(Code.SHELF_EXISTS_ERROR, csumb.addShelf("sci-fi"));
    }

    @Test
    void testAddShelf() {
        // add by Shelf object
        Shelf shelf = new Shelf(1, "education");
        assertEquals(Code.SUCCESS, csumb.addShelf(shelf));
        // adding same shelf again returns error
        assertEquals(Code.SHELF_EXISTS_ERROR, csumb.addShelf(shelf));
    }

    @Test
    void getShelf() {
        csumb.init(library00);
        // get by subject
        assertNotNull(csumb.getShelf("sci-fi"));
        assertNull(csumb.getShelf("Romance"));
    }

    @Test
    void testGetShelf() {
        csumb.init(library00);
        // get by number
        assertNotNull(csumb.getShelf(1));
        assertNull(csumb.getShelf(99));
    }

    @Test
    void listReaders() {
        csumb.init(library00);
        // library00 has 4 readers
        assertEquals(4, csumb.listReaders());
    }

    @Test
    void testListReaders() {
        csumb.init(library00);
        // listReaders(boolean) should also return 4
        assertEquals(4, csumb.listReaders(true));
        assertEquals(4, csumb.listReaders(false));
    }

    @Test
    void getReaderByCard() {
        csumb.init(library00);
        assertNotNull(csumb.getReaderByCard(1));
        assertNull(csumb.getReaderByCard(99));
    }

    @Test
    void addReader() {
        Reader reader = new Reader(1, "Drew Clinkenbeard", "831-582-4007");
        assertEquals(Code.SUCCESS, csumb.addReader(reader));
        // adding same reader again
        assertEquals(Code.READER_ALREADY_EXISTS_ERROR, csumb.addReader(reader));
        // adding reader with same card number
        Reader reader2 = new Reader(1, "Someone Else", "000-000-0000");
        assertEquals(Code.READER_CARD_NUMBER_ERROR, csumb.addReader(reader2));
    }

    @Test
    void removeReader() {
        csumb.init(library00);
        Reader reader = csumb.getReaderByCard(1);
        assertNotNull(reader);
        // reader has books checked out — should fail
        if (reader.getBookCount() > 0) {
            assertEquals(Code.READER_STILL_HAS_BOOKS_ERROR, csumb.removeReader(reader));
        }
        // reader not in library
        Reader fakeReader = new Reader(99, "Fake Person", "000-000-0000");
        assertEquals(Code.READER_NOT_IN_LIBRARY_ERROR, csumb.removeReader(fakeReader));
    }

    @Test
    void convertInt() {
        // valid conversion
        assertEquals(42, Library.convertInt("42", Code.BOOK_COUNT_ERROR));
        // invalid conversion returns the error code number
        assertEquals(Code.BOOK_COUNT_ERROR.getCode(),
                Library.convertInt("bad", Code.BOOK_COUNT_ERROR));
        assertEquals(Code.PAGE_COUNT_ERROR.getCode(),
                Library.convertInt("bad", Code.PAGE_COUNT_ERROR));
    }

    @Test
    void convertDate() {
        // valid date
        assertEquals(LocalDate.of(2020, 10, 12),
                Library.convertDate("2020-10-12", Code.DATE_CONVERSION_ERROR));
        // "0000" should return epoch
        assertEquals(LocalDate.of(1970, 1, 1),
                Library.convertDate("0000", Code.DATE_CONVERSION_ERROR));
        // bad date should return epoch
        assertEquals(LocalDate.of(1970, 1, 1),
                Library.convertDate("not-a-date", Code.DATE_CONVERSION_ERROR));
    }

    @Test
    void getLibraryCardNumber() {
        // starts at 0 so first card number should be 1
        assertEquals(1, Library.getLibraryCardNumber());
        // after adding a reader with card 5, should return 6
        csumb.addReader(new Reader(5, "Test Reader", "000-000-0000"));
        assertEquals(6, Library.getLibraryCardNumber());
    }
}
