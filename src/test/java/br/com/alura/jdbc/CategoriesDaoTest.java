package br.com.alura.jdbc.test;

import static org.junit.Assert.assertEquals;

import br.com.alura.jdbc.ConnectionPool;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.alura.jdbc.dao.CategoriesDao;
import br.com.alura.jdbc.dao.ProductsDao;
import br.com.alura.jdbc.model.Category;
import br.com.alura.jdbc.model.Product;

public class CategoriesDaoTest {
	
	private Connection connection;
	
	@Before
	public void disableAutoCommitJdbc() throws SQLException {
		connection = new ConnectionPool().getConnection();
		connection.setAutoCommit(false);
	}
	
	@After
	public void rollbackChanges() throws SQLException {
		connection.rollback();
		connection.close();
	}
	
	@Test
	public void insertCategory() throws SQLException {
		List<Category> categoryCreated;
		
		Category category = new Category("Electronic");
		
		CategoriesDao categoriesDao = new CategoriesDao(connection);
		categoriesDao.insert(category);
			
		categoryCreated = categoriesDao.list();
		
		assertEquals("Electronic", categoryCreated.get(0).getName());
		assertEquals(1, categoryCreated.size());
	}
	
	@Test
	public void updateCategory() throws SQLException {
		List<Category> categoryCreated;
		List<Category> categoryUpdated;
		
		Category category = new Category("Home Appliances");
		
		CategoriesDao categoriesDao = new CategoriesDao(connection);
		categoriesDao.insert(category);
			
		categoryCreated = categoriesDao.list();
		
		assertEquals("Home Appliances", categoryCreated.get(0).getName());
		assertEquals(1, categoryCreated.size());
		
		category.setName("Electronic");
		categoriesDao.update(category);
		
		categoryUpdated = categoriesDao.list();
		
		assertEquals("Electronic", categoryUpdated.get(0).getName());
		assertEquals(1, categoryUpdated.size());
	}
	
	@Test
	public void deleteCategory() throws SQLException {
		List<Category> categoryCreated;
		List<Category> categoryList;
		
		Category category = new Category("Electronic");
		
		CategoriesDao categoriesDao = new CategoriesDao(connection);
		categoriesDao.insert(category);
			
		categoryCreated = categoriesDao.list();
		
		assertEquals("Electronic", categoryCreated.get(0).getName());
		assertEquals(1, categoryCreated.size());
		
		categoriesDao.delete(category);
		
		categoryList = categoriesDao.list();
		
		assertEquals(0, categoryList.size());
	}
	
	@Test
	public void returnProductsByCategory() throws SQLException {
		CategoriesDao categoriesDao = new CategoriesDao(connection);
		
		Category electronic = new Category("Electronic");
		categoriesDao.insert(electronic);
		
		Category homeAppliances = new Category("Home appliances");
		categoriesDao.insert(homeAppliances);
		
		ProductsDao productsDao = new ProductsDao(connection);
		
		Product tvLcd = new Product("TV LCD", "42 inches", electronic.getId());
		productsDao.insert(tvLcd);
		
		Product bluray = new Product("Blu-ray", "DVD Player 3D", electronic.getId());
		productsDao.insert(bluray);
		
		Product microwave = new Product("Microwave", "30 liters", homeAppliances.getId());
		productsDao.insert(microwave);
		
		Product refrigerator = new Product("Refrigerator", "500 liters", homeAppliances.getId());
		productsDao.insert(refrigerator);
		
		List<Category> listProductsByCategory = categoriesDao.listByProducts();
        
		assertEquals(2, listProductsByCategory.size());
		
		assertEquals("Electronic", listProductsByCategory.get(0).getName());
		assertEquals("TV LCD", listProductsByCategory.get(0).getProducts().get(0).getName());
		assertEquals("Blu-ray", listProductsByCategory.get(0).getProducts().get(1).getName());
				
		assertEquals("Home appliances", listProductsByCategory.get(1).getName());
		assertEquals("Microwave", listProductsByCategory.get(1).getProducts().get(0).getName());
		assertEquals("Refrigerator", listProductsByCategory.get(1).getProducts().get(1).getName());
	}
}