package comp3350.wwsys.persistence.hsqldb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import comp3350.wwsys.objects.User;
import comp3350.wwsys.persistence.UserPersistence;

/**
 * This class implements UserPersistence using an HSQLDB database.
 * It provides methods to insert, update, delete, and fetch users
 * from the database.
 */
public class UserPersistenceHSQLDB extends PersistenceHSQLB implements UserPersistence {

    /**
     * Constructor.
     *
     * @param dbPath the path to the HSQLDB database file
     */
    public UserPersistenceHSQLDB(final String dbPath)  {
        super(dbPath);
    }

    /**
     * Returns a User object matching the given UUID, or null if not found.
     *
     * @param id the UUID of the user
     * @return the User with the given UUID, or null if not found
     */
    @Override
    public User getUserByID(UUID id) {

        String sql = "SELECT * FROM Users WHERE ID = ?";
        String[] values = {id.toString()};
        return getUserHelper(sql,values);
    }

    /**
     * Retrieves the User object matching the given email, or null if not found.
     *
     * @param email the email of the user to retrieve
     * @return the User with the specified email, or null if not found
     */
    @Override
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM Users WHERE EMAIL = ?";
        String[] values = {email};
        return getUserHelper(sql,values);

    }

    /**
     * Checks if a user with the given email already exists in the database.
     *
     * @param email the email to check
     * @return true if there is a user with the provided email, false otherwise
     */
    private boolean isUserEmailTaken(String email) {
        String sql = "SELECT * FROM Users WHERE EMAIL = ?";
        String[] values = {email};
        return getUserHelper(sql,values) != null;
    }

    /**
     * Inserts a new user into the database.
     *
     * @param newUser the User object to insert
     * @return null if inserted, message otherwise
     */
    @Override
    public String insertUser(User newUser) {
        String result = null;

        if (!isUserEmailTaken(newUser.getEmail())) {
            String sql = "INSERT INTO PUBLIC.USERS VALUES (?, ?, ?, ?, ?)";
            String[] values = {UUID.randomUUID().toString(), newUser.getFirstName(), newUser.getLastName(), newUser.getEmail(), newUser.getPassword()};
            if (!updateTable(sql,values) ) {
                result = "Cannot Add User to Database";
            }
        } else {
            result = "Email already in use";
        }

        return  result;
    }

    /**
     * Updates the first name of the specified user in the database.
     *
     * @param currentUser the User whose first name will be updated
     * @return null if user updated, error message otherwise
     */
    @Override
    public String updateUser(User currentUser) {
        String result = null;
        if ( currentUser != null) {

            User findUser = getUserByEmail(currentUser.getEmail());
            if (findUser == null || findUser.getUserID().equals(currentUser.getUserID())) {
                // Continue with the update if no other user with the same email or it's the same user
                String sql = "UPDATE PUBLIC.USERS SET "
                        + "FIRSTNAME = ? , "
                        + "LASTNAME = ? , "
                        + "EMAIL = ? , "
                        + "PASSWORD = ? "
                        + "WHERE ID = ? ";

                String[] values = {
                        currentUser.getFirstName(),
                        currentUser.getLastName(),
                        currentUser.getEmail(),
                        currentUser.getPassword(),
                        currentUser.getUserID().toString()};

                if (!updateTable(sql, values)) {
                    result = "Cannot Update User To Database";
                }
            } else {
                result = "Email Taken";
            }
        }
        return result;
    }

    /**
     * Deletes the specified user from the database.
     *
     * @param currentUser the User to delete
     * @return null if user was deleted, error otherwise
     */
    @Override
    public String deleteUser(User currentUser) {
        String result = null;
            if(currentUser==null){
                result="Current User is Null";
            }else{
                String sql = "DELETE FROM PUBLIC.USERS WHERE ID = ?";
                String[] values = {currentUser.getUserID().toString()};

                if (!updateTable(sql, values)) {
                    result = "Cannot delete User";
                }
            }

        return result;
    }

    /**
     * Helper method to retrieve a User from the database using a given SQL query and values.
     *
     * @param sql    The SQL query to be executed (should contain placeholders '?').
     * @param values An array of string values to be bound to the query parameters.
     * @return The User object if found, otherwise null.
     */
    private User getUserHelper(String sql, String[] values) {
        User user = null;
        try (Connection conn = connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                pstmt.setString(i+1, values[i]);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user = new User(
                            UUID.fromString(rs.getString("ID")),
                            rs.getString("FIRSTNAME"),
                            rs.getString("LASTNAME"),
                            rs.getString("EMAIL"),
                            rs.getString("PASSWORD")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return user;
    }
}
