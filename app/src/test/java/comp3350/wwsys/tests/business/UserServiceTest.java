package comp3350.wwsys.tests.business;

import static org.junit.Assert.*;


import org.junit.Test;

import java.lang.reflect.Field;
import java.util.UUID;

import comp3350.wwsys.business.UserService;
import comp3350.wwsys.business.UserValidationException;
import comp3350.wwsys.persistence.UserPersistence;
import comp3350.wwsys.objects.User;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;
import java.lang.reflect.Method;


public class UserServiceTest {
    @Mock
    private UserPersistence mockUserPersistence;

    private UserService userService;

    @Before
    public void setUp()
    {
        mockUserPersistence = mock(UserPersistence.class);
        userService = new UserService(mockUserPersistence);
    }

    /**
     * Tests the default constructor of UserService to ensure the userPersistence field is initialized correctly.
     */
    @Test
    public void testDefaultConstructorInitialization() throws NoSuchFieldException, IllegalAccessException {
        mockUserPersistence = mock(UserPersistence.class);

        // Act
        UserService userService = new UserService();

        // Use reflection to access the private userPersistence field
        Field field = UserService.class.getDeclaredField("userPersistence");
        field.setAccessible(true); // Allow access to the private field
        UserPersistence actualUserPersistence = (UserPersistence) field.get(userService);

        // Assert
        assertNotNull(actualUserPersistence);
    }

    /**
     * Tests the doesUserExist method when a user exists in the system.
     */
    @Test
    public void testDoesUserExistTrue() {
        // Arrange
        UUID userId = UUID.randomUUID();
        User mockUser = new User("John", "Doe", "john.doe@example.com", "password123");
        when(mockUserPersistence.getUserByID(userId)).thenReturn(mockUser);

        // Act
        boolean result = userService.doesUserExist(userId);

        // Assert
        assertTrue(result); // Verify it returns true when the user exists
    }

    /**
     * Tests the doesUserExist method when a user does not exist in the system.
     */
    @Test
    public void testDoesUserExistFalse() {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(mockUserPersistence.getUserByID(userId)).thenReturn(null);

        // Act
        boolean result = userService.doesUserExist(userId);

        // Assert
        assertFalse(result); // Verify it returns false when the user does not exist
    }

    /**
     * Tests the login method to validate the exception when email and password are invalid.
     */
    @Test(expected = UserValidationException.class)
    public void testLoginValidationFailure() throws UserValidationException {
        // Arrange
        String email = ""; // Invalid email
        String password = "";

        // Act
        userService.login(email, password);

        // Assert: Exception is expected
    }

    /**
     * Tests the login method to validate the exception when the user is null.
     */
    @Test(expected = UserValidationException.class)
    public void testLoginNullUser() throws UserValidationException, NoSuchFieldException, IllegalAccessException {
        // Arrange
        String email = "nonexistent@example.com"; // Non-existent user
        String password = "InvalidPassword";

        // Mocking userPersistence to return null
        UserPersistence mockUserPersistence = Mockito.mock(UserPersistence.class);
        Mockito.when(mockUserPersistence.getUserByEmail(email)).thenReturn(null);

        UserService userService = new UserService();

        // Inject the mock into UserService
        Field field = UserService.class.getDeclaredField("userPersistence");
        field.setAccessible(true);
        field.set(userService, mockUserPersistence);

        // Act
        userService.login(email, password);

        // Assert: Exception is expected
    }

    /**
     * Tests the login method to validate the exception when performLogin fails.
     */
    @Test
    public void testLoginPerformLoginFails() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException {
        // Arrange
        UserService userService = new UserService();
        UserPersistence mockUserPersistence = Mockito.mock(UserPersistence.class);

        // Mock a valid user
        User mockUser = new User("John", "Doe", "existing@example.com", "CorrectPassword12!");
        Mockito.when(mockUserPersistence.getUserByEmail("existing@example.com")).thenReturn(mockUser);

        // Use reflection to mock private performLogin
        Method performLoginMethod = UserService.class.getDeclaredMethod("performLogin", String.class, String.class, User.class);
        performLoginMethod.setAccessible(true);

        // Inject the mock into userService
        Field field = UserService.class.getDeclaredField("userPersistence");
        field.setAccessible(true);
        field.set(userService, mockUserPersistence);

        // Act: Simulate invalid credentials
        UserValidationException exception = assertThrows(
                UserValidationException.class,
                () -> userService.login("existing@example.com", "WrongPassword!")
        );

        // Assert
        assertEquals("Password must contain at least one number.", exception.getMessage());
    }


    /**
     * Tests the addUser method for successful user creation.
     * Ensures that the userPersistence.insertUser method is called with the correct arguments.
     */
    @Test
    public void testAddUserSuccess() throws UserValidationException {
        // Arrange
        String firstName = "Jane";
        String lastName = "Doe";
        String email = "jane.doe@example.com";
        String password = "Password123!";
        String confirmPassword = "Password123!";

        // Stub to simulate successful insertion
        when(mockUserPersistence.insertUser(any(User.class))).thenReturn(null);

        // Act
        userService.addUser(firstName, lastName, email, password, confirmPassword);

        // Assert
        verify(mockUserPersistence).insertUser(any(User.class)); // Ensures insertUser was called
    }

    /**
     * Tests the addUser method for validation failure when invalid inputs are provided.
     * Verifies that a UserValidationException is thrown if validation fails.
     */
    @Test(expected = UserValidationException.class)
    public void testAddUserValidationFails() throws UserValidationException {
        // Arrange
        String firstName = "Jane";
        String lastName = "Doe";
        String email = ""; // Invalid email
        String password = "Password123!";
        String confirmPassword = "Password123!";

        // Act
        userService.addUser(firstName, lastName, email, password, confirmPassword);
    }

    /**
     * Tests the addUser method for a duplicate user scenario.
     * Verifies that a UserValidationException is thrown when the user already exists.
     */
    @Test(expected = UserValidationException.class)
    public void testAddUserDuplicate() throws UserValidationException {
        // Arrange
        User newUser = new User("Jane", "Doe", "jane.doe@example.com", "password123");
        when(mockUserPersistence.insertUser(newUser)).thenReturn("User already exists");

        // Act
        userService.addUser("Jane", "Doe", "jane.doe@example.com", "password123", "password123");
    }

    /**
     * Tests the addUser method for a failed insertion into the database.
     * Verifies that a UserValidationException is thrown with the correct error message.
     */
    @Test
    public void testAddUserThrowsExceptionWhenInsertFails() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        UserService userService = new UserService();
        UserPersistence mockUserPersistence = Mockito.mock(UserPersistence.class);

        // Mock the behavior of userPersistence to return a non-null result
        Mockito.when(mockUserPersistence.insertUser(Mockito.any(User.class))).thenReturn("Insert failed due to database error");

        // Inject the mock into userService
        Field field = UserService.class.getDeclaredField("userPersistence");
        field.setAccessible(true);
        field.set(userService, mockUserPersistence);

        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String password = "Password123!";
        String confirmPassword = "Password123!";

        // Act & Assert
        UserValidationException exception = assertThrows(
                UserValidationException.class,
                () -> userService.addUser(firstName, lastName, email, password, confirmPassword)
        );

        // Verify the exception message
        assertEquals("Insert failed due to database error", exception.getMessage());
    }

    /**
     * Tests the getUserByEmail method for successful retrieval.
     * Verifies that the returned user matches the expected user.
     */
    @Test
    public void testGetUserByEmailSuccess() throws UserValidationException {
        // Arrange
        User user = new User("John", "Doe", "john.doe@example.com", "password123");
        when(mockUserPersistence.getUserByEmail("john.doe@example.com")).thenReturn(user);

        // Act
        User retrievedUser = userService.getUserByEmail("john.doe@example.com");

        // Assert
        assertEquals(user, retrievedUser);
    }

    /**
     * Tests the getUserByEmail method for a case where the user does not exist.
     * Verifies that a UserValidationException is thrown.
     */
    @Test(expected = UserValidationException.class)
    public void testGetUserByEmailNotFound() throws UserValidationException {
        // Arrange
        when(mockUserPersistence.getUserByEmail("invalid@example.com")).thenReturn(null);

        // Act
        userService.getUserByEmail("invalid@example.com");
    }


    @Test
    public void testGetCurrentUserThrowsExceptionWhenCurrentUserIsNull() {
        // Arrange
        UserService userService = new UserService(); // Assuming currentUser is initialized to null

        // Act & Assert
        UserValidationException exception = assertThrows(
                UserValidationException.class,
                userService::getCurrentUser
        );

        // Verify the exception message
        assertEquals("No user was found.", exception.getMessage());
    }


    @Test
    public void testPerformLoginViaReflection() throws Exception {

        UserService userService = new UserService();
        User user = new User("Jane", "Doe", "jane.doe@example.com", "ValidPassword123!");
        // Use reflection to access the private method
        Method method = UserService.class.getDeclaredMethod("performLogin", String.class, String.class, User.class);
        method.setAccessible(true);

        // Act
        Object result = method.invoke(userService, user.getEmail(), user.getPassword(), user);
        assertNotNull(result); // Ensure the result is not null before unboxing
        boolean loginResult = (boolean) result;

        // Assert
        assertTrue(loginResult);
    }

    @Test
    public void testPerformLoginFailureViaReflection() throws Exception {
        // Arrange
        UserService userService = new UserService();
        User user = new User("Jane", "Doe", "jane.doe@example.com", "ValidPassword123!");

        // Use reflection to access the private method
        Method method = UserService.class.getDeclaredMethod("performLogin", String.class, String.class, User.class);
        method.setAccessible(true);

        // Act
        Object result = method.invoke(userService, "wrong.email@example.com", "WrongPassword!", user);
        assertNotNull(result); // Ensure the result is not null before unboxing
        boolean loginResult = (boolean) result;

        // Assert
        assertFalse(loginResult);
    }

    /**
     * Tests the updateUserInfo method for a successful update scenario.
     * Verifies that the persistence layer is called and the current user is updated.
     */
    @Test
    public void testUpdateUserInfoSuccess() throws UserValidationException {
        // Arrange
        User user = new User("Jane", "Doe", "jane.doe@example.com", "ValidPassword123!");
        when(mockUserPersistence.updateUser(user)).thenReturn(null);

        // Act
        userService.updateUserInfo(user);

        // Assert
        verify(mockUserPersistence, times(1)).updateUser(user);
        assertEquals(user, userService.getCurrentUser());
    }

    /**
     * Tests the updateUserInfo method when the update fails.
     * Verifies that a UserValidationException is thrown with the correct error message.
     */
    @Test(expected = UserValidationException.class)
    public void testUpdateUserFailed() throws UserValidationException {
        // Arrange
        User user = new User("John", "Doe", "john.doe@example.com", "newPassword");
        when(mockUserPersistence.updateUser(user)).thenReturn("Update failed");

        // Act
        userService.updateUser(user, "newPassword");
    }

    /**
     * Tests the updateUserInfo method for a failure scenario where the persistence layer returns an error message.
     * Verifies that a UserValidationException is thrown with the correct message.
     */
    @Test
    public void testUpdateUserInfoFailure() {
        // Arrange
        User user = new User("Jane", "Doe", "jane.doe@example.com", "ValidPassword123");
        String errorMessage = "Password must contain at least one special character.";
        when(mockUserPersistence.updateUser(user)).thenReturn(errorMessage);

        try {
            // Act
            userService.updateUserInfo(user);
            fail("Expected UserValidationException not thrown");
        } catch (UserValidationException e) {
            // Assert
            assertEquals(errorMessage, e.getMessage());
        }
    }

    /**
     * Tests the updateUser method when the persistence layer fails to update the user.
     * Verifies that a UserValidationException is thrown with the appropriate error message.
     */
    @Test
    public void testUpdateUserPersistenceFails() {
        // Arrange
        User user = new User("John", "Doe", "john.doe@example.com", "ValidPassword123!");
        String confirmPassword = "ValidPassword123!";
        String errorMessage = "Database update failed";
        when(mockUserPersistence.updateUser(user)).thenReturn(errorMessage);

        try {
            // Act
            userService.updateUser(user, confirmPassword);
            fail("Expected UserValidationException not thrown");
        } catch (UserValidationException e) {
            // Assert
            assertEquals("Could not update user information.", e.getMessage());
        }
    }

    /**
     * Tests the updateUser method for successful completion.
     * Verifies that the persistence layer is called and the current user is updated.
     */
    @Test
    public void testUpdateUserSetsCurrentUser() throws UserValidationException {
        // Arrange
        User user = new User("John", "Doe", "john.doe@example.com", "ValidPassword123!");
        String confirmPassword = "ValidPassword123!";
        when(mockUserPersistence.updateUser(user)).thenReturn(null);

        // Act
        userService.updateUser(user, confirmPassword);

        // Assert
        verify(mockUserPersistence, times(1)).updateUser(user);
        assertEquals(user, userService.getCurrentUser());
    }

    /**
     * Tests the updateUserInfo method to verify it throws a UserValidationException when the update fails.
     */
    @Test
    public void testUpdateUserInfoThrowsExceptionWhenUpdateFails() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        User user = new User("John", "Doe", "john.doe@example.com", "ValidPassword123!");
        UserService userService = new UserService();

        // Mocking userPersistence
        UserPersistence mockUserPersistence = Mockito.mock(UserPersistence.class);
        Mockito.when(mockUserPersistence.updateUser(user)).thenReturn("Update failed due to database error");

        // Inject the mock into userService using reflection
        Field field = UserService.class.getDeclaredField("userPersistence");
        field.setAccessible(true);
        field.set(userService, mockUserPersistence);

        // Act & Assert
        UserValidationException exception = assertThrows(
                UserValidationException.class,
                () -> userService.updateUserInfo(user)
        );

        // Verify the exception message
        assertEquals("Could not update user information.", exception.getMessage());
    }

    /**
     * Tests the deleteAccount method for successful account deletion.
     * Verifies that the persistence layer is called.
     */
    @Test
    public void testDeleteAccount() throws UserValidationException {
        // Arrange
        User user = new User("John", "Doe", "john.doe@example.com", "password123");
        when(mockUserPersistence.deleteUser(user)).thenReturn(null);

        // Act
        userService.deleteAccount(user);

        // Assert
        verify(mockUserPersistence, times(1)).deleteUser(user);
    }

    /**
     * Tests the deleteAccount method for a failure scenario.
     * Verifies that a UserValidationException is thrown when the deletion fails.
     */
    @Test(expected = UserValidationException.class)
    public void testDeleteAccountFailed() throws UserValidationException {
        // Arrange
        User user = new User("John", "Doe", "john.doe@example.com", "password123");
        when(mockUserPersistence.deleteUser(user)).thenReturn("Delete failed");

        // Act
        userService.deleteAccount(user);
    }

    /**
     * Tests the deleteAccount method for a null user scenario.
     * Verifies that a UserValidationException is thrown when attempting to delete a null user.
     */
    @Test
    public void testDeleteAccountUserIsNull() {
        try {
            // Act
            userService.deleteAccount(null);
            fail("Expected UserValidationException not thrown");
        } catch (UserValidationException e) {
            // Assert
            assertEquals("User account not found.", e.getMessage());
        }
    }

    /**
     * Tests the updateUserInfo method when the password contains invalid characters.
     * Verifies that a UserValidationException is thrown with the correct error message.
     */
    @Test
    public void testUpdateUserInvalidPasswordCharacter() {
        // Arrange
        User user = new User("Jane", "Doe", "jane.doe@example.com", "invalid@password");

        try {
            // Act
            userService.updateUserInfo(user);
            fail("Expected UserValidationException not thrown");
        } catch (UserValidationException e) {
            // Assert
            assertEquals("Password must contain at least one uppercase letter.", e.getMessage());
        }
    }


}


