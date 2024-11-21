package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the {@link Board} class.
 * This class ensures the correct initialization and functionality of the Board,
 * including square retrieval and wrapping behavior.
 */
class BoardTest {
    private Board board;

    /**
     * Sets up a new Board instance before each test.
     */
    @BeforeEach
    void setUp() {
        board = new Board();
    }

    /**
     * Tests that the board initializes correctly with 20 squares.
     */
    @Test
    void testBoardInitialization() {
        assertEquals(20, board.getTotalSquares(), "The board should have 20 squares.");
        assertNotNull(board.getSquares(), "Squares list should not be null.");
        assertEquals(20, board.getSquares().size(), "Squares list size should match total squares.");
    }

    /**
     * Tests retrieval of the first square at position 0.
     */
    @Test
    void testGetSquareAtValidPosition() {
        Square square = board.getSquare(0);
        assertEquals("Go", square.getName(), "Square at position 0 should be 'Go'.");
    }

    /**
     * Tests square wrapping behavior for an index greater than the total squares.
     */
    @Test
    void testGetSquareWrappingPositiveIndex() {
        Square square = board.getSquare(21);
        assertEquals("Central", square.getName(), "Square at position 21 should wrap to 'Central'.");
    }

    /**
     * Tests square wrapping behavior for a negative index.
     */
    @Test
    void testGetSquareWrappingNegativeIndex() {
        Square square = board.getSquare(-1);
        assertEquals("Tai O", square.getName(), "Negative index should wrap to the last square 'Tai O'.");
    }

    /**
     * Tests square wrapping for an index far beyond the total squares.
     */
    @Test
    void testGetSquareBeyondTotalSquares() {
        Square square = board.getSquare(40);
        assertEquals("Go", square.getName(), "Square at position 40 should wrap to 'Go'.");
    }

    /**
     * Tests the getTotalSquares method to ensure it returns the correct count.
     */
    @Test
    void testGetTotalSquares() {
        assertEquals(20, board.getTotalSquares(), "getTotalSquares should return the correct number of squares.");
    }

    /**
     * Tests the content of the squares list for correctness.
     */
    @Test
    void testGetSquaresContent() {
        var squares = board.getSquares();
        assertEquals(20, squares.size(), "The list of squares should contain 20 elements.");
        assertEquals("Go", squares.get(0).getName(), "First square should be 'Go'.");
        assertEquals("Central", squares.get(1).getName(), "Second square should be 'Central'.");
        assertEquals("Tai O", squares.get(squares.size() - 1).getName(), "Last square should be 'Tai O'.");
    }

    /**
     * Tests the initialization of specific square types and their properties.
     */
    @Test
    void testSquareInitialization() {
        Square goSquare = board.getSquare(0);
        assertNotNull(goSquare, "Square at position 0 should not be null.");
        assertEquals("Go", goSquare.getName(), "First square should be 'Go'.");
        assertTrue(board.getSquare(1) instanceof PropertySquare, "Second square should be a PropertySquare.");
    }

    /**
     * Tests handling of very large and very small indices for square retrieval.
     */
    @Test
    void testInvalidSquareAccess() {
        try {
            board.getSquare(Integer.MAX_VALUE);
            assertTrue(true, "No exception should be thrown for a very large index.");
        } catch (Exception e) {
            fail("No exception should be thrown for large indices due to wrapping.");
        }

        try {
            board.getSquare(Integer.MIN_VALUE);
            assertTrue(true, "No exception should be thrown for a very small index.");
        } catch (Exception e) {
            fail("No exception should be thrown for small indices due to wrapping.");
        }
    }

    /**
     * Tests retrieval of a square from a large negative index wrapping multiple times.
     */
    @Test
    void testLargeNegativeIndexWrapping() {
        Square square = board.getSquare(-21);
        assertEquals("Tai O", square.getName(), "Square at position -21 should wrap to 'Tai O'.");
    }

    /**
     * Test case for the "In Jail/Just Visiting" square functionality.
     * Verifies the behavior when a player lands on this square, both as "just visiting" and as "in jail".
     */
    @Test
    void testInJailOrJustVisitingSquare() {
        Square inJailSquare = board.getSquare(5);
        assertEquals("In Jail/Just Visiting", inJailSquare.getName(), "Square at position 5 should be 'In Jail/Just Visiting'.");

        Player visitingPlayer = new Player("Visitor");
        visitingPlayer.setInJail(false);
        inJailSquare.landOn(visitingPlayer);

        Player jailedPlayer = new Player("Jailed Player");
        jailedPlayer.setInJail(true);
        inJailSquare.landOn(jailedPlayer);
    }
}
