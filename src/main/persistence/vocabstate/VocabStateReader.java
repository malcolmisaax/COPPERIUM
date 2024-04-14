package persistence.vocabstate;

import model.Proficiency;
import model.Word;
import model.WordPool;
import model.WordType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

// MODELLED AFTER THE JSON DEMO
// Represents a reader that reads wordPool from JSON data stored in file
public class VocabStateReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public VocabStateReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public WordPool read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseWordPool(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses workroom from JSON object and returns it
    private WordPool parseWordPool(JSONObject jsonObject) {
        // String name = jsonObject.getString("name");
        WordPool wp = new WordPool();
        addWords(wp, jsonObject);
        int lastNumOfNewWords = jsonObject.getInt("lastNumOfNewWords");
        wp.setLastNumOfNewWords(lastNumOfNewWords);
        int timeBeforeNumOfNewWords = jsonObject.getInt("timeBeforeNumOfNewWords");
        wp.setTimeBeforeNumOfNewWords(timeBeforeNumOfNewWords);
        int threeTimesAgoNumOfNewWords = jsonObject.getInt("threeTimesAgoNumOfNewWords");
        wp.setThreeTimesAgoNumOfNewWords(threeTimesAgoNumOfNewWords);
        return wp;
    }

    // MODIFIES: wp
    // EFFECTS: parses thingies from JSON object and adds them to workroom
    private void addWords(WordPool wp, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("wordPool");
        for (Object json : jsonArray) {
            JSONObject nextWord = (JSONObject) json;
            addWord(wp, nextWord);
        }
    }

    // MODIFIES: wr
    // EFFECTS: parses thingy from JSON object and adds it to workroom
    private void addWord(WordPool wp, JSONObject jsonObject) {
        String word = jsonObject.getString("word");
        String translation = jsonObject.getString("translation");
        WordType wordType = WordType.valueOf(jsonObject.getString("wordType"));
        Proficiency proficiencyLevel = Proficiency.valueOf(jsonObject.getString("proficiencyLevel"));
        int practiceCounter = jsonObject.getInt("practiceCounter");
        int waitTime = jsonObject.getInt("waitTime"); // NEW
        // what do we do with the practice counter
        Word w = new Word(word, translation, wordType, proficiencyLevel, waitTime);
        incrementWord(w, practiceCounter);
        wp.addWordForJSon(w);
    }

    // EFFECTS: Increments a word pc times (pc = Practice Counter)
    protected void incrementWord(Word w, int pc) {
        for (int i = 0; i < pc; i++) {
            w.incrementCounterForJSon();
        }
    }


}
