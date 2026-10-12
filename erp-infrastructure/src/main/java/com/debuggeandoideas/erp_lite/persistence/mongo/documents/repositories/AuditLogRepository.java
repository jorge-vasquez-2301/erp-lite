package com.debuggeandoideas.erp_lite.persistence.mongo.documents.repositories;

import com.debuggeandoideas.erp_lite.persistence.mongo.documents.AuditLogDocument;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuditLogRepository extends MongoRepository<AuditLogDocument, ObjectId> {
}