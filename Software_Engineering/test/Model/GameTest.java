package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameTest {
    private Game game;
    private Board board;
    private List<Player> players;
    private Random mockedRandom;
    private Scanner mockedScanner;

    @BeforeEach
    void setUp() {
        players = new ArrayList<>();
        players.add(new Player("Alice"));
        players.add(new Player("Bob"));

        board = new Board();
        mockedRandom = mock(Random.class);
        mockedScanner = mock(Scanner.class);

        game = new Game(players, board, mockedRandom, mockedScanner);
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
        when(mockedRandom.nextInt(6)).thenReturn(2, 3); // Dice roll: 3 + 4 = 7
        game.playTurn();
        assertEquals(7, players.get(0).getPosition(), "Alice's position should be updated after rolling the dice.");
    }

    @Test
    void testLandingOnGoSquare() {
        Player alice = players.get(0);
        alice.setPosition(0); // Go Square
        board.getSquare(alice.getPosition()).landOn(alice);
        assertEquals(3000, alice.getMoney(), "Alice should receive HKD 1500 after landing on Go.");
    }

    @Test
    void testLandingOnIncomeTaxSquare() {
        Player alice = players.get(0);
        alice.setPosition(2); // Income Tax Square
        board.getSquare(alice.getPosition()).landOn(alice);
        assertTrue(alice.getMoney() < 1500, "Alice's money should decrease due to income tax.");
    }

    @Test
    void testLandingOnChanceSquare() {
        Player alice = players.get(0);
        alice.setPosition(7); // Chance Square
        when(mockedRandom.nextInt(31)).thenReturn(10); // Random amount (10 * 10 = 100)
        when(mockedRandom.nextBoolean()).thenReturn(true); // Gain money
        board.getSquare(alice.getPosition()).landOn(alice);
        assertEquals(1600, alice.getMoney(), "Alice's money should increase by HKD 100 after landing on Chance.");
    }

    @Test
    void testLandingOnGoToJailSquare() {
        Player alice = players.get(0);
        alice.setPosition(5); // Go To Jail Square
        board.getSquare(alice.getPosition()).landOn(alice);
        assertTrue(alice.isInJail(), "Alice should be in jail after landing on 'Go To Jail'.");
    }

    @Test
    void testPlayerInJailRollForDoublesFailure() {
        Player alice = players.get(0);
        alice.setInJail(true);
        when(mockedScanner.nextLine()).thenReturn("F");
        when(mockedRandom.nextInt(6)).thenReturn(2, 3); // Dice rolls: 3 and 4 (no doubles)
        game.playTurn();
        assertTrue(alice.isInJail(), "Alice should remain in jail if no doubles are rolled.");
    }

    @Test
    void testPlayerInJailRollForDoublesSuccess() {
        Player alice = players.get(0);
        alice.setInJail(true);
        when(mockedScanner.nextLine()).thenReturn("F");
        when(mockedRandom.nextInt(6)).thenReturn(3, 3); // Dice rolls: 4 and 4 (doubles)
        game.playTurn();
        assertFalse(alice.isInJail(), "Alice should be released from jail after rolling doubles.");
    }

    @Test
    void testBankruptcy() {
        Player alice = players.get(0);
        alice.deductMoney(1600); // Make Alice bankrupt
        game.playTurn();
        assertEquals(1, game.getPlayers().size(), "Only Bob should remain after Alice's bankruptcy.");
    }

    @Test
    void testWinnerByMoney() {
        players.get(0).addMoney(500); // Alice has more money
        List<Player> winners = game.getWinner();
        assertEquals(1, winners.size(), "There should be one winner.");
        assertEquals("Alice", winners.get(0).getName(), "Alice should be the winner.");
    }

    @Test
    void testTieBetweenPlayers() {
        players.get(0).setMoney(1500);
        players.get(1).setMoney(1500);
        List<Player> winners = game.getWinner();
        assertEquals(2, winners.size(), "Both players should tie.");
    }

    @Test
    void testGameOverByMaxRounds() {
        when(mockedScanner.nextLine()).thenReturn("yes"); // Simulate default purchase behavior
        when(mockedRandom.nextInt(6)).thenReturn(3, 3); // Randomize dice rolls
        for (int i = 0; i < 100; i++) {
            game.playTurn();
        }
        assertTrue(game.isGameOver(), "Game should end after 100 rounds.");
    }
}
