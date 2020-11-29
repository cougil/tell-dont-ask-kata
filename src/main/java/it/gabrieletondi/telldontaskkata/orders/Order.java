package it.gabrieletondi.telldontaskkata.orders;

import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.domain.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import static it.gabrieletondi.telldontaskkata.orders.OrderStatus.*;
import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;

public class Order {

    private static final BigDecimal ZERO = new BigDecimal("0.00");

    private final List<OrderItem> items = new ArrayList();
    private BigDecimal total = ZERO;
    private BigDecimal tax = ZERO;
    private OrderStatus status = OrderStatus.CREATED;
    private int id;

    Order(int id, OrderStatus status) {
        this.id = id;
        this.status = status;
    }

    Order() {
    }

    BigDecimal getTotal() {
        return total;
    }

    BigDecimal getTax() {
        return tax;
    }

    List<OrderItem> getItems() {
        return items;
    }

    OrderStatus getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public void add(int quantity, Product product) {
        if (product == null) {
            throw new UnknownProductException();
        }

        final BigDecimal unitaryTax = product.getPrice().divide(valueOf(100)).multiply(product.getCategory().getTaxPercentage()).setScale(2, HALF_UP);
        final BigDecimal unitaryTaxedAmount = product.getPrice().add(unitaryTax).setScale(2, HALF_UP);
        final BigDecimal taxedAmount = unitaryTaxedAmount.multiply(valueOf(quantity)).setScale(2, HALF_UP);
        final BigDecimal taxAmount = unitaryTax.multiply(valueOf(quantity));

        addItem( new OrderItem(quantity, product, taxAmount, taxedAmount) );

        this.total = getTotal().add(taxedAmount);
        this.tax = getTax().add(taxAmount);
    }

    private void addItem(OrderItem orderItem) {
        items.add(orderItem);
    }

    void approved(boolean approved) {
        if (approved) this.status = APPROVED;
        else this.status = REJECTED;
    }

    public void shipped() {
        this.status = SHIPPED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id &&
                Objects.equals(items, order.items) &&
                Objects.equals(total, order.total) &&
                Objects.equals(tax, order.tax) &&
                status == order.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(items, total, tax, status, id);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Order.class.getSimpleName() + "[", "]")
                .add("items=" + items)
                .add("total=" + total)
                .add("tax=" + tax)
                .add("status=" + status)
                .add("id=" + id)
                .toString();
    }
}
