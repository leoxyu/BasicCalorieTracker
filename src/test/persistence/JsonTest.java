package persistence;


import model.Food;
import model.Meal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {

    public void checkMeal(String name, Meal meal) {
        assertEquals(name, meal.getMealType());
    }
    public void checkFood(String name, int calories, Food food) {
        assertEquals(name, food.getFoodName());
        assertEquals(calories, food.getCalories());
    }


}
