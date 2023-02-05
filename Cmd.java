
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Cmd {
    LibraryManager libMan;
    boolean isAdmin = false;
    Cmd(String[] fileLoc) {
        libMan = new LibraryManager();
        Scanner inStream = new Scanner(System.in);


        if(fileLoc.length == 1) {
            loadInitLibrary(fileLoc[0]);
        }



        if(login(inStream)) {
            explanation();
            while (inStream.hasNextLine()) {
                String line = inStream.nextLine();
                if (line.length() > 0) {
                    if (!line.startsWith("#")) { // checks for comments
                        if (line.contains("(") && line.contains(")")) {
                            try {
                                String command = line.substring(0, line.indexOf('('));

                                switch (command) {
                                    case "addNewBook":
                                        String[] cleanedParams = grabParams(line);
                                        if (cleanedParams.length != 4) {
                                            parameterWarning();
                                        } else {
                                            int quantity = 0;
                                            int price = 0;
                                            try {
                                                quantity = Integer.parseInt(cleanedParams[2]);
                                                price = Integer.parseInt(cleanedParams[3]);
                                            } catch (NumberFormatException err) {
                                                parameterWarning();
                                            }
                                            if(isAdmin) {
                                                libMan.addNewBook(cleanedParams[0], cleanedParams[1], quantity, price, true);
                                            }
                                            else {
                                                System.out.println("This command is for Useristrators only. ");
                                            }
                                        }
                                        break;
                                    case "addBooks":
                                        cleanedParams = grabParams(line);
                                        if (cleanedParams.length != 2) {
                                            parameterWarning();
                                        } else {
                                            int quantity = 0;
                                            try {
                                                quantity = Integer.parseInt(cleanedParams[1]);
                                            } catch (NumberFormatException err) {
                                                parameterWarning();
                                            }
                                            if(isAdmin) {
                                                libMan.addBooks(cleanedParams[0], quantity);
                                            }
                                            else {
                                                System.out.println("This command is for Useristrators only. ");
                                            }
                                        }
                                        break;
                                    case "removeBooks":
                                        cleanedParams = grabParams(line);

                                        if (cleanedParams.length != 2) {
                                            parameterWarning();
                                        } else {
                                            int quantity = 0;
                                            try {
                                                quantity = Integer.parseInt(cleanedParams[1]);
                                            } catch (NumberFormatException err) {
                                                parameterWarning();
                                            }
                                            if(isAdmin) {
                                                libMan.removeBook(cleanedParams[0], quantity);
                                            }
                                            else {
                                                System.out.println("This command is for Useristrators only. ");
                                            }
                                        }

                                        break;
                                    case "getAuthor":
                                        cleanedParams = grabParams(line);
                                        if (cleanedParams.length != 1) {
                                            parameterWarning();
                                        } else {
                                            libMan.getAuthor(cleanedParams[0]);
                                        }

                                        break;
                                    case "searchByTitle":
                                        cleanedParams = grabParams(line);
                                        if (cleanedParams.length != 1) {
                                            parameterWarning();
                                        } else {
                                            libMan.searchByTitle(cleanedParams[0]);
                                        }

                                        break;

                                    case "test":
                                        System.out.println("This test case passed.");
                                        break;
                                    case "getBookPrice":
                                        cleanedParams = grabParams(line);
                                        if (cleanedParams.length != 1) {
                                            parameterWarning();
                                        } else {
                                            libMan.getBookPrice(cleanedParams[0]);
                                        }

                                        break;
                                    case "viewCatalogue":
                                        libMan.viewCatalogue();
                                        break;
                                    case "searchByAuthor":
                                        cleanedParams = grabParams(line);
                                        if (cleanedParams.length != 1) {
                                            parameterWarning();
                                        } else {
                                            libMan.searchAuthorCatalogue(cleanedParams[0]);
                                        }
                                        break;
                                    case "getBookID":
                                        cleanedParams = grabParams(line);
                                        if (cleanedParams.length != 1) {
                                            parameterWarning();
                                        } else {
                                            libMan.getBookID(cleanedParams[0]);
                                        }
                                        break;

                                    case "explain":
                                        explanation();
                                        break;
                                    case "commands":
                                        System.out.println("Current working commands: ");
                                        System.out.println("explain() -> explanation at beginning of program. ");
                                        System.out.println("quit() -> shut down the library");
                                        System.out.println("addNewBook(title, author, book-quantity, price)");
                                        System.out.println("getAuthor(title) -> get the author of a book. ");
                                        System.out.println("addBooks(title, num_books) -> increase the quantity " +
                                                "of an existing book");
                                        System.out.println("viewCatalogue() -> view the library's contents.");
                                        System.out.println("getBookID(title) -> view the book ID of a book.");
                                        System.out.println("getBookPrice(title) -> get the price of a book.");
                                        System.out.println("searchByAuthor(author) -> returns all books written by " +
                                                "specified author. ");
                                        System.out.println("searchByTitle(title) -> returns book info on book");
                                        System.out.println("removeBooks(title, quantity) -> removes quantity of a given " +
                                                "book. ");
                                        break;

                                    case "quit":
                                        System.out.println("Shutting down... ");
                                        System.exit(0);
                                        break;
                                    default:
                                        System.err.println("No such command exists. Use commands() for a list of commands.");
                                }


                            } catch (IndexOutOfBoundsException idxErr) {
                                System.err.println("There was an issue with the given command. (Index Problem)");
                                System.err.println("For a list of commands, please use the commands() function," +
                                        " or use explain() for a general library overview.");
                            }

                        }

                    } // otherwise, it is a comment.

                } else { // then this is either not a command and not a comment; we need something else;
                    System.err.println("There must be a command given.");
                    System.out.println("Use commands() for a list of commands.");
                }

            }
        }
    }
    // CURRENTLY WORKS
    public void loadInitLibrary(String fileName) {
        try{
            Scanner fileInput = new Scanner(new File(fileName));

            System.err.println("Loading in input library file... ");
            while(fileInput.hasNextLine()) {
                String[] params;
                String[] cleanedParams;
                String line = fileInput.nextLine();
                params = line.split(",", 0);
                cleanedParams = new String[params.length];
                for (int i = 0; i < params.length; i++) {
                    if (params[i].charAt(0) == ' ') {
                        cleanedParams[i] = params[i].substring(1);
                    } else {
                        cleanedParams[i] = params[i];
                    }
                }
                libMan.addNewBook(cleanedParams[0], cleanedParams[1],
                        Integer.parseInt(cleanedParams[2]), Integer.parseInt(cleanedParams[3]), false);
            }
        } catch(FileNotFoundException err) {
            System.err.println("File not found for initial library-- shutting down. ");
            System.exit(1);

        }

    }
    // separate method to make the command handler look less messy. grabs ( ) arguments and cleans em up.
    private String[] grabParams(String line) {
        String args;
        String[] params;
        String[] cleanedParams;
        args = line.substring(line.lastIndexOf('('));
        args = args.substring(1, args.length() - 1);
        params = args.split(",", 0);
        cleanedParams = new String[params.length];
        for (int i = 0; i < params.length; i++) {
            if (params[i].charAt(0) == ' ') {
                cleanedParams[i] = params[i].substring(1);
            } else {
                cleanedParams[i] = params[i];
            }
        }
        return cleanedParams;
    }

    private void explanation() {
        System.out.println("Welcome to the library. There are a variety of functions you can use here. ");
        System.out.println("We can store book objects which have an associated author, quantity, ID, and price.");
        System.out.println("Commands consist of an existed function, followed by a '()', where the parentheses");
        System.out.println("contain the required parameters. ");
        System.out.println("For the list of possible commands, use the 'commands()' command.");
        System.out.println("To repeat this message, use the 'explain()' command.");
        System.out.println("To exit the library, use the 'quit()' command. ");
        System.out.println("Thank you, and enjoy. ");
    }

    private void parameterWarning() {
        System.out.println("The formatting of the parameters are wrong. ");
        System.out.println("Please refer to the commands() command for proper formatting. ");
    }

    private boolean login(Scanner in) {
      logInfo: while(true) {
            System.out.println("Login Options (please choose 1, 2, or 3): ");
            System.out.println("1.) Create a new account");
            System.out.println("2.) Sign in to an existing account");
            System.out.println("3.) Quit");
            String choice = in.nextLine();

             switch(choice) {
                case "1":
                    createAccount(in);
                    return true;
                case "2":
                    if(signIn(in)) {
                        return true;
                    }
                    break;
                 case "3":
                     System.exit(0);
                     break;
                 default:
                     System.out.println("Please enter a valid choice. ");
             }
        }
    }

    private void createAccount(Scanner in) {
        String username;
        String password;
        while (true) {
            System.out.println("New Account Username: ");
            username = in.nextLine();
            if (libMan.UserExists(username, "", false)) {
                System.out.println("That username already exists. ");
            } else {
                System.out.println("New Account Password: ");
                password = in.nextLine();
                break;
            }
        }
        libMan.addUser(username, password, false);
    }
    private boolean signIn(Scanner in) {
        String username;
        String password;
        while(true) {

            System.out.println("Username: ");
            username = in.nextLine();
            if(libMan.UserExists(username, "", false)) {
                System.out.println("Password: ");
                password = in.nextLine();
                if(libMan.UserExists(username, password, true)) {
                    return true;
                }
                else {
                    System.out.println("That is the incorrect password. ");
                }
            }
            else {
                System.out.println("No such username exists.  ");
                }
            }
    }




}
