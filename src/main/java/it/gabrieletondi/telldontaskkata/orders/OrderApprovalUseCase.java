package it.gabrieletondi.telldontaskkata.orders;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.*;

public class OrderApprovalUseCase {
    private final OrderRepository orderRepository;

    public OrderApprovalUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void run(OrderApprovalRequest request) {
        final Order order = orderRepository.getById(request.getOrderId());

        if (SHIPPED.equals(order.getStatus())) {
            throw new ShippedOrdersCannotBeChangedException();
        }

        if (request.isApproved() && REJECTED.equals(order.getStatus())) {
            throw new RejectedOrderCannotBeApprovedException();
        }

        if (!request.isApproved() && APPROVED.equals(order.getStatus())) {
            throw new ApprovedOrderCannotBeRejectedException();
        }

        order.approved(request.isApproved());
        orderRepository.save(order);
    }
}
