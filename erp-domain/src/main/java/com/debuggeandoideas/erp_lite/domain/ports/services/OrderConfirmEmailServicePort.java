package com.debuggeandoideas.erp_lite.domain.ports.services;

import com.debuggeandoideas.erp_lite.domain.entities.order.OrderId;
import com.debuggeandoideas.erp_lite.domain.shared.Email;
import com.debuggeandoideas.erp_lite.domain.shared.Money;

/**
 * Port for email service in order created
 */
public interface OrderConfirmEmailServicePort {

    void sendMail(
            Email email,
            OrderId orderId,
            String orderNumber,
            Money money,
            String customerName,
            Integer itemsCount
    );
}