package comp3350.wwsys.tests.acceptance;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AccountMgmtTest.class,
        IncomeTrackerTest.class,
        ExpenseTrackerTest.class,
        AccountEditingTest.class,
        SummaryAnalyticsTest.class,
        IncomeAnalyticsTest.class,
        ExpenseAnalyticsTest.class
})

public class AllAcceptanceTests {
}
