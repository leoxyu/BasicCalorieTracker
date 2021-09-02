package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FoodTest {
    Food food;

    @BeforeEach
    public void setup() {
        food = new Food("Apple", 100);
    }

    @Test
    public void testFoodEditCalories() {
        food.editCalories(50);
        assertEquals(50, food.getCalories());
    }

    @Test
    public void testEditName() {
        food.editName("Toast");
        assertEquals("Toast", food.getFoodName());
    }
}