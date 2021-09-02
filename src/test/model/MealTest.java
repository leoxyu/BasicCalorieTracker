package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MealTest {
    private Meal testMeal;

    @BeforeEach
    void runBefore() {
        testMeal = new Meal("breakfast");
    }

    @Test
    public void testAddFood() {
        testMeal.addFood(new Food("A", 100));
        assertEquals(1, testMeal.getMealSize());
    }

    @Test
    public void testSetMealType() {
        testMeal.setMealType("Breakfast");
        assertEquals("Breakfast", testMeal.getMealType());
    }

    @Test
    public void testAddMultipleFoods() {
        testMeal.addFood(new Food("A", 100));
        testMeal.addFood(new Food("B", 700));
        assertEquals(2, testMeal.getMealSize());
    }

    @Test
    public void testRemoveFood() {
        testMeal.addFood(new Food("A", 100));
        testMeal.addFood(new Food("B", 700));
        assertTrue(testMeal.removeFood("B"));
        assertEquals(1, testMeal.getMealSize());
        assertEquals("A", testMeal.getFood(0).getFoodName());
    }

    @Test
    public void testRemoveFoodEmpty() {
        assertFalse(testMeal.removeFood("A"));
    }

    @Test
    public void testRemoveFoodNotFound() {
        testMeal.addFood(new Food("A", 100));
        testMeal.addFood(new Food("B", 700));
        testMeal.addFood(new Food("C", 1350));
        assertFalse(testMeal.removeFood("Apple"));
    }

    @Test
    public void testTotalCalories() {
        testMeal.addFood(new Food("A", 100));
        testMeal.addFood(new Food("B", 700));
        testMeal.addFood(new Food("C", 1350));
        assertEquals(2150, testMeal.totalCalories());
    }

    @Test
    public void testFoodExists() {
        testMeal.addFood(new Food("A", 100));
        testMeal.addFood(new Food("B", 700));
        testMeal.addFood(new Food("C", 1350));
        assertTrue(testMeal.foodExistsInMeal("B"));
    }

    @Test
    public void testFoodDoesNotExist() {
        testMeal.addFood(new Food("A", 100));
        testMeal.addFood(new Food("B", 700));
        testMeal.addFood(new Food("C", 1350));
        assertFalse(testMeal.foodExistsInMeal("D"));
    }


}