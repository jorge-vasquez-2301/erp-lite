package com.debuggeandoideas.erp_lite.catalog;

import com.debuggeandoideas.erp_lite.domain.catalog.CatalogItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CatalogItem Domain Test")
class CatalogItemTest {

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Code Is Blank or null")
    void shouldThrowIllegalArgumentExceptionWhenCodeIsBlankOrNull() {
        final String msgEx = "Code cannot be null or empty";

        IllegalArgumentException targetExForNull = assertThrows(IllegalArgumentException.class,
                () -> {
                    new CatalogItem(
                            UUID.randomUUID().toString(),
                            null,
                            "Some value",
                            "This product is a test",
                            1,
                            Map.of("Vale display", "Test")
                    );
                });

        assertEquals(msgEx, targetExForNull.getMessage());

        IllegalArgumentException targetExForEmpty = assertThrows(IllegalArgumentException.class,
                () -> {
                    new CatalogItem(
                            UUID.randomUUID().toString(),
                            "",
                            "Some value",
                            "This product is a test",
                            1,
                            Map.of("Vale display", "Test")
                    );
                });

        assertEquals(msgEx, targetExForEmpty.getMessage());
    }

    @Test
    @DisplayName("Should Create CatalogItem With Valid Data And Set Defaults")
    void shouldCreateCatalogItemWithValidDataAndSetDefaults() {
        final String id = UUID.randomUUID().toString();

        CatalogItem item = new CatalogItem(
                id,
                "CODE-001",
                "Some value",
                "This product is a test",
                10,
                Map.of("display", "Test")
        );

        assertEquals(id, item.getId());
        assertEquals("CODE-001", item.getCode());
        assertEquals("Some value", item.getValue());
        assertEquals("This product is a test", item.getDescription());
        assertEquals(10, item.getDisplayOrder());
        assertTrue(item.isActive());

        assertEquals("Test", item.getMetadata("display"));
        assertTrue(item.hasMetadata("display"));
        assertFalse(item.hasMetadata("missing"));
    }

    @Test
    @DisplayName("Should Copy Metadata And Make It Unmodifiable")
    void shouldCopyMetadataAndMakeItUnmodifiable() {
        Map<String, Object> original = new HashMap<>();
        original.put("k1", "v1");

        CatalogItem item = new CatalogItem(
                UUID.randomUUID().toString(),
                "CODE-002",
                "Some value",
                "This product is a test",
                1,
                original
        );

        // Mutate original map AFTER creation; item metadata must not change
        original.put("k1", "CHANGED");
        original.put("k2", "v2");

        assertEquals("v1", item.getMetadata("k1"));
        assertFalse(item.hasMetadata("k2"));

        // Ensure internal metadata is unmodifiable (Map.copyOf)
        assertThrows(UnsupportedOperationException.class,
                () -> {
                    item.getMetadata().put("k3", "v3");
                });
    }

    @Test
    @DisplayName("Should Use Empty Metadata When Null Is Provided")
    void shouldUseEmptyMetadataWhenNullIsProvided() {
        CatalogItem item = new CatalogItem(
                UUID.randomUUID().toString(),
                "CODE-003",
                "Some value",
                "This product is a test",
                1,
                null
        );

        assertNotNull(item.getMetadata());
        assertTrue(item.getMetadata().isEmpty());
        assertNull(item.getMetadata("any"));
        assertFalse(item.hasMetadata("any"));

        assertThrows(UnsupportedOperationException.class,
                () -> {
                    item.getMetadata().put("k", "v");
                });
    }

    @Test
    @DisplayName("Should Turn Off And Turn On Status")
    void shouldTurnOffAndTurnOnStatus() {
        CatalogItem item = new CatalogItem(
                UUID.randomUUID().toString(),
                "CODE-004",
                "Some value",
                "This product is a test",
                1,
                Map.of()
        );

        assertTrue(item.isActive());

        item.turnOffStatus();
        assertFalse(item.isActive());

        item.turnOnStatus();
        assertTrue(item.isActive());
    }

    @Test
    @DisplayName("Should Support Equals And HashCode By ID (Same Class)")
    void shouldSupportEqualsAndHashCodeByIdSameClass() {
        final String sameId = UUID.randomUUID().toString();

        CatalogItem item1 = new CatalogItem(
                sameId,
                "CODE-005",
                "Value 1",
                "Desc 1",
                1,
                Map.of("a", 1)
        );

        CatalogItem item2 = new CatalogItem(
                sameId,
                "CODE-OTHER",
                "Value 2",
                "Desc 2",
                999,
                Map.of("b", 2)
        );

        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    @DisplayName("Should Not Be Equal When IDs Differ")
    void shouldNotBeEqualWhenIdsDiffer() {
        CatalogItem item1 = new CatalogItem(
                UUID.randomUUID().toString(),
                "CODE-006",
                "Value 1",
                "Desc 1",
                1,
                Map.of()
        );

        CatalogItem item2 = new CatalogItem(
                UUID.randomUUID().toString(),
                "CODE-006",
                "Value 1",
                "Desc 1",
                1,
                Map.of()
        );

        assertNotEquals(item1, item2);
    }

    @Test
    @DisplayName("Should Not Be Equal When Compared With Null Or Other Type")
    void shouldNotBeEqualWhenComparedWithNullOrOtherType() {
        CatalogItem item = new CatalogItem(
                UUID.randomUUID().toString(),
                "CODE-007",
                "Value 1",
                "Desc 1",
                1,
                Map.of()
        );

        assertNotEquals(null, item);
        assertNotEquals(item, "not-a-catalog-item");
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Entity ID Is Null")
    void shouldThrowIllegalArgumentExceptionWhenEntityIdIsNull() {
        final String msgEx = "Entity ID cannot be null";

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> {
                    new CatalogItem(
                            null,
                            "CODE-008",
                            "Some value",
                            "This product is a test",
                            1,
                            Map.of()
                    );
                });

        assertEquals(msgEx, targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Have A Non Null ToString")
    void shouldHaveANonNullToString() {
        CatalogItem item = new CatalogItem(
                UUID.randomUUID().toString(),
                "CODE-009",
                "Some value",
                "This product is a test",
                1,
                Map.of("k", "v")
        );

        assertNotNull(item.toString());
        assertFalse(item.toString().isEmpty());
    }
}