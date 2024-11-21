package Controller;

import Model.*;
import View.Display;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the `GameController` class, ensuring its functionality in various scenarios.
 */
class GameControllerTest {
    private GameController gameController;
    private Game mockGame;
    private Display mockView;
    private Scanner mockScanner;
    private List<Player> players;

    /**
     * Initializes the test setup, including mocked objects and default player list.
     */
    @BeforeEach
    void setUp() {
        players = new ArrayList<>();
        players.add(new Player("Ilyas"));
        players.add(new Player("Brian"));

        Board mockBoard = mock(Board.class);
        mockGame = mock(Game.class);
        when(mockGame.getPlayers()).thenReturn(players);
        when(mockGame.getBoard()).thenReturn(mockBoard);
        when(mockGame.isGameOver()).thenReturn(false, false, true);
        when(mockGame.getWinner()).thenReturn(List.of(players.get(0)));

        mockView = mock(Display.class);
        mockScanner = mock(Scanner.class);
        gameController = new GameController(mockGame, mockView);
    }

    /**
     * Tests valid integer input handling.
     */
    @Test
    void testGetValidatedIntegerInput_ValidInput() {
        when(mockScanner.nextInt()).thenReturn(4);
        when(mockScanner.nextLine()).thenReturn("");
        int result = GameController.getValidatedIntegerInput(mockScanner, "Enter a number:", 2, 6);
        assertEquals(4, result, "Should return the valid input.");
    }

    /**
     * Tests invalid integer input handling with recovery.
     */
    @Test
    void testGetValidatedIntegerInput_InvalidInput() {
        when(mockScanner.nextInt()).thenThrow(new IllegalArgumentException("Invalid input")).thenReturn(3);
        when(mockScanner.nextLine()).thenReturn("");
        int result = GameController.getValidatedIntegerInput(mockScanner, "Enter a number:", 2, 6);
        assertEquals(3, result, "Should return the valid input after invalid input.");
    }

    /**
     * Tests selecting the default board option.
     */
    @Test
    void testSelectOrCustomizeBoard_DefaultBoard() {
        when(mockScanner.nextInt()).thenReturn(1);
        when(mockScanner.nextLine()).thenReturn("");
        Board board = GameController.selectOrCustomizeBoard(mockScanner, mockView);
        assertNotNull(board, "Default board should be returned.");
    }

    /**
     * Tests selecting the load saved board option with no saved boards.
     */
    @Test
    void testSelectOrCustomizeBoard_NoSavedBoards() {
        when(mockScanner.nextInt()).thenReturn(2);
        when(mockScanner.nextLine()).thenReturn("");
        Board board = GameController.selectOrCustomizeBoard(mockScanner, mockView);
        assertNotNull(board, "Default board should be returned when no saved boards are available.");
    }

    /**
     * Tests customizing a board using the BoardDesigner.
     */
    @Test
    void testSelectOrCustomizeBoard_CustomizeBoard() {
        when(mockScanner.nextInt()).thenReturn(3);
        when(mockScanner.nextLine()).thenReturn("yes", "CustomBoard");

        BoardDesigner mockBoardDesigner = mock(BoardDesigner.class);
        doNothing().when(mockBoardDesigner).customizeBoard(any(Board.class));

        Board board = GameController.customizeBoard(mockScanner, mockBoardDesigner);
        assertNotNull(board, "Customized board should not be null.");
        verify(mockBoardDesigner).customizeBoard(any(Board.class));
    }

    /**
     * Tests the startGame method for multiple rounds.
     */
    @Test
    void testStartGame_LoopExecution() {
        when(mockGame.getRoundCount()).thenReturn(0, 1);
        when(mockGame.getCurrentPlayer()).thenReturn(players.get(0), players.get(1));
        when(mockGame.isGameOver()).thenReturn(false, false, true);

        gameController.startGame();

        verify(mockView).displayBoard(mockGame.getBoard());
        verify(mockView, times(2)).displayPlayerTurn(anyInt(), any(Player.class));
        verify(mockView, times(2)).displayGameStatus(anyList());
        verify(mockView).displayWinner(anyList());
    }

    /**
     * Tests playing a single round.
     */
    @Test
    void testPlayRound() {
        when(mockGame.getRoundCount()).thenReturn(0);
        when(mockGame.getCurrentPlayer()).thenReturn(players.get(0));

        gameController.playRound();

        verify(mockView).displayPlayerTurn(0, players.get(0));
        verify(mockGame).playTurn();
        verify(mockView).displayGameStatus(players);
    }

    /**
     * Tests declaring a winner when the game ends.
     */
    @Test
    void testDeclareWinner() {
        when(mockGame.isGameOver()).thenReturn(true);
        when(mockGame.getWinner()).thenReturn(List.of(players.get(0)));

        gameController.startGame();
        verify(mockView).displayWinner(List.of(players.get(0)));
    }

    /**
     * Tests creating players from user input.
     */
    @Test
    void testCreatePlayers() {
        when(mockScanner.nextLine())
                .thenReturn("Ilyas")
                .thenReturn("Brian");
        List<Player> createdPlayers = GameController.createPlayers(mockScanner, 2);

        assertEquals(2, createdPlayers.size(), "Should create 2 players.");
        assertEquals("Ilyas", createdPlayers.get(0).getName(), "First player should be Ilyas.");
        assertEquals("Brian", createdPlayers.get(1).getName(), "Second player should be Brian.");
    }

    /**
     * Tests invalid input handling when selecting or customizing a board.
     */
    @Test
    void testSelectOrCustomizeBoard_InvalidInput() {
        when(mockScanner.nextInt()).thenReturn(4, 1);
        when(mockScanner.nextLine()).thenReturn("");

        Board board = GameController.selectOrCustomizeBoard(mockScanner, mockView);
        assertNotNull(board, "Default board should be returned after invalid input.");
    }

    /**
     * Tests interaction validation during playRound execution.
     */
    @Test
    void testPlayRound_InteractionValidation() {
        when(mockGame.getRoundCount()).thenReturn(0);
        when(mockGame.getCurrentPlayer()).thenReturn(players.get(0));
        gameController.playRound();

        ArgumentCaptor<Player> playerCaptor = ArgumentCaptor.forClass(Player.class);
        verify(mockView).displayPlayerTurn(eq(0), playerCaptor.capture());

        Player capturedPlayer = playerCaptor.getValue();
        assertEquals(players.get(0), capturedPlayer, "Should display the correct player for the turn.");
    }

    /**
     * Tests the full flow of initializing and starting a game.
     */
    @Test
    void testInitializeAndStartGame_FullGame() {
        when(mockScanner.nextInt()).thenReturn(2, 1);
        when(mockScanner.nextLine()).thenReturn("Ilyas", "Brian", "", "", "");

        Game mockGame = mock(Game.class);
        when(mockGame.isGameOver()).thenReturn(false, true);
        when(mockGame.getWinner()).thenReturn(List.of(players.get(0)));
        when(mockGame.getCurrentPlayer()).thenReturn(players.get(0));

        GameController.initializeAndStartGame(mockScanner, mockView, mockGame);

        verify(mockView).displayPlayerTurn(anyInt(), any(Player.class));
        verify(mockView).displayGameStatus(anyList());
        verify(mockView).displayWinner(anyList());
    }
}
