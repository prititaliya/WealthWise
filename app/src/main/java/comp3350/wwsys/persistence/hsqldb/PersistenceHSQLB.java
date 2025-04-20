package comp3350.wwsys.persistence.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This class implements Persistence using an HSQLDB database.
 * It provides methods to connection and update SQL methods
 * for the database.
 */
public abstract class PersistenceHSQLB {

    private final String dbPath; //path to the database

    /**
     * Constructor.
     *
     * @param dbPath the path to the HSQLDB database file
     */
    protected PersistenceHSQLB(String dbPath) {
        this.dbPath = dbPath;

    }

    /**
     * Creates and returns a connection to the HSQLDB database.
     *
     * @return a Connection object to the HSQLDB database
     * @throws SQLException if a database access error occurs
     */
    protected Connection connection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";shutdown=true", "SA", "");
    }

    /**
     * Executes a SQL statement
     *
     * @param sql the SQL statement to execute
     * @param values the parameter values to substitute into the SQL statement
     * @return true if one or more rows were affected, false otherwise
     */
    protected boolean updateTable(String sql, String[] values) {
        int added = 0;
        try (Connection connection = connection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setString(i + 1, values[i]);
            }
            added = preparedStatement.executeUpdate();
        } catch (SQLException e) {
           System.err.println("Error: " + e.getMessage());
        }
        return added > 0;
    }

}
