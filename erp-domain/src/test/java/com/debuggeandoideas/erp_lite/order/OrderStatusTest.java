package com.debuggeandoideas.erp_lite.order;

import com.debuggeandoideas.erp_lite.domain.entities.order.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderStatus Domain Test")
class OrderStatusTest {

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Status Is Null")
    void shouldThrowIllegalArgumentExceptionWhenStatusIsNull() {
        final String msgEx = "Order status cannot be null";

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> new OrderStatus(null));

        assertEquals(msgEx, targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Status Is Invalid")
    void shouldThrowIllegalArgumentExceptionWhenStatusIsInvalid() {
        String[] invalidStatuses = {"INVALID", "PROCESSING", "COMPLETED", "", "pending", "confirmed"};

        for (String invalidStatus : invalidStatuses) {
            IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                    () -> new OrderStatus(invalidStatus),
                    "Should throw exception for: " + invalidStatus);

            assertTrue(targetEx.getMessage().contains("Invalid order status"),
                    "Exception message should contain 'Invalid order status' for: " + invalidStatus);
        }
    }

    @Test
    @DisplayName("Should Create OrderStatus With Valid Status Values")
    void shouldCreateOrderStatusWithValidStatusValues() {
        OrderStatus pending = new OrderStatus(OrderStatus.PENDING);
        OrderStatus confirmed = new OrderStatus(OrderStatus.CONFIRMED);
        OrderStatus shipped = new OrderStatus(OrderStatus.SHIPPED);
        OrderStatus delivered = new OrderStatus(OrderStatus.DELIVERED);
        OrderStatus cancelled = new OrderStatus(OrderStatus.CANCELLED);

        assertEquals(OrderStatus.PENDING, pending.value());
        assertEquals(OrderStatus.CONFIRMED, confirmed.value());
        assertEquals(OrderStatus.SHIPPED, shipped.value());
        assertEquals(OrderStatus.DELIVERED, delivered.value());
        assertEquals(OrderStatus.CANCELLED, cancelled.value());
    }

    @Test
    @DisplayName("Should Create OrderStatus Using of Method")
    void shouldCreateOrderStatusUsingOfMethod() {
        OrderStatus status = OrderStatus.of(OrderStatus.PENDING);

        assertEquals(OrderStatus.PENDING, status.value());
    }

    @Test
    @DisplayName("Should Create OrderStatus Using Factory Methods")
    void shouldCreateOrderStatusUsingFactoryMethods() {
        OrderStatus pending = OrderStatus.pending();
        OrderStatus confirmed = OrderStatus.confirmed();
        OrderStatus shipped = OrderStatus.shipped();
        OrderStatus delivered = OrderStatus.delivered();
        OrderStatus cancelled = OrderStatus.cancelled();

        assertEquals(OrderStatus.PENDING, pending.value());
        assertEquals(OrderStatus.CONFIRMED, confirmed.value());
        assertEquals(OrderStatus.SHIPPED, shipped.value());
        assertEquals(OrderStatus.DELIVERED, delivered.value());
        assertEquals(OrderStatus.CANCELLED, cancelled.value());
    }

    @Test
    @DisplayName("Should Allow Transition From PENDING To CONFIRMED")
    void shouldAllowTransitionFromPENDINGToCONFIRMED() {
        OrderStatus pending = OrderStatus.pending();
        OrderStatus confirmed = OrderStatus.confirmed();

        assertTrue(pending.canTransitionTo(confirmed));
    }

    @Test
    @DisplayName("Should Allow Transition From PENDING To CANCELLED")
    void shouldAllowTransitionFromPENDINGToCANCELLED() {
        OrderStatus pending = OrderStatus.pending();
        OrderStatus cancelled = OrderStatus.cancelled();

        assertTrue(pending.canTransitionTo(cancelled));
    }

    @Test
    @DisplayName("Should Allow Transition From CONFIRMED To SHIPPED")
    void shouldAllowTransitionFromCONFIRMEDToSHIPPED() {
        OrderStatus confirmed = OrderStatus.confirmed();
        OrderStatus shipped = OrderStatus.shipped();

        assertTrue(confirmed.canTransitionTo(shipped));
    }

    @Test
    @DisplayName("Should Allow Transition From CONFIRMED To CANCELLED")
    void shouldAllowTransitionFromCONFIRMEDToCANCELLED() {
        OrderStatus confirmed = OrderStatus.confirmed();
        OrderStatus cancelled = OrderStatus.cancelled();

        assertTrue(confirmed.canTransitionTo(cancelled));
    }

    @Test
    @DisplayName("Should Allow Transition From SHIPPED To DELIVERED")
    void shouldAllowTransitionFromSHIPPEDToDELIVERED() {
        OrderStatus shipped = OrderStatus.shipped();
        OrderStatus delivered = OrderStatus.delivered();

        assertTrue(shipped.canTransitionTo(delivered));
    }

    @Test
    @DisplayName("Should Not Allow Transition From PENDING To SHIPPED")
    void shouldNotAllowTransitionFromPENDINGToSHIPPED() {
        OrderStatus pending = OrderStatus.pending();
        OrderStatus shipped = OrderStatus.shipped();

        assertFalse(pending.canTransitionTo(shipped));
    }

    @Test
    @DisplayName("Should Not Allow Transition From PENDING To DELIVERED")
    void shouldNotAllowTransitionFromPENDINGToDELIVERED() {
        OrderStatus pending = OrderStatus.pending();
        OrderStatus delivered = OrderStatus.delivered();

        assertFalse(pending.canTransitionTo(delivered));
    }

    @Test
    @DisplayName("Should Not Allow Transition From CONFIRMED To DELIVERED")
    void shouldNotAllowTransitionFromCONFIRMEDToDELIVERED() {
        OrderStatus confirmed = OrderStatus.confirmed();
        OrderStatus delivered = OrderStatus.delivered();

        assertFalse(confirmed.canTransitionTo(delivered));
    }

    @Test
    @DisplayName("Should Not Allow Transition From SHIPPED To CANCELLED")
    void shouldNotAllowTransitionFromSHIPPEDToCANCELLED() {
        OrderStatus shipped = OrderStatus.shipped();
        OrderStatus cancelled = OrderStatus.cancelled();

        assertFalse(shipped.canTransitionTo(cancelled));
    }

    @Test
    @DisplayName("Should Not Allow Transitions From DELIVERED")
    void shouldNotAllowTransitionsFromDELIVERED() {
        OrderStatus delivered = OrderStatus.delivered();

        assertFalse(delivered.canTransitionTo(OrderStatus.pending()));
        assertFalse(delivered.canTransitionTo(OrderStatus.confirmed()));
        assertFalse(delivered.canTransitionTo(OrderStatus.shipped()));
        assertFalse(delivered.canTransitionTo(OrderStatus.cancelled()));
    }

    @Test
    @DisplayName("Should Not Allow Transitions From CANCELLED")
    void shouldNotAllowTransitionsFromCANCELLED() {
        OrderStatus cancelled = OrderStatus.cancelled();

        assertFalse(cancelled.canTransitionTo(OrderStatus.pending()));
        assertFalse(cancelled.canTransitionTo(OrderStatus.confirmed()));
        assertFalse(cancelled.canTransitionTo(OrderStatus.shipped()));
        assertFalse(cancelled.canTransitionTo(OrderStatus.delivered()));
    }

    @Test
    @DisplayName("Should Correctly Identify PENDING Status")
    void shouldCorrectlyIdentifyPENDINGStatus() {
        OrderStatus pending = OrderStatus.pending();

        assertTrue(pending.isPending());
        assertFalse(pending.isConfirmed());
        assertFalse(pending.isShipped());
        assertFalse(pending.isDelivered());
        assertFalse(pending.isCancelled());
    }

    @Test
    @DisplayName("Should Correctly Identify CONFIRMED Status")
    void shouldCorrectlyIdentifyCONFIRMEDStatus() {
        OrderStatus confirmed = OrderStatus.confirmed();

        assertFalse(confirmed.isPending());
        assertTrue(confirmed.isConfirmed());
        assertFalse(confirmed.isShipped());
        assertFalse(confirmed.isDelivered());
        assertFalse(confirmed.isCancelled());
    }

    @Test
    @DisplayName("Should Correctly Identify SHIPPED Status")
    void shouldCorrectlyIdentifySHIPPEDStatus() {
        OrderStatus shipped = OrderStatus.shipped();

        assertFalse(shipped.isPending());
        assertFalse(shipped.isConfirmed());
        assertTrue(shipped.isShipped());
        assertFalse(shipped.isDelivered());
        assertFalse(shipped.isCancelled());
    }

    @Test
    @DisplayName("Should Correctly Identify DELIVERED Status")
    void shouldCorrectlyIdentifyDELIVEREDStatus() {
        OrderStatus delivered = OrderStatus.delivered();

        assertFalse(delivered.isPending());
        assertFalse(delivered.isConfirmed());
        assertFalse(delivered.isShipped());
        assertTrue(delivered.isDelivered());
        assertFalse(delivered.isCancelled());
    }

    @Test
    @DisplayName("Should Correctly Identify CANCELLED Status")
    void shouldCorrectlyIdentifyCANCELLEDStatus() {
        OrderStatus cancelled = OrderStatus.cancelled();

        assertFalse(cancelled.isPending());
        assertFalse(cancelled.isConfirmed());
        assertFalse(cancelled.isShipped());
        assertFalse(cancelled.isDelivered());
        assertTrue(cancelled.isCancelled());
    }

    @Test
    @DisplayName("Should Identify DELIVERED As Final State")
    void shouldIdentifyDELIVEREDAsFinalState() {
        OrderStatus delivered = OrderStatus.delivered();

        assertTrue(delivered.isFinalState());
    }

    @Test
    @DisplayName("Should Identify CANCELLED As Final State")
    void shouldIdentifyCANCELLEDAsFinalState() {
        OrderStatus cancelled = OrderStatus.cancelled();

        assertTrue(cancelled.isFinalState());
    }

    @Test
    @DisplayName("Should Not Identify Non-Final States As Final")
    void shouldNotIdentifyNonFinalStatesAsFinal() {
        assertFalse(OrderStatus.pending().isFinalState());
        assertFalse(OrderStatus.confirmed().isFinalState());
        assertFalse(OrderStatus.shipped().isFinalState());
    }

    @Test
    @DisplayName("Should Support Equals And HashCode By Value")
    void shouldSupportEqualsAndHashCodeByValue() {
        OrderStatus status1 = OrderStatus.pending();
        OrderStatus status2 = OrderStatus.pending();

        assertEquals(status1, status2);
        assertEquals(status1.hashCode(), status2.hashCode());
    }

    @Test
    @DisplayName("Should Not Be Equal When Values Differ")
    void shouldNotBeEqualWhenValuesDiffer() {
        OrderStatus pending = OrderStatus.pending();
        OrderStatus confirmed = OrderStatus.confirmed();

        assertNotEquals(pending, confirmed);
    }

    @Test
    @DisplayName("Should Have A Non Null ToString")
    void shouldHaveANonNullToString() {
        OrderStatus status = OrderStatus.pending();

        assertNotNull(status.toString());
        assertFalse(status.toString().isEmpty());
        assertTrue(status.toString().contains("PENDING"));
    }
}
