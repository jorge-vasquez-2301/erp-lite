package com.debuggeandoideas.erp_lite.shared;

import com.debuggeandoideas.erp_lite.domain.shared.Email;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Email Domain Test")
class EmailTest {

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Email Is Null")
    void shouldThrowIllegalArgumentExceptionWhenEmailIsNull() {
        final String msgEx = "Email cannot be null";

        IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                () -> new Email(null));

        assertEquals(msgEx, targetEx.getMessage());
    }

    @Test
    @DisplayName("Should Throw IllegalArgumentException When Email Format Is Invalid")
    void shouldThrowIllegalArgumentExceptionWhenEmailFormatIsInvalid() {
        String[] invalidEmails = {
                "invalid",
                "invalid@",
                "@invalid.com",
                "invalid@.com",
                "invalid@domain",
                "invalid @domain.com",
                "invalid@domain .com",
                "",
                "test@",
                "@test.com"
        };

        for (String invalidEmail : invalidEmails) {
            IllegalArgumentException targetEx = assertThrows(IllegalArgumentException.class,
                    () -> new Email(invalidEmail),
                    "Should throw exception for: " + invalidEmail);

            assertTrue(targetEx.getMessage().contains("Invalid email format"),
                    "Exception message should contain 'Invalid email format' for: " + invalidEmail);
        }
    }

    @Test
    @DisplayName("Should Create Email With Valid Format")
    void shouldCreateEmailWithValidFormat() {
        String validEmail = "test@example.com";

        Email email = new Email(validEmail);

        assertEquals(validEmail, email.value());
    }

    @Test
    @DisplayName("Should Create Email Using of Method")
    void shouldCreateEmailUsingOfMethod() {
        String validEmail = "user@domain.com";

        Email email = Email.of(validEmail);

        assertEquals(validEmail, email.value());
    }

    @Test
    @DisplayName("Should Accept Valid Email Formats")
    void shouldAcceptValidEmailFormats() {
        String[] validEmails = {
                "simple@example.com",
                "user.name@example.com",
                "user+tag@example.co.uk",
                "user_123@test-domain.com",
                "test123@subdomain.example.com",
                "a@b.co"
        };

        for (String validEmail : validEmails) {
            Email email = assertDoesNotThrow(() -> new Email(validEmail),
                    "Should not throw exception for: " + validEmail);

            assertEquals(validEmail, email.value());
        }
    }

    @Test
    @DisplayName("Should Support Equals And HashCode By Value")
    void shouldSupportEqualsAndHashCodeByValue() {
        Email email1 = new Email("test@example.com");
        Email email2 = new Email("test@example.com");

        assertEquals(email1, email2);
        assertEquals(email1.hashCode(), email2.hashCode());
    }

    @Test
    @DisplayName("Should Not Be Equal When Values Differ")
    void shouldNotBeEqualWhenValuesDiffer() {
        Email email1 = new Email("test1@example.com");
        Email email2 = new Email("test2@example.com");

        assertNotEquals(email1, email2);
    }

    @Test
    @DisplayName("Should Have A Non Null ToString")
    void shouldHaveANonNullToString() {
        Email email = new Email("test@example.com");

        assertNotNull(email.toString());
        assertFalse(email.toString().isEmpty());
    }
}
