
public class DayTests {
    private static void fail(String message) {
        throw new RuntimeException(message);
    }

    private static void assertTrue(boolean expression) {
        if (!expression) {
            fail("assertion failed");
        }
    }

    private static void assertFalse(boolean expression) {
        assertTrue(!expression);
    }

    private static void assertEquals(Object expected, Object actual) {
        if (!expected.equals(actual)) {
            fail("Expected " + expected + " but got " + actual);
        }
    }

    public void testIsLeapYear() {
        assertTrue(Day.isLeapYear(1580));
        assertTrue(Day.isLeapYear(1600));
        assertFalse(Day.isLeapYear(1700));
        assertFalse(Day.isLeapYear(1800));
        assertTrue(Day.isLeapYear(2000));
        assertTrue(Day.isLeapYear(2020));
        assertFalse(Day.isLeapYear(2019));
        assertTrue(Day.isLeapYear(2400));
    }

    public void testCreateDayPreconditions() {
        new Day(2019, Day.Month.DEC, 27);

        try {
            new Day(2019, Day.Month.JAN, 0);
            fail("Created day with 0 day");
        } catch (IllegalArgumentException expected) {}
        try {
            new Day(2019, Day.Month.JAN, -50);
            fail("Created day with negative day");
        } catch (IllegalArgumentException expected) {}
        try {
            new Day(2019, Day.Month.DEC, 40);
            fail("Created day with too high day");
        } catch (IllegalArgumentException expected) {}
        
        new Day(2020, Day.Month.FEB, 29);
        try {
            new Day(2021, Day.Month.FEB, 29);
            fail("Created day with too high day in feb during leap year");
        } catch (IllegalArgumentException expected) {}
        try {
            new Day(0, Day.Month.FEB, 1);
            fail("Created day with zero year");
        } catch (IllegalArgumentException expected) {}
        try {
            new Day(2019, null, 1);
            fail("Created day with null month");
        } catch (NullPointerException expected) {}
    }

    public void testGetDaysInMonthPreconditions() {
        try {
            Day.getDaysInMonth(null, 2019);
            fail("Called getDaysInMonth with null month");
        } catch (NullPointerException expected) {}
        try {
            Day.getDaysInMonth(Day.Month.FEB, 0);
            fail("Called getDaysInMonth with 0 year");
        } catch (IllegalArgumentException expected) {}
    }

    public void testIsLeapYearPreconditions() {
        try {
            Day.isLeapYear(0);
            fail("Called isLeapYear with 0 year");
        } catch (IllegalArgumentException expected) {}
    }

    public void testDaysFrom() {
        assertEquals(0, new Day(2019, Day.Month.NOV, 1).daysFrom(new Day(2019, Day.Month.NOV, 1)));
        assertEquals(1, new Day(2019, Day.Month.NOV, 1).daysFrom(new Day(2019, Day.Month.NOV, 2)));
        assertEquals(14, new Day(2019, Day.Month.NOV, 1).daysFrom(new Day(2019, Day.Month.NOV, 15)));
        assertEquals(44, new Day(2019, Day.Month.NOV, 1).daysFrom(new Day(2019, Day.Month.DEC, 15)));
        assertEquals(75, new Day(2019, Day.Month.NOV, 1).daysFrom(new Day(2020, Day.Month.JAN, 15)));
        assertEquals(135, new Day(2019, Day.Month.NOV, 1).daysFrom(new Day(2020, Day.Month.MAR, 15)));
        assertEquals(365, new Day(-1, Day.Month.JAN, 1).daysFrom(new Day(1, Day.Month.JAN, 1)));
        assertEquals(20, new Day(1582, Day.Month.OCT, 1).daysFrom(new Day(1582, Day.Month.OCT, 31)));
        assertEquals(37, new Day(1582, Day.Month.SEP, 15).daysFrom(new Day(1582, Day.Month.NOV, 1)));
        assertEquals(-37, new Day(1582, Day.Month.NOV, 1).daysFrom(new Day(1582, Day.Month.SEP, 15)));
    }

    public void testAddDays() {
        Day start = new Day(2012, Day.Month.DEC, 15);

        assertEquals(new Day(2012, Day.Month.DEC, 30), start.addDays(15));
        assertEquals(new Day(2013, Day.Month.JAN, 4), start.addDays(20));
        assertEquals(new Day(2012, Day.Month.DEC, 1), start.addDays(-14));

        start = new Day(1, Day.Month.JAN, 1);
        assertEquals(new Day(-1, Day.Month.DEC, 31), start.addDays(-1));

        start = new Day(1582, Day.Month.OCT, 1);
        assertEquals(new Day(1582, Day.Month.OCT, 31), start.addDays(20));
    }

    public void testDaysFromPreconditions() {
        Day start = new Day(2012, Day.Month.DEC, 15);
        try {
            start.daysFrom(null);
            fail("Called daysFrom with null");
        } catch (NullPointerException expected) {
        }
    }

    public void testAddDaysPostConditions() {
        Day start = new Day(2012, Day.Month.DEC, 15);
        // For a bunch of +ve and -ve numbers, check that we never return null.
        for (int i = -10; i < 10; i++) {
            assertTrue(start.addDays(i) != null);
        }
    }
    
    public static void main(String[] args) {
        DayTests tests = new DayTests();
        tests.testIsLeapYear();
        tests.testCreateDayPreconditions();
        tests.testDaysFrom();
        tests.testAddDays();
        tests.testGetDaysInMonthPreconditions();
        tests.testIsLeapYearPreconditions();
        tests.testDaysFromPreconditions();
        tests.testAddDaysPostConditions();

        System.out.println("ALL TESTS PASSED!!!");
    }
}