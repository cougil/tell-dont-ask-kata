package it.gabrieletondi.telldontaskkata.domain;

import it.gabrieletondi.telldontaskkata.orders.UnknownProductException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;

public class Order {

    private static final BigDecimal ZERO = new BigDecimal("0.00");

    private List<OrderItem> items = new ArrayList();
    private BigDecimal total = ZERO;
    private BigDecimal tax = ZERO;
    private String currency = "EUR";
    private OrderStatus status = OrderStatus.CREATED;
    private int id;

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

        setTotal(getTotal().add(taxedAmount));
        setTax(getTax().add(taxAmount));
    }

    private void addItem(OrderItem orderItem) {
        items.add(orderItem);
    }
}
