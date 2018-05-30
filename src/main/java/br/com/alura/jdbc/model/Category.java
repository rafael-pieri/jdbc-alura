package br.com.alura.jdbc.model;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private final List<Product> products = new ArrayList<>();
    private Integer id;
    private String name;

    public Category() {}

    public Category(String name) {
        this.name = name;
    }

    public void add(Product product) {
        products.add(product);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }
}