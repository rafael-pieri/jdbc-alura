package br.com.alura.jdbc.test;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.alura.jdbc.ConnectionPool;
import br.com.alura.jdbc.dao.CategoriesDao;
import br.com.alura.jdbc.dao.ProductsDao;
import br.com.alura.jdbc.model.Category;
import br.com.alura.jdbc.model.Product;

public class ProductsDaoTest {

	Connection connection;
	CategoriesDao categoriesDao;
	
	@Before
	public void disableAutoCommitJdbc() throws SQLException {
		connection = new ConnectionPool().getConnection();
		connection.setAutoCommit(false);
		
		Category category = new Category("Eletroeletronic");
		categoriesDao = new CategoriesDao(connection);
		categoriesDao.insert(category);
	}
	
	@After
	public void rollbackChanges() throws SQLException {
		connection.rollback();
		connection.close();
	}
	
	@Test
	public void insertProduct() throws SQLException {
		List<Product> productCreated;
		
		List<Category> category = categoriesDao.list();

		Product product = new Product("TV LCD", "42 polegadas", category.get(0).getId());
		
		ProductsDao productsDao = new ProductsDao(connection);
		productsDao.insert(product);
			
		productCreated = productsDao.list();
		
		assertEquals("TV LCD", productCreated.get(0).getName());
		assertEquals("42 polegadas", productCreated.get(0).getDescription());
		assertEquals("Eletroeletronic", categoriesDao.findById(productCreated.get(0).getCategoryId()).getName());
		assertEquals(1, productCreated.size());
	}
	
	@Test
	public void updateProduct() throws SQLException {
		List<Product> productCreated;
		List<Product> productUpdated;
		
		List<Category> category = categoriesDao.list();

		Product product = new Product("TV LCD", "42 polegadas", category.get(0).getId());
		
		ProductsDao productsDao = new ProductsDao(connection);
		productsDao.insert(product);
			
		productCreated = productsDao.list();
		
		assertEquals("TV LCD", productCreated.get(0).getName());
		assertEquals("42 polegadas", productCreated.get(0).getDescription());
		assertEquals("Eletroeletronic", categoriesDao.findById(productCreated.get(0).getCategoryId()).getName());
		assertEquals(1, productCreated.size());
		
		product.setName("TV Plasma");
		product.setDescription("38 polegadas");
		productsDao.update(product);
		
		productUpdated = productsDao.list();
		
		assertEquals("TV Plasma", productUpdated.get(0).getName());
		assertEquals("38 polegadas", productUpdated.get(0).getDescription());
		assertEquals("Eletroeletronic", categoriesDao.findById(productCreated.get(0).getCategoryId()).getName());
		assertEquals(1, productUpdated.size());
	}
	
	@Test
	public void deleteProduct() throws SQLException {
		List<Product> productCreated;
		List<Product> productList;
		
		List<Category> category = categoriesDao.list();

		Product product = new Product("TV LCD", "42 polegadas", category.get(0).getId());
		
		ProductsDao productsDao = new ProductsDao(connection);
		productsDao.insert(product);
			
		productCreated = productsDao.list();
		
		assertEquals("TV LCD", productCreated.get(0).getName());
		assertEquals("42 polegadas", productCreated.get(0).getDescription());
		assertEquals("Eletroeletronic", categoriesDao.findById(productCreated.get(0).getCategoryId()).getName());
		assertEquals(1, productCreated.size());
		
		productsDao.delete(product);
		
		productList = productsDao.list();
		
		assertEquals(0, productList.size());
	}

}
