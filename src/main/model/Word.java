package model;

import model.eventlogging.Event;
import model.eventlogging.EventLog;
import org.json.JSONObject;
import persistence.Writable;

import java.util.Objects;

import static model.Proficiency.*;


// A word with a string, translation, wordtype, proficiencyLevel, and counter
   // represents a word in a vocabulary

public class Word implements Writable {


    private static final int MAX_COPPER_COUNT = 3;
    private static final int MAX_BRONZE_COUNT = 3;
    private static final int MAX_SILVER_COUNT = 3;
    private static final int MAX_GOLD_COUNT = 3;


    private String word;
    private String translation;
    private WordType wordType;
    private Proficiency proficiencyLevel;
    private int practiceCounter;
    private int waitTime;


    // MODIFIES: This
    // EFFECTS: Constructs a word with given word, translation, wordtype, proficiency, and waittime
    //          Sets practiceCounter
    public Word(String word,
                String translation,
                WordType wordType,
                Proficiency proficiencyLevel,
                int waitTime) {
        try {
            this.word = word.substring(0, 1).toUpperCase()
                    + word.substring(1).toLowerCase();
        } catch (Exception e) {
            this.word = word;
        }
        try {
            this.translation = translation.substring(0, 1).toUpperCase()
                    + translation.substring(1).toLowerCase();
        } catch (Exception e) {
            this.translation = translation;
        }
        this.wordType = wordType;
        this.proficiencyLevel = proficiencyLevel;
        this.practiceCounter = 0;
        this.waitTime = waitTime;
    }


    // EFFECTS: returns the word in the provided language
    public String getWord() {
        return this.word;
    }


    // EFFECTS: returns the english translation
    public String getTranslation() {
        return this.translation;
    }


    // EFFECTS: returns the word type (Verb, Noun, etc...)
    public WordType getWordType() {
        return this.wordType;
    }


    // EFFECTS: returns ProficiencyLevel (Enum) (Copper, Bronze, etc...)
    public Proficiency getProficiency() {
        return this.proficiencyLevel;
    }


    // EFFECTS: returns number of times word has been practiced with
    public int getPracticeCounter() {
        return this.practiceCounter;
    }


    // EFFECTS: Returns waitTime of word
    public int getWaitTime() {
        return this.waitTime;
    }

    // MODIFIES: This
    // EFFECTS: Lowers the word's wait time by one
    public void lowerWaitTime() {
        this.waitTime -= 1;
    }


    // MODIFIES: This
    // EFFECTS: Changes proficiency Level
    public void setProficiencyLevel(Proficiency proficiencyLevel) {
        this.proficiencyLevel = proficiencyLevel;
    }

    // MODIFIES: this
    // EFFECTS: increments counter, resets it's waitTime, and moves word if it has
    //          reached enough proficiency
    public void incrementCounter() {
        this.practiceCounter += 1;
        if (proficiencyLevel == Proficiency.Copper
                && practiceCounter == MAX_COPPER_COUNT) {
            promoteWord();
        } else if (proficiencyLevel == Proficiency.Bronze
                && practiceCounter == MAX_BRONZE_COUNT) {
            promoteWord();
        } else if (proficiencyLevel == Proficiency.Silver
                && practiceCounter == MAX_SILVER_COUNT) {
            promoteWord();
        } else if (proficiencyLevel == Proficiency.Gold
                && practiceCounter == MAX_GOLD_COUNT) {
            promoteWord();
        } else {
            EventLog.getInstance().logEvent(new Event("You practiced with word '" + this.getWord()
                    + "'."));
        }
        this.resetWaitTime();

    }

    // MODIFIES: This
    // EFFECTS: Increments the word counter without resetting waitTime
    public void incrementCounterForJSon() {
        this.practiceCounter += 1;
        if (proficiencyLevel == Proficiency.Copper
                && practiceCounter == MAX_COPPER_COUNT) {
            promoteWord();
        } else if (proficiencyLevel == Proficiency.Bronze
                && practiceCounter == MAX_BRONZE_COUNT) {
            promoteWord();
        } else if (proficiencyLevel == Proficiency.Silver
                && practiceCounter == MAX_SILVER_COUNT) {
            promoteWord();
        } else if (proficiencyLevel == Proficiency.Gold
                && practiceCounter == MAX_GOLD_COUNT) {
            promoteWord();
        }
    }

    // MODIFIES: this
    // EFFECTS: Changes proficiency level to given proficiency
    // and resets the counter to 0
    public void changeProficiency(Proficiency p) {
        this.proficiencyLevel = p;
        this.practiceCounter = 0;

    }

    // MODIFIES: This
    // EFFECTS: Resets the waittime for a word
    public void resetWaitTime() {
        Proficiency p = this.proficiencyLevel;
        if (p == Copper) {
            this.waitTime = 0;
        } else if (p == Platinum) {
            this.waitTime = 999999;
        } else if (p == Bronze) {
            this.waitTime = 1;
        } else if (p == Silver) {
            this.waitTime = 2;
        } else {
            this.waitTime = 3;
        }
    }


    // REQUIRES: proficiencyLevel != PLATINUM
    // MODIFIES: this
    // EFFECTS: Changes the proficiency level of a word to
    //          a better proficiency
    public void promoteWord() {
        if (proficiencyLevel == Proficiency.Copper) {
            changeProficiency(Proficiency.Bronze);
            EventLog.getInstance().logEvent(new Event("You practiced with word '" + this.getWord()
                    + "'." + " It has been promoted to BRONZE standing."));
        } else if (proficiencyLevel == Bronze) {
            changeProficiency(Proficiency.Silver);
            EventLog.getInstance().logEvent(new Event("You practiced with word '" + this.getWord()
                    + "'." + " It has been promoted to SILVER standing."));
        } else if (proficiencyLevel == Proficiency.Silver) {
            changeProficiency(Proficiency.Gold);
            EventLog.getInstance().logEvent(new Event("You practiced with word '" + this.getWord()
                    + "'." + " It has been promoted to GOLD standing."));
        } else if (proficiencyLevel == Proficiency.Gold) {
            changeProficiency(Proficiency.Platinum);
            EventLog.getInstance().logEvent(new Event("You practiced with word '" + this.getWord()
                    + "'." + " It has been promoted to PLATINUM standing."));
        }
    }


    // REQUIRES: proficiencyLevel != COPPER
    // MODIFIES: this
    // EFFECTS: Changes the proficiency level of a word to
    //          a worse proficiency
    public void demoteWord() {
        if (proficiencyLevel == Proficiency.Platinum) {
            changeProficiency(Proficiency.Gold);
        } else if (proficiencyLevel == Proficiency.Gold) {
            changeProficiency(Proficiency.Silver);
        } else if (proficiencyLevel == Proficiency.Silver) {
            changeProficiency(Proficiency.Bronze);
        } else if (proficiencyLevel == Proficiency.Bronze) {
            changeProficiency(Proficiency.Copper);
        }
    }

    /*
    private static final int MAX_COPPER_COUNT = 3;
    private static final int MAX_BRONZE_COUNT = 4;
    private static final int MAX_SILVER_COUNT = 4;
    private static final int MAX_GOLD_COUNT = 4;

    private String word;
    private String translation;
    private WordType wordType;
    private Proficiency proficiencyLevel;
    private int practiceCounter;

    public Word(String word,
                String translation,
                WordType wordType,
                Proficiency proficiencyLevel) {
        this.word = word.substring(0, 1).toUpperCase()
                + word.substring(1).toLowerCase();
        this.translation = translation.substring(0, 1).toUpperCase()
                + translation.substring(1).toLowerCase();
        this.wordType = wordType;
        this.proficiencyLevel = proficiencyLevel;
        this.practiceCounter = 0;
    }

    // EFFECTS: returns the word in the provided language
    public String getWord() {
        return this.word;
    }

    // EFFECTS: returns the english translation
    public String getTranslation() {
        return this.translation;
    }

    // EFFECTS: returns the word type (Verb, Noun, etc...)
    public WordType getWordType() {
        return this.wordType;
    }

    // EFFECTS: returns ProficiencyLevel (Enum) (Copper, Bronze, etc...)
    public Proficiency getProficiency() {
        return this.proficiencyLevel;
    }

    // EFFECTS: returns number of times word has been practiced with
    public int getPracticeCounter() {
        return this.practiceCounter;
    }

    // MODIFIES: This
    // EFFECTS: Changes proficiency Level
    public void setProficiencyLevel(Proficiency proficiencyLevel) {
        this.proficiencyLevel = proficiencyLevel;
    }


    // MODIFIES: this
    // EFFECTS: increments counter and moves word if it has
    //          reached enough proficiency
    public void incrementCounter() {
        this.practiceCounter += 1;
        if (proficiencyLevel == Proficiency.Copper
                && practiceCounter == MAX_COPPER_COUNT) {
            promoteWord();
        } else if (proficiencyLevel == Proficiency.Bronze
                && practiceCounter == MAX_BRONZE_COUNT) {
            promoteWord();

        } else if (proficiencyLevel == Proficiency.Silver
                && practiceCounter == MAX_SILVER_COUNT) {
            promoteWord();

        } else if (proficiencyLevel == Proficiency.Gold
                && practiceCounter == MAX_GOLD_COUNT) {
            promoteWord();
        }
    }

    // MODIFIES: this
    // EFFECTS: Changes proficiency level to given proficiency
    // and resets the counter to 0
    public void changeProficiency(Proficiency p) {
        this.proficiencyLevel = p;
        this.practiceCounter = 0;
    }

    // REQUIRES: proficiencyLevel != PLATINUM
    // MODIFIES: this
    // EFFECTS: Changes the proficiency level of a word to
    //          a better proficiency
    public void promoteWord() {
        if (proficiencyLevel == Proficiency.Copper) {
            changeProficiency(Proficiency.Bronze);
        } else if (proficiencyLevel == Bronze) {
            changeProficiency(Proficiency.Silver);
        } else if (proficiencyLevel == Proficiency.Silver) {
            changeProficiency(Proficiency.Gold);
        } else if (proficiencyLevel == Proficiency.Gold) {
            changeProficiency(Proficiency.Platinum);
        }
    }

    // REQUIRES: proficiencyLevel != COPPER
    // MODIFIES: this
    // EFFECTS: Changes the proficiency level of a word to
    //          a worse proficiency
    public void demoteWord() {
        if (proficiencyLevel == Proficiency.Platinum) {
            changeProficiency(Proficiency.Gold);
        } else if (proficiencyLevel == Proficiency.Gold) {
            changeProficiency(Proficiency.Silver);
        } else if (proficiencyLevel == Proficiency.Silver) {
            changeProficiency(Proficiency.Bronze);
        } else if (proficiencyLevel == Proficiency.Bronze) {
            changeProficiency(Proficiency.Copper);
        }
    }

     */

    // EFFECTS: Turns a word into a json save
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("word", word);
        json.put("translation", translation);
        json.put("wordType", wordType);
        json.put("proficiencyLevel", proficiencyLevel);
        json.put("practiceCounter", practiceCounter);
        json.put("waitTime", waitTime); // NEW
        return json;
    }

    // EFFECTS: Returns true or false if the words the same (same word, translation, wordtype, )
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Word word1 = (Word) o;
        return Objects.equals(word, word1.word) && Objects.equals(translation, word1.translation)
                && wordType == word1.wordType;
    }

    // EFFECTS: Hashcode override for contain (.equals())
    @Override
    public int hashCode() {
        return Objects.hash(word, translation, wordType);
    }
}
