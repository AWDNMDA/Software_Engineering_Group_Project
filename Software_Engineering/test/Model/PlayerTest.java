package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("Ilyas");
    }

    @Test
    void testAddMoney() {
        player.addMoney(200);
        assertEquals(1700, player.getMoney(), "Money should increase by 200.");
    }

    @Test
    void testDeductMoney() {
        player.deductMoney(300);
        assertEquals(1200, player.getMoney(), "Money should decrease by 300.");
    }

    @Test
    void testJailStatus() {
        player.setInJail(true);
        assertTrue(player.isInJail(), "Player should be in jail.");
    }

    @Test
    void testPropertyManagement() {
        PropertySquare property = new PropertySquare("Central", 800, 90);
        player.addProperty(property);
        assertTrue(player.getProperties().contains(property), "Player should own the property.");
        player.removeProperty(property);
        assertFalse(player.getProperties().contains(property), "Player should no longer own the property.");
    }
}
