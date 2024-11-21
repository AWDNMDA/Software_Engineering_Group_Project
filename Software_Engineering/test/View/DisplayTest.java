package View;

import Model.Board;
import Model.Player;
import Model.PropertySquare;
import Model.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the `Display` class to validate visual output logic.
 */
class DisplayTest {
    private Display display;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    /**
     * Sets up the Display instance and redirects system output for capturing test results.
     */
    @BeforeEach
    void setUp() {
        display = new Display();
        System.setOut(new PrintStream(outputStream));
    }

    /**
     * Tests that the welcome message is displayed correctly.
     */
    @Test
    void testDisplayWelcomeMessage() {
        display.displayWelcomeMessage();
        String output = outputStream.toString();
        assertTrue(output.contains("Welcome to Monopoly Game"), "Should display the welcome message.");
    }

    /**
     * Tests the player turn display for a specific round.
     */
    @Test
    void testDisplayPlayerTurn() {
        Player player = new Player("Ilyas");
        player.setMoney(1500);
        player.setPosition(0);

        display.displayPlayerTurn(1, player);

        String output = outputStream.toString();
        assertTrue(output.contains("Round 1"), "Should display the correct round number.");
        assertTrue(output.contains("Ilyas"), "Should display the player's name.");
        assertTrue(output.contains("HKD 1500"), "Should display the player's money.");
        assertTrue(output.contains("Position: 0"), "Should display the player's position.");
    }

    /**
     * Tests the display of the game status for all players.
     */
    @Test
    void testDisplayGameStatus() {
        Player player1 = new Player("Ilyas");
        player1.setMoney(1500);
        player1.setPosition(0);

        Player player2 = new Player("Brian");
        player2.setMoney(1200);
        player2.setPosition(2);

        List<Player> players = List.of(player1, player2);

        display.displayGameStatus(players);

        String output = outputStream.toString();
        assertTrue(output.contains("Ilyas"), "Should display player 1's name.");
        assertTrue(output.contains("Brian"), "Should display player 2's name.");
        assertTrue(output.contains("HKD 1500"), "Should display player 1's money.");
        assertTrue(output.contains("HKD 1200"), "Should display player 2's money.");
    }

    /**
     * Tests the winner display when there is a single winner.
     */
    @Test
    void testDisplayWinner_SingleWinner() {
        Player winner = new Player("Ilyas");
        winner.setMoney(2000);

        display.displayWinner(List.of(winner));

        String output = outputStream.toString();
        assertTrue(output.contains("Congratulations! Ilyas is the winner"), "Should display the winner's name.");
        assertTrue(output.contains("HKD 2000"), "Should display the winner's money.");
    }

    /**
     * Tests the winner display when there is a tie between players.
     */
    @Test
    void testDisplayWinner_Tie() {
        Player player1 = new Player("Ilyas");
        player1.setMoney(2000);

        Player player2 = new Player("Brian");
        player2.setMoney(2000);

        display.displayWinner(List.of(player1, player2));

        String output = outputStream.toString();
        assertTrue(output.contains("It's a tie"), "Should indicate a tie.");
        assertTrue(output.contains("Ilyas"), "Should display player 1's name.");
        assertTrue(output.contains("Brian"), "Should display player 2's name.");
    }

    /**
     * Tests the display of saved boards.
     */
    @Test
    void testDisplaySavedBoards() {
        display.displaySavedBoards();

        String output = outputStream.toString();
        assertTrue(output.contains("Displaying all saved boards"), "Should display saved boards header.");
    }

    /**
     * Tests the visual representation of a cell for a PropertySquare.
     */
    @Test
    void testFillCellWithPropertySquare() {
        PropertySquare square = new PropertySquare("Park Lane", 350, 50);
        String[] cell = display.fillCell(Display.BLUE_BACKGROUND, square.getName(), 14, 4, square);

        assertEquals(6, cell.length, "Cell array should have 6 lines (4 content + 2 borders).");
        assertTrue(cell[0].contains("-"), "Top border should contain dashes.");
        assertTrue(cell[5].contains("-"), "Bottom border should contain dashes.");
        assertTrue(cell[1].contains("Park Lane"), "Content should include square name.");
        assertTrue(cell[2].contains("Price: 350"), "Content should include property price.");
        assertTrue(cell[3].contains("Rent: 50"), "Content should include property rent.");
    }

    /**
     * Tests the text alignment logic when the text exceeds the given width.
     */
    @Test
    void testCenterAlignText_LongText() {
        String centered = display.centerAlignText("This is a very long text that should be truncated", 10);
        assertEquals("This is a ", centered, "Text should be truncated to fit the given width.");
    }

    /**
     * Tests the wrap text logic for edge cases.
     */
    @Test
    void testWrapText_EdgeCase() {
        List<String> wrapped = display.wrapText("Word", 4);
        assertEquals(1, wrapped.size(), "Wrapped text should have one line when it fits exactly.");
        assertEquals("Word", wrapped.get(0), "The single word should fit within the width without extra lines.");
    }

    /**
     * Tests the color assignment for a Jail square.
     */
    @Test
    void testGetColorForSquare_JailSquare() {
        Square square = new Square("Jail") {
            @Override
            public void landOn(Player player) {
                System.out.println("Jail");
            }
        };
        String color = display.getColorForSquare(square, 5, 40, 10);
        assertEquals(Display.ORANGE_BACKGROUND + Display.BLACK_TEXT, color, "Color should match the expected value for Jail squares.");
    }

    /**
     * Tests the color assignment for a Free Parking square.
     */
    @Test
    void testGetColorForSquare_FreeParking() {
        Square square = new Square("Free Parking") {
            @Override
            public void landOn(Player player) {
                System.out.println("Free Parking");
            }
        };
        String color = display.getColorForSquare(square, 0, 40, 10);
        assertEquals(Display.GREEN_BACKGROUND + Display.BLACK_TEXT, color, "Color should match the expected value for Free Parking squares.");
    }

    /**
     * Tests the board display logic for a larger board.
     */
    @Test
    void testDisplayBoard_LargeBoard() {
        Board mockBoard = mock(Board.class);
        when(mockBoard.getTotalSquares()).thenReturn(20); // Larger board

        Square property = new PropertySquare("Property", 100, 10);
        when(mockBoard.getSquare(anyInt())).thenReturn(property);
        display.displayBoard(mockBoard);

        String output = outputStream.toString();
        assertTrue(output.contains("Property"), "Output should include repeated Property squares.");
    }
}
