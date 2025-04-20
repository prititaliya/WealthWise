package comp3350.wwsys.persistence;

import java.util.UUID;

import comp3350.wwsys.objects.User;

/**
 * UserPersistence - Interface defining data access methods for user management.
 * Provides methods for retrieving, inserting, updating, and deleting user records.
 */
public interface UserPersistence {

    /**
     * Retrieves a user by their unique ID.
     *
     * @param ID the unique identifier of the user
     * @return the user object corresponding to the given ID, or null if not found
     */
    User getUserByID(UUID ID);

    /**
     * Inserts a new user into the database.
     *
     * @param newUser the user object to be inserted
     * @return a status message or identifier of the inserted user
     */
    String insertUser(User newUser);

    /**
     * Updates an existing user's details in the database.
     *
     * @param currentUser the user object with updated details
     * @return a status message indicating success or failure
     */
    String updateUser(User currentUser);

    /**
     * Deletes a user from the database.
     *
     * @param currentUser the user object to be deleted
     * @return a status message indicating success or failure
     */
    String deleteUser(User currentUser);


    /**
     * Retrieves a user by their email address.
     *
     * @param email the email of the user
     * @return the user object corresponding to the given email, or null if not found
     */
    User getUserByEmail(String email);
}
