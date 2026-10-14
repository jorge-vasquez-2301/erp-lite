package com.debuggeandoideas.erp_lite.domain.ports;

import com.debuggeandoideas.erp_lite.domain.order.OrderId;
import com.debuggeandoideas.erp_lite.domain.shared.Email;
import com.debuggeandoideas.erp_lite.domain.shared.Money;

/**
 * Port for email service in order created
 */
public interface OrderConfirmEmailService {

    void sendMail(
            Email email,
            OrderId orderId,
            String orderNumber,
            Money money,
            String customerName,
            Integer itemsCount
    );
}