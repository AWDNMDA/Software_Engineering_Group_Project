package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PropertySquareTest {
    private PropertySquare property;
    private Player owner;
    private Player tenant;

    @BeforeEach
    void setUp() {
        property = new PropertySquare("Central", 800, 90);
        owner = new Player("Alice");
        tenant = new Player("Bob");
    }

    @Test
    void testBuyProperty() {
        property.buyProperty(owner);
        assertTrue(property.isOwned(), "Property should be owned.");
        assertEquals(owner, property.getOwner(), "Owner should be Alice.");
    }

    @Test
    void testLandOnOwnedProperty() {
        property.buyProperty(owner);
        property.landOn(tenant);
        assertEquals(1410, tenant.getMoney(), "Tenant's money should decrease by rent.");
        assertEquals(1590, owner.getMoney(), "Owner's money should increase by rent.");
    }

    @Test
    void testLandOnUnownedProperty() {
        property.landOn(tenant);
        assertFalse(property.isOwned(), "Property should remain unowned.");
    }
}
