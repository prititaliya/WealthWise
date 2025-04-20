package comp3350.wwsys.application;


public class Main
{
    private static String dbName="WealthWiseDB";


    public static void setDBPathName(final String name) {
        dbName = name;
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver").newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            // Database initialization failed: Unable to load JDBC driver
            throw new RuntimeException("Unable to connect to the database. Please try again later.", e);
        }

    }

    public static String getDBPathName() {
        return dbName;
    }
}

