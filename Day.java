import java.util.Objects;
/**
 * Represents a date.
 */
public class Day {
    private final int year;
    private final Month month;
    private final int day;

    /**
     * A calendar month.
     */
    public enum Month {
        JAN(31), FEB(28), MAR(31), APR(30), MAY(31), JUN(30), JUL(31), AUG(31), SEP(30), OCT(31), NOV(30), DEC(31);
        
        // The number of days in the month (normally, excluding feb during leap years)
        private final int numDays;

        /** 
         * Constructs a Month.
         * @param numDays the number of days in the month.
         */
        private Month(int numDays) {
            this.numDays = numDays;
        }

        /**
         * Helper method to get the zero-based month number.
         * 
         * @return a the zero-based number of this month (e.g. 0 == Jan, 11 = Dec).
         */
        private int getMonthNumber() {
            for (int i = 0; i < values().length; i++) {
                if (values()[i] == this) {
                    return i;
                }
            }
            //throw exception if the input doesn't match the list in the enum
            throw new IllegalStateException("Unknown month");
        }
        
        /**
         * Gets the month after this month.
         * 
         * @return the month after this month (e.g. if this month is JAN, returns FEB).
         *   If this month is DEC, returns JAN.
         */
        Month getNextMonth() {
            if (this == DEC) return JAN;
            return values()[getMonthNumber() + 1];
        }

        /**
         * Gets the month before this month.
         * 
         * @return the month before this month (e.g. if this month is FEB, returns JAN).
         *   If this month is JAN, returns DEC.
         */
        Month getPrevMonth() {
            if (this == JAN) return DEC;
            return values()[getMonthNumber() - 1];
        }
    }

    /**
     * Constructs a Day.
     * 
     * @param year the year of the date.
     * @precondition year != 0
     * @param month the month of the date.
     * @precondition month != null
     * @param day the day of the date.
     * @precondition day > 0 && day <= getDaysInMonth(month, year)
     * 
     * @throws IllegalArgumentException if the year or day are invalid.
     * @throws NullPointerException if the month is null.
     */
    public Day(int year, Month month, int day) {
        if (year == 0) {
            throw new IllegalArgumentException("Invalid year");
        }
        if (day <= 0 || day > getDaysInMonth(month, year)) {
            throw new IllegalArgumentException("Invalid day: " + day);
        }
        if (month == null) {
            throw new NullPointerException("month cannot be null");
        }
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     * @return a String representation of the date in yyyy-MON-dd format.
     */
    @Override
    public String toString() {
        return String.format("%04d-%s-%02d", year, month, day);
    }
    
    /** 
     * @return true if this Day is equal to another day.
     */
    @Override
    public boolean equals(Object day) {
        if (this == day) return true;
        if (!(day instanceof Day)) return false;
        Day otherDay = (Day) day;
        return otherDay.year == this.year && otherDay.month == this.month && otherDay.day == this.day;
    }

    /**
     * @return a hashcode for this day.
     */
    @Override
    public int hashCode() {
        return Objects.hash(year, month, day);
    }

    /**
     * Gets the number of days in a month, taking into account leap years.
     * 
     * @param month a calendar month. Must not be null.
     * @precondition month != null
     * @param year the year. Must not be 0.
     * @precondition year != 0
     * @return the number of days in the given month and year.s
     * @throws IllegalArgumentException if the year is invalid.
     * @throws NullPointerException if the month is null.
     */
    public static int getDaysInMonth(Month month, int year) {
        if (month == null) {
            throw new NullPointerException("Null month");
        }
        if (year == 0) {
            throw new IllegalArgumentException("Invalid year: " + year);
        }
        int daysOfMonth = month.numDays;
        if (isLeapYear(year) && month == Month.FEB) {
            daysOfMonth = 29;
        }
        return daysOfMonth;
    }

    /**
     * Helper method to determine if a given year is a leap year.
     * @param year the year.
     * @precondition year != 0
     * @return true if the year is a leap year.
     * @throws IllegalArgumentException if the year is invalid.
     */
    static boolean isLeapYear(int year) {
        if (year == 0) {
            throw new IllegalArgumentException("Invalid year: " + year);
        }
        // If the year <= 1582, then leap if div by 4
        if (year <= 1582) {
            return year % 4 == 0;
        }
        
        if (year % 4 != 0) {
            return false;
        } 
        
        if (year % 100 != 0) {
            return true;
        }

        if (year % 400 != 0) {
            return false;
        }

        return true;
    }
    
    /**
     * @return the day of the month.
     */
    public int getDay() {
        return day;
    }

    /**
     * @return the month.
     */
    public Month getMonth() {
        return month;
    }

    /**
     * @return the year.
     */
    public int getYear() {
        return year;
    }

    private static boolean isGregorianCutover(Month month, int year) {
        return year == 1582 && month == Month.OCT;
    }

    // Returns a numerical representation of the day in yyyymmdd format.
    private static long getNumericalDay(Day day) {
        return (day.year * 10000) + ((day.month.ordinal() + 1) * 100) + day.day;
    }

    private static Day nextDay(Day from) {        
        int day = from.day + 1;
        Month month = from.month;
        int year = from.year;

        if (isGregorianCutover(month, year) && day == 5) {
            day = 15;
        }

        if (day > getDaysInMonth(from.month, from.year)) {
            day = 1;
            month = from.month.getNextMonth();
            if (month == Month.JAN) {
                year++;
                if (year == 0) year = 1;
            }
        }
        return new Day(year, month, day);
    }

    private static Day prevDay(Day from) {
        int day = from.day - 1;
        Month month = from.month;
        int year = from.year;

        if (day == 0) {
            month = month.getPrevMonth();
            day = getDaysInMonth(month, year);
            if (month == Month.DEC) {
                year--;
                if (year == 0) year = -1;
            }
        }
        return new Day(year, month, day);
    }

    /**
     * Calculates the number of days from this day to the given otherDay.
     * 
     * @param otherDay the other day to count days to. May be before or after
     *    this day (if before, will return a negative number of days).
     * @precondition otherDay != null
     * 
     * @return the number of days from this day to the given otherDay. 
     * 
     * @throws NullPointerException if otherDay is null.
     */
    public int daysFrom(Day otherDay) {
        if (otherDay == null) {
            throw new NullPointerException("otherDay is null");
        }
        int totalDays = 0;

        // Start with the lower of the two days.
        long thisNumerical = getNumericalDay(this);
        long otherNumerical = getNumericalDay(otherDay);

        if (thisNumerical == otherNumerical) return 0;

        Day startDay = thisNumerical < otherNumerical ? this : otherDay;
        Day endDay = thisNumerical < otherNumerical ? otherDay : this;

        Day curDay = startDay;

        while (curDay.year != endDay.year || curDay.month != endDay.month || curDay.day != endDay.day) {
            curDay = nextDay(curDay);
            totalDays++;
        }

        if (startDay != this) {
            totalDays = -totalDays;
        }

        return totalDays;
    }

    /**
     * Adds the given number of days to this day, and returns a new day 
     * representing that day.
     * 
     * @param days the number of days to add. If negative, subtracts that number of days.
     * @return a day. 
     * @postcondition day != null
     */
    public Day addDays(int days) {
        Day result = this;
        for (int i = 0; i < Math.abs(days); i++) {
            if (days > 0) {
                result = nextDay(result);
            } else {
                result = prevDay(result);
            }
        }
        return result;
    }
}