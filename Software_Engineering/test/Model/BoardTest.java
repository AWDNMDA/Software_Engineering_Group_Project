package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void testBoardInitialization() {
        assertEquals(20, board.getTotalSquares(), "The board should have 20 squares.");
        assertNotNull(board.getSquares(), "Squares list should not be null.");
        assertEquals(20, board.getSquares().size(), "Squares list size should match total squares.");
    }

    @Test
    void testGetSquareAtValidPosition() {
        Square square = board.getSquare(0);
        assertEquals("Go", square.getName(), "Square at position 0 should be 'Go'.");
    }

    @Test
    void testGetSquareWrappingPositiveIndex() {
        Square square = board.getSquare(21); // Wrap around to position 1
        assertEquals("Central", square.getName(), "Square at position 21 should wrap to 'Central'.");
    }

    @Test
    void testGetSquareWrappingNegativeIndex() {
        Square square = board.getSquare(-1); // Wrap negative index to the last square
        assertEquals("Tai O", square.getName(), "Negative index should wrap to the last square 'Tai O'.");
    }

    @Test
    void testGetSquareBeyondTotalSquares() {
        Square square = board.getSquare(40); // Wrap around multiple times
        assertEquals("Go", square.getName(), "Square at position 40 should wrap to 'Go'.");
    }

    @Test
    void testGetTotalSquares() {
        assertEquals(20, board.getTotalSquares(), "getTotalSquares should return the correct number of squares.");
    }

    @Test
    void testGetSquaresContent() {
        var squares = board.getSquares();
        assertEquals(20, squares.size(), "The list of squares should contain 20 elements.");
        assertEquals("Go", squares.get(0).getName(), "First square should be 'Go'.");
        assertEquals("Central", squares.get(1).getName(), "Second square should be 'Central'.");
        assertEquals("Tai O", squares.get(squares.size() - 1).getName(), "Last square should be 'Tai O'.");
    }

    @Test
    void testSquareInitialization() {
        Square goSquare = board.getSquare(0);
        assertNotNull(goSquare, "Square at position 0 should not be null.");
        assertEquals("Go", goSquare.getName(), "First square should be 'Go'.");
        assertTrue(board.getSquare(1) instanceof PropertySquare, "Second square should be a PropertySquare.");
    }

    @Test
    void testInvalidSquareAccess() {
        try {
            board.getSquare(Integer.MAX_VALUE); // This should wrap correctly
            assertTrue(true, "No exception should be thrown for a very large index.");
        } catch (Exception e) {
            fail("No exception should be thrown for large indices due to wrapping.");
        }

        try {
            board.getSquare(Integer.MIN_VALUE); // This should wrap correctly
            assertTrue(true, "No exception should be thrown for a very small index.");
        } catch (Exception e) {
            fail("No exception should be thrown for small indices due to wrapping.");
        }
    }
}
