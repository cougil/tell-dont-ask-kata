package it.gabrieletondi.telldontaskkata.orders;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository;
import org.junit.jupiter.api.Test;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class OrderApprovalUseCaseShould {
    private final TestOrderRepository orderRepository = new TestOrderRepository();
    private final OrderApprovalUseCase useCase = new OrderApprovalUseCase(orderRepository);

    @Test
    public void approvedExistingOrder() {
        Order initialOrder = new Order(1, CREATED);
        orderRepository.addOrder(initialOrder);

        OrderApprovalRequest request = new OrderApprovalRequest();
        request.setOrderId(1);
        request.setApproved(true);

        useCase.run(request);

        final Order savedOrder = orderRepository.getSavedOrder();
        assertThat(savedOrder.getStatus()).isEqualTo(OrderStatus.APPROVED);
    }

    @Test
    public void rejectedExistingOrder() {
        Order initialOrder = new Order(1, CREATED);
        orderRepository.addOrder(initialOrder);

        OrderApprovalRequest request = new OrderApprovalRequest();
        request.setOrderId(1);
        request.setApproved(false);

        useCase.run(request);

        final Order savedOrder = orderRepository.getSavedOrder();
        assertThat(savedOrder.getStatus()).isEqualTo(OrderStatus.REJECTED);
    }

    @Test
    public void cannotApproveRejectedOrder() {
        Order initialOrder = new Order(1, REJECTED);
        orderRepository.addOrder(initialOrder);

        OrderApprovalRequest request = new OrderApprovalRequest();
        request.setOrderId(1);
        request.setApproved(true);

        assertThatExceptionOfType(RejectedOrderCannotBeApprovedException.class)
                .isThrownBy(() -> useCase.run(request) );

        assertThat(orderRepository.getSavedOrder()).isNull();
    }

    @Test
    public void cannotRejectApprovedOrder() {
        Order initialOrder = new Order(1, APPROVED);
        orderRepository.addOrder(initialOrder);

        OrderApprovalRequest request = new OrderApprovalRequest();
        request.setOrderId(1);
        request.setApproved(false);

        assertThatExceptionOfType(ApprovedOrderCannotBeRejectedException.class)
                .isThrownBy(() -> useCase.run(request));

        assertThat(orderRepository.getSavedOrder()).isNull();
    }

    @Test
    public void shippedOrdersCannotBeApproved() {
        Order initialOrder = new Order(1, SHIPPED);
        orderRepository.addOrder(initialOrder);

        OrderApprovalRequest request = new OrderApprovalRequest();
        request.setOrderId(1);
        request.setApproved(true);

        assertThatExceptionOfType(ShippedOrdersCannotBeChangedException.class)
                .isThrownBy(() -> useCase.run(request) );

        assertThat(orderRepository.getSavedOrder()).isNull();
    }

    @Test
    public void shippedOrdersCannotBeRejected() {
        Order initialOrder = new Order(1, SHIPPED);
        orderRepository.addOrder(initialOrder);

        OrderApprovalRequest request = new OrderApprovalRequest();
        request.setOrderId(1);
        request.setApproved(false);

        assertThatExceptionOfType(ShippedOrdersCannotBeChangedException.class)
                .isThrownBy(() -> useCase.run(request));

        assertThat(orderRepository.getSavedOrder()).isNull();
    }
}
