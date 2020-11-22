package it.gabrieletondi.telldontaskkata.domain;

import it.gabrieletondi.telldontaskkata.orders.OrderObjectMother;
import it.gabrieletondi.telldontaskkata.orders.UnknownProductException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.CREATED;
import static java.math.RoundingMode.HALF_UP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class OrderShould {

    private static final BigDecimal ZERO = new BigDecimal("0.00");
    private Order order;
    public static final int QUANTITY = 5;

    @BeforeEach
    void setUp() {
        order = new Order();
    }

    @Test
    void initialized_properly() {
        assertThat(order.getStatus()).isEqualTo(CREATED);
        assertThat(order.getItems()).isEmpty();
        assertThat(order.getCurrency()).isEqualTo("EUR");
        assertThat(order.getTotal()).isEqualTo(ZERO);
        assertThat(order.getTax()).isEqualTo(ZERO);
    }

    @Test
    void fail_when_adding_an_unknown_product() {
        Product unknownProduct = null;
        assertThatExceptionOfType(UnknownProductException.class)
                .isThrownBy( () -> order.add(10, unknownProduct) );
    }

    @Test
    void add_an_order_item_calculating_its_total_and_tax_when_adding_a_valid_product() {
        final Product validProduct = OrderObjectMother.TOMATO;
        order.add(QUANTITY, validProduct);

        final List<OrderItem> items = order.getItems();
        assertThat(items).hasSize(1);

        final OrderItem orderItem = items.get(0);
        final BigDecimal taxedAmount = new BigDecimal(25.60).setScale(2, HALF_UP);
        final BigDecimal tax = new BigDecimal(2.35).setScale(2, HALF_UP);
        final OrderItem expectedOrderItem = new OrderItem(QUANTITY, validProduct, tax, taxedAmount);

        assertThat(orderItem).isEqualTo(expectedOrderItem);

        assertThat(order.getTotal()).isEqualTo(taxedAmount);
        assertThat(order.getTax()).isEqualTo(tax);
    }

    @Test
    void identify_if_is_equal_to_another() {
        final Product validProduct = OrderObjectMother.TOMATO;
        order.add(QUANTITY, validProduct);

        Order expectedOrder = new Order();
        expectedOrder.add(QUANTITY, validProduct);
        assertThat(order).isEqualTo(expectedOrder);
    }
}
