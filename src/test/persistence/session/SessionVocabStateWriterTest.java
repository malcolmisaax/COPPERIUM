package persistence.session;


import org.junit.jupiter.api.Test;

import model.Proficiency;
import model.Word;
import model.WordType;
import persistence.JsonTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SessionVocabStateWriterTest extends JsonTest {
    //MODELLED OFF OF THE DEMO

    @Test
    void testSessionWriterInvalidFile() {
        try {
            SessionJsonWriter writer = new SessionJsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testSessionWriterEmptyWordPool() {
        try {
            List<Word> session = new ArrayList<>();
            SessionJsonWriter writer = new SessionJsonWriter("./data/testSessionEmptyWriter.json");
            writer.open();
            writer.write(session,0, 0);
            writer.close();

            SessionJsonReader reader = new SessionJsonReader("./data/testSessionEmptyWriter.json");
            session = reader.read();
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testSessionWriterSomeSession() {
        try {
            List<Word> session = new ArrayList<>();
            session.add(new Word("romperse", "break", WordType.Verb, Proficiency.Copper, 0));
            session.add(new Word("rayar", "scratch", WordType.Verb, Proficiency.Copper, 0));
            SessionJsonWriter writer = new SessionJsonWriter("./data/testGeneralSessionWriter.json");
            writer.open();
            writer.write(session, 0, 1);
            writer.close();

            SessionJsonReader reader = new SessionJsonReader("./data/testGeneralSessionWriter.json");
            session = reader.read();
            checkWord("Romperse", "Break", WordType.Verb, Proficiency.Copper, 0, session.get(0));
            checkWord("Rayar", "Scratch", WordType.Verb, Proficiency.Copper, 0, session.get(1));
            assertEquals(2, session.size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}