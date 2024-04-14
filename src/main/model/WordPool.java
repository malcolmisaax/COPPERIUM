package model;

import model.eventlogging.Event;
import model.eventlogging.EventLog;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordPool implements Writable {
    private final List<Word> wordPool;
    private int lastNumOfNewWords;
    private int timeBeforeNumOfNewWords;
    private int threeTimesAgoNumOfNewWords;


    // A collection of Words
    public WordPool() {
        this.wordPool = new ArrayList<>();
        this.lastNumOfNewWords = 1;
        this.timeBeforeNumOfNewWords = 1;
        this.threeTimesAgoNumOfNewWords = 1;
    }

    // EFFECTS: Returns the integer of how many words need to be practiced with that day
    public int howManyWordsAreThere() {
        int i = 0;
        for (Word w: wordPool) {
            if (w.getWaitTime() == 0) {
                i += 1;
            }
        }
        return i;
    }

    // EFFECTS: Sets the lastNumOfNewWords variable, indicating how many words the user added last session
    public void setLastNumOfNewWords(int i) {
        this.lastNumOfNewWords = i;
    }

    // EFFECTS: Gets the lastNumOfNewWords variable, indicating how many words the user added last session
    public int getLastNumOfNewWords() {
        return this.lastNumOfNewWords;
    }

    // EFFECTS: Sets the timeBeforeNumOfNewWords variable, indicating how many words the user added two sessions ago
    public void setTimeBeforeNumOfNewWords(int i) {
        this.timeBeforeNumOfNewWords = i;
    }

    // EFFECTS: Gets the timeBeforeNumOfNewWords variable, indicating how many words the user added two sessions ago
    public int getTimeBeforeNumOfNewWords() {
        return this.timeBeforeNumOfNewWords;
    }

    // EFFECTS: Gets the threeTimesAgoNumOfNewWords variable, indicating how many words the user added three
    //          sessions ago
    public int getThreeTimesAgoNumOfNewWords() {
        return this.threeTimesAgoNumOfNewWords;
    }

    // EFFECTS: Sets the threeTimesAgoNumOfNewWords variable, indicating how many words the user added three
    //          sessions ago
    public void setThreeTimesAgoNumOfNewWords(int i) {
        this.threeTimesAgoNumOfNewWords = i;
    }

    // EFFECTS: returns the word in position i
    public Word get(int i) {
        return wordPool.get(i);
    }

    // EFFECTS: returns the size of the wordPool
    public int size() {
        return wordPool.size();
    }


    // MODIFIES: This
    // EFFECTS: adds a word to the word pool
    public void addWord(Word w) {
        if (!containsWord(w)) {
            this.wordPool.add(w);
        }
        EventLog.getInstance().logEvent(new Event("Added word '" + w.getWord()
                + "' to your vocabulary."));
    }

    // MODIFIES: This
    // EFFECTS: adds a word to the word pool
    public void addWordForJSon(Word w) {
        if (!containsWord(w)) {
            this.wordPool.add(w);
        }
        // EventLog.getInstance().logEvent(new Event("Added word:  " + w.getWord()));
    }

    // MODIFIES: This
    // EFFECTS:
    public void addWords(Word... words) {
        for (Word w : words) {
            addWord(w);
        }
    }

    // EFFECTS: checks to see if the word is already in the pool
    public boolean containsWord(Word w) {
        return wordPool.contains(w);
    }


    // EFFECTS: Filters word pool by proficiency and returns
    //          new filtered word pool
    public List<Word> filterByProficiency(Proficiency p) {
        List<Word> filteredWords = new ArrayList<>();
        for (Word w: wordPool) {
            if (w.getProficiency() == p) {
                filteredWords.add(w);
            }
        }
        return filteredWords;
    }


    // EFFECTS: Filters word pool by given proficiency
    //          then shuffles the order randomly
    //          then returns this new sublist
    public List<Word> sublistAndShuffle(Proficiency p) {
        List<Word> filtered = filterByProficiency(p);
        Collections.shuffle(filtered);
        return filtered;
    }


    // REQUIRES: remainingWords > 0
    //           ratio > 0.0
    // EFFECTS:  figures out how many words should come from
    //           provided filteredSublist of words based on
    //           given ratio and given remainingWords.
    //           If there are fewer words in the sublist than
    //           there are words needed, just make the final
    //           num the size of the sublist
    public int decideNumWordsForPool(int remainingWords,
                                     double ratio,
                                     List<Word> filteredSubList) {
        int finalNum = (int) Math.ceil(remainingWords * ratio);
        if (finalNum >= filteredSubList.size()) {
            finalNum = filteredSubList.size();
        }
        return finalNum;
    }


    // REQUIRES: numWords > 0
    // MODIFIES: sessionList
    // EFFECTS:  with given int numWords, adds the proper amount of unpracticed platinum words to the given sessionList
    public void createMainSessionList(List<Word> sessionList,
                                      List<Word> filteredPlat,
                                      int numWords) {
        int sizeNeeded = numWords;
        if (numWords > 0) {
            sessionList.addAll(filteredPlat.subList(0, 1));
            sizeNeeded -= 1;
        }
        // add numWords - 1 platinum words with wait time 0. If numWords - 1 is less than the amount of
        // words with wait time i, re-iterate through with i + 1, and numWords reduces by the amount of words with
        // wait time i
        Boolean stop = false;
        for (int i = 0; !stop; i++) {
            List<Word> platPracticeCountList = getListOfPlatPracticeCounter(i);
            if (platPracticeCountList.size() <= sizeNeeded) {
                sessionList.addAll(platPracticeCountList);
                sizeNeeded -= platPracticeCountList.size();
            } else {
                sessionList.addAll(platPracticeCountList.subList(0, sizeNeeded));

                stop = true;
            }
        }
    }

    // EFFECTS: returns a filtered list of platinum words with given practice counter
    public List<Word> getListOfPlatPracticeCounter(int i) {
        List<Word> platWords = new ArrayList<>();
        for (int n = (wordPool.size() - 1); n >= 0; n--) {
            Word w = wordPool.get(n);
            if (w.getProficiency() == Proficiency.Platinum && w.getPracticeCounter() == i) {
                platWords.add(w);
            }
        }
        return platWords;
    }



    // REQUIRES: wordPool.size() <= n &&
    //           30 >= n >= 20
    // EFFECTS:  creates a list of words of size n for the user
    //           to practice with
    public List<Word> getListOfSizeN(int n) {
        // create sublists for each proficiency level
        // and shuffle
        List<Word> sessionList = new ArrayList<>();
        List<Word> filteredPlat = sublistAndShuffle(Proficiency.Platinum);
        listOfProficiency(sessionList);
        int numPlat = n - sessionList.size();
        try {
            createMainSessionList(sessionList, filteredPlat, numPlat);
        } catch (Exception e) {
            //
        }
        for (Word w: wordPool) {
            if (!sessionList.contains(w) && !(w.getProficiency() == Proficiency.Platinum)) {
                w.lowerWaitTime();
            }
        }
        EventLog.getInstance().logEvent(new Event("A practice set of size " + sessionList.size()
                + " has been created."));
        return sessionList;
    }

    // EFFECTS: Adds all Copper, Bronze, Silver and Gold words with waitTime 0 in that order (Copper -> Gold)
    public void listOfProficiency(List<Word> sessionList) {
        addWordToSession(sessionList, Proficiency.Copper);
        addWordToSession(sessionList, Proficiency.Bronze);
        addWordToSession(sessionList, Proficiency.Silver);
        addWordToSession(sessionList, Proficiency.Gold);
    }

    // EFFECTS: adds words with given proficiency with waitTime 0 to the sessionList, iterating in reverse order
    public void addWordToSession(List<Word> sessionList, Proficiency p) {
        for (int i = wordPool.size() - 1; i >= 0; i--) {
            Word w = wordPool.get(i);
            if (w.getWaitTime() == 0 && w.getProficiency() == p) {
                sessionList.add(w);
            }
        }
    }

    /*
    // EFFECTS: Filters word pool by proficiency and returns
    //          new filtered word pool
    public List<Word> filterByProficiency(Proficiency p) {
        List<Word> filteredWords = new ArrayList<>();
        for (Word w: wordPool) {
            if (w.getProficiency() == p) {
                filteredWords.add(w);
            }
        }
        return filteredWords;
    }

    // EFFECTS: Filters word pool by given proficiency
    //          then shuffles the order randomly
    //          then returns this new sublist
    public List<Word> sublistAndShuffle(Proficiency p) {
        List<Word> filtered = filterByProficiency(p);
        Collections.shuffle(filtered);
        return filtered;
    }

    // REQUIRES: remainingWords > 0
    //           ratio > 0.0
    // EFFECTS:  figures out how many words should come from
    //           provided filteredSublist of words based on
    //           given ratio and given remainingWords.
    //           If there are fewer words in the sublist than
    //           there are words needed, just make the final
    //           num the size of the sublist
    public int decideNumWordsForPool(int remainingWords,
                                     double ratio,
                                     List<Word> filteredSubList) {
        int finalNum = (int) Math.ceil(remainingWords * ratio);
        if (finalNum >= filteredSubList.size()) {
            finalNum = filteredSubList.size();
        }
        return finalNum;
    }

    // REQUIRES: numWords > 0
    // MODIFIES: sessionList
    // EFFECTS:  with given int numWords, takes the first n
    //           words from the shuffled list and returns a smaller
    //           sublist of n words
    public void createMainSessionList(List<Word> sessionList,
                                            List<Word> filteredList,
                                            int numWords) {
        List<Word> subList = new ArrayList<>(filteredList.subList(0, numWords));
        sessionList.addAll(subList);
    }

    // REQUIRES: wordPool.size() <= n &&
    //           30 >= n >= 15
    // EFFECTS:  creates a list of words of size n for the user
    //           to practice with
    public List<Word> getListOfSizeN(int n) {
        // create sublists for each proficiency level
        // and shuffle
        List<Word> sessionList = new ArrayList<>();
        List<Word> filteredBronze = sublistAndShuffle(Proficiency.Bronze);
        List<Word> filteredSilver = sublistAndShuffle(Proficiency.Silver);
        List<Word> filteredGold = sublistAndShuffle(Proficiency.Gold);
        List<Word> filteredPlat = sublistAndShuffle(Proficiency.Platinum);
        List<Word> filteredCopper = filterByProficiency(Proficiency.Copper);
        // n words minus # of copper words
        n = n - filteredCopper.size();
        // calculate 50% of n-remaining words to be Bronze
        int numBronze = decideNumWordsForPool(n, 0.5, filteredBronze);
        n = n - numBronze;
        // calculate 60% of n-remaining words to be Silver
        int numSilver = decideNumWordsForPool(n, 0.6, filteredSilver);
        n = n - numSilver;
        // calculate 70% of n-remaining words to be Gold
        int numGold = decideNumWordsForPool(n, 0.7, filteredGold);
        // calculate rest of n words to be Platinum
        int numPlat = n - numGold;
        // add copper words
        sessionList.addAll(filteredCopper);
        // add previously decided number of Bronze words
        createMainSessionList(sessionList, filteredBronze, numBronze);
        // add previously decided number of Silver words
        createMainSessionList(sessionList, filteredSilver, numSilver);
        // add previously decided number of Gold words
        createMainSessionList(sessionList, filteredGold, numGold);
        // add previously decided number of Plat words
        createMainSessionList(sessionList, filteredPlat, numPlat);
        return sessionList;
    }

     */

    // EFFECTS: Turn wordPool into JSON
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("wordPool", wordPoolToJson());
        json.put("lastNumOfNewWords", lastNumOfNewWords);
        json.put("timeBeforeNumOfNewWords", timeBeforeNumOfNewWords);
        json.put("threeTimesAgoNumOfNewWords", threeTimesAgoNumOfNewWords);
        return json;
    }

    // EFFECTS: Parses wordPool, word by word, to JSON
    private JSONArray wordPoolToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Word w : wordPool) {
            jsonArray.put(w.toJson());
        }

        return jsonArray;
    }


}