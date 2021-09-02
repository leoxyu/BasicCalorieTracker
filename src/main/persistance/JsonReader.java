package persistance;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import model.Calendar;
import model.Day;
import model.Meal;
import model.Food;

import org.json.*;


public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }
        return contentBuilder.toString();
    }

    // EFFECTS: reads a day JSON file, and return the containing item
    public Calendar read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseCalendar(jsonObject);
    }

    private Calendar parseCalendar(JSONObject jsonObject) {
        Calendar c = new Calendar();
        List<Day> days = readDays(jsonObject);
        for (Day d : days) {
            c.days.add(d);
        }
        return c;
    }

    // EFFECTS: Parses a calendar JSONObject and returns a list of the containing days
    private List<Day> readDays(JSONObject jsonObject) {
        JSONArray daysArray = jsonObject.getJSONArray("days");
        List<Day> days = new ArrayList<>();
        for (Object d : daysArray) {
            JSONObject daysObject = (JSONObject) d;
            days.add(parseDay(daysObject));
        }
        return days;
    }


    // EFFECTS: returns a day from a read JSONObject
    private Day parseDay(JSONObject jsonObject) {
        int dayNum = jsonObject.getInt("dayNum");
        Day d = new Day(dayNum);
        List<Meal> meals = readMeals(jsonObject);
        for (Meal m : meals) {
            d.addMeal(m);
        }
        return d;
    }

    // EFFECTS: Parses a day JSONObject and returns a list of the containing meals
    private List<Meal> readMeals(JSONObject jsonObject) {
        JSONArray mealsArray = jsonObject.getJSONArray("meals");
        List<Meal> meals = new ArrayList<>();
        for (Object m : mealsArray) {
            JSONObject mealsObject = (JSONObject) m;
            String mealName = mealsObject.getString("mealName");
            List<Food> foods = getFoods(mealsObject);
            Meal tempMeal = new Meal(mealName);
            for (Food food : foods) {
                tempMeal.addFood(food);
            }
            meals.add(tempMeal);
        }
        return meals;
    }

    //EFFECTS: Parses a meal JSONObject, and returns the list of foods inside.
    private List<Food> getFoods(JSONObject jsonObject) {
        JSONArray foodArray = jsonObject.getJSONArray("foods");
        List<Food> foods = new ArrayList<>();
        for (Object json : foodArray) {
            JSONObject food = (JSONObject) json;
            foods.add(getFood(food));
        }
        return foods;
    }

    //EFFECTS: Parses a food JSONObject, and returns a single food.
    private Food getFood(JSONObject jsonObject) {
        String name = jsonObject.getString("foodName");
        int calories = jsonObject.getInt("calories");
        return new Food(name, calories);
    }


}



