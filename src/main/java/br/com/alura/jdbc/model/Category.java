package br.com.alura.jdbc.model;

import java.util.ArrayList;
import java.util.List;

public class Category {

	private Integer id;
	private String name;
	private final List<Product> products = new ArrayList<>();

	public Category(String name) {
		this.name = name;
	}
	
	public Category() {

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
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public List<Product> getProducts() {
		return products;
	}

}
