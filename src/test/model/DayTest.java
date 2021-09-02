package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DayTest {
    private Day testDay;

    @BeforeEach
    void runBefore() {
        testDay = new Day(1);
    }

    @Test
    public void testAddMeal() {
        testDay.addMeal(new Meal("Breakfast"));
        assertEquals(1, testDay.getNumberOfMeals());
    }

    @Test
    public void setDayNum() {
        testDay.setDayNum(2);
        try {
            assertEquals(2, testDay.getDayNum());
        } catch (Exception exception) {
            fail("Exception not expected");
        }
    }


    @Test
    public void testAddMultipleMeal() {
        testDay.addMeal(new Meal("Breakfast"));
        testDay.addMeal(new Meal("Lunch"));
        assertEquals(2, testDay.getNumberOfMeals());
    }

    @Test
    public void testRemoveMeal() {
        testDay.addMeal(new Meal("Breakfast"));
        testDay.addMeal(new Meal("Lunch"));
        assertTrue(testDay.removeMeal("Breakfast"));
        assertEquals(1, testDay.getNumberOfMeals());
        assertEquals("Lunch", testDay.getMeal(0).getMealType());
    }

    @Test
    public void testRemoveFoodEmpty() {
        assertFalse(testDay.removeMeal("A"));
    }

    @Test
    public void testRemoveFoodNotFound() {
        testDay.addMeal(new Meal("Breakfast"));
        testDay.addMeal(new Meal("Lunch"));
        assertFalse(testDay.removeMeal("Dinner"));
    }

    @Test
    public void testTotalCalories() {
        Meal breakfast = new Meal("Breakfast");
        breakfast.addFood(new Food("Toast", 200));
        breakfast.addFood(new Food("Milk", 100));
        Meal lunch = new Meal("Lunch");
        breakfast.addFood(new Food("Sandwich", 700));
        breakfast.addFood(new Food("Eggs", 400));
        testDay.addMeal(breakfast);
        testDay.addMeal(lunch);
        assertEquals(1400, testDay.totalCalories());
    }

    @Test
    public void testMealExists() {
        testDay.addMeal(new Meal("Breakfast"));
        testDay.addMeal(new Meal("Lunch"));
        assertTrue(testDay.mealExistsInDay("Breakfast"));
    }

    @Test
    public void testMealNotExist() {
        testDay.addMeal(new Meal("Breakfast"));
        testDay.addMeal(new Meal("Lunch"));
        assertFalse(testDay.mealExistsInDay("Dinner"));
    }

    @Test
    public void testGetDayNumExceptionThrown() {
        testDay.setDayNum(0);
        try {
            assertEquals(0, testDay.getDayNum());
            fail("Exception expected");
        } catch (Exception exception) {
            //Exception expected
        }
    }

    @Test
    public void testGetDayNumExceptionNotThrown() {
        testDay.setDayNum(2);
        try {
            assertEquals(2, testDay.getDayNum());
        } catch (Exception exception) {
            fail("Exception not expected");
        }
    }
}