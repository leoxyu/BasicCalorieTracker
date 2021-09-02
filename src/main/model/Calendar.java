//Represents a calendar and contains a list of days

package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistance.Writable;

import java.util.ArrayList;
import java.util.List;

public class Calendar implements Writable {
    public List<Day> days;

    public Calendar() {
        days = new ArrayList<>();
    }

    //EFFECTS: adds parameter day into list of days
    public void addDay(Day day) {
        days.add(day);
    }

    //EFFECTS: returns day with index i
    public Day getDay(int i) {
        return days.get(i);
    }

    //EFFECTS: returns number of days inside the list of days
    public int getNumDays() {
        return days.size();

    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject(); //Create new JSON Object
        JSONArray dayArray = new JSONArray(); //Create a JSONArray for the list of days.
        for (Day d : days) {
            dayArray.put(d.toJson()); //For each days, place the JSON version of the days in the JSON Object.
        }
        json.put("days", dayArray); // Place the array of teams in the JSON.
        return json;
    }
}
