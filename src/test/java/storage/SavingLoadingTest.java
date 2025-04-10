package storage;

import deck.Deck;
import deck.DeckManager;
import deck.Flashcard;

import command.Command;
import command.CommandCreateFlashcard;
import command.CommandDeleteFlashcard;
import exceptions.FlashCLIArgumentException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SavingLoadingTest {
    private Deck testDeck;

    @BeforeEach
    void setUp() {
        testDeck = new Deck("TestDeck");
        DeckManager.currentDeck = testDeck;
        DeckManager.decks.put("TestDeck", testDeck);
    }

    @AfterEach
    void tearDown() {
        File file = new File("./data/decks/TestDeck.txt");
        if (file.exists()) {
            file.delete();
        }
        DeckManager.decks.clear();
        DeckManager.currentDeck = null;
    }

    @Test
    void saveAndLoadDeck_preservesFlashcardData() {
        String input = "/q What is Java? /a A programming language.";
        Command createCommand = new CommandCreateFlashcard(input);
        createCommand.executeCommand();

        try {
            Saving.saveDeck("TestDeck", testDeck);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        DeckManager.decks.clear();
        Map<String, Deck> loaded = Loading.loadAllDecks();
        Deck loadedDeck = loaded.get("TestDeck");

        assertNotNull(loadedDeck);
        assertEquals(1, loadedDeck.getFlashcards().size());

        Flashcard f = loadedDeck.getFlashcards().get(0);
        assertEquals("What is Java?", f.getQuestion());
        assertEquals("A programming language.", f.getAnswer());
    }

    @Test
    void saveDeck_afterDeletingFlashcard_doesNotIncludeItOnReload() {
        String input = "/q What is Java? /a A programming language.";
        Command createCommand = new CommandCreateFlashcard(input);
        createCommand.executeCommand();

        Command deleteCommand = new CommandDeleteFlashcard("1");
        deleteCommand.executeCommand();

        try {
            Saving.saveDeck("TestDeck", testDeck);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DeckManager.decks.clear();
        Map<String, Deck> loaded = Loading.loadAllDecks();
        Deck loadedDeck = loaded.get("TestDeck");

        assertNotNull(loadedDeck);
        assertEquals(0, loadedDeck.getFlashcards().size());
    }

    @Test
    void renameDeckFile_changesFilenameCorrectly() throws IOException {
        Command createCommand = new CommandCreateFlashcard("/q What is Java? /a A language.");
        createCommand.executeCommand();
        try {
            Saving.saveDeck("TestDeck", testDeck);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Saving.renameDeckFile("TestDeck", "RenamedDeck");

        File oldFile = new File("./data/decks/TestDeck.txt");
        File newFile = new File("./data/decks/RenamedDeck.txt");

        assertFalse(oldFile.exists());
        assertTrue(newFile.exists());

        newFile.delete();
    }

    @Test
    void deleteDeckFile_removesDeckFile() throws IOException {
        Command createCommand = new CommandCreateFlashcard("/q What is Java? /a A language.");
        createCommand.executeCommand();
        try {
            Saving.saveDeck("TestDeck", testDeck);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File file = new File("./data/decks/TestDeck.txt");
        assertTrue(file.exists());

        Saving.deleteDeckFile("TestDeck");

        assertFalse(file.exists());
    }

    @Test
    void loadDecks_whenDataFolderMissing_returnsEmptyMap() {
        File folder = new File("./data/decks");
        if (folder.exists()) {
            for (File f : folder.listFiles()) {
                f.delete();
            }
            folder.delete();
        }

        Map<String, Deck> loaded = Loading.loadAllDecks();
        assertTrue(loaded.isEmpty());
    }

    @Test
    void saveAndLoadDeck_preservesLearnedStatus() throws FlashCLIArgumentException {
        String input = "/q What is Java? /a A programming language.";
        Command createCommand = new CommandCreateFlashcard(input);
        createCommand.executeCommand();

        // Mark the flashcard as learned
        testDeck.changeIsLearned("1", true);

        try {
            Saving.saveDeck("TestDeck", testDeck);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        DeckManager.decks.clear();
        Map<String, Deck> loaded = Loading.loadAllDecks();
        Deck loadedDeck = loaded.get("TestDeck");

        assertNotNull(loadedDeck);
        assertEquals(1, loadedDeck.getFlashcards().size());

        Flashcard f = loadedDeck.getFlashcards().get(0);
        assertEquals("What is Java?", f.getQuestion());
        assertEquals("A programming language.", f.getAnswer());
        assertTrue(f.getIsLearned(), "Flashcard should be marked as learned after reloading.");
    }

    @Test
    void createDeck_withInvalidName_throwsFlashCLIArgumentException() {
        assertThrows(FlashCLIArgumentException.class, () -> {
            DeckManager.createDeck("Invalid/Name");
        });

        assertThrows(FlashCLIArgumentException.class, () -> {
            DeckManager.createDeck("Invalid\\Name");
        });
    }

    @Test
    void saveDeck_withEmptyFlashcard_doesNotPersistInvalidFlashcard() throws IOException {
        testDeck.insertFlashcard(new Flashcard(1, "", "", false));
        Saving.saveDeck("TestDeck", testDeck);

        DeckManager.decks.clear();
        Deck loadedDeck = Loading.loadAllDecks().get("TestDeck");

        assertNotNull(loadedDeck);
        assertEquals(0, loadedDeck.getFlashcards().size());
    }

    @Test
    void saveAndLoadDeck_withMultipleFlashcards_preservesAll() throws IOException, FlashCLIArgumentException {
        testDeck.createFlashcard("/q What is Java? /a A language.");
        testDeck.createFlashcard("/q What is OOP? /a A paradigm.");
        Saving.saveDeck("TestDeck", testDeck);

        DeckManager.decks.clear();
        Deck loadedDeck = Loading.loadAllDecks().get("TestDeck");

        assertNotNull(loadedDeck);
        assertEquals(2, loadedDeck.getFlashcards().size());
    }

    @Test
    void loadDeck_missingLearnedField_defaultsToUnlearned() throws IOException {
        File file = new File("./data/decks/TestDeck.txt");
        file.getParentFile().mkdirs();

        try (FileWriter fw = new FileWriter(file)) {
            fw.write("Q: Sample question\n");
            fw.write("A: Sample answer\n\n");
        }

        DeckManager.decks.clear();
        Deck loadedDeck = Loading.loadAllDecks().get("TestDeck");

        assertNotNull(loadedDeck);
        assertEquals(1, loadedDeck.getFlashcards().size());
        assertFalse(loadedDeck.getFlashcards().get(0).getIsLearned());
    }
}
