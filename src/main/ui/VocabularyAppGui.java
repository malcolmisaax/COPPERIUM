package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;

import model.Proficiency;
import model.Word;
import model.WordPool;
import model.WordType;
import model.eventlogging.EventLog;
import model.eventlogging.Event;
import persistence.vocabstate.VocabStateReader;
import persistence.vocabstate.VocabStateWriter;
import persistence.session.SessionJsonReader;
import persistence.session.SessionJsonWriter;
import ui.jobjects.BuddyButton;
import ui.jobjects.JPanelBackgroundBorder;
import ui.jobjects.JPanelBackgroundFlow;
import ui.jobjects.RoundedButton;

import java.util.ArrayList;
import java.util.List;


import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;


import java.io.IOException;

import static java.awt.Font.BOLD;
import static model.Proficiency.*;

// UI for COPPERIUM Vocabulary app

public class VocabularyAppGui extends JFrame {

    private static final String JSON_STORE = "./data/GUIwordpool.json";
    private static final String SESSION_JSON_STORE = "./data/sessionGUI.json";
    private VocabStateWriter vocabStateWriter;
    private VocabStateReader vocabStateReader;
    private SessionJsonWriter sessionWriter;
    private SessionJsonReader sessionReader;

    private WordPool wordPool;

    private String color1;
    private String color2;
    private String color3;
    private String color4;
    private String color5;
    private String color6;
    private int colorScheme;

    private ImageIcon copperImage;
    private ImageIcon bronzeImage;
    private ImageIcon silverImage;
    private ImageIcon goldImage;
    private ImageIcon platinumImage;

    private ImageIcon copperPair;
    private ImageIcon bronzePair;
    private ImageIcon silverPair;
    private ImageIcon goldPair;

    private ImageIcon copperTrio;
    private ImageIcon bronzeTrio;
    private ImageIcon silverTrio;
    private ImageIcon goldTrio;

    private JLabel buddy;

    // EFFECTS: starts the program
//    public static void main(String[] args) {
//        new VocabularyAppGui().setVisible(true);
//    }

    // MODIFIES: This
    // EFFECTS: sets initial color scheme, reads wordpool and session list, creates UI window
    public VocabularyAppGui() {
        vocabStateWriter = new VocabStateWriter(JSON_STORE);
        vocabStateReader = new VocabStateReader(JSON_STORE);
        sessionWriter = new SessionJsonWriter(SESSION_JSON_STORE);
        sessionReader = new SessionJsonReader(SESSION_JSON_STORE);
        instantiateColors();

        instantiateImages();
        buddy = new JLabel();

        setSize(1200, 750);
        getContentPane().setBackground(Color.decode(color1));
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                printEventLog();
                System.exit(0);
            }
        });

        addAppHeader();

        firstSession();
    }

    // EFFECTS: Prints eventLog to the console
    public void printEventLog() {
        EventLog eventLog = EventLog.getInstance();

        for (Event event : eventLog) {
            System.out.println(event);
        }
    }

    // MODIFIES: This
    // EFFECTS: Instantiates color hex codes
    private void instantiateColors() {
        color1 = "#DA8A67";
        color2 = "#b9722d";
        color3 = "#E0E0E0";
        color4 = "#D4Af37";
        color5 = "#A0B2C6";
        color6 = "#E5E4E2";
        colorScheme = 1;
    }

    private void instantiateImages() {
        instantiateImageIcons("copper");
        instantiateImageIcons("bronze");
        instantiateImageIcons("silver");
        instantiateImageIcons("gold");
        instantiateImageIcons("platinum");

        instantiateImagePairs("copper");
        instantiateImagePairs("bronze");
        instantiateImagePairs("silver");
        instantiateImagePairs("gold");

        instantiateImageTrios("copper");
        instantiateImageTrios("bronze");
        instantiateImageTrios("silver");
        instantiateImageTrios("gold");
    }

    // MODIFIES: this
    // EFFECTS: instantiates ImageIcons
    private void instantiateImageIcons(String metal) {
        ImageIcon imageIcon = new ImageIcon("./data/" + metal +  "Cartoon.png");
        Image image = imageIcon.getImage();
        Image brick = image.getScaledInstance(50, 39,  java.awt.Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(brick);
        if (metal == "copper") {
            copperImage = imageIcon;
        } else if (metal == "bronze") {
            bronzeImage = imageIcon;
        } else if (metal == "silver") {
            silverImage = imageIcon;
        } else if (metal == "gold") {
            goldImage = imageIcon;
        } else {
            platinumImage = imageIcon;
        }
    }

    // MODIFIES: this
    // EFFECTS: instantiates ImagePairs of metals
    private void instantiateImagePairs(String metal) {
        ImageIcon imageIcon = new ImageIcon("./data/" + metal + "Pair" + ".png");
        Image image = imageIcon.getImage();
        Image brick = image.getScaledInstance(50, 39,  java.awt.Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(brick);
        if (metal == "copper") {
            copperPair = imageIcon;
        } else if (metal == "bronze") {
            bronzePair = imageIcon;
        } else if (metal == "silver") {
            silverPair = imageIcon;
        } else {
            goldPair = imageIcon;
        }
    }

    // MODIFIES: this
    // EFFECTS: instantiates ImageTrios of metals
    private void instantiateImageTrios(String metal) {
        ImageIcon imageIcon = new ImageIcon("./data/" + metal + "Trio" + ".png");
        Image image = imageIcon.getImage();
        Image brick = image.getScaledInstance(50, 39,  java.awt.Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(brick);
        if (metal == "copper") {
            copperTrio = imageIcon;
        } else if (metal == "bronze") {
            bronzeTrio = imageIcon;
        } else if (metal == "silver") {
            silverTrio = imageIcon;
        } else {
            goldTrio = imageIcon;
        }
    }

    // MODIFIES: This
    // EFFECTS: Creates word pool. Loads user's word pool if there is one to load
    private void firstSession() {
        wordPool = new WordPool();
        if (Files.exists(Paths.get(JSON_STORE))) {
            loadWordPool();
            setupInitialUIButtons();

        } else {
            handleWordSetup(0, "know very well (PLATINUM).");
        }

    }

    // EFFECTS: creates a user input text box with a prompt for the user, centered
    private void backgroundPromptTextBox(JPanel prompt, JLabel promptLabel, JTextField wordInput) {
        prompt.setLayout(new BoxLayout(prompt, BoxLayout.Y_AXIS));
        prompt.setBackground(Color.decode(color1));

        promptLabel.setFont(new Font("Impact", Font.PLAIN, 25));
        promptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        wordInput.setFont(new Font("Impact", Font.PLAIN, 35));
        wordInput.setPreferredSize(new Dimension(30, wordInput.getPreferredSize().height));

        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(Color.decode(color1));
        inputPanel.add(wordInput);

        prompt.add(promptLabel);
        prompt.add(Box.createRigidArea(new Dimension(0, 5)), Color.decode(color1));
        prompt.add(inputPanel);
        prompt.add(Box.createRigidArea(new Dimension(0, 350)), Color.decode(color1));
    }


    // EFFECTS: removes display to make room for the next, revalidates, repaints content pane
    private void removeRevalidateRepaint(JPanel jp) {
        getContentPane().remove(jp);
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    // EFFECTS: adds tro the display, revalidates, repaints content pane
    private void addRevalidateRepaint(JPanel jp) {
        getContentPane().add(jp, BorderLayout.SOUTH);
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    // EFFECTS: prompts the user to enter a word in their desired language for one of their starting words
    private void handleWordSetup(int i, String proficiencyString) {
        JPanel prompt = new JPanel();
        JLabel promptLabel = new JLabel("Please enter word number " + (i + 1)
                + " in your desired language that you " + proficiencyString);
        JTextField wordInput = new JTextField(20);
        backgroundPromptTextBox(prompt, promptLabel, wordInput);

        wordInput.addActionListener(e -> {
            String word = wordInput.getText();
            removeRevalidateRepaint(prompt);
            handleTranslationSetup(i, word);
        });

        addRevalidateRepaint(prompt);
    }

    // EFFECTS: Prompts the user to enter a translation for one of their starting words
    private void handleTranslationSetup(int i, String word) {
        JPanel prompt = new JPanel();
        JLabel promptLabel = new JLabel("Please enter the translation");
        JTextField wordInput = new JTextField(20);
        backgroundPromptTextBox(prompt, promptLabel, wordInput);

        wordInput.addActionListener(e -> {
            String translation = wordInput.getText();
            removeRevalidateRepaint(prompt);
            handleWordTypeSetup(i, word, translation);
        });

        addRevalidateRepaint(prompt);
    }

    // EFFECTS: prompts the user to choose a word type from the buttons for one of their starting words
    private void handleWordTypeSetup(int i, String word, String translation) {
        JPanelBackgroundFlow wordTypeButtons = new JPanelBackgroundFlow(color1);

        JLabel prompt = new JLabel("Please choose a word type.");
        prompt.setFont(new Font("Impact", BOLD, 25));
        wordTypeButtons.add(prompt);

        addButtonSetup(wordTypeButtons, i, "Noun",color2, word, translation);
        addButtonSetup(wordTypeButtons, i, "Verb",color3, word, translation);
        addButtonSetup(wordTypeButtons, i, "Adjective",color4, word, translation);
        addButtonSetup(wordTypeButtons, i, "Adverb",color5, word, translation);
        addButtonSetup(wordTypeButtons, i, "Phrase",color6, word, translation);

        getContentPane().add(wordTypeButtons, BorderLayout.CENTER);
    }

    // EFFECTS: Creates the object of a rounded button but given string, hex color, width and height
    private JButton createRoundButton(String s, String color, int w, int h) {
        JButton button = new RoundedButton(s, color);
        //JButton button = new JButton(s);
        button.setPreferredSize(new Dimension(w, h));
        button.setBackground(Color.decode(color));
        button.setFocusPainted(false);
        return button;
    }

    // EFFECTS: If user hasn't entered 20 words, they continue the cycle
    //          If the user has entered their necessary 20 words, they are offered to enter more words
    //          If the user has decided to add more than the necessary 20 words, they move on to choosing proficiency
    private void addButtonSetup(JPanel window, int i, String buttonString, String buttonColor,
                                String word, String translation) {
        JButton button = createRoundButton(buttonString, buttonColor, 100, 100);

        window.add(button);

        button.addActionListener(e -> {
            removeRevalidateRepaint(window);
            Proficiency proficiency = intToProficiency(i);
            WordType wordType = buttonStringToWordType(buttonString);
            Word newWord = new Word(word, translation, wordType, Platinum, 99999); // NEW ADDED 99999, and changed
            int n = i + 1;                                                     // proficiency to platinum
            if (n < 25) {
                wordPool.addWord(newWord);
                nextWordSetup(n);
            } else {
                removeRevalidateRepaint(window);
                saveWordPool();
                setupInitialUIButtons();
            }
        });
    }

    // EFFECTS: Converts the string from a button to word type and returns it
    private WordType buttonStringToWordType(String s) {
        if (s == "Noun") {
            return WordType.Noun;
        } else if (s == "Verb") {
            return WordType.Verb;
        } else if (s == "Adverb") {
            return WordType.Adverb;
        } else if (s == "Adjective") {
            return WordType.Adjective;
        } else {
            return WordType.Phrase;
        }
    }

    // EFFECTS: Converts the current position of the user in the initial word setup process to a Proficiency level of
    //         the word they are adding
    private Proficiency intToProficiency(int i) {
        if (i < 7) {
            return Bronze;
        } else if (i < 13) {
            return Silver;
        } else if (i < 17) {
            return Gold;
        } else {
            return Platinum;
        }
    }

    // EFFECTS: decides what word proficiency the user will be adding next; prompts them to add it
    private void nextWordSetup(int i) {
        handleWordSetup(i, "know very well (PLATINUM).");
    }

    // EFFECTS: Creates yes or no buttons for the user to decide if they want to add more words in the
    //          initial word pool setup
    private void promptToAddMoreWords(int i) {
        JPanelBackgroundFlow yesnoButtons = new JPanelBackgroundFlow(color1);

        JLabel prompt = new JLabel("Do you want to add another word?");
        prompt.setFont(new Font("Impact", BOLD, 30));
        yesnoButtons.add(prompt);

        addDecisionButtons(yesnoButtons, i, "Yes",color3);
        addDecisionButtons(yesnoButtons, i, "No",color4);

        getContentPane().add(yesnoButtons, BorderLayout.CENTER);
    }

    // EFFECTS: Adds a yes button or a no button of size 100 x 100 to given window with given color and given title,
    //          giving the user the chance to continue or stop adding more words during their inital word setup process
    private void addDecisionButtons(JPanel window, int i, String buttonString, String buttonColor) {
        JButton button = createRoundButton(buttonString, buttonColor, 100, 100);
        window.add(button);

        button.addActionListener(e -> {
            removeRevalidateRepaint(window);
            if (buttonString == "Yes") {
                handleWordSetup(i, "want to practice with.");
            } else {
                saveWordPool();
                setupInitialUIButtons();
            }
        });
    }

    // EFFECTS: Turns the given proficiency button into a corresponding image
    private ImageIcon proficiencyToImage(Proficiency p, int i) {
        if (p == Bronze) {
            return whichImage(i, bronzeImage, bronzePair, bronzeTrio);
        } else if (p == Silver) {
            return whichImage(i, silverImage, silverPair, silverTrio);
        } else if (p == Gold) {
            return whichImage(i, goldImage, goldPair, goldTrio);
        } else if (p == Platinum) {
            return platinumImage;
        } else {
            return whichImage(i, copperImage, copperPair, copperTrio);
        }
    }

    // EFFECTS: returns either a singe metal block, a pair, or a trio, depending on practiceCounter
    private ImageIcon whichImage(int i, ImageIcon image1, ImageIcon image2, ImageIcon image3) {
        if (i == 0) {
            return image1;
        } else if (i == 1) {
            return image2;
        } else {
            return image3;
        }
    }

    // EFFECTS: Turns the given proficiency into a string
    private String wordTypeToString(WordType wordType) {
        if (wordType == WordType.Noun) {
            return "noun";
        } else if (wordType == WordType.Adjective) {
            return "adjective";
        } else if (wordType == WordType.Adverb) {
            return "adverb";
        } else  if (wordType == WordType.Phrase) {
            return "phrase";
        } else {
            return "verb";
        }
    }

    // EFFECTS: Creates the logo header for the main window
    private void addAppHeader() {
        ImageIcon imageIcon = new ImageIcon("./data/copperiumLogo.png");
        Image image = imageIcon.getImage();
        Image letterC = image.getScaledInstance(270, 150,  java.awt.Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(letterC);

        JLabel copperium = new JLabel("", imageIcon, JLabel.CENTER);

        copperium.setHorizontalTextPosition(JLabel.RIGHT);
        copperium.setVerticalTextPosition(JLabel.CENTER);
        copperium.setFont(new Font("Brush Script", Font.PLAIN, 50));
        copperium.setForeground(Color.BLACK);

        add(copperium, BorderLayout.NORTH);
    }

    // EFFECTS: Calls the home screen menu
    private void setupInitialUIButtons() {
        JPanelBackgroundFlow startupButtons = new JPanelBackgroundFlow(color1);

        startupButtons.add(Box.createRigidArea(new Dimension(0, 500)), Color.decode(color1));
        startSessionButton(startupButtons);
        if (Files.exists(Paths.get(SESSION_JSON_STORE))) {
            loadSessionButton(startupButtons);
        }
        seeVocabButton(startupButtons);
        seeStatsButton(startupButtons);
        seeShopButton(startupButtons); //new
        addChangeThemeButton(startupButtons);

        getContentPane().add(startupButtons, BorderLayout.CENTER);
    }

    // EFFECTS: Before deleting a session, each work gets incremented
    private void incrementDeletedSessionWords(List<Word> session) {
        for (int i = 0; i < session.size(); i++) {
            incrementWordFromSession(session.get(i));
        }
        saveWordPool();
    }

    // EFFECTS: Creates a new session button and adds it to the given window. Runs session if clicked
    private void startSessionButton(JPanel... window) {
        JButton newSessionButton = createRoundButton("New Session", color2, 175, 50);

        window[0].add(newSessionButton);

        newSessionButton.addActionListener(e -> {
            for (JPanel w : window) {
                removeRevalidateRepaint(w);
            }
            if (Files.exists(Paths.get(SESSION_JSON_STORE))) {
                incrementDeletedSessionWords(loadSession());
                deleteSessionFile();
            }
            showBuddy();
            runSession();
        });
    }

    // EFFECTS: Creates a load session button and its it to the given window. Loads previously saved session
    private void loadSessionButton(JPanel... window) {

        JButton loadSessionButton = createRoundButton("Load Session", color2, 175, 50);

        window[0].add(loadSessionButton);

        loadSessionButton.addActionListener(e -> {
            showBuddy();
            for (JPanel w: window) {
                removeRevalidateRepaint(w);
            }
            if (Files.exists(Paths.get(SESSION_JSON_STORE))) {
                List<Word> session = loadSession();
                int start = sessionReader.getIntStart();
                incrementWordFromSession(session.get(start));
                practiceWithWord((start + 1), session, getTotalMoney());
            } else {
                setupInitialUIButtons();
            }
        });
    }

    // EFFECTS: Creates a button to see the user's entire vocabulary
    private void seeVocabButton(JPanel... window) {
        JButton seeVocabButton = createRoundButton("See my vocabulary", color3, 175, 50);

        window[0].add(seeVocabButton);

        seeVocabButton.addActionListener(e -> {
            for (JPanel w: window) {
                removeRevalidateRepaint(w);
            }
            hideBuddy();
            seeMyVocabulary();
        });
    }

    // EFFECTS: Creates a button to close the window that shows your session list brings you back to main screen
    private void hideSessionListButton(JPanel... window) {
        JButton hideSessionButton = createRoundButton("Hide session list", color3, 175, 50);

        window[0].add(hideSessionButton);

        hideSessionButton.addActionListener(e -> {
            for (JPanel w: window) {
                removeRevalidateRepaint(w);
            }
            showBuddy();
            setupInitialUIButtons();
        });
    }

    // EFFECTS: Creates a button to change the app's color scheme
    private void addChangeThemeButton(JPanel... window) {
        JButton changThemeButton = createRoundButton("?", color6, 50, 50);

        window[0].add(changThemeButton);

        changThemeButton.addActionListener(e -> {
            for (JPanel w: window) {
                removeRevalidateRepaint(w);
            }
            changeColorScheme();
            getContentPane().removeAll(); // NEW
            getContentPane().revalidate(); // NEW
            addAppHeader();
            getContentPane().setBackground(Color.decode(color1));
            getContentPane().repaint(); // NEW
            setupInitialUIButtons();
            showBuddy();
        });
    }

    // EFFECTS: creates a button that allows the user to see their game stats
    private void seeStatsButton(JPanel... window) {
        JButton hideSessionButton = createRoundButton("Stats", color4, 100, 50);

        window[0].add(hideSessionButton);

        hideSessionButton.addActionListener(e -> {
            for (JPanel w: window) {
                removeRevalidateRepaint(w);
            }
            showStats();
            hideBuddy();
        });
    }

    // EFFECTS: Creates a button that takes the user to the shop
    private void seeShopButton(JPanel... window) {
        JButton hideSessionButton = createRoundButton("Shop", color5, 75, 50);

        window[0].add(hideSessionButton);

        hideSessionButton.addActionListener(e -> {
            for (JPanel w: window) {
                removeRevalidateRepaint(w);
            }
            openShop();
            showBuddy();
        });
    }

    // EFFECTS: Displays the shop to the user
    private void openShop() {
        int total = getTotalMoney();

        JPanelBackgroundBorder promptPanel = new JPanelBackgroundBorder(color1);

        JLabel prompt = promptSetFontCenterWithImage(openPileImage("total"),
                "$" + addCommasToInteger(total)
                        + " COPPERCOINS. Continue practicing to unlock new buddies.", Font.PLAIN, 22);

        JPanelBackgroundFlow buddyButtons = new JPanelBackgroundFlow(color1);

        promptPanel.add(prompt, BorderLayout.NORTH);
        promptPanel.add(buddyButtons, BorderLayout.CENTER);

        JPanel centeringPanel = centeringPanelCreator(1000, 160);
        closeBuddyButton(centeringPanel, buddyButtons);
        addBuddyButton(buddyButtons, total, 80, "dog", color2);
        addBuddyButton(buddyButtons, total, 160, "ducky", color3);
        addBuddyButton(buddyButtons, total, 300, "hedgehog", color4);
        addBuddyButton(buddyButtons, total, 450, "dino", color5);
        addBuddyButton(buddyButtons, total, 650, "unicorn", color6);
        addBuddyButton(buddyButtons, total, 900, "dragon", color6);

        centeringPanel.add(promptPanel);

        closeShopButton(centeringPanel, buddyButtons);

        getContentPane().add(centeringPanel, BorderLayout.CENTER);
    }

    // EFFECTS: Removes the buddy from the display
    private void hideBuddy() {
        try {
            getContentPane().remove(buddy);
        } catch (Exception exception) {
            //
        }
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    // EFFECTS: Adds the button to the display
    private void showBuddy() {
        getContentPane().add(buddy, BorderLayout.SOUTH);
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    // EFFECTS: creates a button that allows the user to remove the buddy from the display
    private void closeBuddyButton(JPanel... window) {
        JButton closeStatsButton = createRoundButton("Hide Buddy", "#FFFF00", 90, 20);
        closeStatsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        window[1].add(closeStatsButton, BorderLayout.SOUTH);

        closeStatsButton.addActionListener(e -> {
            try {
                getContentPane().remove(buddy);
            } catch (Exception exception) {
                //
            }
            buddy = new JLabel();
            getContentPane().revalidate();
            getContentPane().repaint();
        });
    }

    // EFFECTS: Closes shop, displays main menu
    private void closeShopButton(JPanel... window) {
        JButton closeStatsButton = createRoundButton("EXIT", "#ff0000", 90, 20);
        closeStatsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        window[1].add(closeStatsButton, BorderLayout.SOUTH);

        closeStatsButton.addActionListener(e -> {
            for (JPanel w: window) {
                removeRevalidateRepaint(w);
            }
            setupInitialUIButtons();
        });
    }

    // EFFECTS: Returns buddyIcon based on given string and width and height
    private ImageIcon getBuddyIcon(String buddyString, int width, int height) {
        ImageIcon imageIcon = new ImageIcon("./data/" + buddyString + ".png");
        Image image = imageIcon.getImage();
        Image brick = image.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(brick);
        return imageIcon;
    }

    // MODIFIES: This
    // EFFECTS: instantiates the buddy field to the buddy icon with given string
    private void instantiateBuddy(String buddyString) {
        ImageIcon imageIcon = getBuddyIcon(buddyString, 125, 120);
        JLabel buddyLabel = new JLabel(imageIcon);
        buddy = buddyLabel;
    }

    // EFFECTS: Adds a buddy to the display
    private void addBuddyButton(JPanel window, int total, int unlockAt, String buddyType,
                                String buttonColor) {
        String color = buttonColor;
        String buttonString = "Unlocked at $" + unlockAt + "k";
        if (total < (unlockAt * 1000)) {
            color = "#808080";
            buttonString = "Unlock at $" + unlockAt + "k";
        }
        ImageIcon buddyIcon = getBuddyIcon(buddyType, 100, 97);
        JButton button = createBuddyButton(buddyIcon, buttonString, color, 140, 140);
        window.add(button);

        button.addActionListener(e -> {
            if (total >= (unlockAt * 1000)) {
                try {
                    getContentPane().remove(buddy);
                } catch (Exception exception) {
                    //
                }
                instantiateBuddy(buddyType);
                getContentPane().add(buddy, BorderLayout.SOUTH);
                getContentPane().revalidate();
                getContentPane().repaint();
            }
        });
    }

    // EFFECTS: creates a buddy button to be displayed
    private JButton createBuddyButton(ImageIcon buddyIcon, String s, String color, int w, int h) {
        JButton button = new BuddyButton(s, color, buddyIcon);
        button.setPreferredSize(new Dimension(w, h));
        button.setBackground(Color.decode(color));
        button.setFocusPainted(false);
        return button;
    }

    // EFFECTS: Returns total money that user has accumulated
    private int getTotalMoney() {
        int copper = getTotalProficiencyMoney(Copper, 10);
        int bronze = getTotalProficiencyMoney(Bronze, 50);
        int silver = getTotalProficiencyMoney(Silver, 200);
        int gold = getTotalProficiencyMoney(Gold, 1000);
        int plat = getTotalProficiencyMoney(Platinum, 5000);
        return copper + bronze + silver + gold + plat;
    }

    // EFFECTS: Returns the total money user has depending on given proficiency
    private int getTotalProficiencyMoney(Proficiency p, int multiplier) {
        int total = 0;
        if (p == Platinum) {
            total = -125000;
        }
        for (int i = 0; i < wordPool.size(); i++) {
            Word w = wordPool.get(i);
            if (p == Platinum && w.getProficiency() == Platinum) {
                total += multiplier;
            } else if (p == Copper && w.getProficiency() == Copper) {
                total += w.getPracticeCounter() * multiplier;
            } else if (w.getProficiency() == p) {
                total += (w.getPracticeCounter() + 1) * multiplier;
            }
        }
        return total;
    }

    // EFFECTS: creates the stats window
    private void showStats() {
        JPanelBackgroundBorder statsWindow = new JPanelBackgroundBorder(color1);

        JPanelBackgroundFlow closeButton = new JPanelBackgroundFlow(color1);

        JPanel stats = new JPanel();
        stats.setLayout(new BoxLayout(stats, BoxLayout.Y_AXIS));
        stats.setBackground(Color.decode(color1));
        Font wordFont = new Font("Monospaced", Font.PLAIN, 22);

        addAllStats(stats, wordFont);

        statsWindow.add(stats, BorderLayout.CENTER);
        statsWindow.add(closeButton, BorderLayout.SOUTH);

        JPanel centeringPanel = centeringPanelCreator(1000, 131);

        centeringPanel.add(statsWindow);

        closeStatsButton(centeringPanel, stats);

        getContentPane().add(centeringPanel, BorderLayout.CENTER);
    }

    // EFFECTS: creates a close button and adds it to the window
    private void closeStatsButton(JPanel... window) {
        JButton closeStatsButton = createRoundButton("       EXIT       ", "#ff0000", 50, 50);
        closeStatsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        window[1].add(closeStatsButton, BorderLayout.SOUTH);

        closeStatsButton.addActionListener(e -> {
            for (JPanel w: window) {
                removeRevalidateRepaint(w);
            }
            showBuddy();
            setupInitialUIButtons();
        });
    }

    // EFFECTS: adds all the statistics to one JPanel
    private void addAllStats(JPanel stats, Font wordFont) {
        ImageIcon copperPile = openPileImage("copper");
        ImageIcon bronzePile = openPileImage("bronze");
        ImageIcon silverPile = openPileImage("silver");
        ImageIcon goldPile = openPileImage("gold");
        ImageIcon platinumPile = openPileImage("platinum");
        int copperTotal = addStat(stats, copperPile, Copper, 10, wordFont);
        stats.add(Box.createRigidArea(new Dimension(200, 8)), Color.BLACK);
        int bronzeTotal = addStat(stats, bronzePile, Bronze, 50, wordFont);
        stats.add(Box.createRigidArea(new Dimension(200, 8)), Color.BLACK);
        int silverTotal = addStat(stats, silverPile, Silver, 200, wordFont);
        stats.add(Box.createRigidArea(new Dimension(200, 8)), Color.BLACK);
        int goldTotal = addStat(stats, goldPile, Gold, 1000, wordFont);
        stats.add(Box.createRigidArea(new Dimension(200, 8)), Color.BLACK);
        int platinumTotal = addStat(stats, platinumPile, Platinum, 5000, wordFont);
        stats.add(Box.createRigidArea(new Dimension(200, 20)), Color.BLACK);

        addTotalStat(copperTotal, bronzeTotal, silverTotal, goldTotal, platinumTotal, wordFont, stats);
    }

    // EFFECTS: creates an image of a pile of metal from ./data and returns it
    private ImageIcon openPileImage(String metal) {
        ImageIcon imageIcon = new ImageIcon("./data/" + metal + "Pile.png");
        Image image = imageIcon.getImage();
        Image brick = image.getScaledInstance(60, 46,  java.awt.Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(brick);
        return imageIcon;
    }

    // EFFECTS: adds a stat to the window, given an Image, a proficiency, and a multiplier
    private int addStat(JPanel stats, ImageIcon metals, Proficiency p, int multiplier,  Font wordFont) {
        int total = 0;
        if (p == Platinum) {
            total = -125000;
        }
        for (int i = 0; i < wordPool.size(); i++) {
            Word w = wordPool.get(i);
            if (p == Platinum && w.getProficiency() == Platinum) {
                total += multiplier;
            } else if (p == Copper && w.getProficiency() == Copper) {
                total += w.getPracticeCounter() * multiplier;
            } else if (w.getProficiency() == p) {
                total += (w.getPracticeCounter() + 1) * multiplier;
            }
        }
        String string1 = "$" + addCommasToInteger(multiplier) + "/" + p.toString().toUpperCase();
        String string2 = "$" + addCommasToInteger(total) + "CC";
        int dif2 = 50 - string1.length() - string2.length();
        String spaces2 = createStringOfSpaces(dif2);
        stats.add(createLabelWithFontAndImage(metals, string1 + spaces2 + string2, wordFont));
        return total;
    }

    // EFFECTS: Turns an int into a string with commas per thousand
    public String addCommasToInteger(int number) {
        return NumberFormat.getIntegerInstance().format(number);
    }

    // EFFECTS: adds the Total stat to the window
    public void addTotalStat(int copper, int bronze, int silver, int gold, int platinum, Font wordFont, JPanel stats) {
        int totalTotal = copper + bronze + silver + gold + platinum;
        String string1 = "TOTAL COPPERCOINS:";
        String string2 = "$" + addCommasToInteger(totalTotal) + "CC";
        int dif = 50 - string1.length() - string2.length();
        String spaces = createStringOfSpaces(dif);

        ImageIcon totalPile = openPileImage("total");

        stats.add(createLabelWithFontAndImage(totalPile, string1 + spaces + string2, wordFont));
    }

    // EFFECTS: Creates a JLabel with an image next to it and with the given string and font
    public JLabel createLabelWithFontAndImage(ImageIcon image, String text, Font font) {
        JLabel label = new JLabel(text, image, JLabel.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(font);
        return label;
    }

    // MODIFIES: This
    // EFFECTS: Changes the application's color scheme
    private void changeColorScheme() {
        switch (colorScheme) {
            case 1:
                colorScheme = 2;
                colorSchemeX("#f8d2f9", "#ffcae5", "#ffaed7", "#ff77bc", "#ff48a5", "#ff0081", 2);
                break;
            case 2:
                colorScheme = 3;
                colorSchemeX("#ff595e", "#ff924c", "#ffca3a", "#8ac926", "#1982c4", "#8400ff", 3);
                break;
            case 3:
                colorScheme = 4;
                colorSchemeX("#adff02", "#01befe", "#ffdd00", "#ff7d00", "#ff006d", "#8f00ff", 4);
                break;
            case 4:
                colorScheme = 5;
                colorSchemeX("#ff99c8", "#fec8c3", "#fcf6bd", "#d0f4de", "#a9def9", "#e4c1f9", 5);
                break;
            default:
                changeColorSchemePartTwo();
                break;
        }
    }

    // MODIFIES: This
    // EFFECTS: Changes the application's color scheme  (more options)
    private void changeColorSchemePartTwo() {
        switch (colorScheme) {
            case 4:
                colorScheme = 5;
                colorSchemeX("#ff99c8", "#fec8c3", "#fcf6bd", "#d0f4de", "#a9def9", "#e4c1f9", 5);
                break;
            case 5:
                colorScheme = 6;
                colorSchemeX("#00ffaa", "#ffff00", "#ff00aa", "#aaff00", "#ffaa00", "#00aaff", 6);
                break;
            case 6:
                colorScheme = 7;
                colorSchemeX("#e6f6ff", "#cdecff", "#b3e3ff", "#9adaff", "#80d0ff", "#67c7ff", 7);
                break;
            case 7:
                colorScheme = 8;
                colorSchemeX("#ffb600", "#ff9e00", "#ff8500", "#ff7900", "#ff6d00", "#ff5400", 8);
                break;
            default:
                changeColorSchemePartThree();
                break;
        }
    }

    // MODIFIES: This
    // EFFECTS: Changes the application's color scheme  (even more options)
    private void changeColorSchemePartThree() {
        switch (colorScheme) {
            case 8:
                colorScheme = 9;
                colorSchemeX("#0bfeff", "#0affdd", "#0bffb3", "#08ffa3", "#11ff7b", "#0bff50", 9);
                break;
            case 9:
                colorScheme = 10;
                colorSchemeX("#ffff00", "#00fef6", "#ff008c", "#0082fe", "#d500f9", "#42ff00", 10);
                break;
            case 10:
                colorScheme = 11;
                colorSchemeX("#18f1ec", "#48f7bc", "#8afe77", "#c5fd3a", "#defc21", "#f9fa06", 11);
                break;
            case 11:
                colorScheme = 1;
                colorSchemeX("#DA8A67", "#b9722d", "#E0E0E0", "#D4Af37", "#A0B2C6", "#E5E4E2", 1);
                break;
            default:
                changeColorScheme();
                break;
        }
    }

    // MODIFIES: This
    // EFFECTS: Changes the color scheme of the application based on give colors
    private void colorSchemeX(String c1, String c2, String c3, String c4, String c5, String c6, int csNum) {
        color1 = c1;
        color2 = c2;
        color3 = c3;
        color4 = c4;
        color5 = c5;
        color6 = c6;
        colorScheme = csNum;
    }

    // EFFECTS: Show's entire vocabulary list along with main screen buttons
    private void seeMyVocabulary() {
        JPanel vocab = createFullVocabularyPanel();

        JPanelBackgroundFlow startupButtonsAndWordList = new JPanelBackgroundFlow(color1);

        JPanelBackgroundFlow wordListPanel = new JPanelBackgroundFlow(color6);
        wordListPanel.add(vocab);

        startupButtonsAndWordList.add(Box.createRigidArea(new Dimension(3000, 220)),Color.decode(color1));
        startupButtonsAndWordList.add(Box.createRigidArea(new Dimension(5, 2)), Color.decode(color1));
        startSessionButton(startupButtonsAndWordList, wordListPanel);
        if (Files.exists(Paths.get(SESSION_JSON_STORE))) {
            loadSessionButton(startupButtonsAndWordList, wordListPanel);
        }
        hideMyVocabButton(startupButtonsAndWordList, wordListPanel);
        seeStatsButton(startupButtonsAndWordList, wordListPanel);
        seeShopButton(startupButtonsAndWordList, wordListPanel);
        addChangeThemeButton(startupButtonsAndWordList, wordListPanel);

        getContentPane().add(startupButtonsAndWordList, BorderLayout.CENTER);

        getContentPane().add(wordListPanel, BorderLayout.SOUTH);

    }

    // EFFECTS: Closes the vocabulary vocabularly list and reboots the main screen
    private void hideMyVocabButton(JPanel... window) {
        JButton hideMyVocabButton = createRoundButton("Hide my vocabulary", color4, 175, 50);

        window[0].add(hideMyVocabButton);

        hideMyVocabButton.addActionListener(e -> {
            for (JPanel w: window) {
                removeRevalidateRepaint(w);
            }
            showBuddy();
            setupInitialUIButtons();
        });
    }

    // EFFECTS: Creates a panel of JLabels of user's entire vocabulary
    private JPanel createFullVocabularyPanel() {
        JPanel vocabulary = new JPanel();
        vocabulary.setLayout(new BoxLayout(vocabulary, BoxLayout.Y_AXIS));
        vocabulary.setBackground(Color.decode(color6));
        Font wordFont = new Font("Impact", Font.PLAIN, 10);

        for (int i = 0; i < wordPool.size(); i += 15) {
            if ((wordPool.size() - i) < 15) {
                String lastLine = createLastLine(i);
                vocabulary.add(createLabelWithFont(lastLine, wordFont));
            } else {
                String regularLine = makeLineOfVocab(i);
                vocabulary.add(createLabelWithFont(regularLine, wordFont));
            }
        }
        return vocabulary;
    }

    // EFFECTS: Returns string of last word in vocabulary
    private String createLastLine(int i) {
        String lastLine = "";
        for (int n = i; n < wordPool.size(); n++) {
            lastLine = lastLine + wordPool.get(n).getWord() + ", ";
        }
        return lastLine;
    }

    // EFFECTS: Returns string of two words at position i and i + 1 in user's vocabulary
    private String makeLineOfVocab(int i) {
        Word w1 = wordPool.get(i);
        Word w2 = wordPool.get(i + 1);
        Word w3 = wordPool.get(i + 2);
        Word w4 = wordPool.get(i + 3);
        Word w5 = wordPool.get(i + 4);
        Word w6 = wordPool.get(i + 5);
        Word w7 = wordPool.get(i + 6);
        Word w8 = wordPool.get(i + 7);
        Word w9 = wordPool.get(i + 8);
        Word w10 = wordPool.get(i + 9);
        Word w11 = wordPool.get(i + 10);
        Word w12 = wordPool.get(i + 11);
        Word w13 = wordPool.get(i + 12);
        Word w14 = wordPool.get(i + 13);
        Word w15 = wordPool.get(i + 14);
        return w1.getWord() + ", " + w2.getWord() + ", " + w3.getWord()
                + ", " + w4.getWord() + ", " + w5.getWord() + ", " + w6.getWord() + ", " + w7.getWord()
                + ", " + w8.getWord() + ", " + w9.getWord() + ", " + w10.getWord() + "," + w8.getWord()
                + ", " + w9.getWord() + ", " + w10.getWord() + ", " + w11.getWord() + "," + w12.getWord()
                + ", " + w13.getWord() + ", " + w14.getWord() + ", " + w15.getWord() + ",";
    }

    // MODIFIES: This
    // EFFECTS: starts a new session, starting with adding new words
    private void runSession() {
        loadWordPool();
        addNewCopperYesOrNo(0);
    }

    // EFFECTS: Asks the user if they want to add a new word
    private void addNewCopperYesOrNo(int i) {
        JPanelBackgroundBorder promptPanel = new JPanelBackgroundBorder(color1);

        JPanelBackgroundFlow yesnoButtons = new JPanelBackgroundFlow(color1);

        JLabel prompt = promptSetFontCenter("do you want to add a new word?", Font.BOLD, 30);

        promptPanel.add(prompt, BorderLayout.NORTH);
        promptPanel.add(yesnoButtons, BorderLayout.CENTER);

        JPanel centeringPanel = centeringPanelCreator(1000, 160);
        addYesnoButtonNewWord(yesnoButtons, centeringPanel, i, "Yes",color3);
        addYesnoButtonNewWord(yesnoButtons, centeringPanel, i, "No",color4);

        centeringPanel.add(promptPanel);

        getContentPane().add(centeringPanel, BorderLayout.CENTER);
    }

    // EFFECTS: creates a JLabel with gievn string, fontType, and size, and centers it
    private JLabel promptSetFontCenter(String givenPrompt, int fontType, int size) {
        JLabel prompt = new JLabel(givenPrompt);
        prompt.setFont(new Font("Impact", fontType, size));
        prompt.setHorizontalAlignment(JLabel.CENTER);
        return prompt;
    }

    // EFFECTS: creates a JLabel with gievn string, fontType, and size, and centers it
    private JLabel promptSetFontCenterWithImage(ImageIcon image, String givenPrompt, int fontType, int size) {
        JLabel prompt = new JLabel(givenPrompt, image, JLabel.CENTER);
        prompt.setFont(new Font("Impact", fontType, size));
        prompt.setHorizontalAlignment(JLabel.CENTER);
        return prompt;

    }

    // EFFECTS: Creates a yes button or a no button for whether the user wants to add a new word
    private void addYesnoButtonNewWord(JPanel window, JPanel promptPanel, int i, String buttonString,
                                       String colorString) {
        JButton button = createRoundButton(buttonString, colorString, 100, 100);
        window.add(button);

        button.addActionListener(e -> {
            removeRevalidateRepaint(promptPanel);
            if (buttonString == "Yes") {
                addNewWord(i);
                saveWordPool();
            } else {
                wordPool.setThreeTimesAgoNumOfNewWords(wordPool.getTimeBeforeNumOfNewWords());
                wordPool.setTimeBeforeNumOfNewWords(wordPool.getLastNumOfNewWords());
                wordPool.setLastNumOfNewWords(i);
                int btw = wordPool.howManyWordsAreThere();
                howManyWords("How many words do you want to practice with today? (" + btw + "-30)");
            }
        });
    }

    // EFFECTS: Prompts user to enter their new word for the day into the input text box
    private void addNewWord(int i) {
        JPanel prompt = new JPanel();
        prompt.setBackground(Color.decode(color2));

        JLabel promptLabel = new JLabel("Please enter your word here.");
        JTextField wordInput = new JTextField(20);
        backgroundPromptTextBox(prompt, promptLabel, wordInput);

        wordInput.addActionListener(e -> {
            String word = wordInput.getText();
            removeRevalidateRepaint(prompt);
            handleTranslation(i, word);
        });

        addRevalidateRepaint(prompt);
    }

    // EFFECTS: Prompts the user to enter their new daily word's translation into the input text box
    private void handleTranslation(int i, String word) {
        JPanel prompt = new JPanel();
        JLabel promptLabel = new JLabel("Please enter the translation");
        JTextField wordInput = new JTextField(20);
        backgroundPromptTextBox(prompt, promptLabel, wordInput);


        wordInput.addActionListener(e -> {
            String translation = wordInput.getText();
            removeRevalidateRepaint(prompt);
            handleWordType(i, word, translation);
        });

        addRevalidateRepaint(prompt);
    }

    // EFFECTS: Prompts user to choose a word type for their new daily word by offering five buttons for wordtypes
    private void handleWordType(int i, String word, String translation) {
        JPanelBackgroundBorder promptPanel = new JPanelBackgroundBorder(color1);

        JLabel prompt = promptSetFontCenter("Please choose word type", Font.BOLD, 25);

        JPanelBackgroundFlow wordTypeButtons = new JPanelBackgroundFlow(color1);

        promptPanel.add(prompt, BorderLayout.NORTH);
        promptPanel.add(wordTypeButtons, BorderLayout.CENTER);

        JPanel centeringPanel = centeringPanelCreator(1000, 160);
        addWordTypeButton(wordTypeButtons, centeringPanel, i, "Noun", color2, word, translation);
        addWordTypeButton(wordTypeButtons, centeringPanel, i, "Verb", color3, word, translation);
        addWordTypeButton(wordTypeButtons, centeringPanel, i, "Adjective", color4, word, translation);
        addWordTypeButton(wordTypeButtons, centeringPanel, i, "Adverb", color5, word, translation);
        addWordTypeButton(wordTypeButtons, centeringPanel, i, "Phrase", color6, word, translation);
        centeringPanel.add(promptPanel);

        getContentPane().add(centeringPanel, BorderLayout.CENTER);
    }

    // EFFECTS: Creates a JPanel and centers it with a boxlayout on the y-axis
    private JPanel centeringPanelCreator(int width, int height) {
        JPanel centeringPanel = new JPanel();
        centeringPanel.setLayout(new BoxLayout(centeringPanel, BoxLayout.Y_AXIS));
        centeringPanel.add(Box.createRigidArea(new Dimension(width, height)), Color.decode(color1));
        centeringPanel.setBackground(Color.decode(color1));
        return centeringPanel;
    }

    // EFFECTS: Creates a button of word type for the user's new daily word
    private void addWordTypeButton(JPanel window, JPanel centeringPanel, int i, String buttonString, String buttonColor,
                                   String word, String translation) {
        JButton button = createRoundButton(buttonString, buttonColor, 100, 100);
        window.add(button);

        button.addActionListener(e -> {
            removeRevalidateRepaint(centeringPanel);
            WordType wordType = buttonStringToWordType(buttonString);
            Word newWord = new Word(word, translation, wordType, Copper, 0); // NEW ADDED 0,
            wordPool.addWord(newWord);
            int n = i + 1;
            if (n < 2 && wordPool.getLastNumOfNewWords() != 2 && wordPool.getTimeBeforeNumOfNewWords() != 2
                    && wordPool.getThreeTimesAgoNumOfNewWords() != 2) {
                addNewCopperYesOrNo(n);
            } else {
                wordPool.setThreeTimesAgoNumOfNewWords(wordPool.getTimeBeforeNumOfNewWords());
                wordPool.setTimeBeforeNumOfNewWords(wordPool.getLastNumOfNewWords());
                wordPool.setLastNumOfNewWords(n);
                int btw = wordPool.howManyWordsAreThere();
                howManyWords("How many words do you want to practice with today? (" + btw + "-30)");
            }
        });
    }

    // EFFECTS: Prompts the user to enter how many words they want to practice with that day, creates session
    //          Makes sure the user enters a number and not word
    //          Makes sure the user enters a number >= 20 and <= 30
    private void howManyWords(String s) {
        JPanel prompt = new JPanel();
        JLabel promptLabel = new JLabel(s);
        JTextField wordInput = new JTextField(20);
        backgroundPromptTextBox(prompt, promptLabel, wordInput);

        int btw = wordPool.howManyWordsAreThere();

        wordInput.addActionListener(e -> {
            removeRevalidateRepaint(prompt);
            try {
                int num = Integer.parseInt(wordInput.getText());
                if (num > wordPool.size()) {
                    howManyWords("Please enter a number between " + btw + "-" + String.valueOf(wordPool.size()) + ".");
                } else if (num < btw || num > 30) {
                    howManyWords("Please enter a number between " + btw + "-30");
                } else if (num >= btw && num < 41) {
                    List<Word> session = wordPool.getListOfSizeN(num);
                    saveSession(session, 0, session.size());
                    practiceWithWord(0, session, getTotalMoney());
                }
            } catch (NumberFormatException ex) {
                howManyWords("Please enter a number.");
            }
        });
        addRevalidateRepaint(prompt);
    }

    // EFFECTS: Shows the user their i'th word in current session of session list along with a metal brick signifying
    //          the correspond word's proficiency. They are able them to:
    //          translate the word,
    //          save their session for later,
    //          or move on to the next word
    private void practiceWithWord(int i, List<Word> session, int curTotalCoins) {
        JPanelBackgroundBorder promptPanel = new JPanelBackgroundBorder(color1);

        JPanelBackgroundFlow practiceWordButtons = new JPanelBackgroundFlow(color1);

        Word word = session.get(i);

        ImageIcon imageIcon = proficiencyToImage(session.get(i).getProficiency(), session.get(i).getPracticeCounter());

        JLabel prompt = promptSetFontCenterWithImage(imageIcon, (i + 1) + ". " + word.getWord() + " ["
                + wordTypeToString(word.getWordType()) + "]", Font.BOLD, 30);

        promptPanel.add(prompt, BorderLayout.NORTH);
        promptPanel.add(practiceWordButtons, BorderLayout.CENTER);

        JPanel centeringPanel = centeringPanelCreator(1000, 135);
        addTranslateOrMoveButton(practiceWordButtons, centeringPanel, session, i, "Translate",color4,
                word, curTotalCoins);
        if (!(i == (session.size() - 1))) {
            addTranslateOrMoveButton(practiceWordButtons, centeringPanel, session, i, "Save",color5,
                    word, curTotalCoins);
        }
        String moveOnOrFinish = "Move on";
        if ((i == (session.size() - 1))) {
            moveOnOrFinish = "Finish";
        }
        addTranslateOrMoveButton(practiceWordButtons, centeringPanel, session, i, moveOnOrFinish,color6,
                word, curTotalCoins);

        centeringPanel.add(promptPanel);

        getContentPane().add(centeringPanel, BorderLayout.CENTER);
    }

    // EFFECTS: Creates buttons for during your session to translate the word, save, or move on
    private void addTranslateOrMoveButton(JPanel window, JPanel centeringPanel, List<Word> session, int i,
                                          String buttonString, String color, Word w, int curTotalCoins) {
        JButton button = createRoundButton(buttonString, color, 116, 116);
        window.add(button);

        button.addActionListener(e -> {
            int n = i + 1;
            removeRevalidateRepaint(centeringPanel);
            if (buttonString == "Translate") {
                showTranslation(session, i, w, w.getTranslation(), curTotalCoins);
            } else if (buttonString == "Save") {
                saveWordPool();
                saveSession(session, i, session.size());
            } else {
                incrementWordFromSession(w);
                saveWordPool();
                saveSession(session, i, session.size());
                if (n < session.size()) {
                    practiceWithWord(n, session, curTotalCoins);
                } else {
                    deleteSessionFile();
                    askToSeeSessionList(session, curTotalCoins);
                }
            }
        });
    }

    // EFFECTS: Turns a sessionList word into a wordPool word so it can be incremented
    private void incrementWordFromSession(Word w) {
        for (int i = 0; i < wordPool.size(); i++) {
            Word word = wordPool.get(i);
            if (w.equals(word)) {
                word.incrementCounter();
            }
        }
    }

    // EFFECTS: Displays the translation of the word to the user and displays buttons to:
    //          show the word again,
    //          save the current session,
    //          or move on to the next word
    private void showTranslation(List<Word> session,  int i, Word w, String translation, int curTotalCoins) {
        JPanelBackgroundBorder promptPanel = new JPanelBackgroundBorder(color1);

        JPanelBackgroundFlow practiceWordButtons = new JPanelBackgroundFlow(color1);

        Word word = session.get(i);
        JLabel prompt = promptSetFontCenter("Translation: " + translation, Font.BOLD, 30);

        promptPanel.add(prompt, BorderLayout.NORTH);
        promptPanel.add(practiceWordButtons, BorderLayout.CENTER);

        JPanel centeringPanel = centeringPanelCreator(1000, 138);
        addShowWordOrMoveButton(practiceWordButtons, centeringPanel, session, i, "Show word", color3,
                word, curTotalCoins);
        if (!(i == (session.size() - 1))) {
            addShowWordOrMoveButton(practiceWordButtons, centeringPanel, session, i, "Save", color5,
                    word, curTotalCoins);
        }
        String moveOnOrFinish = "Move on";
        if ((i == (session.size() - 1))) {
            moveOnOrFinish = "Finish";
        }
        addShowWordOrMoveButton(practiceWordButtons, centeringPanel, session, i, moveOnOrFinish, color6,
                word, curTotalCoins);
        centeringPanel.add(promptPanel);

        getContentPane().add(centeringPanel, BorderLayout.CENTER);
    }

    // EFFECTS: Creates buttons for during your session to show the word, save, or move on
    private void addShowWordOrMoveButton(JPanel window, JPanel centeringPanel, List<Word> session, int i,
                                          String buttonString, String color, Word w, int curTotalCoins) {
        JButton button = createRoundButton(buttonString, color, 116, 116);
        window.add(button);

        button.addActionListener(e -> {
            int n = i + 1;
            removeRevalidateRepaint(centeringPanel);
            if (buttonString == "Show word") {
                practiceWithWord(i, session, curTotalCoins);
            } else if (buttonString == "Save") {
                saveWordPool();
                saveSession(session, i, session.size());
            } else {
                incrementWordFromSession(w);
                saveWordPool();
                saveSession(session, i, session.size());
                if (n < session.size()) {
                    practiceWithWord(n, session, curTotalCoins);
                } else {
                    deleteSessionFile();
                    askToSeeSessionList(session, curTotalCoins);
                }
            }
        });
    }

    // EFFECTS: Asks the user if they want to see the list of words they practiced with today, displays yes or
    //          no buttons
    private void askToSeeSessionList(List<Word> session, int curTotalCoins) {
        JPanelBackgroundBorder promptPanel = new JPanelBackgroundBorder(color1);

        JPanelBackgroundFlow yesnoButtons = new JPanelBackgroundFlow(color1);

        JLabel prompt = promptSetFontCenter("Do you want to see the list of words you practiced with today?",
                Font.BOLD, 20);

        promptPanel.add(prompt, BorderLayout.NORTH);
        promptPanel.add(yesnoButtons, BorderLayout.CENTER);

        JPanel centeringPanel = centeringPanelCreator(1000, 160);
        addYesnoSeeSessionButton(yesnoButtons, centeringPanel, session, "Yes",color3, curTotalCoins);
        addYesnoSeeSessionButton(yesnoButtons, centeringPanel, session, "No",color2, curTotalCoins);
        centeringPanel.add(promptPanel);

        getContentPane().add(centeringPanel, BorderLayout.CENTER);
    }

    // EFFECTS: Creates yes or no buttons and displays them on given window.
    private void addYesnoSeeSessionButton(JPanel window, JPanel centeringPanel,
                                          List<Word> session, String buttonString, String color, int curTotalCoins) {
        JButton button = createRoundButton(buttonString, color, 100, 100);
        window.add(button);

        button.addActionListener(e -> {
            removeRevalidateRepaint(centeringPanel);
            if (buttonString == "Yes") {
                hideBuddy();
                showSessionList(session, curTotalCoins);
            } else {
                setupInitialUIButtons();
            }
        });
    }

    // EFFECTS: Displays the main screen along with the words the user practiced with in the most recent session
    private void showSessionList(List<Word> session, int curTotalCoins) {
        JPanel wordList = createWordListPanel(session, curTotalCoins);

        JPanelBackgroundFlow startupButtonsAndWordList = new JPanelBackgroundFlow(color1);

        JPanelBackgroundFlow wordListPanel = new JPanelBackgroundFlow(color1);
        wordListPanel.add(wordList);

        startupButtonsAndWordList.add(Box.createRigidArea(new Dimension(3000, 220)), Color.decode(color1));
        startupButtonsAndWordList.add(Box.createRigidArea(new Dimension(5, 2)), Color.decode(color1));
        startSessionButton(startupButtonsAndWordList, wordListPanel);
        if (Files.exists(Paths.get(SESSION_JSON_STORE))) {
            loadSessionButton(startupButtonsAndWordList, wordListPanel);
        }
        hideSessionListButton(startupButtonsAndWordList, wordListPanel);
        // seeVocabButton(startupButtonsAndWordList, wordListPanel);
        seeStatsButton(startupButtonsAndWordList, wordListPanel);
        seeShopButton(startupButtonsAndWordList, wordListPanel);
        addChangeThemeButton(startupButtonsAndWordList, wordListPanel);

        getContentPane().add(startupButtonsAndWordList, BorderLayout.CENTER);
        getContentPane().add(wordListPanel, BorderLayout.SOUTH);
    }

    // EFFECTS: Creates a panel containing all the words the user practiced with in their most recent session
    private JPanel createWordListPanel(List<Word> session, int prevTotalCoins) {
        JPanel wordList = new JPanel();
        wordList.setLayout(new BoxLayout(wordList, BoxLayout.Y_AXIS));
        wordList.setBackground(Color.decode(color6));
        Font wordFont = new Font("Monospaced", Font.PLAIN, 10);
        String newCoinsString = "COPPER COINS EARNED: $" + addCommasToInteger(prevTotalCoins) + " + $"
                + addCommasToInteger((getTotalMoney() - prevTotalCoins)) + " = $" + addCommasToInteger(getTotalMoney());
        wordList.add(createLabelWithFont(newCoinsString, wordFont));

        for (int i = 0; i < session.size(); i += 2) {
            Word w = session.get(i);
            String w1String = w.getWord()
                    + "  -  " + w.getTranslation() + "  -  " + w.getWordType()  + "  -  " + w.getProficiency();
            if (i == (session.size() - 1)) {
                wordList.add(createLabelWithFont(w1String, wordFont));
            } else {
                Word w2 = session.get(i + 1);
                String w2String = w2.getWord() + "  -  " + w2.getTranslation() + "  -  " + w2.getWordType()  + "  -  "
                        + w2.getProficiency();
                int dif = 90 - w1String.length();
                String spaces = createStringOfSpaces(dif);
                wordList.add(createLabelWithFont(w1String + spaces + w2String, wordFont));
            }
        }
        return wordList;
    }

    // EFFECTS: takes in a length l, and creates l spaces in order to create propper line spacing
    private String createStringOfSpaces(int length) {
        String string = "";
        for (int i = 0; i < length; i++) {
            string += " ";
        }
        return string;
    }

    // EFFECTS: displays the main screen along with the words "session saved..."
    private void sessionSaved() {
        JPanelBackgroundBorder promptPanel = new JPanelBackgroundBorder(color1);

        JPanelBackgroundFlow startupButtons = new JPanelBackgroundFlow(color1);

        JLabel prompt = promptSetFontCenter("Session saved...", Font.ITALIC, 15);

        promptPanel.add(prompt, BorderLayout.NORTH);
        promptPanel.add(startupButtons, BorderLayout.CENTER);

        JPanel centeringPanel = centeringPanelCreator(1000, 160);
        centeringPanel.add(Box.createRigidArea(new Dimension(3000, 42)), Color.decode(color1));
        centeringPanel.add(Box.createRigidArea(new Dimension(100, 5)), Color.decode(color1));
        startSessionButton(startupButtons, centeringPanel);
        if (Files.exists(Paths.get(SESSION_JSON_STORE))) {
            loadSessionButton(startupButtons, centeringPanel);
        }
        seeVocabButton(startupButtons, centeringPanel);
        seeStatsButton(startupButtons, centeringPanel);
        seeShopButton(startupButtons, centeringPanel);
        addChangeThemeButton(startupButtons, centeringPanel);
        centeringPanel.add(promptPanel);

        getContentPane().add(centeringPanel, BorderLayout.CENTER);
    }

    // EFFECTS: loads previous session to list and returns it
    private List<Word> loadSession() {
        try {
            List<Word> session = sessionReader.read();
            deleteSessionFile();
            loadWordPool(); // FOR PHASE 2 REQUIREMENTS
            return session;
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    // EFFECTS: deletes session file in ./data
    private void deleteSessionFile() {
        try {
            Files.delete(Paths.get(SESSION_JSON_STORE));
        } catch (IOException e) {
            //
        }
    }

    // EFFECTS: creates and returns a jlabel with given string and font
    public JLabel createLabelWithFont(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label;
    }





    // EFFETCS: saves a session that is not yet finished
    private void saveSession(List<Word> session, int i, int end) {
        try {
            sessionWriter.open();
            sessionWriter.write(session, i, end);
            sessionWriter.close();
        } catch (IOException e) {
            //
        }
        saveWordPool(); // FOR PHASE 2 REQUIREMENTS
    }

    // EFFECTS: saves the wordpool to file
    private void saveWordPool() {
        try {
            vocabStateWriter.open();
            vocabStateWriter.write(wordPool);
            vocabStateWriter.close();
        } catch (FileNotFoundException e) {
            //
        }
        sessionSaved();
    }

    // MODIFIES: this
    // EFFECTS: loads wordpool from file
    private void loadWordPool() {
        try {
            wordPool = vocabStateReader.read();
        } catch (IOException e) {
            //
        }
    }

}
