package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BoardSaverTest {
    private Board board;
    private Scanner mockedScanner;

    @BeforeEach
    void setUp() {
        BoardSaver.clearSavedBoards();
        board = new Board();
        mockedScanner = mock(Scanner.class);

        // Ensure the save directory exists
        File saveDir = new File("boards/");
        if (!saveDir.exists()) saveDir.mkdir();
    }

    @Test
    void testSaveBoard() {
        BoardSaver.saveBoard(board, "TestBoard");

        File savedFile = new File("boards/TestBoard.dat");
        assertTrue(savedFile.exists(), "Saved board file should exist.");
    }

    @Test
    void testLoadSavedBoard() {
        BoardSaver.saveBoard(board, "LoadTestBoard");
        when(mockedScanner.nextInt()).thenReturn(1);

        Board loadedBoard = BoardSaver.loadBoard(mockedScanner);

        assertNotNull(loadedBoard, "Loaded board should not be null.");
        assertEquals("Go", loadedBoard.getSquare(0).getName(), "Loaded board should match saved board.");
    }

    @Test
    void testDisplaySavedBoards() {
        BoardSaver.saveBoard(board, "DisplayTestBoard");
        // Redirect output to verify console message
        BoardSaver.displayBoards();

        // No direct assertions, manually verify "DisplayTestBoard" is shown
    }

    @Test
    void testHasSavedBoards() {
        assertFalse(BoardSaver.hasSavedBoards(), "No saved boards should exist initially.");
        BoardSaver.saveBoard(board, "CheckTestBoard");
        assertTrue(BoardSaver.hasSavedBoards(), "Saved boards should exist after saving.");
    }

    @Test
    void testGetInvalidBoard() {
        Board invalidBoard = BoardSaver.getBoard(999); // Invalid index
        assertNotNull(invalidBoard, "Invalid index should return a default board.");
        assertEquals("Go", invalidBoard.getSquare(0).getName(), "Default board should have 'Go' at position 0.");
    }

    @Test
    void testLoadBoardEmpty() {
        when(mockedScanner.nextInt()).thenReturn(1);
        Board loadedBoard = BoardSaver.loadBoard(mockedScanner);

        assertNotNull(loadedBoard, "No saved boards should return a default board.");
        assertEquals("Go", loadedBoard.getSquare(0).getName(), "Default board should have 'Go' at position 0.");
    }
}

