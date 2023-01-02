
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Cmd {
    // todo: trying to do a running stream like I did in project3.
    //  the challenge is that the arguments I will be working with are going to be more extensive than in project 3
    //  current goal:
    //  get the in stream working so that i can interact with the library while the program is running
    //

    // currently creating commands that would only be available for an "admin"
    LibraryManager libMan;
    boolean isAdmin = false;
    Cmd(String[] fileLoc) {
        // call the introduction 'explanation' first thing.

        libMan = new LibraryManager();
        if(fileLoc.length == 1) {
            loadInitLibrary(fileLoc[0]);
        }
        Scanner inStream = new Scanner(System.in);
        boolean hasLoggedIn = login(inStream);

        if(hasLoggedIn) {
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
                                                System.out.println("This command is for administrators only. ");
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
                                                System.out.println("This command is for administrators only. ");
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
                                                System.out.println("This command is for administrators only. ");
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
        System.out.println("For the list of possible commands, use the 'printCmd()' command.");
        System.out.println("To repeat this message, use the 'explain()' command.");
        System.out.println("To exit the library, use the 'quit()' command. ");
        System.out.println("Thank you, and enjoy. ");
    }

    private void parameterWarning() {
        System.out.println("The formatting of the parameters are wrong. ");
        System.out.println("Please refer to the commands() command for proper formatting. ");
    }

    private boolean login(Scanner in) {
        System.out.println("Are you logging in or creating a new account? (login/new)");
        while(in.hasNextLine()) {
            String line = in.nextLine();
            if(line.length() > 0) {
                //fixme: i think we need a switch statement.
                try {
                    switch(line) {
                        case "login": //todo: for logging in as either a user or an admin.
                            System.out.println("Are you logging in as a user or an admin? (user/admin)");
                            line = in.nextLine();
                            if(line.equals("admin")) {
                                System.out.println("What is the username of the admin?");
                                try {
                                    String user = in.nextLine();
                                    if(user.length() > 0) {
                                        System.out.println("What is the password for '" + user + "'?");
                                        String pass = in.nextLine();
                                        if(pass.length() > 0) {
                                            if(libMan.adminExists(user, pass)) {
                                                System.out.println("Logged in... ");
                                                isAdmin = true;
                                                return true;
                                            }
                                        }
                                    }

                                } catch(Exception e) {
                                    System.out.println("There was a problem at CMD[263]");
                                }

                            }
                            break;
                        case "new": // todo: for creating a new user.
                            System.out.println("What would you like your username to be?");
                            line = in.nextLine();

                            break;
                        case "override": // todo: override is for creating a new administrator.
                            System.out.println("Now in override mode. What is the name of the new admin? ");
                            String user = in.nextLine();
                            // fixme: add more rules for creating a username/password
                            if(user.length() > 0) {
                                System.out.println("What is the password for the new admin? ");
                                String pass = in.nextLine();
                                if(pass.length() > 0) {
                                    libMan.addAdmin(line, pass);
                                    if (libMan.adminExists(line, line)) {
                                        System.out.println("The admin account has been successfully created.");
                                    }
                                }
                            }

                        default:
                            System.out.println("Please specify if you are logging in or making a new account.");
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("(ERROR CAUGHT)");
                    System.out.println("There was an issue with the login process. ");
                    return false;
                }
            }
        }

        return false;
    }




}
