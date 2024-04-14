package persistence.vocabstate;


import org.junit.jupiter.api.Test;

import model.Proficiency;
import model.Word;
import model.WordPool;
import model.WordType;
import persistence.JsonTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class VocabStateWriterTest extends JsonTest {
    //MODELLED OFF OF THE DEMO

    @Test
    void testWriterInvalidFile() {
        try {
            WordPool wp = new WordPool();
            VocabStateWriter writer = new VocabStateWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyWordPool() {
        try {
            WordPool wp = new WordPool();
            VocabStateWriter writer = new VocabStateWriter("./data/testWriterEmptyWordPool.json");
            writer.open();
            writer.write(wp);
            writer.close();

            VocabStateReader reader = new VocabStateReader("./data/testWriterEmptyWordPool.json");
            wp = reader.read();
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterSomeWordPool() {
        try {
            WordPool wp = new WordPool();
            wp.addWord(new Word("romperse", "break", WordType.Verb, Proficiency.Copper, 0));
            wp.addWord(new Word("rayar", "scratch", WordType.Verb, Proficiency.Copper, 0));
            VocabStateWriter writer = new VocabStateWriter("./data/testWriterGeneralWordPool.json");
            writer.open();
            writer.write(wp);
            writer.close();

            VocabStateReader reader = new VocabStateReader("./data/testWriterGeneralWordPool.json");
            wp = reader.read();
            checkWord("Romperse", "Break", WordType.Verb, Proficiency.Copper, 0, wp.get(0));
            checkWord("Rayar", "Scratch", WordType.Verb, Proficiency.Copper, 0, wp.get(1));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}