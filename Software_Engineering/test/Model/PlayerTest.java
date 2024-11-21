package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Player class.
 * Verifies the behavior of player actions such as managing money, jail status, and properties.
 */
class PlayerTest {
    private Player player;

    /**
     * Initializes a Player instance for testing.
     */
    @BeforeEach
    void setUp() {
        player = new Player("Ilyas");
    }

    /**
     * Tests adding money to the player's account.
     */
    @Test
    void testAddMoney() {
        player.addMoney(200);
        assertEquals(1700, player.getMoney(), "Money should increase by 200.");
    }

    /**
     * Tests deducting money from the player's account.
     */
    @Test
    void testDeductMoney() {
        player.deductMoney(300);
        assertEquals(1200, player.getMoney(), "Money should decrease by 300.");
    }

    /**
     * Tests the player's jail status.
     */
    @Test
    void testJailStatus() {
        player.setInJail(true);
        assertTrue(player.isInJail(), "Player should be in jail.");
    }

    /**
     * Tests the addition and removal of properties from the player's list.
     */
    @Test
    void testPropertyManagement() {
        PropertySquare property = new PropertySquare("Central", 800, 90);
        player.addProperty(property);
        assertTrue(player.getProperties().contains(property), "Player should own the property.");
        player.removeProperty(property);
        assertFalse(player.getProperties().contains(property), "Player should no longer own the property.");
    }
}
