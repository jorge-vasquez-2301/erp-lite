package com.debuggeandoideas.erp_lite;

import com.debuggeandoideas.erp_lite.domain.order.OrderId;
import com.debuggeandoideas.erp_lite.domain.shared.Email;
import com.debuggeandoideas.erp_lite.domain.shared.Money;
import com.debuggeandoideas.erp_lite.persistence.mail.adapters.GmailAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Currency;

@SpringBootApplication
public class ErpLiteApplication implements CommandLineRunner {
    @Autowired
    GmailAdapter gmailAdapter;

    @Override
    public void run(String... args) throws Exception {
        gmailAdapter.sendMail(
                Email.of("jv2301@gmail.com"),
                OrderId.generate(),
                "100",
                Money.of(100.0, Currency.getInstance("USD")),
                "Test customer",
                10
        );
    }

    static void main(String[] args) {
        SpringApplication.run(ErpLiteApplication.class, args);
    }

}
