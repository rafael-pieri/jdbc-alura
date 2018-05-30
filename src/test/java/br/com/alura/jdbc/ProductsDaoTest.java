package br.com.alura.jdbc;

import static org.junit.Assert.assertEquals;

import br.com.alura.jdbc.dao.CategoriesDao;
import br.com.alura.jdbc.dao.ProductsDao;
import br.com.alura.jdbc.model.Category;
import br.com.alura.jdbc.model.Product;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProductsDaoTest {

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
    public void insertProduct() throws SQLException {
        Category category = new Category("Electronic");
        CategoriesDao categoriesDao = new CategoriesDao(connection);
        categoriesDao.insert(category);

        List<Product> productCreated;

        List<Category> categoryList = categoriesDao.list();

        Product product = new Product("TV LCD", "42 inches", categoryList.get(0).getId());

        ProductsDao productsDao = new ProductsDao(connection);
        productsDao.insert(product);

        productCreated = productsDao.list();

        assertEquals("TV LCD", productCreated.get(0).getName());
        assertEquals("42 inches", productCreated.get(0).getDescription());
        assertEquals("Electronic", categoriesDao.findById(productCreated.get(0).getCategoryId()).getName());
        assertEquals(1, productCreated.size());
    }

    @Test
    public void updateProduct() throws SQLException {
        Category category = new Category("Electronic");
        CategoriesDao categoriesDao = new CategoriesDao(connection);
        categoriesDao.insert(category);

        List<Product> productCreated;
        List<Product> productUpdated;

        List<Category> categoryList = categoriesDao.list();

        Product product = new Product("TV LCD", "42 inches", categoryList.get(0).getId());

        ProductsDao productsDao = new ProductsDao(connection);
        productsDao.insert(product);

        productCreated = productsDao.list();

        assertEquals("TV LCD", productCreated.get(0).getName());
        assertEquals("42 inches", productCreated.get(0).getDescription());
        assertEquals("Electronic", categoriesDao.findById(productCreated.get(0).getCategoryId()).getName());
        assertEquals(1, productCreated.size());

        product.setName("TV Plasma");
        product.setDescription("38 inches");
        productsDao.update(product);

        productUpdated = productsDao.list();

        assertEquals("TV Plasma", productUpdated.get(0).getName());
        assertEquals("38 inches", productUpdated.get(0).getDescription());
        assertEquals("Electronic", categoriesDao.findById(productCreated.get(0).getCategoryId()).getName());
        assertEquals(1, productUpdated.size());
    }

    @Test
    public void deleteProduct() throws SQLException {
        Category category = new Category("Electronic");
        CategoriesDao categoriesDao = new CategoriesDao(connection);
        categoriesDao.insert(category);

        List<Product> productCreated;
        List<Product> productList;

        List<Category> categoryList = categoriesDao.list();

        Product product = new Product("TV LCD", "42 inches", categoryList.get(0).getId());

        ProductsDao productsDao = new ProductsDao(connection);
        productsDao.insert(product);

        productCreated = productsDao.list();

        assertEquals("TV LCD", productCreated.get(0).getName());
        assertEquals("42 inches", productCreated.get(0).getDescription());
        assertEquals("Electronic", categoriesDao.findById(productCreated.get(0).getCategoryId()).getName());
        assertEquals(1, productCreated.size());

        productsDao.delete(product);

        productList = productsDao.list();

        assertEquals(0, productList.size());
    }
}