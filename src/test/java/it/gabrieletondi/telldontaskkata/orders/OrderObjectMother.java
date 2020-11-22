package it.gabrieletondi.telldontaskkata.orders;

import it.gabrieletondi.telldontaskkata.domain.Category;
import it.gabrieletondi.telldontaskkata.domain.Product;
import it.gabrieletondi.telldontaskkata.doubles.InMemoryProductCatalog;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;

import java.math.BigDecimal;
import java.util.Arrays;

public class OrderObjectMother {
    private static Category food = new Category() {{
        setName("food");
        setTaxPercentage(new BigDecimal("10"));
    }};
    public static final Product SALAD = new Product() {{
        setName("salad");
        setPrice(new BigDecimal("3.56"));
        setCategory(food);
    }};
    public static final Product TOMATO = new Product() {{
        setName("tomato");
        setPrice(new BigDecimal("4.65"));
        setCategory(food);
    }};
    public static final ProductCatalog productCatalog = new InMemoryProductCatalog(
            Arrays.asList(SALAD,TOMATO)
    );
}
