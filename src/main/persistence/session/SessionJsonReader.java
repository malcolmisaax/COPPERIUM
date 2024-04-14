package persistence.session;

import model.Proficiency;
import model.Word;
import model.WordType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.json.*;

// MODELLED AFTER THE JSON DEMO
// Represents a reader that reads wordPool from JSON data stored in file
public class SessionJsonReader {
    private String source;
    private int intStart;
    private int intEnd;

    // EFFECTS: constructs reader to read from source file
    public SessionJsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads session List<Word> from file and returns it;
    // throws IOException if an error occurs reading data from file
    public List<Word> read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseSession(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses session List<Word> from JSON object and returns it
    private List<Word> parseSession(JSONObject jsonObject) {
        // String name = jsonObject.getString("name");
        List<Word> session = new ArrayList<>();
        addWords(session, jsonObject);
        this.intStart = jsonObject.getInt("int1"); // New
        this.intEnd = jsonObject.getInt("int2"); // New
        return session;
    }

    // MODIFIES: wp
    // EFFECTS: parses words from JSON object and adds them to session list
    private void addWords(List<Word> session, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("session");
        for (Object json : jsonArray) {
            JSONObject nextWord = (JSONObject) json;
            addWord(session, nextWord);
        }
    }

    // MODIFIES: wr
    // EFFECTS: parses word from JSON object and adds it to session list
    private void addWord(List<Word> session, JSONObject jsonObject) {
        String word = jsonObject.getString("word");
        String translation = jsonObject.getString("translation");
        WordType wordType = WordType.valueOf(jsonObject.getString("wordType"));
        Proficiency proficiencyLevel = Proficiency.valueOf(jsonObject.getString("proficiencyLevel"));
        int practiceCounter = jsonObject.getInt("practiceCounter");
        int waitTime = jsonObject.getInt("waitTime"); // NEW
        // what do we do with the practice counter
        Word w = new Word(word, translation, wordType, proficiencyLevel, waitTime);
        incrementWord(w, practiceCounter);
        session.add(w);
    }

    // EFFECTS: Increments a word pc times (pc = Practice Counter)
    protected void incrementWord(Word w, int pc) {
        for (int i = 0; i < pc; i++) {
            w.incrementCounterForJSon();
        }
    }

    // EFFECTS: returns json intStart
    public int getIntStart() {
        return intStart;
    }

    // EFFECTS: returns json intEnd
    public int getIntEnd() {
        return intEnd;
    }
}
