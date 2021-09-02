//Represents a day and has meals. Each meal in the day is represented by a Meal in the list called "day"

package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistance.Writable;


import java.util.ArrayList;
import java.util.List;

public class Day implements Writable {
    private int dayNum;
    public List<Meal> day;

    public Day(int d) {
        dayNum = d;
        day = new ArrayList<>();
    }


    //EFFECTS: calculates total calories of all the meals within the day
    //         returns that amount as an int
    public int totalCalories() {
        Meal tempMeal;
        int totalCal = 0;
        for (Meal meal : day) {
            tempMeal = meal;
            for (int j = 0; j < tempMeal.getMealSize(); j++) {
                totalCal += tempMeal.getFood(j).getCalories();
            }
        }
        return totalCal;
    }

    //MODIFIES: this
    //EFFECTS: adds parameter meal to day
    public void addMeal(Meal meal) {
        day.add(meal);
    }


    //MODIFIES: this
    //EFFECTS: removes meal from day based on name inputted and returns true
    //         if food doesn't exist, return false.
    public boolean removeMeal(String name) {
        for (int i = 0; i < day.size(); i++) {
            if (name.equals(day.get(i).getMealType())) {
                day.remove(i);
                return true;
            }
        }
        return false;
    }

    //EFFECTS: if food parameter is found in the meal, return true
    //         if food doesn't exist, return false.
    public boolean mealExistsInDay(String name) {
        for (Meal meal : day) {
            if (name.equals(meal.getMealType())) {
                return true;
            }
        }
        return false;

    }

    //EFFECTS: returns number of meals in the day
    public int getNumberOfMeals() {
        return day.size();
    }

    //EFFECTS: returns meal in day with index i
    public Meal getMeal(int i) {
        return day.get(i);
    }

    //EFFECTS: returns dayNum
    public int getDayNum() throws NoDaysExistException {
        if (dayNum != 0) {
            return dayNum;
        } else {
            throw new NoDaysExistException();
        }

    }

    //MODIFIES: This
    //EFFECTS: changes dayNum to parameter i
    public void setDayNum(int i) {
        dayNum = i;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject(); //Create new JSON Object
        json.put("dayNum", dayNum); // Place the day number name in the JSON Object.
        JSONArray mealArray = new JSONArray(); //Create a JSONArray for the list of meals.
        for (Meal m : day) {
            mealArray.put(m.toJson()); //For each meal, place the JSON version of the meal in the list of JSON meals
        }
        json.put("meals", mealArray); // Place the array of meals in the JSON.
        return json;
    }


}
