package comp3350.wwsys.business;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateGetter {

    private final static int DAYS_OF_WEEK = 7;
    private final static int NUM_MONTHS = 12;
    private final static int NUM_YEARS = 5;

    public  static DayOfWeek[] getListOfWeek() {

        DayOfWeek[] week = new DayOfWeek[DAYS_OF_WEEK];
        DayOfWeek today = LocalDateTime.now().getDayOfWeek();


        for (int i = 0; i < DAYS_OF_WEEK; i++) {
            DayOfWeek day = today.minus(DAYS_OF_WEEK - 1 - i);
            week[i] = day;

        }
        return week;
    }

    public static String[] getStringListOfWeek() {

        String[] week = new String[DAYS_OF_WEEK];
        DayOfWeek today = LocalDateTime.now().getDayOfWeek();


        for (int i = 0; i < DAYS_OF_WEEK; i++) {
            DayOfWeek day = today.minus(DAYS_OF_WEEK - 1 - i);
            week[i] = day.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        }
        return week;
    }

    public static Month[] getListOfMonths() {


        Month[] months = new Month[NUM_MONTHS];
        Month currMonth = LocalDateTime.now().getMonth();

        for (int i = 0; i < NUM_MONTHS; i++) {
            Month month = currMonth.minus(NUM_MONTHS - 1 - i);
            months[i] = month;

        }
        return months;
    }
    public static String[] getStringListOfMonths() {


        String[] months = new String[NUM_MONTHS];
        Month currMonth = LocalDateTime.now().getMonth();

        for (int i = 0; i < NUM_MONTHS; i++) {
            Month month = currMonth.minus(NUM_MONTHS - 1 - i);
            months[i] = month.getDisplayName(TextStyle.SHORT,Locale.ENGLISH);
        }

        return months;
    }

    public static Year[] getListOfYears() {
        Year[] years = new Year[NUM_YEARS];
        Year thisYear = Year.now();

        for (int i = 0; i < NUM_YEARS; i++) {
            years[NUM_YEARS - 1 - i] = thisYear.minusYears(i);
        }
        return years;
    }

    public static String[] getStringListOfYears() {
        String[] years = new String[NUM_YEARS];
        Year thisYear = Year.now();
        for (int i = 0; i < NUM_YEARS; i++) {
            years[NUM_YEARS - 1 - i] = thisYear.minusYears(i).toString();
        }
        return years;
    }
}
