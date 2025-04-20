package comp3350.wwsys.business;

import static comp3350.wwsys.business.UserValidator.validateInsert;

import java.util.UUID;

import comp3350.wwsys.application.Services;
import comp3350.wwsys.objects.User;

import comp3350.wwsys.persistence.UserPersistence;

/*
 * UserService class handles the logic for user related services.
 */

public class UserService {

    private final UserPersistence userPersistence;
    private User currentUser;

    /**
     * Constructor for UserService
     */
    public UserService() {
        this.userPersistence = Services.getUserPersistence();
    }

    /**
     * Constructor for stub database in UserService
     */
    public UserService(UserPersistence userPersistence) {
        this.userPersistence = userPersistence;
    }



    /**
     * Login method for users.
     *
     * @param email    The user's email.
     * @param password The user's password.
     * @throws UserValidationException if the login fields are invalid or if the user
     *                                  with the provided credentials is not found.
     */
    public void login(String email, String password) throws UserValidationException {
        UserValidator.validateLogin(email, password);
        User user = userPersistence.getUserByEmail(email);

        if (user == null || !performLogin(email, password, user)) {
            throw new UserValidationException(StringConfig.USER_INCORRECT_CREDENTIALS);
        }
    }

    /**
     * Register a new user.
     *
     * @param firstName       The first name of the user.
     * @param lastName        The last name of the user.
     * @param email           The email of the user.
     * @param password        The password of the user.
     * @param confirmPassword The repeated password for confirmation.
     * @throws UserValidationException if the signup fields are invalid or if the user
     *                                  cannot be created (e.g., duplicate email).
     */
    public void addUser(String firstName, String lastName, String email, String password, String confirmPassword) throws UserValidationException {
        validateInsert(email, password, confirmPassword, firstName, lastName);

        User accountCreated = new User(firstName, lastName, email, password);
        String result = userPersistence.insertUser(accountCreated);

        if (result != null) {

            if (result.toLowerCase().contains("email") || result.toLowerCase().contains("duplicate")) {
                throw new UserValidationException(StringConfig.USER_EMAIL_EXISTS);
            } else {
                throw new UserValidationException(result);
            }
        }
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email The email of the user to retrieve.
     * @return The user object corresponding to the given email.
     * @throws UserValidationException if no user is found for the given email.
     */
    public User getUserByEmail(String email) throws UserValidationException {
        User user = userPersistence.getUserByEmail(email);
        if (user == null) {
            throw new UserValidationException(StringConfig.USER_NOT_FOUND);
        }
        return user;
    }

    /**
     * Checks the user, it sets the current user and returns true; otherwise, it returns false.
     *
     * @param email    The user's email address.
     * @param password The user's password.
     * @param user     The user object for comparison.
     * @return True if the email and password match, false otherwise.
     */
    private boolean performLogin(String email, String password, User user) throws UserValidationException {
        boolean result = false;
        if (user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {
            try {
                setCurrentUser(user);
                result = true;
            } catch (UserValidationException e) {
                throw new UserValidationException(StringConfig.USER_LOGIN_ERROR);
            }
        }
        return result;
    }

    /**
     * Checks if a user exists in the system by their unique ID.
     *
     * @param user The user ID.
     * @return True if the user exists, otherwise false.
     */
    public boolean doesUserExist(UUID user) {
        return userPersistence.getUserByID(user) != null;
    }

    /**
     * Sets the currently logged-in user.
     *
     * @param user The user to set as currently logged in.
     */
    public void setCurrentUser(User user) throws UserValidationException {
        if (user != null) {
            this.currentUser = user;
        } else {
            throw new UserValidationException(StringConfig.USER_NULL_ERROR);
        }
    }

    /**
     * Retrieves the currently logged-in user.
     *
     * @return The current user.
     * @throws UserValidationException if no user is logged in.
     */
    public User getCurrentUser() throws UserValidationException {
        if (currentUser == null) {
            throw new UserValidationException(StringConfig.USER_NO_CURRENT_USER);
        }
        return currentUser;
    }

    /**
     * Updates user information, including password change validation.
     */
    public void updateUser(User user, String confirmPassword) throws UserValidationException {
        UserValidator.updateUser(user, confirmPassword);
        String result = userPersistence.updateUser(user);
        if (result == null) {
            setCurrentUser(user);
        } else {
            throw new UserValidationException(StringConfig.USER_UPDATE_ERROR);
        }
    }

    /**
     * Updates user profile information without changing the password.
     */
    public void updateUserInfo(User user) throws UserValidationException {
        UserValidator.updateUser(user);
        String result = userPersistence.updateUser(user);
        if (result == null) {
            setCurrentUser(user);
        } else {
            throw new UserValidationException(StringConfig.USER_UPDATE_ERROR);
        }
    }

    /**
     * Deletes a user account from the system.
     *
     * @param user The user to delete.
     * @throws UserValidationException if the user cannot be deleted.
     */
    public void deleteAccount(User user) throws UserValidationException {
        if (user != null) {
            String result = userPersistence.deleteUser(user);
            if (result != null) {
                throw new UserValidationException(StringConfig.USER_DELETE_ERROR);
            }
        } else {
            throw new UserValidationException(StringConfig.USER_NOT_FOUND);
        }
    }

} //UserService