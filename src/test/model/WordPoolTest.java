package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

import static model.Proficiency.*;

public class WordPoolTest {

    private WordPool wordPool;
    private List<Word> filteredList;
    private List<Word> sessionList;
    private Word word1;
    private Word word2;
    private Word word3;
    private Word word4;
    private Word word5;
    private Word word6;
    private Word word7;
    private Word word8;
    private Word word9;
    private Word word10;
    private Word word11;
    private Word word12;
    private Word word13;
    private Word word14;
    private Word word15;
    private Word word16;
    private Word word17;
    private Word word18;
    private Word word19;
    private Word word20;
    private Word word21;
    private Word word22;
    private Word word23;
    private Word word24;
    private Word word25;

    @BeforeEach
    public void Setup() {
        wordPool = new WordPool();
        word1 = new Word("hola", "hello", WordType.Noun, Copper, 0);
        word2 = new Word("que tal", "how are you", WordType.Phrase, Bronze, 1);
        word3 = new Word("en breve", "shortly", WordType.Adverb, Silver, 0);
        word4 = new Word("romper", "break", WordType.Verb, Gold, 0);
        word5 = new Word("sucio", "dirty", WordType.Adjective, Platinum, 0);
        word6 = new Word("mover", "move", WordType.Verb, Platinum, 0);
        word7 = new Word("lamentar", "feel sorry for", WordType.Verb, Silver, 0);
        word8 = new Word("atreverse", "dare", WordType.Verb, Bronze, 0);
        word9 = new Word("caber", "fit", WordType.Verb, Gold, 0);
        word10 = new Word("suceder", "happen", WordType.Verb, Platinum, 0);
        word11 = new Word("de camino", "on my way", WordType.Phrase, Bronze, 0);
        word12 = new Word("de sobra", "plenty", WordType.Phrase, Silver, 0);
        word13 = new Word("a juego", "matching", WordType.Adjective, Platinum, 0);
        word14 = new Word("presion", "pressure", WordType.Noun, Gold, 0);
        word15 = new Word("en breve", "shortly", WordType.Phrase, Bronze, 0);
        word16 = new Word("dentro de poco", "in no time", WordType.Phrase, Gold, 0);
        word17 = new Word("comprobar", "test", WordType.Verb, Silver, 0);
        word18 = new Word("ir al grano", "get to the point", WordType.Phrase, Copper, 0);
        word19 = new Word("ensayar", "practice", WordType.Verb, Copper, 0);
        word20 = new Word("ajeno/a", "someone else's", WordType.Adjective, Platinum, 0);
        word21 = new Word("que tal", "how are things", WordType.Phrase,Copper, 0);
        word22 = new Word("tu", "you", WordType.Noun, Bronze, 0);
        word23 = new Word("yo", "me", WordType.Noun, Bronze, 0);
        word24 = new Word("el", "he", WordType.Noun, Bronze, 0);
        word25 = new Word("ella", "she", WordType.Noun, Bronze, 0);
        filteredList = new ArrayList<>();
        sessionList = new ArrayList<>();
        filteredList.add(word1);
        filteredList.add(word2);
        filteredList.add(word3);
        filteredList.add(word4);
        filteredList.add(word5);
        filteredList.add(word6);
        filteredList.add(word7);
        filteredList.add(word8);
        filteredList.add(word9);
        filteredList.add(word10);
        wordPool.addWords(word1, word2, word3, word4);
        wordPool.addWords(word5, word6, word7, word8);
        wordPool.addWords(word9, word10, word11, word12);
        wordPool.addWords(word13, word14, word15, word16);
        wordPool.addWords(word17, word18, word19, word20);

    }

    @Test
    public void howManyWordsAreThereTest() {
        assertEquals(19, wordPool.howManyWordsAreThere());
    }

    @Test
    public void howManyWordsAreThereZeroTest() {
        WordPool wordPool2 = new WordPool();
        assertEquals(0, wordPool2.howManyWordsAreThere());
    }

    @Test
    public void getLastNumOfNewWordsTest() {
        WordPool wordPool2 = new WordPool();
        assertEquals(1, wordPool2.getLastNumOfNewWords());
    }

    @Test
    public void setLastNumOfNewWordsTest() {
        WordPool wordPool2 = new WordPool();
        wordPool2.setLastNumOfNewWords(3);
        assertEquals(3, wordPool2.getLastNumOfNewWords());
    }

    @Test
    public void getTimeBeforeNumOfNewWordsTest() {
        WordPool wordPool2 = new WordPool();
        assertEquals(1, wordPool2.getTimeBeforeNumOfNewWords());
    }

    @Test
    public void setTimeBeforeNumOfNewWordsTest() {
        wordPool.setTimeBeforeNumOfNewWords(2);
        assertEquals(2, wordPool.getTimeBeforeNumOfNewWords());
    }

    @Test
    public void getThreeTimesAgoNumOfNewWordsTest() {
        WordPool wordPool2 = new WordPool();
        assertEquals(1, wordPool2.getThreeTimesAgoNumOfNewWords());
    }

    @Test
    public void setThreeTimesAgoNumOfNewWordsTest() {
        wordPool.setThreeTimesAgoNumOfNewWords(3);
        assertEquals(3, wordPool.getThreeTimesAgoNumOfNewWords());
    }

    @Test
    public void getTest0() {
        Word w = wordPool.get(0);
        assertEquals(word1, w);
    }

    @Test
    public void getTest1() {
        Word w = wordPool.get(1);
        assertEquals(word2, w);
    }

    @Test
    public void getTest2() {
        Word w = wordPool.get(2);
        assertEquals(word3, w);
    }

    @Test
    public void sizeTest() {
        assertEquals(20, wordPool.size());
    }

    @Test
    public void sizeZeroTest() {
        WordPool wp = new WordPool();
        assertEquals(0, wp.size());
    }

    @Test
    public void addWordTest() {
        wordPool.addWord(word21);
        assertTrue(wordPool.containsWord(word21));
    }

    @Test
    public void addWordTryToAddSameTwiceTest() {
        wordPool.addWord(word21);
        wordPool.addWord(word21);
        assertTrue(wordPool.containsWord(word21));
    }

    @Test
    public void addWordsTwoTest() {
        wordPool.addWords(word21, word22);
        assertTrue(wordPool.containsWord(word21));
        assertTrue(wordPool.containsWord(word22));
    }

    @Test
    public void addWordsThreeTest() {
        wordPool.addWords(word21, word22, word23);
        assertTrue(wordPool.containsWord(word21));
        assertTrue(wordPool.containsWord(word22));
        assertTrue(wordPool.containsWord(word23));
    }

    @Test
    public void addWordTestForJson() {
        wordPool.addWordForJSon(word21);
        assertTrue(wordPool.containsWord(word21));
    }

    @Test
    public void addWordTryToAddSameTwiceTestForJSon() {
        wordPool.addWordForJSon(word21);
        wordPool.addWordForJSon(word21);
        assertTrue(wordPool.containsWord(word21));
    }

    @Test
    public void addWordsTwoTestForJson() {
        wordPool.addWordForJSon(word21);
        wordPool.addWordForJSon(word22);
        assertTrue(wordPool.containsWord(word21));
        assertTrue(wordPool.containsWord(word22));
    }

    @Test
    public void addWordsThreeTestForJSon() {
        wordPool.addWordForJSon(word21);
        wordPool.addWordForJSon(word22);
        wordPool.addWordForJSon(word23);
        assertTrue(wordPool.containsWord(word21));
        assertTrue(wordPool.containsWord(word22));
        assertTrue(wordPool.containsWord(word23));
    }

    @Test
    public void containsWordDoesContainTest() {
        assertTrue(wordPool.containsWord(word1));
    }

    @Test
    public void containsWordDoesContainDifferentHashTest() {
        Word word1copy = new Word("hola", "hello", WordType.Noun, Copper, 0);
        assertTrue(wordPool.containsWord(word1copy));
    }

    @Test
    public void containsWordDoesNotContainTest() {
        assertFalse(wordPool.containsWord(word23));
    }

    @Test
    public void filterByProficiencyCopperTest() {
        List<Word> filterTest = new ArrayList<>();
        filterTest.add(word1);
        filterTest.add(word18);
        filterTest.add(word19);
        assertEquals(filterTest, wordPool.filterByProficiency(Copper));
    }

    @Test
    public void filterByProficiencyBronzeTest() {
        List<Word> filterTest = new ArrayList<>();
        filterTest.add(word2);
        filterTest.add(word8);
        filterTest.add(word11);
        filterTest.add(word15);
        assertEquals(filterTest, wordPool.filterByProficiency(Bronze));
    }

    @Test
    public void filterByProficiencySilverTest() {
        List<Word> filterTest = new ArrayList<>();
        filterTest.add(word3);
        filterTest.add(word7);
        filterTest.add(word12);
        filterTest.add(word17);
        assertEquals(filterTest, wordPool.filterByProficiency(Silver));
    }

    @Test
    public void filterByProficiencyGoldTest() {
        List<Word> filterTest = new ArrayList<>();
        filterTest.add(word4);
        filterTest.add(word9);
        filterTest.add(word14);
        filterTest.add(word16);
        assertEquals(filterTest, wordPool.filterByProficiency(Gold));
    }

    @Test
    public void filterByProficiencyPlatinumTest() {
        List<Word> filterTest = new ArrayList<>();
        filterTest.add(word5);
        filterTest.add(word6);
        filterTest.add(word10);
        filterTest.add(word13);
        filterTest.add(word20);
        assertEquals(filterTest, wordPool.filterByProficiency(Platinum));
    }

    @Test
    public void sublistAndShuffleCopperTest() {
        assertEquals(3, wordPool.sublistAndShuffle(Copper).size());
    }

    @Test
    public void sublistAndShuffleBronzeTest() {
        assertEquals(4, wordPool.sublistAndShuffle(Bronze).size());
    }

    @Test
    public void sublistAndShuffleSilverTest() {
        assertEquals(4, wordPool.sublistAndShuffle(Silver).size());
    }
    @Test
    public void sublistAndShuffleGoldTest() {
        assertEquals(4, wordPool.sublistAndShuffle(Gold).size());
    }

    @Test
    public void sublistAndShufflePlatinumTest() {
        assertEquals(5, wordPool.sublistAndShuffle(Platinum).size());
    }

    @Test
    public void decideNumWordsForPoolTest() {
        assertEquals(5, wordPool.decideNumWordsForPool(10,
                0.5,
                filteredList));
    }

    @Test
    public void decideNumWordsForPoolRoundTest() {
        assertEquals(3, wordPool.decideNumWordsForPool(10,
                0.25,
                filteredList));
    }

    @Test
    public void decideNumWordsForPoolButMoreWordsNeededThanHaveTest() {
        assertEquals(10, wordPool.decideNumWordsForPool(20,
                0.75,
                filteredList));
    }


    @Test
    public void createMainSessionListNumWordsLessThanSizeTest() {
        List<Word> filteredPlatList = new ArrayList<>();
        filteredPlatList.add(word5);
        filteredPlatList.add(word6);
        filteredPlatList.add(word10);
        wordPool.createMainSessionList(sessionList, filteredPlatList, 3);
        assertEquals(3, sessionList.size());
        assertEquals(Platinum, sessionList.get(0).getProficiency());
        assertEquals(word20, sessionList.get(1));
        assertEquals(word13, sessionList.get(2));
        assertEquals(0, sessionList.get(1).getWaitTime());
        assertEquals(0, sessionList.get(2).getWaitTime());
    }

    @Test
    public void createMainSessionListNumMoreThanSizeTest() {
        List<Word> filteredPlatList = new ArrayList<>();
        word5.incrementCounter();
        filteredPlatList.add(word5);
        filteredPlatList.add(word6);
        filteredPlatList.add(word10);
        filteredPlatList.add(word20);
        wordPool.createMainSessionList(sessionList, filteredPlatList, 5);
        assertEquals(5, sessionList.size());
        assertEquals(Platinum, sessionList.get(0).getProficiency());
        assertEquals(word20, sessionList.get(1));
        assertEquals(word13, sessionList.get(2));
        assertEquals(1, sessionList.get(0).getPracticeCounter());
        assertEquals(0, sessionList.get(3).getPracticeCounter());
        assertEquals(0, sessionList.get(4).getPracticeCounter());
    }

    @Test
    public void createMainSessionListNumWordsEqualToSizeTest() {
        List<Word> filteredPlatList = new ArrayList<>();
        filteredPlatList.add(word5);
        filteredPlatList.add(word6);
        filteredPlatList.add(word10);
        wordPool.createMainSessionList(sessionList, filteredPlatList, 4);
        assertEquals(4, sessionList.size());
        assertEquals(Platinum, sessionList.get(0).getProficiency());
        assertEquals(word20, sessionList.get(1));
        assertEquals(word13, sessionList.get(2));
    }

    @Test
    public void getListOfSizeNWithEnoughWordsForEachSublistTest() {
        wordPool.addWord(word22);
        wordPool.addWord(word23);
        wordPool.addWord(word24);
        wordPool.addWord(word25);
        List<Word> practiceList = wordPool.getListOfSizeN(20);
        assertEquals(20, practiceList.size());
        assertEquals(word19.getWord(), practiceList.get(0).getWord());
        assertEquals(word18, practiceList.get(1));
        assertEquals(word1, practiceList.get(2));
        assertEquals(Bronze, practiceList.get(3).getProficiency());
        assertEquals(Silver, practiceList.get(11).getProficiency());
        assertEquals(Gold, practiceList.get(15).getProficiency());
        assertEquals(Platinum, practiceList.get(19).getProficiency());
    }

    @Test
    public void getListOfSizeNWithoutEnoughWordsForEachSublistTest() {
        List<Word> practiceList = wordPool.getListOfSizeN(19);
        assertEquals(19, practiceList.size());
        assertEquals(word19, practiceList.get(0));
        assertEquals(word18, practiceList.get(1));
        assertEquals(word1, practiceList.get(2));
        assertEquals(Bronze, practiceList.get(3).getProficiency());
        assertEquals(Silver, practiceList.get(6).getProficiency());
        assertEquals(Silver, practiceList.get(7).getProficiency());
        assertEquals(Gold, practiceList.get(10).getProficiency());
        assertEquals(Gold, practiceList.get(11).getProficiency());
        assertEquals(Platinum, practiceList.get(14).getProficiency());
        assertEquals(Platinum, practiceList.get(17).getProficiency());
    }

    @Test
    public void testGetListOfProficiency() {
        List<Word> session = new ArrayList<>();
        wordPool.listOfProficiency(session);
        assertEquals(word19, session.get(0));
        assertEquals(Copper, session.get(2).getProficiency());
        assertEquals(Bronze, session.get(6).getProficiency());
        assertEquals(Silver, session.get(10).getProficiency());
        assertEquals(Gold, session.get(13).getProficiency());
    }

    @Test
    public void  testGetListOfProficiencyEmpty() {
        WordPool wordPool2 = new WordPool();
        Word word = new Word("hi", "jo", WordType.Noun, Gold, 3);
        List<Word> session = new ArrayList<>();
        wordPool2.listOfProficiency(session);
        assertEquals(0, session.size());
    }

    @Test
    public void getListOfPlatPracticeCounterZeroTest() {
        List<Word> list = wordPool.getListOfPlatPracticeCounter(0);
        assertEquals(5, list.size());
        assertEquals(word20, list.get(0));
    }

    @Test
    public void getListOfPlatPracticeCounterOneWhenThereArentAnyTest() {
        List<Word> list = wordPool.getListOfPlatPracticeCounter(1);
        assertEquals(0, list.size());
    }

    @Test
    public void getListOfPlatPracticeCounterOneWhenThereAreTest() {
        word20.incrementCounter();
        List<Word> list = wordPool.getListOfPlatPracticeCounter(1);
        assertEquals(1, list.size());
    }

    @Test
    public void addWordToSessionTest() {
        List<Word> emptyList = new ArrayList<>();
        wordPool.addWordToSession(emptyList, Platinum);
        assertEquals(5, emptyList.size());
    }

}
