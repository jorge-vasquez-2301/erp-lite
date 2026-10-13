package com.debuggeandoideas.erp_lite.domain.shared;

import java.time.Instant;

/**
 * Audit information for aggregates.
 * Tracks who created/updated the entity and when.
 *
 * @param createdBy  the username who created the entity
 * @param createdAt  the timestamp when the entity was created
 * @param updatedAt  the timestamp when the entity was last updated
 */
public record AuditInfo(
        String createdBy,
        Instant createdAt,
        Instant updatedAt
) {

    public AuditInfo {
        if (createdBy == null || createdBy.isBlank()) {
            throw new IllegalArgumentException("Created by cannot be null or blank");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("Created at cannot be null");
        }
        if (updatedAt == null) {
            throw new IllegalArgumentException("Updated at cannot be null");
        }
        if (updatedAt.isBefore(createdAt)) {
            throw new IllegalArgumentException("Updated at cannot be before created at");
        }
    }

    /**
     * Creates a new AuditInfo with the current timestamp for both creation and update.
     *
     * @param createdBy the username who created the entity
     * @param timestamp the timestamp for creation and update
     * @return a new AuditInfo instance
     */
    public static AuditInfo create(String createdBy, Instant timestamp) {
        return new AuditInfo(createdBy, timestamp, timestamp);
    }

    /**
     * Updates the timestamp to the current time.
     *
     * @return a new AuditInfo instance with updated timestamp
     */
    public AuditInfo updateTimestamp() {
        return new AuditInfo(this.createdBy, this.createdAt, Instant.now());
    }
}
