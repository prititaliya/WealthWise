package comp3350.wwsys.tests.business;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import comp3350.wwsys.business.DateGetter;

public class DateGetterTest {
    @Before
    public void setUp(){
        System.out.println("Setting up DateGetterTest");
    }

    @Test
    public void testGetListOfWeek() {
        System.out.println("Testing getListOfWeek");
        assertEquals(7,DateGetter.getListOfWeek().length);
    }

    @Test
    public void testGetStringListOfWeek() {
        System.out.println("Testing getStringListOfWeek");
        assertEquals(7, DateGetter.getStringListOfWeek().length);
    }

    @Test
    public void testGetListOfMonths() {
        System.out.println("Testing getListOfMonths");
        assertEquals(12, DateGetter.getListOfMonths().length);
    }

    @Test
    public void testGetStringListOfMonths() {
        System.out.println("Testing getStringListOfMonths");
        assertEquals(12,DateGetter.getStringListOfMonths().length);
    }

    @Test
    public void testGetListOfYears() {
        System.out.println("Testing getListOfYears");
        assertEquals(5,DateGetter.getListOfYears().length);
    }

    @Test
    public void testGetStringListOfYears() {
        System.out.println("Testing getStringListOfYears");
        assertEquals(5,DateGetter.getStringListOfYears().length);
    }

    @After
    public void tearDown() {
        System.out.println("Tearing down DateGetterTest");
    }
}
