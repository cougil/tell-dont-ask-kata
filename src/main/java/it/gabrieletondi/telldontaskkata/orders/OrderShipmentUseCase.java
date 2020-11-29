package it.gabrieletondi.telldontaskkata.orders;

import it.gabrieletondi.telldontaskkata.repository.OrderRepository;
import it.gabrieletondi.telldontaskkata.service.ShipmentService;

import static it.gabrieletondi.telldontaskkata.orders.OrderStatus.*;

public class OrderShipmentUseCase {
    private final OrderRepository orderRepository;
    private final ShipmentService shipmentService;

    public OrderShipmentUseCase(OrderRepository orderRepository, ShipmentService shipmentService) {
        this.orderRepository = orderRepository;
        this.shipmentService = shipmentService;
    }

    public void run(OrderShipmentRequest request) {
        final Order order = orderRepository.getById(request.getOrderId());

        if (CREATED.equals(order.getStatus()) || REJECTED.equals(order.getStatus())) {
            throw new OrderCannotBeShippedException();
        }

        if (SHIPPED.equals(order.getStatus())) {
            throw new OrderCannotBeShippedTwiceException();
        }

        shipmentService.ship(order);

        orderRepository.save(order);
    }
}
