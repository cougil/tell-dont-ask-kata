package it.gabrieletondi.telldontaskkata.orders;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static it.gabrieletondi.telldontaskkata.orders.OrderObjectMother.SALAD;
import static it.gabrieletondi.telldontaskkata.orders.OrderObjectMother.TOMATO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class OrderCreationUseCaseShould {
    private final TestOrderRepository orderRepository = new TestOrderRepository();
    private final OrderCreationUseCase useCase = new OrderCreationUseCase(orderRepository, OrderObjectMother.productCatalog);

    @Test
    public void sellMultipleItems() {
        SellItemRequest saladRequest = new SellItemRequest();
        saladRequest.setProductName("salad");
        saladRequest.setQuantity(2);

        SellItemRequest tomatoRequest = new SellItemRequest();
        tomatoRequest.setProductName("tomato");
        tomatoRequest.setQuantity(3);

        final SellItemsRequest request = new SellItemsRequest();
        request.setRequests(new ArrayList<>());
        request.getRequests().add(saladRequest);
        request.getRequests().add(tomatoRequest);

        useCase.run(request);

        final Order insertedOrder = orderRepository.getSavedOrder();
        Order expectedOrder = new Order();
        expectedOrder.add(2, SALAD);
        expectedOrder.add(3, TOMATO);
        assertThat(insertedOrder).isEqualTo(expectedOrder);

        final OrderItem saladItem = insertedOrder.getItems().get(0);
        OrderItem expectedSaladItem = new OrderItem(2, SALAD, new BigDecimal("0.72"), new BigDecimal("7.84"));
        assertThat(saladItem).isEqualTo(expectedSaladItem);

        final OrderItem tomatoItem = insertedOrder.getItems().get(1);
        OrderItem expectedTomatoItem = new OrderItem(3, TOMATO, new BigDecimal("1.41"), new BigDecimal("15.36"));
        assertThat(tomatoItem).isEqualTo(expectedTomatoItem);
    }

    @Test
    public void unknownProduct() {
        SellItemsRequest request = new SellItemsRequest();
        request.setRequests(new ArrayList<>());
        SellItemRequest unknownProductRequest = new SellItemRequest();
        unknownProductRequest.setProductName("unknown product");
        request.getRequests().add(unknownProductRequest);

        assertThatExceptionOfType(UnknownProductException.class)
                .isThrownBy( () -> useCase.run(request));
    }
}
