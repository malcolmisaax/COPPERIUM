package persistence.session;

import model.Word;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.*;
import java.util.List;

// MODELLED FROM DEMO

// Represents a writer that writes JSON representation of wordPool to file
public class SessionJsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;
    private int intStart; // New
    private int intEnd; // New

    // EFFECTS: constructs writer to write to destination file
    public SessionJsonWriter(String destination) {
        this.destination = destination;
        this.intStart = intStart; // New
        this.intEnd = intEnd; // New
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of workroom to file
    // New added fields
    public void write(List<Word> session, int start, int end) {
        JSONObject json = sessionToJson(session, start, end);
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }

    // EFFECTS: writes a JSon file for the session
    public JSONObject sessionToJson(List<Word> session, int start, int end) {
        JSONObject json = new JSONObject();
        json.put("session", sessionListToJson(session));
        json.put("int1", start); // New
        json.put("int2", end); // New
        return json;
    }

    // EFFECTS: Returns a JSONArray of the loaded session
    private JSONArray sessionListToJson(List<Word> session) {
        JSONArray jsonArray = new JSONArray();

        for (Word w : session) {
            jsonArray.put(w.toJson());
        }

        return jsonArray;
    }
}
