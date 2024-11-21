package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the BoardDesigner class.
 * This class tests the functionality of board customization, including modifying properties,
 * rearranging squares, handling invalid inputs, and validating the customization workflow.
 */
class BoardDesignerTest {
    private Board board;
    private BoardDesigner boardDesigner;
    private Scanner mockedScanner;

    /**
     * Set up method to initialize objects before each test.
     * A new Board instance and mocked Scanner are created, and the BoardDesigner is instantiated with the mocked Scanner.
     */
    @BeforeEach
    void setUp() {
        board = new Board();
        mockedScanner = mock(Scanner.class);
        boardDesigner = new BoardDesigner(mockedScanner);
    }

    /**
     * Test for the default constructor of BoardDesigner.
     * Validates that the constructor initializes the object correctly.
     */
    @Test
    void testDefaultConstructor() {
        BoardDesigner defaultBoardDesigner = new BoardDesigner();
        assertNotNull(defaultBoardDesigner, "Default constructor should initialize the BoardDesigner.");
    }

    /**
     * Test for modifying a property square at the boundary index of the board.
     * Verifies that properties (price and rent) of the square are updated correctly.
     */
    @Test
    void testModifyPropertyBoundaryIndex() {
        when(mockedScanner.nextInt()).thenReturn(1, board.getTotalSquares() - 1, 3);
        when(mockedScanner.nextLine()).thenReturn("", "", "", "700", "80");

        PropertySquare property = new PropertySquare("Tai O", 600, 25);
        board.getSquares().set(board.getTotalSquares() - 1, property);

        boardDesigner.customizeBoard(board);

        PropertySquare updatedProperty = (PropertySquare) board.getSquare(board.getTotalSquares() - 1);
        assertEquals(700, updatedProperty.getPrice(), "Price should be updated.");
        assertEquals(80, updatedProperty.getRent(), "Rent should be updated.");
    }

    /**
     * Test for attempting to modify a non-property square (e.g., "Go" square).
     * Verifies that no changes are made to the square.
     */
    @Test
    void testModifyNonPropertySquare() {
        when(mockedScanner.nextInt()).thenReturn(1, 0, 3);
        boardDesigner.customizeBoard(board);
        assertEquals("Go", board.getSquare(0).getName(), "Non-property square should remain unchanged.");
    }

    /**
     * Test for rearranging squares with the same index.
     * Ensures no changes occur when attempting to swap a square with itself.
     */
    @Test
    void testRearrangeSquaresSameIndex() {
        when(mockedScanner.nextInt()).thenReturn(2, 1, 1, 3);
        boardDesigner.customizeBoard(board);
        assertEquals("Central", board.getSquare(1).getName(), "Square should remain unchanged when rearranged with itself.");
    }

    /**
     * Test for handling invalid input during property modification.
     * Ensures the process recovers from invalid input and applies valid updates correctly.
     */
    @Test
    void testInvalidInputHandling() {
        when(mockedScanner.nextInt())
                .thenThrow(new IllegalArgumentException("Invalid input"))
                .thenReturn(1, 1, 3);
        when(mockedScanner.nextLine()).thenReturn("", "", "", "", "1000", "100");

        PropertySquare property = new PropertySquare("Central", 800, 90);
        board.getSquares().set(1, property);

        boardDesigner.customizeBoard(board);

        PropertySquare updatedProperty = (PropertySquare) board.getSquare(1);
        assertEquals(1000, updatedProperty.getPrice(), "Price should be updated after handling invalid input.");
        assertEquals(100, updatedProperty.getRent(), "Rent should be updated after handling invalid input.");
    }

    /**
     * Test for rearranging squares followed by modifying properties in a single session.
     * Validates the workflow where multiple customization actions are performed sequentially.
     */
    @Test
    void testRearrangeAndModifyProperty() {
        when(mockedScanner.nextInt())
                .thenReturn(2, 1, 2, 1, 1, 3);
        when(mockedScanner.nextLine()).thenReturn("", "", "", "", "", "", "1500", "200");

        PropertySquare property = new PropertySquare("Central", 800, 90);
        board.getSquares().set(1, property);

        boardDesigner.customizeBoard(board);
        assertEquals("Wan Chai", board.getSquare(1).getName(), "Central should swap with Wan Chai.");
        assertEquals("Central", board.getSquare(2).getName(), "Wan Chai should swap with Central.");

        PropertySquare updatedProperty = (PropertySquare) board.getSquare(2);
        assertEquals(800, updatedProperty.getPrice(), "Price should remain unchanged.");
        assertEquals(90, updatedProperty.getRent(), "Rent should remain unchanged.");
    }
}
