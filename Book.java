public class Book {

    private String author;
    private String bookName;
    private int bookID;
    private int price;
    private int bookQuantity;

    Book(String author, String bookName, int price) {
        this.author = author;
        this.bookName = bookName;
        this.price = price;
        bookQuantity = 0;

    }

    public int getBookID() {
        return bookID;
    }

    public int getBookQuantity() {
        return bookQuantity;
    }

    public int getPrice() {
        return price;
    }

    public String getAuthor() {
        return author;
    }

    public String getBookName() {
        return bookName;
    }
    public void setBookID(int id) {
        this.bookID = id;
    }
    public void setBookQuantity(int bookQuantity) {
        this.bookQuantity = bookQuantity;
    }
}

