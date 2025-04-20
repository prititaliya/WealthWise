package comp3350.wwsys.tests.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import comp3350.wwsys.business.UserService;
import comp3350.wwsys.business.UserValidationException;
import comp3350.wwsys.objects.User;
import comp3350.wwsys.persistence.hsqldb.UserPersistenceHSQLDB;
import comp3350.wwsys.tests.Utils.TestUtils;

public class UserServiceIT {
    private UserService userService;
    @Before
    public void setUp() throws IOException {
        File tempDB = TestUtils.copyDB();
        String path = tempDB.getAbsolutePath().replace(".script", "");
        userService = new UserService(new UserPersistenceHSQLDB(path));
    }
    @Test
    public void testget() throws UserValidationException {
        System.out.println("Testing add user.");
        String email = "prit@italiya.com";
        User user = userService.getUserByEmail(email);
        assertNotNull(user);
        assertEquals("Prit", user.getFirstName());
        assertEquals("Italiya", user.getLastName());
        assertNotNull(user.getUserID());
        assertEquals(email, user.getEmail());
    }
    @Test
    public void testLogin() {
        System.out.println("Testing login.");
        String email = "prit@italiya.com";
        String password = "Prit234!";
       assertDoesNotThrow(() -> userService.login(email, password));

    }
    @Test
    public void testLoginInvalidLogin() {
        System.out.println("Testing login with invalid email.");
        final String wrongEmail = "prit@.com";
        final String password ="Prit234!";
        assertThrows(UserValidationException.class, () -> userService.login(wrongEmail, password));
       final String email = "prit@italiya.com";
       final String password2 = "Prit2323!";
       assertThrows(UserValidationException.class, () -> userService.login(email, password2));
       assertDoesNotThrow(()->userService.login(email, password));
    }

    @Test
    public void testAddUser() {
        System.out.println("Testing add user.");
        String firstName = "Prit";
        String lastName = "Italiya";
        String email = "prit@italiya1.com";
        String password = "Prit234!";
        String confirmPassword = "Prit234!";
        assertDoesNotThrow(()->userService.addUser(firstName, lastName, email, password, confirmPassword));
    }
    @Test
    public void testAddUserWithSameInfo() {
        System.out.println("Testing add user.");
        String firstName = "Prit";
        String lastName = "Italiya";
        String email = "prit@italiya.com";
        String password = "Prit234!";
        String confirmPassword = "Prit234!";
        assertThrows(UserValidationException.class,(()->userService.addUser(firstName, lastName, email, password, confirmPassword)));
    }   @Test
    public void testAddUserWithInvalidInfo() {
        System.out.println("Testing add user.");
        String firstName = "Prit";
        String lastName = "Italiya";
        String email = "";
        String password = "Prit234!";
        String confirmPassword = "Prit234!";
        assertThrows(UserValidationException.class,(()->userService.addUser(firstName, lastName, email, password, confirmPassword)));
        String email1="prit@italiya1.com";
        String password1="Prit234";
        assertThrows(UserValidationException.class,(()->userService.addUser(firstName, lastName, email1, password1, confirmPassword)));
    }
    @Test
    public void testGetUserByEmail() throws UserValidationException {
        System.out.println("Testing get user by email.");
        String email = "prit@italiya.com";
        User user = userService.getUserByEmail(email);
        assertNotNull(user);
    }    @Test
    public void testGetUserByEmail1(){
        System.out.println("Testing get user by email.");
        String email = "prit@italiy1a.com";
        try{

       userService.getUserByEmail(email);
        }catch (Exception e) {
            assertEquals("User account not found.",e.getMessage());
        }
    }

    @Test
    public void testDoesUserExist() {
        System.out.println("Testing does user exist.");
        UUID userID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        assertTrue(userService.doesUserExist(userID));
    }
    @Test
    public void testDoesUserExist1() {
        System.out.println("Testing does user exist.");
        UUID userID = UUID.fromString("550e8000-e29b-41d4-a716-446655440000");
        assertFalse(userService.doesUserExist(userID));
    }
    @Test
    public void testGetCurrentUser(){
        System.out.println("Testing get current user.");
       assertThrows(UserValidationException.class, ()->userService.getCurrentUser());
    }
    @Test
    public void setCurrentUser() throws UserValidationException {
        System.out.println("Testing set current user.");
        UUID userID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        User user = new User(userID,"Prit","Italiya","prit@italiya.com","Prit234!");
        userService.setCurrentUser(user);
        User user1=userService.getCurrentUser();
        assertEquals(user1,user);
    }

   @Test
    public void tesrtDeleteUser(){
        System.out.println("Testing delete current user.");
        UUID userID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        User user = new User(userID,"Prit","Italiya","prit@italiya.com","Prit234!");
        assertDoesNotThrow(()->userService.deleteAccount(user));
    }
    @Test
    public void tesrtDeleteUser1()  {
        System.out.println("Testing delete current user.");
        assertThrows(UserValidationException.class,()->userService.deleteAccount(null));
    }
    @Test
    public void tesrtDeleteUser2()  {
        System.out.println("Testing delete current user.");
        UUID userID = UUID.fromString("550e9400-e29b-41d4-a716-446655440000");
        User user = new User(userID,"Prit","Italiya","prit@italiya.com","Prit234!");
        assertThrows(UserValidationException.class,()->userService.deleteAccount(user));
    }


}
