import java.util.ArrayList;
import java.util.HashMap;

public class LibraryManager {

    // need a way to store books in our library; think arraylist??
    HashMap<String, Book> hashLib;
    HashMap<String, ArrayList<String>> hashByAuthor;
    ArrayList<String> catalogueReferences;
    HashMap<String, Admin> administrators;
    int id;
    LibraryManager() {
        hashLib = new HashMap<>();
        hashByAuthor = new HashMap<>();
        catalogueReferences = new ArrayList<>();
        administrators = new HashMap<>();
        id = 0;
    }
    // todo> start of admin methods
    public void addAdmin(String user, String pass) {
        administrators.put(user, new Admin(user, pass));
    }
    // for login purposes; if administrator exists in hashmap, and the password is correct, return true;
    public boolean adminExists(String user, String pass) {
        if(administrators.containsKey(user)) {
            if(administrators.get(user).isPassword(pass)) {
                return true;
            }
            else {
                System.out.println("That is the incorrect password. ");
                return false;
            }
        }
        else {
            System.out.println("There is no such admin account. ");
            return false;
        }
    }
    // todo: end of admin methods.

    public void viewCatalogue() {
        for (String name : catalogueReferences) {
            Book currBook = hashLib.get(name);
            getBookInfo(currBook);
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
            if(hashLib.get(bookName).getBookQuantity() - bookQuantity < 0) {
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