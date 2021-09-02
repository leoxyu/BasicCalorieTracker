package model;

import java.util.List;

public class ExceptionValidator {

    public ExceptionValidator() {

    }

    //function to test the exception in order to pass Jacoco code coverage
    public void validateNoDaysException(List<Day> days) throws NoDaysExistException {
        if (days.size() == 0) {
            throw new NoDaysExistException();
        } else {
            System.out.println("There are days in this list");
        }
    }
}
