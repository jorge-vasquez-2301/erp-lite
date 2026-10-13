package com.debuggeandoideas.erp_lite.domain.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class for all Aggregate Roots.
 * Aggregate Roots are entities that serve as the entry point to an aggregate.
 * They are responsible for maintaining the consistency of the aggregate and publishing domain events.
 *
 * @param <ID> the type of the aggregate root identifier
 */
public abstract class AggregateRoot<ID> extends Entity<ID> {

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    protected AggregateRoot(ID id) {
        super(id);
    }

    /**
     * Registers a domain event to be published.
     *
     * @param event the domain event to register
     */
    protected void registerEvent(DomainEvent event) {
        if (event != null) {
            this.domainEvents.add(event);
        }
    }

    /**
     * Returns all domain events and clears the internal list.
     * This method should be called by the infrastructure layer after persisting the aggregate.
     *
     * @return an unmodifiable list of domain events
     */
    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    /**
     * Clears all domain events.
     * This method should be called by the infrastructure layer after publishing events.
     */
    public void clearDomainEvents() {
        this.domainEvents.clear();
    }
}
