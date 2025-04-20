package comp3350.wwsys.tests.business;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.Before;
import org.junit.Test;

import comp3350.wwsys.business.UserEntryInputValidationException;
import comp3350.wwsys.business.ValidateUserEntryInput;

public class ValidateUserEntryInputTest {

    @Before
    public void setUp() {
        System.out.println("\nStarting ValidateUserEntryInputTest");
    }

    /**
     * validateamount test
     */

    @Test
    public void testValidateAmount() {
        System.out.println("Testing validateAmount.");
        assertDoesNotThrow(() -> ValidateUserEntryInput.validateAmount("100.00"));
        assertDoesNotThrow(() -> ValidateUserEntryInput.validateAmount("100"));
        assertDoesNotThrow(() -> ValidateUserEntryInput.validateAmount("-100.0"));
        assertThrows((UserEntryInputValidationException.class), () -> ValidateUserEntryInput.validateAmount("100.0.0"));
        assertEquals("Invalid Amount", assertThrows((UserEntryInputValidationException.class), () -> ValidateUserEntryInput.validateAmount("100.0.0")).getMessage());
    }
    /**
     * passwordChnage test
     */
    @Test
    public void testPasswordChange() {
        System.out.println("Testing passwordChange.");
        assertDoesNotThrow(() -> ValidateUserEntryInput.validatePasswordChange("Prit1234!"));
        assertThrows((UserEntryInputValidationException.class), () -> ValidateUserEntryInput.validatePasswordChange(""));
    }

    
}
