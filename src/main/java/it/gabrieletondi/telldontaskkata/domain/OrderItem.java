package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

public class OrderItem {
    private final Product product;
    private final int quantity;
    private final BigDecimal taxedAmount;
    private final BigDecimal tax;

    public OrderItem(int quantity, Product product, BigDecimal tax, BigDecimal taxedAmount) {
        this.quantity = quantity;
        this.product = product;
        this.tax = tax;
        this.taxedAmount = taxedAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return quantity == orderItem.quantity &&
                Objects.equals(product, orderItem.product) &&
                Objects.equals(taxedAmount, orderItem.taxedAmount) &&
                Objects.equals(tax, orderItem.tax);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, quantity, taxedAmount, tax);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", OrderItem.class.getSimpleName() + "[", "]")
                .add("product=" + product)
                .add("quantity=" + quantity)
                .add("taxedAmount=" + taxedAmount)
                .add("tax=" + tax)
                .toString();
    }
}
