package com.jdbc.example.dao;

import com.jdbc.example.model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductsDao {

    private Connection con;

    public ProductsDao(Connection con) {
        this.con = con;
    }

    public void insert(Product product) throws SQLException {
        String sql = "INSERT INTO product (name, description, category_id) VALUES (?, ?, ?)";

        try (PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setInt(3, product.getCategoryId());
            statement.execute();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    Integer id = resultSet.getInt(1);
                    product.setId(id);
                }
            }
        }
    }

    public void update(Product product) throws SQLException {
        String sql = "UPDATE product SET name = ? , description = ? WHERE id = ?";

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setInt(3, product.getId());
            statement.execute();
        }
    }

    public void delete(Product product) throws SQLException {
        String sql = "DELETE FROM product WHERE id = ?";

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, product.getId());
            statement.execute();
        }
    }

    public List<Product> list() throws SQLException {
        List<Product> products = new ArrayList<>();

        String sql = "SELECT * FROM product";

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.execute();
            transformResultInProducts(statement, products);
        }

        return products;
    }

    private void transformResultInProducts(PreparedStatement statement, List<Product> products) throws SQLException {
        try (ResultSet resultSet = statement.getResultSet()) {
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                Integer categoryId = resultSet.getInt("category_id");
                Product product = new Product(name, description, categoryId);
                product.setId(id);
                products.add(product);
            }
        }
    }
}