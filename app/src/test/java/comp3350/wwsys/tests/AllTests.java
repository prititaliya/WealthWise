package comp3350.wwsys.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import comp3350.wwsys.tests.business.DateGetterTest;
import comp3350.wwsys.tests.business.EntryServiceTest;
import comp3350.wwsys.tests.business.EntryValidatorTest;
import comp3350.wwsys.tests.business.UserServiceTest;
import comp3350.wwsys.tests.business.UserValidationTest;
import comp3350.wwsys.tests.business.ValidateUserEntryInputTest;
import comp3350.wwsys.tests.objects.EntryTest;
import comp3350.wwsys.tests.objects.ExpenseCatgeoryTest;
import comp3350.wwsys.tests.objects.IncomeCategoryTest;
import comp3350.wwsys.tests.objects.UserTest;
import comp3350.wwsys.tests.persistence.EntryPersistenceStubTest;
import comp3350.wwsys.tests.persistence.UserPersistenceStubTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        UserTest.class,
        EntryTest.class,
        ExpenseCatgeoryTest.class,
        IncomeCategoryTest.class,
        UserValidationTest.class,
        ValidateUserEntryInputTest.class,
        DateGetterTest.class,



        EntryServiceTest.class,
        EntryValidatorTest.class,
        UserServiceTest.class,
        UserValidationTest.class,
        UserPersistenceStubTest.class,
        EntryPersistenceStubTest.class
})


public class AllTests {

}
