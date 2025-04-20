package comp3350.wwsys.persistence.stub;


import java.util.ArrayList;
import java.util.UUID;

import comp3350.wwsys.objects.User;
import comp3350.wwsys.persistence.UserPersistence;

/**
 * UserPersistenceStub
 * Class to simulate the UserPersistence
 */
public class UserPersistenceStub implements UserPersistence {

    // List of users
    ArrayList<User> users;

    /**
     * Constructor for UserPersistenceStub
     * adds some users to the list
     */
    public UserPersistenceStub() {
        users = new ArrayList<>();

        users.add(new User("Max","Waldner","max@email.com", "password123!"));
        users.add(new User("Alanna","lastname","alanna@email.com", "password123!"));
        users.add(new User("prit","lastname1","prit@email.com", "password123!"));
        users.add(new User("Junior","lastname2","junior@email.com", "password123!"));
        users.add(new User("Mahas","lastname3","mahas@email.com", "password123!"));
    }

    public UserPersistenceStub(UUID userId){
        users = new ArrayList<>();
        users.add(new User(userId,"Max","Waldner","max@email.com", "password123!"));
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param ID the unique identifier of the user
     * @return the user object corresponding to the given ID, or null if not found
     */
    @Override
    public  User getUserByID(UUID ID) {
        User foundUser = null;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserID().equals(ID) ) {
                foundUser = users.get(i);
            }
        }
        return foundUser;
    }


    /**
     * Inserts a new user into the database.
     *
     * @param newUser the user object to be inserted
     * @return a status message or identifier of the inserted user
     */
    @Override
    public String insertUser(User newUser) {
        String result = "error";
        if (newUser != null) {
            users.add(new User(UUID.randomUUID(), newUser.getFirstName(), newUser.getLastName(), newUser.getEmail(), newUser.getPassword()));
            result = null;
        }
        return result;
    }

    /**
     * Updates an existing user's details in the database.
     *
     * @param currentUser the user object with updated details
     * @return a status message indicating success or failure
     */
    @Override
    public String updateUser(User currentUser) {

        String bruh = "Cannot update user";
        for (int i = 0; i < users.size(); i++){
            User user = users.get(i);
            if (user.getUserID().equals(currentUser.getUserID())) {
                users.set(i, currentUser);
                bruh = null;
            }
        }
        return bruh;
    }

    /**
     * Deletes a user from the database.
     *
     * @param currentUser the user object to be deleted
     * @return a status message indicating success or failure
     */
    @Override
    public String deleteUser(User currentUser) {
        String userDeleted = "Cannot delete User";
        User foundUser = getUserByID(currentUser.getUserID());
        if (foundUser != null) {
            users.remove(foundUser);
            userDeleted = null;
        }
        return userDeleted;

    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email of the user
     * @return the user object corresponding to the given email, or null if not found
     */
    @Override
    public User getUserByEmail(String email) {
        User userReturn = null;
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                userReturn = user;
            }
        }
        return userReturn;
    }

}