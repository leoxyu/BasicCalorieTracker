package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class CalendarTest {

    private Calendar testCal;

    @BeforeEach
    public void setup() {
        testCal = new Calendar();
        testCal.addDay(new Day(1));
    }

    @Test
    public void testAddOneDay() {
        assertEquals(1, testCal.getNumDays());
    }

    @Test
    public void testAddMultipleDays() {
        testCal.addDay(new Day(2));
        testCal.addDay(new Day(3));

        assertEquals(3, testCal.getNumDays());
    }

    @Test
    public void testGetDay() {
        testCal.addDay(new Day(2));

        try {
            assertEquals(2, testCal.getDay(1).getDayNum());
        } catch (NoDaysExistException exception) {
            fail("Exception not expected");
        }

    }
    @Test
    public void testGetDayNoDays() {
        testCal.addDay(new Day(2));

        try {
            assertEquals(2, testCal.getDay(1).getDayNum());
            fail("Exception expected");
        } catch (NoDaysExistException exception) {
            //Pass exception was expected
        }

    }

}
