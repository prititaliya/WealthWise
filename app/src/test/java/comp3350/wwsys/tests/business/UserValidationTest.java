package comp3350.wwsys.tests.business;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import comp3350.wwsys.business.UserValidationException;
import comp3350.wwsys.business.UserValidator;
import comp3350.wwsys.objects.User;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.Objects;


public class UserValidationTest {
    User user;

    @Before
    public void setUp() {
        System.out.println("Starting UserValidationTest.");
        user = new User("prit", "italiya", "prit@prit.com", "Prit1234!");
        assertNotNull(user);
    }

    @Test
    public void testValidateLogin() {
        System.out.println("Testing validateLogin.");
         assertDoesNotThrow(() -> UserValidator.validateLogin("prit@prit.com", "Prit1234!"));
    }

    @Test
    public void testValidateInsert() {
        System.out.println("Testing validateInsert.");
         assertDoesNotThrow( ()->  UserValidator.validateInsert("Prit@Prit.com", "Prit1234!", "Prit1234!", "Prit", "Italiya"));
    }

    @Test
    public void testUpdateUser() {
        System.out.println("Testing updateUser.");
        User user = new User("prit", "italiya", "prit@prit.com", "Prit1234!");
        assertDoesNotThrow(() -> UserValidator.updateUser(user, "Pri1234!"));
    }

    @Test
    public void testUpdateUser2() {
        System.out.println("Testing updateUser2.");

        assertDoesNotThrow(() -> UserValidator.updateUser(user));

    }

    @Test
    public void testCheckFirstName() {
        System.out.println("Testing checkFirstName.");
//        assertDoesNotThrow(() -> UserValidator.checkFirstName("Prit"));
    }

    @Test
    public void testCheckLastName() {
        System.out.println("Testing checkFirstName2.");
//            assertDoesNotThrow(() -> UserValidator.checkFirstName("italiya"));
    }

    @Test
    public void testPasswordMatch() {
        System.out.println("Testing passwordMatch.");
        assertDoesNotThrow(() -> UserValidator.checkPasswordMatch("Prit1234!", "Prit1234!"));
    }

    @Test
    public void testCheckCurrentPassword() {
        System.out.println("Testing checkCurrentPassword.");
            assertDoesNotThrow(() -> UserValidator.checkCurrentPassword(user,"Prit1234!"));
    }

    @Test
    public void testClassUserValidationException() {
        System.out.println("Testing UserValidationException.");
        UserValidationException e = new UserValidationException("Test");
        assert (Objects.equals(e.getMessage(), "Test"));
    }


}