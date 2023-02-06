import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String username;
    private final String password;
    public boolean isAdmin;
    private ArrayList<Book> userCatalogue;

    User(String user, String pass, boolean is_admin) {
        this.username = user;
        this.password = pass;
        isAdmin = is_admin;
        userCatalogue = new ArrayList<>();
    }

    public void addBookToUserCatalogue(Book book, int copies) {
        book.setBookQuantity(copies);
        userCatalogue.add(book);
    }
    public ArrayList<Book> getThisCatalogue() {
        return this.userCatalogue;
    }
    public void seeMyCatalogue() {
        for(Book currBook : userCatalogue) {
            System.out.println("=======================");
            System.out.println("Title: " + currBook.getBookName());
            System.out.println("Author: " + currBook.getAuthor());
            System.out.println("Book Quantity: " + currBook.getBookQuantity());
            System.out.println("Book Price: " + currBook.getPrice());
            System.out.println("Book ID: " + currBook.getBookID());
        }
    }

  
    public boolean isPassword(String pass) {
        return pass.equals(password);
    }

}
