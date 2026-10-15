package com.debuggeandoideas.erp_lite.persistence.jpa.adapters;

import com.debuggeandoideas.erp_lite.domain.entities.order.OrderId;
import com.debuggeandoideas.erp_lite.domain.entities.order.OrderItem;
import com.debuggeandoideas.erp_lite.domain.entities.order.OrderNumber;
import com.debuggeandoideas.erp_lite.domain.entities.order.OrderRoot;
import com.debuggeandoideas.erp_lite.domain.ports.repositories.OrderRepositoryPort;
import com.debuggeandoideas.erp_lite.domain.shared.CustomerId;
import com.debuggeandoideas.erp_lite.persistence.jpa.entities.OrderEntity;
import com.debuggeandoideas.erp_lite.persistence.jpa.entities.OrderProductEntity;
import com.debuggeandoideas.erp_lite.persistence.jpa.mappers.OrderJpaMapper;
import com.debuggeandoideas.erp_lite.persistence.jpa.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

@Repository
@Slf4j
@AllArgsConstructor
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final OrderRepository orderRepository;
    private OrderJpaMapper orderJpaMapper;

    @Override
    public OrderRoot save(OrderRoot order) {
        log.debug("Saving order {}", order);

        try {
            OrderEntity orderEntity = this.orderJpaMapper.toEntity(order);

            orderEntity.setId(order.getId().value());

            this.zipOrderItemsIds(order, orderEntity);

            OrderEntity savedOrderEntity = this.orderRepository.save(orderEntity);

            log.debug("Saved order success {}", savedOrderEntity);

            return this.orderJpaMapper.toDomain(savedOrderEntity);
        } catch (Exception e) {
            log.error("Error on persist order", e);
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Optional<OrderRoot> findById(OrderId id) {
        log.debug("Finding all orders by id {}", id);
        try {
            Optional<OrderEntity> entityOpt = this.orderRepository.findById(id.value());

            if (entityOpt.isEmpty()) {
                log.debug("No order found with id {}", id);
                return Optional.empty();
            }

            OrderEntity orderEntity = entityOpt.get();

            return Optional.of(this.orderJpaMapper.toDomain(orderEntity));
        } catch (Exception e) {
            log.error("Error on findAllById order", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<OrderRoot> findAllByOrderNumber(OrderNumber orderNumber) {
        log.debug("Finding order by number: {}", orderNumber.value());

        try {
            Optional<OrderEntity> entityOpt = this.orderRepository.findByOrderNumber(orderNumber.value());

            if (entityOpt.isEmpty()) {
                log.debug("Order not found with number: {}", orderNumber.value());
                return Optional.empty();
            }

            OrderEntity entity = entityOpt.get();
            log.debug("Order found with number: {} (ID: {})",
                    entity.getOrderNumber(),
                    entity.getId());

            OrderRoot domain = this.orderJpaMapper.toDomain(entity);

            return Optional.of(domain);

        } catch (Exception ex) {
            log.error("Failed to find order by number: {}", orderNumber.value(), ex);
            return Optional.empty();
        }
    }

    @Override
    public List<OrderRoot> findByCustomerId(CustomerId customerId) {
        log.debug("Finding orders by customer ID: {}", customerId.value());

        try {
            List<OrderEntity> entities = this.orderRepository.findAllByCustomerId(customerId.value());

            log.debug("Found {} orders for customer ID: {}", entities.size(), customerId.value());
            List<OrderRoot> orders = entities.stream()
                    .map(entity -> {
                        try {
                            return this.orderJpaMapper.toDomain(entity);
                        } catch (Exception ex) {
                            log.error("Failed to map order entity to domain: {}", entity.getId(), ex);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList();

            log.debug("Successfully rehydrated {} orders", orders.size());

            return orders;

        } catch (Exception ex) {
            log.error("Failed to find orders by customer ID: {}", customerId.value(), ex);
            return List.of();
        }
    }

    @Override
    public void delete(OrderRoot order) {
        log.info("Deleting order {}", order);

        try {
            this.orderRepository.deleteById(order.getId().value());
            log.info("Order deleted successfully");
        } catch (Exception e) {
            log.error("Failed to delete order with ID: {}", order.getId().value(), e);
            throw new IllegalStateException(e);
        }
    }

    private void zipOrderItemsIds(OrderRoot root, OrderEntity entity) {
        if (root.getItems().isEmpty() || root.getItems() == null) {
            log.debug("Items EMPTY");
            return;
        }

        List<OrderProductEntity> itemsEntity = entity.getItems();
        List<OrderItem> itemsDomain = root.getItems();

        IntStream.range(0, itemsDomain.size()).forEach(i ->
                itemsEntity.get(i).setId(itemsDomain.get(i).getId().value())
        );
    }
}