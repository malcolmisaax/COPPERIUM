package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import static model.Proficiency.*;

class WordTest {
    private Word word1;
    private Word word2;
    private Word word3;
    private Word word4;
    private Word word5;
    private Word word6;

    @BeforeEach
    public void Setup() {
        word1 = new Word("hola", "hello", WordType.Noun, Copper, 0);
        word2 = new Word("que tal", "how are you", WordType.Phrase, Bronze, 0);
        word3 = new Word("en breve", "shortly", WordType.Adverb, Silver, 0);
        word4 = new Word("romper", "break", WordType.Verb, Gold, 0);
        word5 = new Word("sucio", "dirty", WordType.Adjective, Platinum, 0);
        word6 = new Word(" ", " ", WordType.Phrase, Platinum, 1);
    }

    @Test
    public void getWordTest() {
        assertEquals("Hola", word1.getWord());
    }

    @Test
    public void getTranslationTest() {
        assertEquals("Hello", word1.getTranslation());
    }

    @Test
    public void getWordTypeTest() {
        assertEquals(WordType.Noun, word1.getWordType());
    }

    @Test
    public void getProficiencyTest() {
        assertEquals(Copper, word1.getProficiency());
    }

    @Test
    public void getProficiencyTestAfterMovingPools() {
        word1.setProficiencyLevel(Gold);
        assertEquals(Gold, word1.getProficiency());
    }

    @Test
    public void getPracticeCounterTest() {
        assertEquals(0, word1.getPracticeCounter());
    }

    @Test
    public void getPracticeCounterTestAfterOneIncrement() {
        word1.incrementCounter();
        assertEquals(1, word1.getPracticeCounter());
    }

    @Test
    public void resetWaitTimeTest() {
        word4.resetWaitTime();
        assertEquals(3, word4.getWaitTime());
    }

    @Test
    public void getPracticeCounterTestAfterTwoIncrements() {
        word1.incrementCounter();
        word1.incrementCounter();
        assertEquals(2, word1.getPracticeCounter());
    }

    @Test
    public void getWaitTimeTest() {
        assertEquals(0, word1.getWaitTime());
    }
    
    @Test
    public void lowerWaitTimeOneTest() {
        word3.incrementCounter();
        word3.lowerWaitTime();
        assertEquals(1, word3.getWaitTime());

    }

    @Test
    public void incrementCounterTestAfterOneIncrement() {
        word1.incrementCounter();
        assertEquals(1, word1.getPracticeCounter());
        assertEquals(0, word1.getWaitTime());
        assertEquals(Copper, word1.getProficiency());
    }

    @Test
    public void incrementCounterTestAfterTwoIncrements() {
        word1.incrementCounter();
        word1.incrementCounter();
        assertEquals(2, word1.getPracticeCounter());
        assertEquals(0, word1.getWaitTime());
    }

    @Test
    public void incrementCounterTestAfterMovingPoolCopper() {
        word1.incrementCounter();
        word1.incrementCounter();
        word1.incrementCounter();
        assertEquals(0, word1.getPracticeCounter());
        assertEquals(Bronze, word1.getProficiency());
        assertEquals(1, word1.getWaitTime());
    }

    @Test
    public void incrementCounterTestAfterMovingFromBronze() {
        word2.incrementCounter();
        word2.incrementCounter();
        word2.incrementCounter();
        assertEquals(0, word2.getPracticeCounter());
        assertEquals(Silver, word2.getProficiency());
        assertEquals(2, word2.getWaitTime());
    }

    @Test
    public void incrementCounterTestAfterMovingFromSilver() {
        word3.incrementCounter();
        word3.incrementCounter();
        word3.incrementCounter();
        assertEquals(0, word3.getPracticeCounter());
        assertEquals(Gold, word3.getProficiency());
        assertEquals(3, word3.getWaitTime());
    }

    @Test
    public void incrementCounterTestAfterMovingFromGold() {
        word4.incrementCounter();
        word4.incrementCounter();
        word4.incrementCounter();
        assertEquals(0, word4.getPracticeCounter());
        assertEquals(Platinum, word4.getProficiency());
        assertEquals(999999, word4.getWaitTime());
    }

    @Test
    public void incrementCounterPlatinumHasNoMaximum() {
        word5.incrementCounter();
        word5.incrementCounter();
        word5.incrementCounter();
        word5.incrementCounter();
        word5.incrementCounter();
        word5.incrementCounter();
        assertEquals(6, word5.getPracticeCounter());
        assertEquals(Platinum, word5.getProficiency());
        assertEquals(999999, word5.getWaitTime());
    }

    @Test
    public void incrementCounterForJsonTestAfterOneIncrement() {
        word1.incrementCounterForJSon();
        assertEquals(1, word1.getPracticeCounter());
        assertEquals(Copper, word1.getProficiency());
        assertEquals(0, word1.getWaitTime());
    }

    @Test
    public void incrementCounterForJsonTestAfterTwoIncrements() {
        word1.incrementCounterForJSon();
        word1.incrementCounterForJSon();
        assertEquals(2, word1.getPracticeCounter());
        assertEquals(0, word1.getWaitTime());
    }

    @Test
    public void incrementCounterForJSonTestAfterMovingPoolCopper() {
        word1.incrementCounterForJSon();
        word1.incrementCounterForJSon();
        word1.incrementCounterForJSon();
        assertEquals(0, word1.getPracticeCounter());
        assertEquals(Bronze, word1.getProficiency());
        assertEquals(0, word1.getWaitTime());
    }

    @Test
    public void incrementCounterForJSonTestAfterMovingFromBronze() {
        word2.incrementCounterForJSon();
        word2.incrementCounterForJSon();
        word2.incrementCounterForJSon();
        assertEquals(0, word2.getPracticeCounter());
        assertEquals(Silver, word2.getProficiency());
        assertEquals(0, word2.getWaitTime());
    }

    @Test
    public void incrementCounterForJSonTestAfterMovingFromSilver() {
        word3.incrementCounterForJSon();
        word3.incrementCounterForJSon();
        word3.incrementCounterForJSon();
        assertEquals(0, word3.getPracticeCounter());
        assertEquals(Gold, word3.getProficiency());
        assertEquals(0, word3.getWaitTime());
    }

    @Test
    public void incrementCounterForJSonTestAfterMovingFromGold() {
        word4.incrementCounterForJSon();
        word4.incrementCounterForJSon();
        word4.incrementCounterForJSon();
        assertEquals(0, word4.getPracticeCounter());
        assertEquals(Platinum, word4.getProficiency());
        assertEquals(0, word4.getWaitTime());
    }

    @Test
    public void incrementCounterForJSonPlatinumHasNoMaximum() {
        word5.incrementCounterForJSon();
        word5.incrementCounterForJSon();
        word5.incrementCounterForJSon();
        word5.incrementCounterForJSon();
        word5.incrementCounterForJSon();
        word5.incrementCounterForJSon();
        assertEquals(6, word5.getPracticeCounter());
        assertEquals(Platinum, word5.getProficiency());
        assertEquals(0, word5.getWaitTime());
    }

    @Test
    public void changeProficiencyToGoldTest() {
        word5.changeProficiency(Gold);
        assertEquals(Gold, word5.getProficiency());
    }

    @Test
    public void changeProficiencyToSilver() {
        word1.changeProficiency(Silver);
        assertEquals(Silver, word1.getProficiency());
    }

    @Test
    public void changeProficiencyToBronze() {
        word3.changeProficiency(Bronze);
        assertEquals(Bronze, word3.getProficiency());
    }

    @Test
    public void changeProficiencyToPlatinum() {
        word4.changeProficiency(Platinum);
        assertEquals(Platinum, word4.getProficiency());
    }

    @Test
    public void changeProficiencyToCopper() {
        word2.changeProficiency(Copper);
        assertEquals(Copper, word2.getProficiency());
    }

    @Test
    public void promoteWordFromCopperTest() {
        word1.promoteWord();
        assertEquals(Bronze, word1.getProficiency());
        assertEquals(0, word1.getPracticeCounter());
    }

    @Test
    public void promoteWordFromBronzeTest() {
        word2.promoteWord();
        assertEquals(Silver, word2.getProficiency());
        assertEquals(0, word2.getPracticeCounter());
    }

    @Test
    public void promoteWordFromSilverTest() {
        word3.promoteWord();
        assertEquals(Gold, word3.getProficiency());
        assertEquals(0, word3.getPracticeCounter());
    }

    @Test
    public void promoteWordFromGoldTest() {
        word4.promoteWord();
        assertEquals(Platinum, word4.getProficiency());
        assertEquals(0, word4.getPracticeCounter());
    }

    @Test
    public void promoteWordFromPlatTest() {
        word5.promoteWord();
        assertEquals(Platinum, word5.getProficiency());
        assertEquals(0, word5.getPracticeCounter());
    }

    @Test
    public void demoteWordFromPlatinumTest() {
        word5.demoteWord();
        assertEquals(Gold, word5.getProficiency());
    }

    @Test
    public void demoteWordFromGoldTest() {
        word4.demoteWord();
        assertEquals(Silver, word4.getProficiency());
        assertEquals(0, word4.getPracticeCounter());
    }

    @Test
    public void demoteWordTestFromSilver() {
        word3.demoteWord();
        assertEquals(Bronze, word3.getProficiency());
        assertEquals(0, word3.getPracticeCounter());
    }

    @Test
    public void demoteWordTestFromBronze() {
        word2.demoteWord();
        assertEquals(Copper, word2.getProficiency());
        assertEquals(0, word2.getPracticeCounter());
    }

    @Test
    public void demoteWordTestFromCopper() {
        word1.demoteWord();
        assertEquals(Copper, word1.getProficiency());
        assertEquals(0, word1.getPracticeCounter());
    }
}