public class Admin {
    private String username;
    private String password;

    Admin(String user, String pass) {
        this.username = user;
        this.password = pass;
    }

    // used for logging in as an admin
    public boolean isPassword(String pass) {
        return pass.equals(password);
    }
    public boolean isUsername(String user) {
        return user.equals(username);
    }

}
