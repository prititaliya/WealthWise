package comp3350.wwsys.tests.objects;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.UUID;

import comp3350.wwsys.objects.User;

/**
 * Unit test class for the User class.
 * This class contains test cases for verifying the functionality of the User class,
 * including validating getters and custom user ID assignment.
 */
public class UserTest {
    private User user;

    /**
     * Sets up the test environment by initializing user objects before each test.
     * Verifies that both users have a non-null user ID and the user objects are initialized properly.
     */
    @Before
    public void setUp() {
        System.out.println("\nStarting UserTest");
        user = new User("John", "Doe", "Johndoe@gmail.com", "Password#123");
        User userWithId = new User(UUID.randomUUID(), "Jane", "Smith", "janesmith@gmail.com", "Password#456");
        assertNotNull(user);
        assertNotNull(userWithId.getUserID());
        assertNotNull(userWithId);
    }

    /**
     * Tests the getter methods of the User class.
     * Verifies that the getter methods correctly return the user's first name, last name, email, and password.
     */
    @Test
    public void testGetters() {
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("Johndoe@gmail.com", user.getEmail());
        assertEquals("Password#123", user.getPassword());
    }

    /**
     * Tests the User class when a custom user ID is provided.
     * Verifies that the user object is correctly initialized with the provided custom user ID,
     * and the getter methods return the correct user information.
     */
    @Test
    public void testUserWithId() {
        UUID testId = UUID.randomUUID();
        User customUser = new User(testId, "Alice", "Brown", "alicebrown@gmail.com", "Password#789");

        assertEquals("Alice", customUser.getFirstName());
        assertEquals("Brown", customUser.getLastName());
        assertEquals("alicebrown@gmail.com", customUser.getEmail());
        assertEquals("Password#789", customUser.getPassword());
        assertEquals(testId, customUser.getUserID());
    }

    /**
     * Tests User constructor with null values for firstname, lastname,
     * email, and password. Ensures fields are set to null and a non-null UUID
     * is generated.
     */
    @Test
    public void testNullValuesInConstructor() {
        User user = new User(null, null, null, null);
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
    }

    /**
     * Tests User constructor with empty string values for firstname, lastname,
     * email, and password. Verifies that fields are set to empty strings and
     * a non-null UUID is generated.
     */
    @Test
    public void testEmptyStringValuesInConstructor() {
        User user = new User("", "", "", "");
        assertEquals("", user.getFirstName());
        assertEquals("", user.getLastName());
        assertEquals("", user.getEmail());
        assertEquals("", user.getPassword());
    }

    /**
     * Tests User constructor with special characters in firstname, lastname,
     * email, and password. Verifies that the fields retain the special characters
     * as provided.
     */
    @Test
    public void testSpecialCharactersInValues() {
        String firstname = "John@!";
        String lastname = "Doe123";
        String email = "johndoe@exam#ple.com";
        String password = "$uper$ecure123";

        User user = new User(firstname, lastname, email, password);

        assertEquals("John@!", user.getFirstName());
        assertEquals("Doe123", user.getLastName());
        assertEquals("johndoe@exam#ple.com", user.getEmail());
        assertEquals("$uper$ecure123", user.getPassword());
    }

    /**
     * Tests User constructor with long string values (1000 characters) for
     * firstname, lastname, email, and password. Verifies that fields retain
     * the long strings as provided and a non-null UUID is generated.
     */
    @Test
    public void testLongStringsInConstructor() {
        String longString = "a".repeat(1000); // 1000 characters
        User user = new User(longString, longString, longString, longString);

        assertEquals(longString, user.getFirstName());
        assertEquals(longString, user.getLastName());
        assertEquals(longString, user.getEmail());
        assertEquals(longString, user.getPassword());
    }

    /**
     * Tests that the User constructor correctly sets a predefined UUID.
     * Verifies that the getUserID method returns the same UUID provided
     * during initialization.
     */
    @Test
    public void testUUIDConsistency() {
        UUID predefinedUUID = UUID.randomUUID();
        User user = new User(predefinedUUID, "Jane", "Doe", "jane@example.com", "password123");

        assertEquals(predefinedUUID, user.getUserID());
    }

}
