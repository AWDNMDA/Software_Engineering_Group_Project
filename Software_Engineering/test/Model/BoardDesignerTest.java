package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
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
        mockedScanner = mock(Scanner.class);
        boardDesigner = new BoardDesigner(mockedScanner);
    }

    @Test
    void testModifyPropertyValidIndex() {
        // Mock user input sequence
        when(mockedScanner.nextInt())
                .thenReturn(1) // Choose "Modify Property" action
                .thenReturn(1) // Select square at index 1 (Central)
                .thenReturn(3); // Exit customization

        when(mockedScanner.nextLine())
                .thenReturn("")
                .thenReturn("")
                .thenReturn("") // Keep the name unchanged
                .thenReturn("1000") // Update price
                .thenReturn("120"); // Update rent

        // Pre-populate the board with properties
        List<Square> squares = new ArrayList<>(board.getSquares());
        PropertySquare property = new PropertySquare("Central", 800, 90);
        squares.set(1, property);
        board.setSquares(squares);

        // Run the customization process
        boardDesigner.customizeBoard(board);

        // Validate the property was updated correctly
        PropertySquare updatedProperty = (PropertySquare) board.getSquare(1);
        assertEquals("Central", updatedProperty.getName(), "The property name should remain unchanged.");
        assertEquals(1000, updatedProperty.getPrice(), "The property price should be updated.");
        assertEquals(120, updatedProperty.getRent(), "The property rent should be updated.");
    }


    @Test
    void testModifyPropertyInvalidIndex() {
        // Mock inputs for selecting the action and providing an invalid index
        when(mockedScanner.nextInt())
                .thenReturn(1) // Choose "Modify Property" action
                .thenReturn(25) // Invalid square index
                .thenReturn(3); // Exit customization

        when(mockedScanner.nextLine()).thenReturn(""); // Handle empty calls

        // Run the customization process
        boardDesigner.customizeBoard(board);

        // Validate no changes occurred to properties
        PropertySquare square = (PropertySquare) board.getSquare(1);
        assertEquals("Central", square.getName(), "Property at index 1 should remain unchanged.");
        assertEquals(800, square.getPrice(), "Price should remain unchanged.");
        assertEquals(90, square.getRent(), "Rent should remain unchanged.");
    }

    @Test
    void testRearrangeSquaresValidIndices() {
        // Mock inputs for valid rearrangement and exit
        when(mockedScanner.nextInt())
                .thenReturn(2) // Rearrange option
                .thenReturn(1, 2) // Indices for swapping Central and Wan Chai
                .thenReturn(3); // Exit customization

        // Run the customization process
        boardDesigner.customizeBoard(board);

        // Validate the swap
        assertEquals("Wan Chai", board.getSquare(1).getName(), "Central should swap with Wan Chai.");
        assertEquals("Central", board.getSquare(2).getName(), "Wan Chai should swap with Central.");
    }

    @Test
    void testRearrangeSquaresInvalidIndices() {
        // Mock invalid input, then valid input
        when(mockedScanner.nextInt())
                .thenReturn(2) // Choose rearrange option
                .thenReturn(1) // First valid index
                .thenReturn(25) // Invalid second index
                .thenReturn(3)
                .thenReturn(3);

        // Run the customization process
        boardDesigner.customizeBoard(board);
        // Validate no changes occurred due to invalid input
        assertEquals("Income Tax", board.getSquare(1).getName(), "Square 1 should remain unchanged.");
        assertEquals("Wan Chai", board.getSquare(2).getName(), "Square 2 should remain unchanged.");
    }


    @Test
    void testExitCustomization() {
        when(mockedScanner.nextInt()).thenReturn(3); // Exit customization immediately

        boardDesigner.customizeBoard(board);

        // Ensure the board remains unchanged
        assertEquals("Central", board.getSquare(1).getName(), "Board should remain unchanged after exit.");
    }

    @Test
    void testModifyPropertyNoChanges() {
        // Mock sequence for valid property modification with no actual changes
        when(mockedScanner.nextInt())
                .thenReturn(1) // Choose "Modify Property" action
                .thenReturn(1) // Select square at index 1 (Central)
                .thenReturn(3); // Exit customization

        when(mockedScanner.nextLine())
                .thenReturn("") // Keep the name unchanged
                .thenReturn("") // Keep the price unchanged
                .thenReturn(""); // Keep the rent unchanged

        // Run the customization process
        boardDesigner.customizeBoard(board, mockedScanner);

        // Validate that no changes occurred
        PropertySquare square = (PropertySquare) board.getSquare(1);
        assertEquals("Central", square.getName(), "The property name should remain unchanged.");
        assertEquals(800, square.getPrice(), "The property price should remain unchanged.");
        assertEquals(90, square.getRent(), "The property rent should remain unchanged.");
    }

}
