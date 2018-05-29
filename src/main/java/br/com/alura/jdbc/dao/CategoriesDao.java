package br.com.alura.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.alura.jdbc.model.Category;
import br.com.alura.jdbc.model.Product;

public class CategoriesDao {

	private final Connection con;

	public CategoriesDao(Connection con) {
		this.con = con;
	}

	public void insert(Category category) throws SQLException {
		String sql = "insert into category (name) values (?)";

		try (PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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
		String sql = "update category set name = ? where id = ?";

		try (PreparedStatement statement = con.prepareStatement(sql)) {
			statement.setString(1, category.getName());
			statement.setInt(2, category.getId());
			statement.execute();
		}
	}

	public void delete(Category category) throws SQLException {
		String sql = "delete from category where id = ?";

		try (PreparedStatement statement = con.prepareStatement(sql)) {
			statement.setInt(1, category.getId());
			statement.execute();
		}
	}
	
	public Category findById(Integer id) throws SQLException {
		Category category = new Category();
		
		String sql = "select name from category where id = ?";

		try (PreparedStatement statement = con.prepareStatement(sql)) {
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

		String sql = "select * from category";

		try (PreparedStatement statement = con.prepareStatement(sql)) {
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

		String sql = "select c.id as c_id, "
				   + "       c.name as c_name, "
				   + "       p.id as p_id, "
				   + "       p.name as p_name, "
				   + "       p.description as p_description, "
				   + "       p.category_id as p_category_id "
				   + "  from category as c join product as p on p.category_id = c.id "
				   + " order by c.name";

		try (PreparedStatement statement = con.prepareStatement(sql)) {

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