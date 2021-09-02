package exception;

import model.Day;
import model.ExceptionValidator;
import model.NoDaysExistException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExceptionTest {

    @Test
    public void exceptionExpected() {
        ExceptionValidator exceptionValidator = new ExceptionValidator();
        List<Day> testDays = new ArrayList<>();

        try {
            exceptionValidator.validateNoDaysException(testDays);
            fail("Exception expected");
        } catch (NoDaysExistException exception) {
            //exception expected
        }

    }

    @Test
    public void exceptionNotExpected() {
        ExceptionValidator exceptionValidator = new ExceptionValidator();
        List<Day> testDays = new ArrayList<>();
        testDays.add(new Day(1));

        try {
            exceptionValidator.validateNoDaysException(testDays);
            assertEquals(1, testDays.get(0).getDayNum());
        } catch (NoDaysExistException exception) {
            fail("Exception not expected");
        }

    }
}
