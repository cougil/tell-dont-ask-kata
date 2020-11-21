package it.gabrieletondi.telldontaskkata.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.CREATED;
import static org.assertj.core.api.Assertions.assertThat;

class OrderShould {

    private static final BigDecimal ZERO = new BigDecimal("0.00");

    @Test
    void initialized_properly() {
        Order order = new Order();
        assertThat(order.getStatus()).isEqualTo(CREATED);
        assertThat(order.getItems()).isEmpty();
        assertThat(order.getCurrency()).isEqualTo("EUR");
        assertThat(order.getTotal()).isEqualTo(ZERO);
        assertThat(order.getTax()).isEqualTo(ZERO);
    }
}
