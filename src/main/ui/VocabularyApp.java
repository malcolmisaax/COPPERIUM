/*
package ui;

import ui.exceptions.SaveException;
import model.Proficiency;
import model.Word;
import model.WordPool;
import model.WordType;
import persistence.vocabstate.VocabStateReader;
import persistence.vocabstate.VocabStateWriter;
import persistence.session.SessionJsonReader;
import persistence.session.SessionJsonWriter;

import java.nio.file.Files;
import java.nio.file.Paths;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static model.Proficiency.*;

// an app that allows you to practice your own vocabulary in your own language

public class VocabularyApp {

    private static final String JSON_STORE = "./data/wordpool.json";
    private static final String SESSION_JSON_STORE = "./data/session.json";
    private WordPool wordPool;
    private Scanner input = new Scanner(System.in);
    private VocabStateWriter vocabStateWriter;
    private VocabStateReader vocabStateReader;
    private SessionJsonWriter sessionWriter;
    private SessionJsonReader sessionReader;


    // EFFECTS: runs the Vocabulary App
    public VocabularyApp() {
        vocabStateWriter = new VocabStateWriter(JSON_STORE);
        vocabStateReader = new VocabStateReader(JSON_STORE);
        sessionWriter = new SessionJsonWriter(SESSION_JSON_STORE);
        sessionReader = new SessionJsonReader(SESSION_JSON_STORE);
        runVocabularyApp();
    }

    // EFFECTS: initialize and run the session
    //          if there is an unfinished saved session, give option
    //          to load it
    private void runVocabularyApp() {
        initialize();
        if (Files.exists(Paths.get(SESSION_JSON_STORE))) {
            loadPreviousSession();
        } else {
            runSession();
        }
    }

    // EFFECTS: runs the main part of the program
    private void runSession() {
        addUpToThreeNewWords();
        List<Word> sessionList = createListOfWords();
        practiceWithWords(sessionList);
        askToSeeSessionList(sessionList);
        askForAnotherSession();
    }

    // MODIFIES: this
    // EFFECTS: decide whether to start with your own words, or a preset list
    private void initialize() {
        wordPool = new WordPool();
        if (Files.exists(Paths.get(JSON_STORE))) {
            loadWordPool();
        } else {
            addOwnWords();
            addMoreWordsInit();
        }
    }

    // EFFECTS: allows the user to add more than the mandatory
    //          20 words they must add
    private void addMoreWordsInit() {
        boolean validInput = false;
        while (!validInput) {
            System.out.println("Do you want to add another word?");
            System.out.println("Please type 'y' for yes and 'n' for no");
            String command = input.nextLine();
            if (command.equals("y")) {
                addOwnWord();
            } else if (command.equals("n")) {
                validInput = true;
            }  else {
                System.out.println("Please enter a valid input");
            }
        }
    }

    // EFFECTS: Allows the user to decide if they want to load a previously
    //          unfinished session
    private void loadPreviousSession() {
        System.out.println("Do you want to load a previously unfinished session?");
        System.out.println("Please type 'y' for yes and 'n' for no.");
        boolean validInput = false;
        while (!validInput) {
            String command = input.nextLine();
            if (command.equals("y")) {
                continuePreviousSession();
                validInput = true;
            } else if (command.equals("n")) {
                runSession();
                validInput = true;
            }  else {
                System.out.println("Please enter a valid input");
            }
        }
    }

    // Loads and runs a previously unfinished session
    private void continuePreviousSession() {
        List<Word> session = loadSession();
        int start = sessionReader.getIntStart();
        int end = sessionReader.getIntEnd();
        practiceWithSessionWords(session, start, end);
        askToSeeSessionList(session);
        askForAnotherSession();
    }


    // MODIFIES: This
    // EFFECTS: Add your own words one by one (7 Bronze, 6 Silver, 4 Gold, 3 Platinum)
    private void addOwnWords() {
        for (int i = 0; i < 7; i++) {
            addOwnWord(i, "don't know so well (BRONZE).", Bronze);
        }
        for (int i = 7; i < 13; i++) {
            addOwnWord(i, "know a little well (SILVER).", Silver);
        }
        for (int i = 13; i < 17; i++) {
            addOwnWord(i, "know quite well (GOLD).", Gold);
        }
        for (int i = 17; i < 20; i++) {
            addOwnWord(i, "know very well (PLATINUM).", Platinum);
        }
    }


    // MODIFIES: This
    // EFFECTS: The user can create their own word, and they can choose
    //          their proficiency
    private void addOwnWord() {
        System.out.println("Please enter a word in your desired language.");
        String word = input.nextLine();
        System.out.println("Please enter the english translation.");
        String translation = input.nextLine();
        displayWordType();
        String commandWordType = input.nextLine();
        WordType wordType = processWordType(commandWordType);
        displayProficiency();
        String commandProficiency = input.nextLine();
        Proficiency p = processProficiency(commandProficiency);
        Word newWord = new Word(word, translation, wordType, p);
        wordPool.addWord(newWord);
    }

    // MODIFIES: This
    // EFFECTS: takes input for your word, translation,
    //          wordtype, given proficiency
    //          Asks the user to input a word based on how well
    //          they know it (proficiencyString)
    private void addOwnWord(int i, String proficiencyString, Proficiency p) {
        System.out.println("Please enter word number " + (i + 1)
                + " in your desired language that you " + proficiencyString);
        String word = input.nextLine();
        System.out.println("Please enter the english translation.");
        String translation = input.nextLine();
        displayWordType();
        String commandWordType = input.nextLine();
        WordType wordType = processWordType(commandWordType);
        Word newWord = new Word(word, translation, wordType, p);
        wordPool.addWord(newWord);
    }


    // EFFECTS: Displays wordType options
    private void displayWordType() {
        System.out.println("Enter the word type (in lowercase):");
        System.out.println("\t n  - Noun");
        System.out.println("\t v  - Verb");
        System.out.println("\t aj - Adjective");
        System.out.println("\t av - Adverb");
        System.out.println("\t p  - Phrase");
    }

    // EFFECTS: Decides the wordtype
    private WordType processWordType(String command) {
        WordType wordType = null;
        boolean validInput = false;
        while (!validInput) {
            if (command.equals("v")) {
                wordType = WordType.Verb;
            } else if (command.equals("n")) {
                wordType = WordType.Noun;
            } else if (command.equals("av")) {
                wordType = WordType.Adverb;
            } else if (command.equals("aj")) {
                wordType = WordType.Adjective;
            } else if (command.equals("p")) {
                wordType = WordType.Phrase;
            } else {
                System.out.println("Please enter a valid input");
                command = input.nextLine();
                continue;
            }
            validInput = true;
        }
        return wordType;
    }


    // EFFECTS: Displays proficiency options to user
    private void displayProficiency() {
        System.out.println("Type in your proficiency level (in lower case)");
        System.out.println("based on how well you know this word.");
        System.out.println("From least known to most known...");
        System.out.println("Bronze -> Silver -> Gold -> Platinum:");
        System.out.println("\t b - Bronze");
        System.out.println("\t s - Silver");
        System.out.println("\t g - Gold");
        System.out.println("\t p - Platinum");
    }


    // EFFECTS: Decides proficiency level
    private Proficiency processProficiency(String command) {
        Proficiency p = null;
        boolean validInput = false;
        while (!validInput) {
            if (command.equals("b")) {
                p = Bronze;
            } else if (command.equals("s")) {
                p = Silver;
            } else if (command.equals("g")) {
                p = Gold;
            } else if (command.equals("p")) {
                p = Platinum;
            } else {
                System.out.println("Please enter a valid input");
                command = input.nextLine();
                continue;
            }
            validInput = true;
        }
        return p;
    }


    // MODIFIES: This
    // EFFECTS: Gives the user the chance to add up to 3 new words to WordPool
    private void addUpToThreeNewWords() {
        System.out.println("To ensure maximum language-learning, please");
        System.out.println("enter a new word that you want to start");
        System.out.println("learning with.");
        addNewCopperWord();
        System.out.println("Do you want to add another new word?");
        System.out.println("Please type 'y' for yes and 'n' for no.");
        String commandYesOrNo = input.nextLine();
        boolean response = addMoreWordsOption(commandYesOrNo);
        if (response) {
            System.out.println("Do you want to add another new word?");
            System.out.println("Please type 'y' for yes and 'n' for no.");
            String commandYesOrNo2 = input.nextLine();
            boolean response2 = addMoreWordsOption(commandYesOrNo2);
        }

    }

    // MODIFIES: This
    // EFFECTS: Adds a new word to WordPool class
    private void addNewCopperWord() {
        System.out.println("Please enter your chosen word in your desired");
        System.out.println("language.");
        String word = input.nextLine();
        System.out.println("Please enter the english translation.");
        String translation = input.nextLine();
        displayWordType();
        String commandWordType = input.nextLine();
        WordType wordType = processWordType(commandWordType);
        Word newWord = new Word(word, translation, wordType, Copper);
        wordPool.addWord(newWord);
        saveWordPool();
    }

    // MODIFIES: This
    // EFFECTS: Gives user the option to add more new words
    private boolean addMoreWordsOption(String command) {
        boolean validInput = false;
        boolean response = false;
        while (!validInput) {
            if (command.equals("y")) {
                response = true;
                validInput = true;
            } else if (command.equals("n")) {
                validInput = true;
            } else {
                System.out.println("Please enter a valid input");
                command = input.nextLine();
            }
        }
        if (response) {
            addNewCopperWord();
        }
        return response;
    }

    // MODIFIES: This
    // EFFECTS: Creates the list of words the user will be practicing with
    private List<Word> createListOfWords() {
        System.out.println("How many words do you want to practice with today?");
        System.out.println("Please enter a number (between 15-30)");
        String command = input.nextLine();
        int numWords = decideHowManyWords(command);
        return wordPool.getListOfSizeN(numWords);
    }

    // EFFECTS: allows the user to decide how many words they want to
    //          practice with
    private int decideHowManyWords(String command) {
        boolean validInput = false;
        int numWords = 0;
        while (!validInput) {
            if (command.matches("\\d+")) {
                numWords = Integer.parseInt(command);
                if (numWords >= 15 && numWords <= 30) {
                    validInput = true;
                } else {
                    System.out.println("Between 15-30, please.");
                    command = input.nextLine();
                }
            } else {
                System.out.println("Please enter a number.");
                command = input.nextLine();
            }
        }
        return numWords;
    }

    // EFFECTS: prints words for user to practice with
    private void practiceWithWords(List<Word> sessionList) {
        try {
            for (int i = 0; i < sessionList.size(); i++) {
                Word w = sessionList.get(i);
                w.incrementCounter();
                saveWordPool();
                System.out.printf((i + 1) + "." + w.getWord(),
                        w.getWordType().name());
                System.out.println("\n Press 't' for translation,"
                        + "'enter' when done practicing");
                String command = input.nextLine();
                translateOrMoveOn(command, w, sessionList, i, sessionList.size());
            }
        } catch (SaveException e) {
            System.out.println(e.getMessage());
        }
    }

    private void practiceWithSessionWords(List<Word> sessionList, int start, int end) {
        try {
            for (int i = 0; i < sessionList.size(); i++) { // changed i=start to , i<end to sessionlist.size()
                Word w = sessionList.get(i);
                w.incrementCounter();
                saveWordPool();
                System.out.printf((i + 2 + start) + "." + w.getWord(),
                        w.getWordType().name());
                System.out.println("\n Press 't' for translation,"
                        + "'enter' when done practicing");
                String command = input.nextLine();
                translateOrMoveOn(command, w, sessionList, i, sessionList.size());
            }
        } catch (SaveException e) {
            System.out.println(e.getMessage());
        }
    }

    // EFFECTS: Gives the user the option to get the translation

    private void translateOrMoveOn(String command, Word w, List<Word> session, int i, int end) throws SaveException {
        boolean validInput = false;
        while (!validInput) {
            if (command.equals("t")) {
                System.out.println(w.getTranslation());
                validInput = true;
            } else if (command.equals("s")) {
                saveSession(session, i, end);
                throw new SaveException("Session saved... exiting.....");
                // END
            } else if (command.isEmpty()) {
                validInput = true;
            } else {
                System.out.println("Please enter a valid input");
                command = input.nextLine();
            }
        }
    }


    // EFFECTS: Gives user the chance to see the words they practiced with
    private void askToSeeSessionList(List<Word> sessionList) {
        System.out.println("Do you want to see the entire list of words");
        System.out.println("you practiced with today?");
        System.out.println("Please type 'y' for yes and 'n' for no.");
        String command = input.nextLine();
        boolean validInput = false;
        while (!validInput) {
            if (command.equals("y")) {
                validInput = true;
                showList(sessionList);
            } else if (command.equals("n")) {
                validInput = true;
            } else {
                System.out.println("Please enter a valid input");
                command = input.nextLine();
            }
        }
    }

    // EFFECTS: Prints list of words practiced with translation
    //          WordType, and Proficiency
    private void showList(List<Word> sessionList) {
        for (int i = 0; i < sessionList.size(); i++) {
            Word w = sessionList.get(i);
            System.out.println(w.getWord()
                    + "  -  " + w.getTranslation()
                    + "  -  " + w.getWordType()
                    + "  -  " + w.getProficiency());
        }
    }

    // EFFECTS: Gives the user the chance to run another session
    private void askForAnotherSession() {
        System.out.println("Do you want to do another session?");
        System.out.println("Please type 'y' for yes and 'n' for no.");
        String command = input.nextLine();
        boolean validInput = false;
        while (!validInput) {
            if (command.equals("y")) {
                validInput = true;
                runSession();
            } else if (command.equals("n")) {
                validInput = true;
            } else {
                System.out.println("Please enter a valid input");
                command = input.nextLine();
            }
        }
    }

    // EFFECTS: saves the wordpool to file
    private void saveWordPool() {
        try {
            vocabStateWriter.open();
            vocabStateWriter.write(wordPool);
            vocabStateWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads wordpool from file
    private void loadWordPool() {
        try {
            wordPool = vocabStateReader.read();
            System.out.println("Loaded from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFETCS: saves a session that is not yet finished
    private void saveSession(List<Word> session, int i, int end) {
        try {
            List<Word> previousSession = session.subList(i + 1, end);
            sessionWriter.open();
            sessionWriter.write(previousSession, i, end);
            sessionWriter.close();
        } catch (IOException e) {
            System.out.println("Unable to write to file: " + SESSION_JSON_STORE);
        }
        saveWordPool(); // FOR PHASE 2 REQUIREMENTS
    }

    // MOFIFIES: This
    // EFFECTS: Loads the session list of a previously unfinished session
    private List<Word> loadSession() {
        try {
            List<Word> session = sessionReader.read();
            System.out.println("Loaded from " + SESSION_JSON_STORE);
            deleteSessionFile();
            loadWordPool(); // FOR PHASE 2 REQUIREMENTS
            return session;
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + SESSION_JSON_STORE);
            List<Word> emptySession = new ArrayList<>();
            return emptySession;
        }
    }

    // MODIFIES: This
    // EFFECTS: Deletes the file that holds a previously unfinished session
    private void deleteSessionFile() {
        try {
            Files.delete(Paths.get(SESSION_JSON_STORE));
        } catch (IOException e) {
            System.out.println("Unable to delete session file: " + SESSION_JSON_STORE);
        }
    }
}


 */