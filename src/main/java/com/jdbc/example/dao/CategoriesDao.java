package com.jdbc.example.dao;

import com.jdbc.example.model.Category;
import com.jdbc.example.model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoriesDao {

    private final Connection connection;

    public CategoriesDao(Connection connection) {
        this.connection = connection;
    }

    public void insert(Category category) throws SQLException {
        String sql = "INSERT INTO category (name) VALUES (?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, category.getName());
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    Integer id = resultSet.getInt(1);
                    category.setId(id);
                }
            }
        }
    }

    public void update(Category category) throws SQLException {
        String sql = "UPDATE category SET name = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category.getName());
            statement.setInt(2, category.getId());
            statement.execute();
        }
    }

    public void delete(Category category) throws SQLException {
        String sql = "DELETE FROM category WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, category.getId());
            statement.execute();
        }
    }

    public Category findById(Integer id) throws SQLException {
        Category category = new Category();

        String sql = "SELECT name FROM category WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.execute();

            try (ResultSet resultSet = statement.getResultSet()) {
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    category.setId(id);
                    category.setName(name);
                }
            }
        }

        return category;
    }

    public List<Category> list() throws SQLException {
        List<Category> categories = new ArrayList<>();

        String sql = "SELECT * FROM category";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
            transformResultInCategories(categories, statement);
        }

        return categories;
    }

    private void transformResultInCategories(List<Category> categories, PreparedStatement statement)
        throws SQLException {

        try (ResultSet resultSet = statement.getResultSet()) {
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Category category = new Category(name);
                category.setId(id);
                categories.add(category);
            }
        }
    }

    public List<Category> listByProducts() throws SQLException {
        List<Category> categories = new ArrayList<>();

        Category lastCategory = null;

        String sql = "SELECT c.id AS c_id, "
            + "       c.name AS c_name, "
            + "       P.id AS p_id, "
            + "       P.name AS p_name, "
            + "       P.description AS p_description, "
            + "       P.category_id AS p_category_id "
            + "  FROM category AS c JOIN product AS P ON P.category_id = c.id "
            + " ORDER BY c.name";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.execute();

            try (ResultSet resultSet = statement.getResultSet()) {

                while (resultSet.next()) {
                    Integer id = resultSet.getInt("c_id");
                    String name = resultSet.getString("c_name");

                    if (lastCategory == null || !lastCategory.getName().equals(name)) {
                        Category category = new Category(name);
                        category.setId(id);
                        categories.add(category);
                        lastCategory = category;
                    }

                    Integer idProduct = resultSet.getInt("p_id");
                    String nameProduct = resultSet.getString("p_name");
                    String descriptionProduct = resultSet.getString("p_description");
                    Integer categoryId = resultSet.getInt("p_category_id");
                    Product product = new Product(nameProduct, descriptionProduct, categoryId);
                    product.setId(idProduct);
                    lastCategory.add(product);
                }
            }
        }

        return categories;
    }
}