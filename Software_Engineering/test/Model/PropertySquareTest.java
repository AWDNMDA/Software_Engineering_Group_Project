package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the PropertySquare class.
 * Ensures proper functionality for property-related actions such as buying, renting, and setting attributes.
 */
class PropertySquareTest {
    private PropertySquare property;
    private Player owner;
    private Player tenant;

    /**
     * Initializes a PropertySquare and Player instances for testing.
     */
    @BeforeEach
    void setUp() {
        property = new PropertySquare("Central", 800, 90);
        owner = new Player("Ilyas");
        tenant = new Player("Brian");
    }

    /**
     * Tests buying a property with sufficient funds.
     */
    @Test
    void testBuyProperty() {
        property.buyProperty(owner);
        assertTrue(property.isOwned(), "Property should be owned.");
        assertEquals(owner, property.getOwner(), "Owner should be Ilyas.");
        assertEquals(700, owner.getMoney(), "Owner's money should decrease by the property price.");
    }

    /**
     * Tests buying a property with insufficient funds.
     */
    @Test
    void testBuyPropertyInsufficientFunds() {
        owner.setMoney(500);
        property.buyProperty(owner);
        assertFalse(property.isOwned(), "Property should not be owned.");
        assertNull(property.getOwner(), "Owner should remain null.");
        assertEquals(500, owner.getMoney(), "Owner's money should remain unchanged.");
    }

    /**
     * Tests landing on a property owned by another player.
     */
    @Test
    void testLandOnOwnedProperty() {
        property.buyProperty(owner);
        property.landOn(tenant);
        assertEquals(1410, tenant.getMoney(), "Tenant's money should decrease by rent.");
        assertEquals(790, owner.getMoney(), "Owner's money should increase by rent.");
    }

    /**
     * Tests landing on a property owned by the player.
     */
    @Test
    void testLandOnSelfOwnedProperty() {
        property.buyProperty(owner);
        property.landOn(owner);
        assertEquals(700, owner.getMoney(), "Owner's money should remain unchanged when landing on their own property.");
    }

    /**
     * Tests landing on an unowned property.
     */
    @Test
    void testLandOnUnownedProperty() {
        property.landOn(tenant);
        assertFalse(property.isOwned(), "Property should remain unowned.");
        assertNull(property.getOwner(), "Owner should remain null.");
    }

    /**
     * Tests setting a valid price for the property.
     */
    @Test
    void testSetValidPrice() {
        property.setPrice(1000);
        assertEquals(1000, property.getPrice(), "Property price should be updated to 1000.");
    }

    /**
     * Tests setting an invalid price for the property.
     */
    @Test
    void testSetInvalidPrice() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            property.setPrice(-500);
        });
        assertEquals("Price must be greater than 0.", exception.getMessage(), "Exception message should match.");
    }

    /**
     * Tests setting a valid rent for the property.
     */
    @Test
    void testSetValidRent() {
        property.setRent(120);
        assertEquals(120, property.getRent(), "Property rent should be updated to 120.");
    }

    /**
     * Tests setting an invalid rent for the property.
     */
    @Test
    void testSetInvalidRent() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            property.setRent(-50);
        });
        assertEquals("Rent must be greater than 0.", exception.getMessage(), "Exception message should match.");
    }

    /**
     * Tests default state of the property.
     */
    @Test
    void testDefaultState() {
        assertFalse(property.isOwned(), "Property should not be owned initially.");
        assertNull(property.getOwner(), "Owner should be null initially.");
        assertEquals("Central", property.getName(), "Property name should match initial value.");
        assertEquals(800, property.getPrice(), "Property price should match initial value.");
        assertEquals(90, property.getRent(), "Property rent should match initial value.");
    }
}
