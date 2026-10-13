package com.debuggeandoideas.erp_lite.domain.shared;

import java.util.regex.Pattern;

/**
 * Email address for notifications.
 * Validates email format using a basic regex pattern.
 *
 * @param value the email address
 */
public record Email(String value) {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    public Email {
        if (value == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + value);
        }
    }

    /**
     * Creates an Email from a String value.
     *
     * @param value the email address
     * @return a new Email instance
     */
    public static Email of(String value) {
        return new Email(value);
    }
}
