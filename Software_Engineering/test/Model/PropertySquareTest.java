package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PropertySquareTest {
    private PropertySquare property;
    private Player owner;

    @BeforeEach
    void setUp() {
        property = new PropertySquare("Central", 800, 90);
        owner = new Player("Alice");
    }

    @Test
    void testPropertyInitialization() {
        assertEquals("Central", property.getName());
        assertEquals(800, property.getPrice());
        assertFalse(property.isOwned());
    }

    @Test
    void testBuyProperty() {
        property.buyProperty(owner);
        assertEquals(owner, property.getOwner());
        assertTrue(property.isOwned());
        assertEquals(700, owner.getMoney());
    }

    @Test
    void testLandOnOwnedProperty() {
        Player tenant = new Player("Bob");
        property.buyProperty(owner);
        property.landOn(tenant);
        assertEquals(1410, tenant.getMoney());
        assertEquals(1590, owner.getMoney());
    }

    @Test
    void testLandOnUnownedProperty() {
        Player player = new Player("Bob");
        property.landOn(player);
        assertFalse(property.isOwned());
    }
}