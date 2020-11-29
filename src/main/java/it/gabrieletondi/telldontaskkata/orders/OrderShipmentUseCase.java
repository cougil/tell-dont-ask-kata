package it.gabrieletondi.telldontaskkata.orders;

import it.gabrieletondi.telldontaskkata.repository.OrderRepository;
import it.gabrieletondi.telldontaskkata.service.ShipmentService;

public class OrderShipmentUseCase {
    private final OrderRepository orderRepository;
    private final ShipmentService shipmentService;

    public OrderShipmentUseCase(OrderRepository orderRepository, ShipmentService shipmentService) {
        this.orderRepository = orderRepository;
        this.shipmentService = shipmentService;
    }

    public void run(OrderShipmentRequest request) {
        final Order order = orderRepository.getById(request.getOrderId());

        if (order.isCreated() || order.isRejected()) {
            throw new OrderCannotBeShippedException();
        }

        if (order.isShipped()) {
            throw new OrderCannotBeShippedTwiceException();
        }

        shipmentService.ship(order);

        orderRepository.save(order);
    }

}
