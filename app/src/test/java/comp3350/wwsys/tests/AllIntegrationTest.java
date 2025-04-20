package comp3350.wwsys.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import comp3350.wwsys.tests.business.EntryServiceIT;
import comp3350.wwsys.tests.business.UserServiceIT;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        UserServiceIT.class,
        EntryServiceIT.class
})
public class AllIntegrationTest {
}
