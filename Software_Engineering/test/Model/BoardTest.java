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
    }

    @Test
    void testGetSquare() {
        Square square = board.getSquare(0);
        assertEquals("Go", square.getName(), "Square at position 0 should be 'Go'.");
    }

    @Test
    void testGetSquareWithWrapping() {
        Square square = board.getSquare(21); // Assuming 21 wraps to 1
        assertEquals("Central", square.getName(), "Square at position 21 should wrap to 'Central'.");
    }

    @Test
    void testGetSquareNegativeIndex() {
        Square square = board.getSquare(-1); // Wrap negative index
        assertEquals("Chance", square.getName(), "Negative index should wrap correctly.");
    }
}
