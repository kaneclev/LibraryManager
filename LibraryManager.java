import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class LibraryManager implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    HashMap<String, Book> hashLib;
    HashMap<String, ArrayList<String>> hashByAuthor;
    ArrayList<String> catalogueReferences;
    HashMap<String, User> users;
    private User currUser; // for identifying who is currently using the library.
    int id;
    LibraryManager() {
        hashLib = new HashMap<>();
        hashByAuthor = new HashMap<>();
        catalogueReferences = new ArrayList<>();
        users = new HashMap<>();
        id = 0;
        addUser("kane", "..", true);
    }
    // todo: serialization methods
    public HashMap<String, Book> getHashLib() {
        return hashLib;
    }
    public HashMap<String, User> getUsers() {
        return users;
    }
    public HashMap<String, ArrayList<String>> getHashByAuthor() {
        return hashByAuthor;
    }
    public ArrayList<String> getCatalogueReferences() {
        return catalogueReferences;
    }
    public ArrayList<Book> getCurrUserCatalogue() {
        return currUser.getThisCatalogue();
    }

    public void serialize() {
        try (FileOutputStream fileOut = new FileOutputStream("lib.ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(this);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    // todo> start of User methods
    // method to set the current user session
    public void setCurrUser(String username) {
        currUser = users.get(username);
    }


    public boolean getPermissions(String username) {
        if(users.get(username).isAdmin) {
            return true;
        }
        else {
            return false;
        }
    }

    public void addUser(String user, String pass, boolean isAdmin) {
        users.put(user, new User(user, pass, isAdmin));

    }
    // for login purposes; if administrator exists in hashmap, and the password is correct, return true;
    public boolean UserExists(String user, String pass, boolean isLogin) {
        if(users.containsKey(user)) {
            if(isLogin) { // if we are trying to log in we need to also get the password
                if (users.get(user).isPassword(pass)) {
                    return true;
                } else {

                    return false;
                }
            }
            else {
                return true;
            }
        }
        return false;
    }
    public void seeUserCatalogue() {
        currUser.seeMyCatalogue();
    }
    // todo: end of User methods.

    public void viewCatalogue() {
        for (String name : catalogueReferences) {
            Book currBook = hashLib.get(name);
            getBookInfo(currBook);
        }
    }

    public void userBookSignOut(String title, int copies) {
        if (hashLib.containsKey(title)) {
            if ((hashLib.get(title).getBookQuantity() - copies < 0)) {
                // then someone wants to sign out more books than are available.
                System.out.println("You have signed out the rest of the copies of the book: " + title);
                currUser.addBookToUserCatalogue(hashLib.get(title), hashLib.get(title).getBookQuantity());
                hashLib.get(title).setBookQuantity(0);
            } else {
                hashLib.get(title).setBookQuantity(hashLib.get(title).getBookQuantity() - copies);
                currUser.addBookToUserCatalogue(hashLib.get(title), hashLib.get(title).getBookQuantity());
                System.out.println("You have signed out " + copies + " copies of " + title);
            }
        }
        else {
            System.out.println("There is no such book in the catalogue.");
        }
    }
    public void addNewBook(String bookName, String author, int bookQuantity, int price, boolean alert) {
        if(!hashLib.containsKey(bookName) && bookQuantity > 0 && price >= 0) {
            Book currBook = new Book(author, bookName, price);
            currBook.setBookID(id);
            hashLib.put(currBook.getBookName(), currBook);
            currBook.setBookQuantity(hashLib.get(bookName).getBookQuantity() + bookQuantity);
            catalogueReferences.add(bookName);
            hashToAuthor(bookName, author);
            id++;
            if(alert) {
                System.out.println("Added " + currBook.getBookName() + " to the catalogue.");
            }
        }
        else if(bookQuantity < 0 || price < 0) {
            System.out.println("The quantity and/or price of the book offered must be greater than 0.");
        }
        else {
            if(!author.equals(hashLib.get(bookName).getAuthor()) || price != hashLib.get(bookName).getPrice()) {
                System.out.println("The book exists in the system, but we cannot add to the book's quantity " +
                        "because the price and/or author of the book offered do not match the book in our current catalogue.");
            }
            else { // adds the quantity of books to the already existing book in the catalogue.
                System.out.println("The book offered already exists in the catalogue so we added more of that book. ");
                hashLib.get(bookName).setBookQuantity(hashLib.get(bookName).getBookQuantity() + bookQuantity);
            }

        }
    }

    public void addBooks(String bookName, int bookQuantity) {
        if(!hashLib.containsKey(bookName)) {
            System.err.println("There is no such book. If you would like to add this book to the catalogue, " +
                    "provide the author and price of the book and refer to the 'addNewBook' function.");
        }
        else if(bookQuantity <= 0) {
            System.out.println("The quantity of books added must be greater than 0. ");
        }
        else {
            hashLib.get(bookName).setBookQuantity(hashLib.get(bookName).getBookQuantity() + bookQuantity);
        }
    }

    // fixme: I currently don't remove the book object for that title when the num_books reaches zero
    //  could possibly change this later to remove the book object for memory's sake.
    public void removeBook(String bookName, int bookQuantity) {
        if(hashLib.containsKey(bookName)) {
            if((hashLib.get(bookName).getBookQuantity() - bookQuantity < 0)) { // admin piece
                System.out.println("There were not " + bookQuantity + " books left to remove for this title.");
                System.out.println("There are now 0 '" + bookName + "' books left.");
                hashLib.get(bookName).setBookQuantity(0);
            }
            else {
                hashLib.get(bookName).setBookQuantity(hashLib.get(bookName).getBookQuantity() - bookQuantity);

            }
        }
        else {
            System.out.println("There is no such book within the catalogue to remove. ");
        }
    }
    public void getAuthor(String bookName) {
        if(hashLib.containsKey(bookName)) {
            System.out.println("Author for the requested book: " + hashLib.get(bookName).getAuthor());
        }
        else {
            System.out.println("There is no such book in the catalogue currently. " +
                    "In order to adda new book to the library, use the 'addNewBook' function. ");
        }
    }
    public void getBookPrice(String bookName) {
        if(hashLib.containsKey(bookName)) {
            System.out.println("The price of the requested book: " + hashLib.get(bookName).getPrice());
        }
        else {
            System.out.println("There is no such book in the catalogue currently. " +
                    "In order to adda new book to the library, use the 'addNewBook' function. ");
        }
    }
    public void getBookID(String bookName) {
        if(hashLib.containsKey(bookName)) {
            System.out.println("ID for the requested book: " + hashLib.get(bookName).getBookID());
        }
        else {
            System.out.println("There is no such book in the catalogue currently. " +
                    "In order to add a new book to the library, use the 'addNewBook' function. ");
        }
    }

    public void searchAuthorCatalogue(String author) {
        if(!hashByAuthor.containsKey(author)) {
            System.out.println("There is no such author in our catalogue.");
        }
        else {
            System.out.println("The following are the titles we currently have authored by " + author + ":");
            for(int i = 0; i < hashByAuthor.get(author).size(); i++) {
                String bookName = hashByAuthor.get(author).get(i);
                Book currBook = hashLib.get(bookName);
                getBookInfo(currBook);
            }
        }
    }
    public void searchByTitle(String title) {
        if(!hashLib.containsKey(title)) {
            System.out.println("There is no such book in the catalogue. ");
        }
        else { // otherwise the book does exist within the catalogue.
            Book currBook = hashLib.get(title);
            getBookInfo(currBook);

        }
    }

    private void hashToAuthor(String bookName, String author) {
        if(hashByAuthor.containsKey(author)) { // if we already have the author listed in the hashmap
            hashByAuthor.get(author).add(bookName); // add to that author's buckets
        }
        else {
            hashByAuthor.put(author, new ArrayList<>());
            hashToAuthor(bookName, author);
        }

    }

    private void getBookInfo(Book currBook) {
        System.out.println("=======================");
        System.out.println("Title: " + currBook.getBookName());
        System.out.println("Author: " + currBook.getAuthor());
        System.out.println("Book Quantity: " + currBook.getBookQuantity());
        System.out.println("Book Price: " + currBook.getPrice());
        System.out.println("Book ID: " + currBook.getBookID());
    }

}
