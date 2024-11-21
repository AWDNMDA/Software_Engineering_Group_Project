package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BoardDesignerTest {
    private Board board;
    private BoardDesigner boardDesigner;
    private Scanner mockedScanner;

    @BeforeEach
    void setUp() {
        board = new Board();
        boardDesigner = new BoardDesigner();
        mockedScanner = mock(Scanner.class);
    }

    @Test
    void testModifyPropertyValidIndex() {
        when(mockedScanner.nextInt()).thenReturn(1); // Modify square at index 1 (Central)
        when(mockedScanner.nextLine()).thenReturn(""); // Mock empty new name
        when(mockedScanner.nextLine()).thenReturn("1000", "120"); // New price, rent

        boardDesigner.customizeBoard(board, mockedScanner);

        PropertySquare square = (PropertySquare) board.getSquare(1);
        assertEquals("Central", square.getName(), "Name should remain unchanged.");
        assertEquals(1000, square.getPrice(), "Price should be updated.");
        assertEquals(120, square.getRent(), "Rent should be updated.");
    }

    @Test
    void testModifyPropertyInvalidIndex() {
        when(mockedScanner.nextInt()).thenReturn(25); // Out of range
        boardDesigner.customizeBoard(board, mockedScanner);

        assertEquals("Central", board.getSquare(1).getName(), "Property at index 1 should remain unchanged.");
    }

    @Test
    void testRearrangeSquaresValidIndices() {
        when(mockedScanner.nextInt()).thenReturn(1, 2); // Swap Central and Wan Chai

        boardDesigner.customizeBoard(board, mockedScanner);

        assertEquals("Wan Chai", board.getSquare(1).getName(), "Central should swap with Wan Chai.");
        assertEquals("Central", board.getSquare(2).getName(), "Wan Chai should swap with Central.");
    }

    @Test
    void testRearrangeSquaresInvalidIndices() {
        when(mockedScanner.nextInt()).thenReturn(1, 25); // Invalid second index

        boardDesigner.customizeBoard(board, mockedScanner);

        assertEquals("Central", board.getSquare(1).getName(), "Properties should remain unchanged.");
        assertEquals("Wan Chai", board.getSquare(2).getName(), "Properties should remain unchanged.");
    }

    @Test
    void testExitCustomization() {
        when(mockedScanner.nextInt()).thenReturn(3); // Exit customization

        boardDesigner.customizeBoard(board, mockedScanner);

        // No further action; just ensuring no errors
        assertEquals("Central", board.getSquare(1).getName(), "Board should remain unchanged after exit.");
    }
}
