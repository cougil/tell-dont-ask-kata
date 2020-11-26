package it.gabrieletondi.telldontaskkata.orders;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository;
import it.gabrieletondi.telldontaskkata.doubles.TestShipmentService;
import org.junit.jupiter.api.Test;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class OrderShipmentUseCaseShould {
    private final TestOrderRepository orderRepository = new TestOrderRepository();
    private final TestShipmentService shipmentService = new TestShipmentService();
    private final OrderShipmentUseCase useCase = new OrderShipmentUseCase(orderRepository, shipmentService);

    @Test
    public void shipApprovedOrder() {
        Order initialOrder = new Order(1, APPROVED);
        orderRepository.addOrder(initialOrder);

        OrderShipmentRequest request = new OrderShipmentRequest();
        request.setOrderId(1);

        useCase.run(request);

        assertThat(orderRepository.getSavedOrder().getStatus()).isEqualTo(OrderStatus.SHIPPED);
        assertThat(shipmentService.getShippedOrder()).isEqualTo(initialOrder);
    }

    @Test
    public void createdOrdersCannotBeShipped() {
        Order initialOrder = new Order(1, CREATED);
        orderRepository.addOrder(initialOrder);

        OrderShipmentRequest request = new OrderShipmentRequest();
        request.setOrderId(1);

        assertThatExceptionOfType(OrderCannotBeShippedException.class)
                .isThrownBy( ()-> useCase.run(request) );

        assertThat(orderRepository.getSavedOrder()).isNull();
        assertThat(shipmentService.getShippedOrder()).isNull();
    }

    @Test
    public void rejectedOrdersCannotBeShipped() {
        Order initialOrder = new Order(1, REJECTED);
        orderRepository.addOrder(initialOrder);

        OrderShipmentRequest request = new OrderShipmentRequest();
        request.setOrderId(1);

        assertThatExceptionOfType(OrderCannotBeShippedException.class)
                .isThrownBy( ()-> useCase.run(request) );

        assertThat(orderRepository.getSavedOrder()).isNull();
        assertThat(shipmentService.getShippedOrder()).isNull();
    }

    @Test
    public void shippedOrdersCannotBeShippedAgain() {
        Order initialOrder = new Order(1, SHIPPED);
        orderRepository.addOrder(initialOrder);

        OrderShipmentRequest request = new OrderShipmentRequest();
        request.setOrderId(1);

        assertThatExceptionOfType(OrderCannotBeShippedTwiceException.class)
                .isThrownBy( ()-> useCase.run(request) );

        assertThat(orderRepository.getSavedOrder()).isNull();
        assertThat(shipmentService.getShippedOrder()).isNull();
    }
}
