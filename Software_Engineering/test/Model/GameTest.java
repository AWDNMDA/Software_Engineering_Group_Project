package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the `Game` class, covering various game scenarios.
 */
class GameTest {
    private Game game;
    private Board board;
    private List<Player> players;
    private Random mockedRandom;
    private Scanner mockedScanner;

    /**
     * Sets up the initial state for each test, including players, board, and mocked objects.
     */
    @BeforeEach
    void setUp() {
        players = new ArrayList<>();
        players.add(new Player("Ilyas"));
        players.add(new Player("Brian"));

        board = new Board();
        mockedRandom = mock(Random.class);
        mockedScanner = mock(Scanner.class);

        game = new Game(players, board, mockedRandom, mockedScanner);
    }

    /**
     * Tests the initialization of the game.
     */
    @Test
    void testGameInitialization() {
        assertEquals(2, game.getPlayers().size(), "There should be 2 players at the start.");
        assertEquals("Ilyas", game.getCurrentPlayer().getName(), "Ilyas should be the first player.");
        assertNotNull(game.getBoard(), "Board should not be null.");
    }

    /**
     * Tests the rotation of turns between players.
     */
    @Test
    void testPlayerTurnRotation() {
        when(mockedScanner.nextLine()).thenReturn("no");
        when(mockedRandom.nextInt(6)).thenReturn(1, 1);
        game.playTurn();
        assertEquals("Brian", game.getCurrentPlayer().getName(), "Turn should rotate to Brian.");
        game.playTurn();
        assertEquals("Ilyas", game.getCurrentPlayer().getName(), "Turn should rotate back to Ilyas.");
    }

    /**
     * Tests player movement on the board based on dice rolls.
     */
    @Test
    void testPlayerMoves() {
        when(mockedScanner.nextLine()).thenReturn("no");
        when(mockedRandom.nextInt(6)).thenReturn(2, 3);
        game.playTurn();
        assertEquals(7, players.get(0).getPosition(), "Ilyas's position should be updated after rolling the dice.");
    }

    /**
     * Tests landing on the "Go" square and receiving money.
     */
    @Test
    void testLandingOnGoSquare() {
        Player ilyas = players.get(0);
        ilyas.setPosition(0);
        board.getSquare(ilyas.getPosition()).landOn(ilyas);
        assertEquals(3000, ilyas.getMoney(), "Ilyas should receive HKD 1500 after landing on Go.");
    }

    /**
     * Tests landing on the "Free Parking" square, where nothing happens.
     */
    @Test
    void testLandingOnFreeParkingSquare() {
        Player ilyas = players.get(0);
        int initialMoney = ilyas.getMoney();
        ilyas.setPosition(10);
        board.getSquare(ilyas.getPosition()).landOn(ilyas);
        assertEquals(initialMoney, ilyas.getMoney(), "Ilyas's money should remain unchanged.");
    }

    /**
     * Tests landing on the "Income Tax" square and paying tax.
     */
    @Test
    void testLandingOnIncomeTaxSquare() {
        Player ilyas = players.get(0);
        ilyas.setPosition(3);
        board.getSquare(ilyas.getPosition()).landOn(ilyas);
        assertEquals(1350, ilyas.getMoney(), "Ilyas's money should decrease due to income tax.");
    }

    /**
     * Tests landing on the "Chance" square with mocked random outcomes.
     */
    @Test
    void testLandingOnChanceSquare() {
        Random mockedRandom = mock(Random.class);
        ChanceSquare chanceSquare = new ChanceSquare("Chance", mockedRandom);

        when(mockedRandom.nextInt(31)).thenReturn(9);
        when(mockedRandom.nextBoolean()).thenReturn(true);

        Player ilyas = players.get(0);
        chanceSquare.landOn(ilyas);

        assertEquals(1600, ilyas.getMoney(), "Ilyas's money should increase by HKD 100 after landing on Chance.");
    }

    /**
     * Tests landing on the "Go To Jail" square and being sent to jail.
     */
    @Test
    void testLandingOnGoToJailSquare() {
        Player ilyas = players.get(0);
        board.getSquare(15).landOn(ilyas);
        assertTrue(ilyas.isInJail(), "Ilyas should be in jail after landing on 'Go To Jail'.");
    }

    /**
     * Tests handling player bankruptcy and removing them from the game.
     */
    @Test
    void testBankruptcy() {
        when(mockedScanner.nextLine()).thenReturn("no");
        Player ilyas = players.get(0);
        ilyas.deductMoney(1600);
        game.playTurn();
        assertEquals(1, game.getPlayers().size(), "Only Brian should remain after Ilyas's bankruptcy.");
    }

    /**
     * Tests that properties owned by bankrupt players are released.
     */
    @Test
    void testPlayerBankruptcyReleasesProperties() {
        PropertySquare property1 = new PropertySquare("Central", 800, 100);
        PropertySquare property2 = new PropertySquare("Wan Chai", 700, 90);
        board.getSquares().set(1, property1);
        board.getSquares().set(2, property2);

        property1.buyProperty(players.get(0));
        property2.buyProperty(players.get(0));
        players.get(0).setMoney(-100);
        game.checkBankruptcy();

        assertNull(property1.getOwner(), "Central should be unowned after bankruptcy.");
        assertNull(property2.getOwner(), "Wan Chai should be unowned after bankruptcy.");
    }

    /**
     * Tests determining the winner by money.
     */
    @Test
    void testWinnerByMoney() {
        players.get(0).addMoney(500);
        List<Player> winners = game.getWinner();
        assertEquals(1, winners.size(), "There should be one winner.");
    }

    /**
     * Tests handling a tie between players.
     */
    @Test
    void testTieBetweenPlayers() {
        players.get(0).setMoney(1500);
        players.get(1).setMoney(1500);
        List<Player> winners = game.getWinner();
        assertEquals(2, winners.size(), "Both players should tie.");
    }

    /**
     * Tests the game ending after reaching the maximum number of rounds.
     */
    @Test
    void testGameOverByMaxRounds() {
        when(mockedScanner.nextLine()).thenReturn("no");
        when(mockedRandom.nextInt(6)).thenReturn(3, 3);
        for (int i = 0; i < 100; i++) {
            game.playTurn();
        }
        assertTrue(game.isGameOver(), "Game should end after 100 rounds.");
    }

    /**
     * Tests a player rolling doubles to exit jail successfully.
     */
    @Test
    void testPlayerInJailRollingDoublesSuccess() {
        Player ilyas = players.get(0);
        ilyas.setInJail(true);
        when(mockedScanner.nextLine()).thenReturn("F");
        when(mockedRandom.nextInt(6)).thenReturn(3, 3);
        game.playTurn();
        assertFalse(ilyas.isInJail(), "Ilyas should be out of jail after rolling doubles.");
    }

    /**
     * Tests a player failing to roll doubles while in jail.
     */
    @Test
    void testPlayerInJailRollingDoublesFailure() {
        Player ilyas = players.get(0);
        ilyas.setInJail(true);
        when(mockedScanner.nextLine()).thenReturn("F");
        when(mockedRandom.nextInt(6)).thenReturn(1, 2);
        game.playTurn();
        assertTrue(ilyas.isInJail(), "Ilyas should remain in jail after failing to roll doubles.");
    }

    /**
     * Tests a player paying a fine to exit jail.
     */
    @Test
    void testPlayerPaysFineToExitJail() {
        Player ilyas = players.get(0);
        ilyas.setInJail(true);
        when(mockedScanner.nextLine()).thenReturn("T");
        game.playTurn();
        assertFalse(ilyas.isInJail(), "Ilyas should be out of jail after paying the fine.");
    }

    /**
     * Tests a player landing on an owned property and paying rent.
     */
    @Test
    void testPlayerLandsOnOwnedProperty() {
        PropertySquare property = new PropertySquare("Central", 800, 100);
        property.buyProperty(players.get(1));
        property.landOn(players.get(0));
        assertEquals(1400, players.get(0).getMoney(), "Ilyas should pay rent to Brian.");
    }

    /**
     * Tests a player landing on their own property.
     */
    @Test
    void testPlayerLandsOnOwnProperty() {
        PropertySquare property = new PropertySquare("Central", 800, 100);
        property.buyProperty(players.get(0));
        property.landOn(players.get(0));
        assertEquals(700, players.get(0).getMoney(), "Ilyas's money should remain unchanged.");
    }

    /**
     * Tests the setName method in the Square class.
     */
    @Test
    void testSetNameForSquare() {
        Square square = new Square("Old Name") {
            @Override
            public void landOn(Player player) {
            }
        };
        square.setName("New Name");
        assertEquals("New Name", square.getName(), "The square's name should be updated to 'New Name'.");
    }

}
