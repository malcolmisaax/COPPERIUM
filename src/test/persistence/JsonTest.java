package persistence;

import model.Proficiency;
import model.Word;
import model.WordPool;
import model.WordType;

import static org.junit.jupiter.api.Assertions.assertEquals;

// MODELLED AFTER THE JSON DEMO

public class JsonTest {
    protected void checkWord(String word, String translation,
                             WordType wordType, Proficiency p, int counter,
                             Word w) {
        assertEquals(word, w.getWord());
        assertEquals(translation, w.getTranslation());
        assertEquals(wordType, w.getWordType());
        assertEquals(p, w.getProficiency());
        assertEquals(counter, w.getPracticeCounter());
    }
}
