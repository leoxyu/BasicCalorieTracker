package persistence;

import model.*;
import org.junit.jupiter.api.Test;
import persistance.JsonReader;
import persistance.JsonWriter;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest extends JsonTest {

    @Test
    public void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("Exception expected");
        } catch (IOException e) {
            //pass
        }
    }

    @Test
    public void testWriterEmptyDay() {
        try {
            Calendar testCalendar = new Calendar();
            Day testDay = new Day(4);
            testCalendar.addDay(testDay);
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyDay.json");
            writer.open();
            writer.write(testCalendar);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyDay.json");
            testCalendar = reader.read();
            assertEquals(4, testCalendar.getDay(0).getDayNum());
        } catch (IOException | NoDaysExistException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testWriterNonEmptyDay() {
        try {
            Calendar testCalendar = new Calendar();
            Day testDay = new Day(3);
            testCalendar.addDay(testDay);
            Meal m1 = new Meal("Breakfast");
            Meal m2 = new Meal("Lunch");
            m1.addFood(new Food("Toast", 300));
            m1.addFood(new Food("Milk", 100));
            m2.addFood(new Food("Chicken", 500));
            m2.addFood(new Food("Sandwich", 800));
            m2.addFood(new Food("Water", 0));
            testDay.addMeal(m1);
            testDay.addMeal(m2);
            JsonWriter writer = new JsonWriter("./data/testWriterNonEmptyDay.json");
            writer.open();
            writer.write(testCalendar);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterNonEmptyDay.json");
            testCalendar = reader.read();
            assertEquals(3, testCalendar.getDay(0).getDayNum());
            assertEquals(2, testCalendar.getDay(0).getNumberOfMeals());
            checkMeal("Breakfast", testCalendar.getDay(0).getMeal(0));
            checkMeal("Lunch", testCalendar.getDay(0).getMeal(1));
            checkFood("Toast", 300, testCalendar.getDay(0).getMeal(0).getFood(0));
            checkFood("Milk", 100, testCalendar.getDay(0).getMeal(0).getFood(1));
            checkFood("Chicken", 500, testCalendar.getDay(0).getMeal(1).getFood(0));
            checkFood("Sandwich", 800, testCalendar.getDay(0).getMeal(1).getFood(1));
            checkFood("Water", 0, testCalendar.getDay(0).getMeal(1).getFood(2));

        } catch (IOException | NoDaysExistException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testWriterMultipleDays() {
        try {
            Calendar testCalendar = new Calendar();
            Day testDay = new Day(1);
            Day testDay2 = new Day(2);
            testCalendar.addDay(testDay);
            testCalendar.addDay(testDay2);
            JsonWriter writer = new JsonWriter("./data/testWriterMultipleDays.json");
            writer.open();
            writer.write(testCalendar);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterMultipleDays.json");
            testCalendar = reader.read();
            assertEquals(1, testCalendar.getDay(0).getDayNum());
            assertEquals(2, testCalendar.getDay(1).getDayNum());
        } catch (IOException | NoDaysExistException e) {
            fail("Exception not expected");
        }
    }


}
