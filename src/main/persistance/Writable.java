package persistance;

import org.json.JSONObject;

public interface Writable {
    //returns this as JSON object
    JSONObject toJson();
}

