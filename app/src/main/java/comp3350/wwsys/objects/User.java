package comp3350.wwsys.objects;

import java.util.UUID;

public class User {
    private String firstname;
    private String lastname;
    private String userEmail;
    private String password;
    private final UUID userID;

    /*
     * Constructor for User
     */
    public User(String firstname, String lastname, String userEmail, String password) {
        setDefaults(firstname,lastname,userEmail,password);
        this.userID = null;
    }

    public User(UUID id, String firstname, String lastname, String userEmail, String password) {
        setDefaults(firstname,lastname,userEmail,password);
        this.userID = id;
    }

    private void setDefaults(String firstname, String lastname, String userEmail, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.userEmail = userEmail;
        this.password = password;
    }

    public String getFirstName() { return firstname; }
    public String getLastName() { return lastname; }
    public String getEmail() { return userEmail; }
    public UUID getUserID() { return this.userID; }
    public String getPassword() { return password; }
} // User Class