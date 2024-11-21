package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        players.add(new Player("Ilyas"));
        players.add(new Player("Brian"));

        board = new Board();
        mockedRandom = mock(Random.class);
        mockedScanner = mock(Scanner.class);

        game = new Game(players, board, mockedRandom, mockedScanner);
    }

    @Test
    void testGameInitialization() {
        assertEquals(2, game.getPlayers().size(), "There should be 2 players at the start.");
        assertEquals("Ilyas", game.getCurrentPlayer().getName(), "Ilyas should be the first player.");
        assertNotNull(game.getBoard(), "Board should not be null.");
    }

    @Test
    void testPlayerTurnRotation() {
        when(mockedScanner.nextLine()).thenReturn("no");
        when(mockedRandom.nextInt(6)).thenReturn(1, 1);
        game.playTurn();
        assertEquals("Brian", game.getCurrentPlayer().getName(), "Turn should rotate to Brian.");
        game.playTurn();
        assertEquals("Ilyas", game.getCurrentPlayer().getName(), "Turn should rotate back to Ilyas.");
    }

    @Test
    void testPlayerMoves() {
        when(mockedScanner.nextLine()).thenReturn("no");
        when(mockedRandom.nextInt(6)).thenReturn(2, 3);
        game.playTurn();
        assertEquals(7, players.get(0).getPosition(), "Ilyas's position should be updated after rolling the dice.");
    }

    @Test
    void testLandingOnGoSquare() {
        Player ilyas = players.get(0);
        ilyas.setPosition(0); // Go Square
        board.getSquare(ilyas.getPosition()).landOn(ilyas);
        assertEquals(3000, ilyas.getMoney(), "Ilyas should receive HKD 1500 after landing on Go.");
    }

    @Test
    void testLandingOnFreeParkingSquare() {
        Player ilyas = players.get(0); // Get the first player
        int initialMoney = ilyas.getMoney(); // Save the initial money

        ilyas.setPosition(10); // Set the position to "Free Parking" (assuming index 10 is Free Parking)
        board.getSquare(ilyas.getPosition()).landOn(ilyas); // Simulate landing on Free Parking

        // Validate no change in money or other states
        assertEquals(initialMoney, ilyas.getMoney(), "Ilyas's money should remain unchanged after landing on Free Parking.");
        assertEquals(10, ilyas.getPosition(), "Ilyas's position should remain at Free Parking.");
    }


    @Test
    void testLandingOnIncomeTaxSquare() {
        Player ilyas = players.get(0);
        ilyas.setPosition(3); // Income Tax Square
        board.getSquare(ilyas.getPosition()).landOn(ilyas);
        assertEquals(1350, ilyas.getMoney(), "Ilyas's money should decrease due to income tax.");
    }

    @Test
    void testLandingOnChanceSquare() {
        Random mockedRandom = mock(Random.class);
        ChanceSquare chanceSquare = new ChanceSquare("Chance", mockedRandom);

        when(mockedRandom.nextInt(31)).thenReturn(9); // Amount: (9 + 1) * 10 = 100
        when(mockedRandom.nextBoolean()).thenReturn(true); // Gain money

        Player ilyas = players.get(0);
        chanceSquare.landOn(ilyas);

        assertEquals(1600, ilyas.getMoney(), "Ilyas's money should increase by HKD 100 after landing on Chance.");
    }

    @Test
    void testLandingOnGoToJailSquare() {
        Player ilyas = players.get(0);
        board.getSquare(15).landOn(ilyas); // Go To Jail
        assertTrue(ilyas.isInJail(), "Ilyas should be in jail after landing on 'Go To Jail'.");
        assertEquals(5, ilyas.getPosition(), "Ilyas's position should be updated to 'In Jail'.");
    }

    @Test
    void testBankruptcy() {
        when(mockedScanner.nextLine()).thenReturn("no");
        Player ilyas = players.get(0);
        ilyas.deductMoney(1600);
        game.playTurn();
        assertEquals(1, game.getPlayers().size(), "Only Brian should remain after Ilyas's bankruptcy.");
        assertEquals("Brian", game.getPlayers().get(0).getName(), "Brian should be the remaining player.");
    }

    @Test
    void testPlayerBankruptcyReleasesProperties() {
        PropertySquare property1 = new PropertySquare("Central", 800, 100);
        PropertySquare property2 = new PropertySquare("Wan Chai", 700, 90);
        board.getSquares().set(1, property1);
        board.getSquares().set(2, property2);

        property1.buyProperty(players.get(0));
        property2.buyProperty(players.get(0));
        assertEquals(players.get(0), property1.getOwner(), "Ilyas should own Central.");
        assertEquals(players.get(0), property2.getOwner(), "Ilyas should own Wan Chai.");

        players.get(0).setMoney(-100);
        game.checkBankruptcy();

        assertNull(property1.getOwner(), "Central should be unowned after Ilyas's bankruptcy.");
        assertNull(property2.getOwner(), "Wan Chai should be unowned after Ilyas's bankruptcy.");
    }




    @Test
    void testWinnerByMoney() {
        players.get(0).addMoney(500); // Ilyas has more money
        List<Player> winners = game.getWinner();
        assertEquals(1, winners.size(), "There should be one winner.");
        assertEquals("Ilyas", winners.get(0).getName(), "Ilyas should be the winner.");
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

    @Test
    void testPlayerInJailRollingDoublesSuccess() {
        Player ilyas = players.get(0);
        ilyas.setInJail(true);
        when(mockedScanner.nextLine()).thenReturn("F");
        when(mockedRandom.nextInt(6)).thenReturn(3, 3); // Rolling doubles
        game.playTurn();
        assertFalse(ilyas.isInJail(), "Ilyas should be out of jail after rolling doubles.");
    }

    @Test
    void testPlayerInJailRollingDoublesFailure() {
        Player ilyas = players.get(0);
        ilyas.setInJail(true);
        when(mockedScanner.nextLine()).thenReturn("F");
        when(mockedRandom.nextInt(6)).thenReturn(1, 2); // No doubles
        game.playTurn();
        assertTrue(ilyas.isInJail(), "Ilyas should remain in jail after failing to roll doubles.");
    }

    @Test
    void testPlayerPaysFineToExitJail() {
        Player ilyas = players.get(0);
        ilyas.setInJail(true);
        when(mockedScanner.nextLine()).thenReturn("T"); // Pay fine
        when(mockedRandom.nextInt(6)).thenReturn(3, 4);
        game.playTurn();
        assertFalse(ilyas.isInJail(), "Ilyas should be out of jail after paying the fine.");
        assertEquals(1350, ilyas.getMoney(), "Ilyas's money should decrease by the fine amount.");
    }

    @Test
    void testPlayerLandsOnOwnedProperty() {
        PropertySquare property = new PropertySquare("Central", 800, 100);
        property.buyProperty(players.get(1)); // Brian owns the property
        property.landOn(players.get(0)); // Ilyas lands on it
        assertEquals(1400, players.get(0).getMoney(), "Ilyas should pay rent to Brian.");
        assertEquals(800, players.get(1).getMoney(), "Brian should receive rent from Ilyas.");
    }

    @Test
    void testPlayerLandsOnOwnProperty() {
        PropertySquare property = new PropertySquare("Central", 800, 100);
        property.buyProperty(players.get(0)); // Ilyas owns the property
        property.landOn(players.get(0)); // Ilyas lands on it
        assertEquals(700, players.get(0).getMoney(), "Ilyas's money should remain unchanged.");
    }
}
