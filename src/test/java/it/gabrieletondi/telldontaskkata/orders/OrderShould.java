package it.gabrieletondi.telldontaskkata.orders;

import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static it.gabrieletondi.telldontaskkata.orders.OrderStatus.*;
import static java.math.RoundingMode.HALF_UP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class OrderShould {

    private static final BigDecimal ZERO = new BigDecimal("0.00");
    public static final int QUANTITY = 5;
    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
    }

    @Test
    void initialized_properly() {
        assertThat(order.getStatus()).isEqualTo(CREATED);
        assertThat(order.getItems()).isEmpty();
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

    @Test
    void change_its_status_to_approved() {
        order.approved(true);
        assertThat(order.getStatus()).isEqualTo(APPROVED);
    }

    @Test
    void change_its_status_to_rejected_when_not_approved() {
        order.approved(false);
        assertThat(order.getStatus()).isEqualTo(REJECTED);
    }

    @Test
    void change_its_status_to_shipped() {
        order.shipped();
        assertThat(order.getStatus()).isEqualTo(SHIPPED);
    }
}
