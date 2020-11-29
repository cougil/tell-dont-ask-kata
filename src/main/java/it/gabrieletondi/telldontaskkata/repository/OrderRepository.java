package it.gabrieletondi.telldontaskkata.repository;

import it.gabrieletondi.telldontaskkata.orders.Order;

public interface OrderRepository {
    void save(Order order);

    Order getById(int orderId);
}
