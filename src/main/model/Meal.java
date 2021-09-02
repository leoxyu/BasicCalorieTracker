//Represents a meal that exists in a day. Contains a list of foods

package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistance.Writable;

import java.util.ArrayList;
import java.util.List;

//A list of Foods
public class Meal implements Writable {
    public String mealType; //meal type eg. breakfast, snack, stc.
    public List<Food> meal;

    public Meal(String t) {
        mealType = t;
        meal = new ArrayList<>();
    }

    //EFFECTS: returns the sum of the calories of all the foods inside the meal
    public int totalCalories() {
        int totalCal = 0;
        for (Food food : meal) {
            totalCal += food.getCalories();
        }
        return totalCal;
    }

    //MODIFIES: this
    //EFFECTS: adds parameter food to meal
    public void addFood(Food food) {
        meal.add(food);
    }

    //MODIFIES: this
    //EFFECTS: removes food from meal based on name inputted and returns true.
    //         if food doesn't exist, return false.
    public boolean removeFood(String name) {
        for (int i = 0; i < meal.size(); i++) {
            if (name.equals(meal.get(i).getFoodName())) {
                meal.remove(i);
                return true;
            }
        }
        return false;
    }

    //EFFECTS: if food parameter is found in the meal, return true
    //         if food doesn't exist, return false.
    public boolean foodExistsInMeal(String name) {
        for (Food food : meal) {
            if (name.equals(food.getFoodName())) {
                return true;
            }
        }
        return false;

    }

    //EFFECTS: returns number of foods in the meal
    public int getMealSize() {
        return meal.size();
    }

    //EFFECTS: returns food in meal with index i
    public Food getFood(int i) {
        return meal.get(i);
    }

    //EFFECTS: sets mealType to parameter s
    public void setMealType(String s) {
        mealType = s;
    }

    //EFFECTS: returns mealType
    public String getMealType() {
        return mealType;
    }


    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject(); //Create new JSON Object
        json.put("mealName", mealType); // Place the meal name in the JSON
        JSONArray foodArray = new JSONArray(); //Create a JSONArray for the array of foods.
        for (Food food : meal) {
            foodArray.put(food.toJson()); //For each food, place the JSON version of the food in the list of JSON foods.
        }
        json.put("foods", foodArray); // Place the array of foods in the JSON.
        return json;
    }
}
