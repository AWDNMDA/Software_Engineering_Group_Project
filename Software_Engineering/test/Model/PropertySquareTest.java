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
        owner = new Player("Ilyas");
        tenant = new Player("Brian");
    }

    @Test
    void testBuyProperty() {
        property.buyProperty(owner);
        assertTrue(property.isOwned(), "Property should be owned.");
        assertEquals(owner, property.getOwner(), "Owner should be Ilyas.");
        assertEquals(700, owner.getMoney(), "Owner's money should decrease by the property price.");
    }

    @Test
    void testBuyPropertyInsufficientFunds() {
        owner.setMoney(500); // Not enough to buy the property
        property.buyProperty(owner);
        assertFalse(property.isOwned(), "Property should not be owned.");
        assertNull(property.getOwner(), "Owner should remain null.");
        assertEquals(500, owner.getMoney(), "Owner's money should remain unchanged.");
    }

    @Test
    void testLandOnOwnedProperty() {
        property.buyProperty(owner);
        property.landOn(tenant);
        assertEquals(1410, tenant.getMoney(), "Tenant's money should decrease by rent.");
        assertEquals(790, owner.getMoney(), "Owner's money should increase by rent.");
    }

    @Test
    void testLandOnSelfOwnedProperty() {
        property.buyProperty(owner);
        property.landOn(owner);
        assertEquals(700, owner.getMoney(), "Owner's money should remain unchanged when landing on their own property.");
    }

    @Test
    void testLandOnUnownedProperty() {
        property.landOn(tenant);
        assertFalse(property.isOwned(), "Property should remain unowned.");
        assertNull(property.getOwner(), "Owner should remain null.");
    }

    @Test
    void testLandOnUnownedPropertyWithMessage() {
        property.landOn(owner);
        // No assertions, as this ensures the unowned branch executes fully
    }

    @Test
    void testSetValidPrice() {
        property.setPrice(1000);
        assertEquals(1000, property.getPrice(), "Property price should be updated to 1000.");
    }

    @Test
    void testSetInvalidPrice() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            property.setPrice(-500);
        });
        assertEquals("Price must be greater than 0.", exception.getMessage(), "Exception message should match.");
    }

    @Test
    void testSetValidRent() {
        property.setRent(120);
        assertEquals(120, property.getRent(), "Property rent should be updated to 120.");
    }

    @Test
    void testSetInvalidRent() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            property.setRent(-50);
        });
        assertEquals("Rent must be greater than 0.", exception.getMessage(), "Exception message should match.");
    }

    @Test
    void testSetName() {
        property.setName("Updated Central");
        assertEquals("Updated Central", property.getName(), "Property name should be updated.");
    }

    @Test
    void testDefaultState() {
        assertFalse(property.isOwned(), "Property should not be owned initially.");
        assertNull(property.getOwner(), "Owner should be null initially.");
        assertEquals("Central", property.getName(), "Property name should match initial value.");
        assertEquals(800, property.getPrice(), "Property price should match initial value.");
        assertEquals(90, property.getRent(), "Property rent should match initial value.");
    }

    @Test
    void testMultiplePlayersLandOnOwnedProperty() {
        property.buyProperty(owner);
        Player tenant2 = new Player("Alex");
        property.landOn(tenant);
        property.landOn(tenant2);
        assertEquals(1410, tenant.getMoney(), "First tenant's money should decrease by rent.");
        assertEquals(1410, tenant2.getMoney(), "Second tenant's money should decrease by rent.");
        assertEquals(880, owner.getMoney(), "Owner's money should increase by rent from both tenants.");
    }

    @Test
    void testPropertyReassignedToNewOwner() {
        property.buyProperty(owner);
        property.setOwner(tenant);
        assertEquals(tenant, property.getOwner(), "Owner should be reassigned to tenant.");
    }
}
