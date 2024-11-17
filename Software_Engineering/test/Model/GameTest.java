package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game game;

    @BeforeEach
    void setUp() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("Alice"));
        players.add(new Player("Bob"));
        game = new Game(players, new Board());
    }

    @Test
    void testGameInitialization() {
        assertEquals(2, game.getPlayers().size(), "There should be 2 players at the start.");
        assertEquals("Alice", game.getCurrentPlayer().getName(), "Alice should be the first player.");
        assertNotNull(game.getBoard(), "Board should not be null.");
    }

    @Test
    void testPlayerTurnRotation() {
        game.playTurn();
        assertEquals("Bob", game.getCurrentPlayer().getName(), "Turn should rotate to Bob.");
        game.playTurn();
        assertEquals("Alice", game.getCurrentPlayer().getName(), "Turn should rotate back to Alice.");
    }

    @Test
    void testPlayerMoves() {
        Player currentPlayer = game.getCurrentPlayer();
        int initialPosition = currentPlayer.getPosition();
        game.playTurn(); // Assume dice roll is handled inside playTurn
        assertNotEquals(initialPosition, currentPlayer.getPosition(), "Player position should change after moving.");
    }

    @Test
    void testPlayerBankruptcy() {
        Player alice = game.getPlayers().get(0);
        alice.deductMoney(1600); // Make Alice bankrupt
        game.playTurn(); // Process Alice's turn and remove her
        assertEquals(1, game.getPlayers().size(), "One player should remain after Alice's bankruptcy.");
        assertEquals("Bob", game.getCurrentPlayer().getName(), "Bob should be the next player.");
    }

    @Test
    void testGameOverByBankruptcy() {
        game.getPlayers().get(1).deductMoney(1600); // Make Bob bankrupt
        game.playTurn(); // Process turn and remove Bob
        assertTrue(game.isGameOver(), "Game should end when only one player remains.");
    }

    @Test
    void testWinner() {
        game.getPlayers().get(0).addMoney(500); // Alice has more money
        List<Player> winners = game.getWinner();
        assertEquals(1, winners.size(), "There should be one winner.");
        assertEquals("Alice", winners.get(0).getName(), "Alice should be the winner.");
    }

    @Test
    void testTieBetweenPlayers() {
        // Make both players have the same amount of money
        game.getPlayers().get(1).addMoney(500); // Add extra money to Bob
        List<Player> winners = game.getWinner();
        assertEquals(2, winners.size(), "Both players should tie with the same money.");
    }

    @Test
    void testGameOverByMaxRounds() {
        for (int i = 0; i < 100; i++) {
            game.playTurn(); // Simulate 100 turns
        }
        assertTrue(game.isGameOver(), "Game should end after 100 rounds.");
    }

    @Test
    void testRemovingBankruptPlayerMidGame() {
        Player alice = game.getPlayers().get(0);
        alice.deductMoney(1600); // Alice goes bankrupt
        game.playTurn(); // Alice should be removed
        assertEquals(1, game.getPlayers().size(), "One player should remain after Alice is removed.");
        assertFalse(game.getPlayers().contains(alice), "Alice should no longer be in the game.");
    }

    @Test
    void testPlayerSkipsTurnInJail() {
        Player currentPlayer = game.getCurrentPlayer();
        currentPlayer.setInJail(true); // Put Alice in jail
        game.playTurn(); // Alice should skip her turn
        assertEquals("Bob", game.getCurrentPlayer().getName(), "Turn should rotate to Bob when Alice is in jail.");
    }

    @Test
    void testRoundCountIncreases() {
        int initialRound = game.getRoundCount();
        game.playTurn();
        assertEquals(initialRound + 1, game.getRoundCount(), "Round count should increase after each turn.");
    }
}
