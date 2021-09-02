//Represents a food that has a name and number of calories

package model;

import org.json.JSONObject;
import persistance.Writable;

public class Food implements Writable {
    protected String foodName;
    protected int calories;

    public Food(String n, int c) {
        foodName = n;
        calories = c;
    }

    public String getFoodName() {
        return foodName;
    }

    public int getCalories() {
        return calories;
    }

    //EFFECTS: sets foodName to parameter s
    public void editName(String s) {
        foodName = s;
    }

    //EFFECTS: sets calories to parameter c
    public void editCalories(int c) {
        calories = c;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject(); //Create new JSON Object
        json.put("foodName", foodName); // Place food name in JSON
        json.put("calories", calories); // Place calories in JSON
        return json;
    }
}
