package persistence.session;

import model.Proficiency;
import model.Word;
import model.WordType;
import org.junit.jupiter.api.Test;
import persistence.JsonTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// MODELLED AFTER THE JSON DEMO

class SessionJsonReaderTest extends JsonTest {

    @Test
    void testSessionReaderNonExistentFile() {
        SessionJsonReader reader = new SessionJsonReader("./data/noSuchFile.json");
        try {
            List<Word> session = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // should pass
        }
    }

    @Test
    void testSessionReaderEmptyWorkRoom() {
        try {
            List<Word> sessionList = new ArrayList<>();
            SessionJsonWriter writer = new SessionJsonWriter("./data/testSessionEmptyReader.json");
            writer.open();
            writer.write(sessionList,0, 0);
            writer.close();
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
        SessionJsonReader reader = new SessionJsonReader("./data/testSessionEmptyReader.json");
        try {
            List<Word> session = reader.read();
            assertEquals(0, session.size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testSessionReaderGeneralWorkRoom() {
        SessionJsonReader reader = new SessionJsonReader("./data/testGeneralSessionReader.json");
        try {
            List<Word> sessionList = new ArrayList<>();
            sessionList.add(new Word("romperse", "break", WordType.Verb, Proficiency.Copper, 0));
            sessionList.add(new Word("rayar", "scratch", WordType.Verb, Proficiency.Copper, 0));
            SessionJsonWriter writer = new SessionJsonWriter("./data/testGeneralSessionReader.json");
            writer.open();
            writer.write(sessionList, 0, 1);
            writer.close();

            List<Word> session = reader.read();
            assertEquals(2, session.size());

            checkWord("Romperse", "Break", WordType.Verb,
                    Proficiency.Copper, 0, session.get(0));
            checkWord("Rayar", "Scratch", WordType.Verb,
                    Proficiency.Copper, 0, session.get(1));
            assertEquals(0, reader.getIntStart());
            assertEquals(1, reader.getIntEnd());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void incrementWordTest() {
        Word word1 = new Word("romperse", "break", WordType.Verb, Proficiency.Copper, 0);
        SessionJsonReader reader = new SessionJsonReader("./data/testGeneralSessionReader.json");
        reader.incrementWord(word1, 1);
        assertEquals(1, word1.getPracticeCounter());
    }

    @Test
    void incrementWordTwiceTest() {
        Word word1 = new Word("romperse", "break", WordType.Verb, Proficiency.Copper, 0);
        SessionJsonReader reader = new SessionJsonReader("./data/testGeneralSessionReader.json");
        reader.incrementWord(word1, 2);
        assertEquals(2, word1.getPracticeCounter());
    }

    @Test
    void incrementWordThriceTest() {
        Word word1 = new Word("romperse", "break", WordType.Verb, Proficiency.Copper, 0);
        SessionJsonReader reader = new SessionJsonReader("./data/testGeneralSessionReader.json");
        reader.incrementWord(word1, 3);
        assertEquals(0, word1.getPracticeCounter());
        assertEquals(Proficiency.Bronze, word1.getProficiency());
    }

}