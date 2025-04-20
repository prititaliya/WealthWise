package comp3350.wwsys.application;

import comp3350.wwsys.persistence.EntryPersistence;
import comp3350.wwsys.persistence.UserPersistence;
import comp3350.wwsys.persistence.hsqldb.EntryPersistenceHSQLDB;
import comp3350.wwsys.persistence.hsqldb.UserPersistenceHSQLDB;

public class Services {

    private static UserPersistence userPersistence = null;
    private static EntryPersistence entryPersistence = null;

    public static synchronized UserPersistence getUserPersistence() {
        if (userPersistence == null) {
            userPersistence = new UserPersistenceHSQLDB(Main.getDBPathName());
        }
        return userPersistence;
    }

    public static synchronized EntryPersistence getEntryPersistence() {
        if (entryPersistence == null) {
            entryPersistence = new EntryPersistenceHSQLDB(Main.getDBPathName());
        }
        return entryPersistence;
    }
}
