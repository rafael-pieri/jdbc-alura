package br.com.alura.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.alura.jdbc.model.Product;

public class ProductsDao {

	private Connection con;

	public ProductsDao(Connection con) {
		this.con = con;
	}

	public void insert(Product product) throws SQLException {
		String sql = "insert into Product (name, description, category_id) values (?, ?, ?)";

		try (PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, product.getName());
			statement.setString(2, product.getDescription());
			statement.setInt(3, product.getCategoryId());
			statement.execute();


		}
	}

	public void update(Product product) throws SQLException {
		String sql = "update Product set name = ? , description = ? where id = ?";

		try (PreparedStatement statement = con.prepareStatement(sql)) {
			statement.setString(1, product.getName());
			statement.setString(2, product.getDescription());
			statement.setInt(3, product.getId());
			statement.execute();
		}
	}
	
	public void delete(Product product) throws SQLException {
		String sql = "delete from Product where id = ?";

		try (PreparedStatement statement = con.prepareStatement(sql)) {
			statement.setInt(1, product.getId());
			statement.execute();
		}
	}

	public List<Product> list() throws SQLException {
		List<Product> products = new ArrayList<>();

		String sql = "select * from Product";

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
