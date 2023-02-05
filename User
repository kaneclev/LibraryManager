public class User {
    private String username;
    private String password;
    private boolean isAdmin;

    User(String user, String pass, boolean is_admin) {
        this.username = user;
        this.password = pass;
        isAdmin = is_admin;
    }

    // used for logging in as an admin
    public boolean isPassword(String pass) {
        return pass.equals(password);
    }
    public boolean isUsername(String user) {
        return user.equals(username);
    }

}
