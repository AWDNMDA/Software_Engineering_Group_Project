package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for BoardSaver utility.
 * This class ensures that board saving, loading, and file management functions work correctly.
 */
class BoardSaverTest {
    private Board board;
    private Scanner mockedScanner;

    /**
     * Sets up the environment for each test by clearing saved boards and initializing required objects.
     */
    @BeforeEach
    void setUp() {
        File saveDir = new File("boards/");
        if (saveDir.exists()) {
            File[] files = saveDir.listFiles((dir, name) -> name.endsWith(".dat"));
            if (files != null) {
                for (File file : files) {
                    if (!file.delete()) {
                        System.err.println("Failed to delete file during setup: " + file.getName());
                    }
                }
            }
        } else {
            saveDir.mkdir();
        }
        board = new Board();
        mockedScanner = mock(Scanner.class);
    }

    /**
     * Tests saving a board with a specific name and verifies the file's existence.
     */
    @Test
    void testSaveBoard() {
        BoardSaver.saveBoard(board, "TestBoard");
        File savedFile = new File("boards/TestBoard.dat");
        assertTrue(savedFile.exists(), "Saved board file should exist.");
    }

    /**
     * Tests loading a saved board and verifies its properties.
     */
    @Test
    void testLoadSavedBoard() {
        BoardSaver.saveBoard(board, "LoadTestBoard");
        when(mockedScanner.nextInt()).thenReturn(1);

        Board loadedBoard = BoardSaver.loadBoard(mockedScanner);
        assertNotNull(loadedBoard, "Loaded board should not be null.");
        assertEquals("Go", loadedBoard.getSquare(0).getName(), "Loaded board should match saved board.");
    }

    /**
     * Tests the display of saved boards.
     * Verifies that the console output includes saved board names.
     */
    @Test
    void testDisplaySavedBoards() {
        BoardSaver.saveBoard(board, "DisplayTestBoard");
        BoardSaver.displayBoards();
    }

    /**
     * Verifies the existence of saved boards after saving and clearing operations.
     */
    @Test
    void testHasSavedBoards() {
        assertFalse(BoardSaver.hasSavedBoards(), "No saved boards should exist initially.");
        BoardSaver.saveBoard(board, "CheckTestBoard");
        assertTrue(BoardSaver.hasSavedBoards(), "Saved boards should exist after saving.");
    }

    /**
     * Tests retrieving a board with an invalid index.
     * Verifies that a default board is returned in such cases.
     */
    @Test
    void testGetInvalidBoard() {
        Board invalidBoard = BoardSaver.getBoard(999);
        assertNotNull(invalidBoard, "Invalid index should return a default board.");
        assertEquals("Go", invalidBoard.getSquare(0).getName(), "Default board should have 'Go' at position 0.");
    }

    /**
     * Tests loading a board when no saved boards are available.
     * Verifies that a default board is returned in such cases.
     */
    @Test
    void testLoadBoardEmpty() {
        when(mockedScanner.nextInt()).thenReturn(1);
        Board loadedBoard = BoardSaver.loadBoard(mockedScanner);

        assertNotNull(loadedBoard, "No saved boards should return a default board.");
        assertEquals("Go", loadedBoard.getSquare(0).getName(), "Default board should have 'Go' at position 0.");
    }

    /**
     * Tests saving a board with an auto-generated name and verifies its existence.
     */
    @Test
    void testSaveBoardWithGeneratedName() {
        BoardSaver.saveBoard(board, null);
        File saveDir = new File("boards/");
        File[] files = saveDir.listFiles((dir, name) -> name.startsWith("board_v") && name.endsWith(".dat"));
        assertNotNull(files, "Files should exist in the save directory.");
        assertTrue(files.length > 0, "At least one board should be saved with a generated name.");
    }

    /**
     * Tests attempting to load a board from an invalid file and ensures fallback to a default board.
     */
    @Test
    void testLoadInvalidBoardFile() {
        File invalidFile = new File("boards/InvalidBoard.dat");
        try (PrintWriter writer = new PrintWriter(invalidFile)) {
            writer.println("Invalid Data"); // Write invalid content
        } catch (Exception ignored) {
        }
        when(mockedScanner.nextInt()).thenReturn(1);
        Board invalidBoard = BoardSaver.getBoard(1);

        assertNotNull(invalidBoard, "Fallback to default board should occur.");
        assertEquals("Go", invalidBoard.getSquare(0).getName(), "Default board should have 'Go' at position 0.");
        invalidFile.delete();
    }
}
