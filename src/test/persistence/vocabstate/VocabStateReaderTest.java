package persistence.vocabstate;

import model.Proficiency;
import model.Word;
import model.WordPool;
import model.WordType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.JsonTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

// MODELLED AFTER THE JSON DEMO

class VocabStateReaderTest extends JsonTest {

    @BeforeEach
    public void Setup() {
        try {
            WordPool wp = new WordPool();
            VocabStateWriter writer = new VocabStateWriter("./data/testReaderEmptyWordPool.json");
            writer.open();
            writer.write(wp);
            writer.close();
        } catch (Exception e) {
            fail("exception found");
        }
        try {
            WordPool wp = new WordPool();
            wp.addWord(new Word("romperse", "break", WordType.Verb, Proficiency.Copper, 0));
            wp.addWord(new Word("rayar", "scratch", WordType.Verb, Proficiency.Copper, 0));
            VocabStateWriter writer = new VocabStateWriter("./data/testReaderGeneralWordPool.json");
            writer.open();
            writer.write(wp);
            writer.close();
        } catch (Exception e) {
            fail("Should not have found exception");
        }

    }


    @Test
    void testReaderNonExistentFile() {
        VocabStateReader reader = new VocabStateReader("./data/noSuchFile.json");
        try {
            WordPool wp = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // should pass
        }
    }

    @Test
    void testReaderEmptyWorkRoom() {
        VocabStateReader reader = new VocabStateReader("./data/testReaderEmptyWordPool.json");
        try {
            WordPool wp = reader.read();
            assertEquals(0, wp.size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralWorkRoom() {
        VocabStateReader reader = new VocabStateReader("./data/testReaderGeneralWordPool.json");
        try {
            WordPool wp = reader.read();
            assertEquals(2, wp.size());

            checkWord("Romperse", "Break", WordType.Verb,
                    Proficiency.Copper, 0, wp.get(0));
            checkWord("Rayar", "Scratch", WordType.Verb,
                    Proficiency.Copper, 0, wp.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void incrementWordTest() {
        Word word1 = new Word("romperse", "break", WordType.Verb, Proficiency.Copper, 0);
        VocabStateReader reader = new VocabStateReader("./data/testReaderGeneralWordPool.json");
        reader.incrementWord(word1, 1);
        assertEquals(1, word1.getPracticeCounter());
    }

    @Test
    void incrementWordTwiceTest() {
        Word word1 = new Word("romperse", "break", WordType.Verb, Proficiency.Copper, 0);
        VocabStateReader reader = new VocabStateReader("./data/testReaderGeneralWordPool.json");
        reader.incrementWord(word1, 2);
        assertEquals(2, word1.getPracticeCounter());
    }

    @Test
    void incrementWordThriceTest() {
        Word word1 = new Word("romperse", "break", WordType.Verb, Proficiency.Copper, 0);
        VocabStateReader reader = new VocabStateReader("./data/testReaderGeneralWordPool.json");
        reader.incrementWord(word1, 3);
        assertEquals(0, word1.getPracticeCounter());
        assertEquals(Proficiency.Bronze, word1.getProficiency());
    }




}