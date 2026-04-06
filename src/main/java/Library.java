import Utilities.Code;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * This class represents a library.
 * It manages all the books, shelves, and readers
 * and handles checking books in and out.
 *
 * @author  Julian Mendoza
 * @version 1.0
 * @since   2026-03-26
 */
public class Library {

    static final int LENDING_LIMIT = 5;

    private String name;

    private static int libraryCard = 0;

    private List<Reader> readers;

    private HashMap<String, Shelf> shelves;

    private HashMap<Book, Integer> books;


    public Library(String name) {
        this.name = name;
        this.readers = new ArrayList<>();
        this.shelves = new HashMap<>();
        this.books = new HashMap<>();
    }


    public String getName() {
        return name;
    }


    public static int getLibraryCardNumber() {
        return libraryCard + 1;
    }


    public static int convertInt(String recordCountString, Code code) {
        try {
            return Integer.parseInt(recordCountString);
        } catch (NumberFormatException e) {
            System.out.println("Value which caused the error: " + recordCountString);
            System.out.println("Error message: " + code.getMessage());
            switch (code) {
                case BOOK_COUNT_ERROR:
                    System.out.println("Error: Could not read number of books");
                    break;
                case PAGE_COUNT_ERROR:
                    System.out.println("Error: could not parse page count");
                    break;
                case DATE_CONVERSION_ERROR:
                    System.out.println("Error: Could not parse date component");
                    break;
                default:
                    System.out.println("Error: Unknown conversion error");
                    break;
            }
            return code.getCode();
        }
    }

    public static LocalDate convertDate(String date, Code errorCode) {
        if (date.equals("0000")) {
            return LocalDate.of(1970, 1, 1);
        }
        String[] parts = date.split("-");
        if (parts.length != 3) {
            System.out.println("ERROR: date conversion error, could not parse " + date);
            System.out.println("Using default date (01-jan-1970)");
            return LocalDate.of(1970, 1, 1);
        }
        int year = convertInt(parts[0], errorCode);
        int month = convertInt(parts[1], errorCode);
        int day = convertInt(parts[2], errorCode);
        if (year < 0 || month < 0 || day < 0) {
            System.out.println("Error converting date: Year " + year);
            System.out.println("Error converting date: Month " + month);
            System.out.println("Error converting date: Day " + day);
            System.out.println("Using default date (01-jan-1970)");
            return LocalDate.of(1970, 1, 1);
        }
        return LocalDate.of(year, month, day);
    }

    private Code errorCode(int codeNumber) {
        for (Code code : Code.values()) {
            if (code.getCode() == codeNumber) {
                return code;
            }
        }
        return Code.UNKNOWN_ERROR;
    }

    /**
     * Reads the CSV file and sets up all books, shelves, and readers.
     *
     * @param filename the path to the CSV file
     * @return Code.SUCCESS if everything loaded, or an error code if something failed
     */
    public Code init(String filename) {
        Scanner scan;
        try {
            scan = new Scanner(new FileReader(filename));
        } catch (FileNotFoundException e) {
            return Code.FILE_NOT_FOUND_ERROR;
        }

        // parse books
        int bookCount = convertInt(scan.nextLine(), Code.BOOK_COUNT_ERROR);
        if (bookCount < 0) {
            return errorCode(bookCount);
        }
        Code result = initBooks(bookCount, scan);
        if (result != Code.SUCCESS) {
            return result;
        }
        listBooks();

        // parse shelves
        int shelfCount = convertInt(scan.nextLine(), Code.SHELF_COUNT_ERROR);
        if (shelfCount < 0) {
            return errorCode(shelfCount);
        }
        result = initShelves(shelfCount, scan);
        if (result != Code.SUCCESS) {
            return result;
        }
        listShelves();

        // parse readers
        int readerCount = convertInt(scan.nextLine(), Code.READER_COUNT_ERROR);
        if (readerCount < 0) {
            return errorCode(readerCount);
        }
        result = initReader(readerCount, scan);
        if (result != Code.SUCCESS) {
            return result;
        }
        listReaders();

        return Code.SUCCESS;
    }

    /**
     * Reads books from the scanner and adds them to the library.
     *
     * @param bookCount the number of books to read
     * @param scan      the scanner positioned at the first book record
     * @return Code.SUCCESS if all books loaded, or an error code if something failed
     */
    private Code initBooks(int bookCount, Scanner scan) {
        if (bookCount < 1) {
            return Code.LIBRARY_ERROR;
        }
        for (int i = 0; i < bookCount; i++) {
            String[] fields = scan.nextLine().split(",");
            if (fields.length < Book.DUE_DATE_ + 1) {
                return Code.BOOK_RECORD_COUNT_ERROR;
            }
            int pageCount = convertInt(fields[Book.PAGE_COUNT_], Code.PAGE_COUNT_ERROR);
            if (pageCount <= 0) {
                return Code.PAGE_COUNT_ERROR;
            }
            LocalDate dueDate = convertDate(fields[Book.DUE_DATE_], Code.DATE_CONVERSION_ERROR);
            if (dueDate == null) {
                return Code.DATE_CONVERSION_ERROR;
            }
            Book book = new Book(
                    fields[Book.ISBN_],
                    fields[Book.TITLE_],
                    fields[Book.SUBJECT_],
                    pageCount,
                    fields[Book.AUTHOR_],
                    dueDate
            );
            addBook(book);
        }
        return Code.SUCCESS;
    }

    /**
     * Reads shelves from the scanner and adds them to the library.
     *
     * @param shelfCount the number of shelves to read
     * @param scan       the scanner positioned at the first shelf record
     * @return Code.SUCCESS if all shelves loaded, or an error code if something failed
     */
    private Code initShelves(int shelfCount, Scanner scan) {
        if (shelfCount < 1) {
            return Code.SHELF_COUNT_ERROR;
        }
        for (int i = 0; i < shelfCount; i++) {
            String[] fields = scan.nextLine().split(",");
            int shelfNumber = convertInt(fields[Shelf.SHELF_NUMBER_], Code.SHELF_NUMBER_PARSE_ERROR);
            if (shelfNumber < 0) {
                return Code.SHELF_NUMBER_PARSE_ERROR;
            }
            addShelf(fields[Shelf.SUBJECT_]);
        }
        if (shelves.size() != shelfCount) {
            System.out.println("Number of shelves doesn't match expected");
            return Code.SHELF_NUMBER_PARSE_ERROR;
        }
        return Code.SUCCESS;
    }

    private Code initReader(int readerCount, Scanner scan) {
        if (readerCount <= 0) {
            return Code.READER_COUNT_ERROR;
        }
        for (int i = 0; i < readerCount; i++) {
            String[] fields = scan.nextLine().split(",");
            int cardNumber = convertInt(fields[Reader.CARD_NUMBER_], Code.READER_COUNT_ERROR);
            Reader reader = new Reader(cardNumber, fields[Reader.NAME_], fields[Reader.PHONE_]);
            addReader(reader);
            int bookCount = convertInt(fields[Reader.BOOK_COUNT_], Code.BOOK_COUNT_ERROR);
            for (int j = 0; j < bookCount; j++) {
                int isbnIndex = Reader.BOOK_START_ + (j * 2);
                int dateIndex = isbnIndex + 1;
                if (dateIndex >= fields.length) {
                    break;
                }
                Book book = getBookByISBN(fields[isbnIndex]);
                if (book == null) {
                    System.out.println("ERROR");
                    continue;
                }
                LocalDate dueDate = convertDate(fields[dateIndex], Code.DATE_CONVERSION_ERROR);
                book.setDueDate(dueDate);
                checkOutBook(reader, book);
            }
        }
        return Code.SUCCESS;
    }


}