package persistence;

import org.json.JSONObject;

// MODELLED AFTER THE JSON DEMO
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
