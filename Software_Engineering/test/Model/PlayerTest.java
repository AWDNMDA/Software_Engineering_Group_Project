package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("Alice");
    }

    @Test
    void testInitialSetup() {
        assertEquals("Alice", player.getName());
        assertEquals(1500, player.getMoney());
        assertEquals(0, player.getPosition());
        assertFalse(player.isInJail());
        assertEquals(0, player.getProperties().size());
    }

    @Test
    void testMoveWithinBoardLimits() {
        player.move(5);
        assertEquals(5, player.getPosition());
    }

    @Test
    void testMoveWithWrappingAroundBoard() {
        player.move(25);
        assertEquals(5, player.getPosition()); // Assuming the board has 20 squares
    }

    @Test
    void testAddProperty() {
        PropertySquare property = new PropertySquare("Central", 800, 90);
        player.addProperty(property);
        assertTrue(player.getProperties().contains(property));
    }

    @Test
    void testRemoveProperty() {
        PropertySquare property = new PropertySquare("Central", 800, 90);
        player.addProperty(property);
        player.removeProperty(property);
        assertFalse(player.getProperties().contains(property));
    }

    @Test
    void testJailStatus() {
        player.setInJail(true);
        assertTrue(player.isInJail());
        player.setInJail(false);
        assertFalse(player.isInJail());
    }

    @Test
    void testMoneyManagement() {
        player.addMoney(200);
        assertEquals(1700, player.getMoney());

        player.deductMoney(300);
        assertEquals(1400, player.getMoney());
    }

    @Test
    void testBankruptcy() {
        player.deductMoney(1600); // Deduct more than available money
        assertTrue(player.getMoney() < 0, "Player should be bankrupt");
    }

    @Test
    void testSetPosition() {
        player.setPosition(15);
        assertEquals(15, player.getPosition());
    }

    @Test
    void testSetName() {
        player.setName("Bob");
        assertEquals("Bob", player.getName());
    }

    @Test
    void testSetMoney() {
        player.setMoney(2000);
        assertEquals(2000, player.getMoney());
    }

    @Test
    void testSetJailTurns() {
        player.setJailTurns(2);
        assertEquals(2, player.getJailTurns());
    }

    @Test
    void testIncrementJailTurns() {
        player.setJailTurns(1);
        player.setJailTurns(player.getJailTurns() + 1);
        assertEquals(2, player.getJailTurns());
    }
}
