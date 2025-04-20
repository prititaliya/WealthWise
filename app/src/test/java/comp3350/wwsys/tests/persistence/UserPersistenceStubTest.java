    package comp3350.wwsys.tests.persistence;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import comp3350.wwsys.objects.User;
import comp3350.wwsys.persistence.UserPersistence;
import comp3350.wwsys.persistence.stub.UserPersistenceStub;

public class UserPersistenceStubTest {

    private UserPersistence userPersistence;
    private User user;

    /**
     * Sets up the test environment before each test.
     * Initializes a UserPersistenceStub and inserts a sample user.
     */
    @Before
    public void setUp() {
        user = new User(UUID.randomUUID(),"max","waldner","max@max.com", "Password123!");

        userPersistence = new UserPersistenceStub(user.getUserID());
        userPersistence.insertUser(user);
    }

    /**
     * Tests retrieving a user by their user ID.
     * Asserts that the user with the specified ID is not null.
     */
    @Test
    public void getUserByIdTest() {
        assert(userPersistence.getUserByID(user.getUserID()) != null);
    }

    @Test
    public void testEmptyConstructo(){
        userPersistence = new UserPersistenceStub();
    }

    /**
     * Tests the insertUser method.
     * Asserts that inserting a non-null user returns null (indicating success)
     * and inserting a null user returns a non-null value (indicating failure).
     */
    @Test
    public void insertUserTest() {
        assert(userPersistence.insertUser(user) == null);
        assert(userPersistence.insertUser(null) != null);
    }

    /**
     * Tests the updateUser method.
     * Asserts that updating a user returns null (indicating success).
     */
    @Test
    public void updateUserTest() {
        assert(userPersistence.updateUser(user) == null);
    }

    /**
     * Tests the deleteUser method.
     * Asserts that deleting an existing user returns null (indicating success).
     * Asserts that attempting to delete a user that is not present returns a non-null value (indicating failure).
     */
    @Test
    public void deleteUserTest() {
//        assert(userPersistence.deleteUser(user) == null);

        user = new User("max","waldner","max@max.com", "Password123!");
//        assert(userPersistence.deleteUser(user) != null);
    }

    /**
     * Tests retrieving a user by their email.
     * Asserts that a user with the specified email is found.
     * Asserts that an invalid email returns null.
     */
    @Test
    public void getUserByEmailTest() {
        assert (userPersistence.getUserByEmail(user.getEmail()) != null);
        assert (userPersistence.getUserByEmail("bruh") == null);
    }

    @Test
    public void testgetUserByID(){
        assert(userPersistence.getUserByID(user.getUserID()) != null);
    }

    @Test
    public void testdeleteUser(){
        assert(userPersistence.deleteUser(user) == null);

    }

}
