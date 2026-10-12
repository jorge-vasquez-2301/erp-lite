package com.debuggeandoideas.erp_lite.persistence.mongo.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "audit_logs")
public class AuditLogDocument {

    @Id
    private ObjectId id;

    private String className;

    private String endpoint;

    private String errorMessage;

    private long executionTimeMs;

    private String ipAddress;

    private String methodName;

    private boolean success;

    private Instant timestamp;

    private String userId;
}