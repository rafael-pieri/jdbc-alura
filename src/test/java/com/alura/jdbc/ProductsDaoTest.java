package com.alura.jdbc;

import static org.junit.Assert.assertEquals;

import com.alura.jdbc.dao.ProductsDao;
import com.alura.jdbc.model.Category;
import com.alura.jdbc.model.Product;
import com.alura.jdbc.dao.CategoriesDao;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProductsDaoTest {

    private static final String ELECTRONIC = "Electronic";
    private static final String TV_LCD = "TV LCD";
    private static final String TV_PLASMA = "TV Plasma";
    private static final String _42_INCHES = "42 inches";
    private static final String _38_INCHES = "38 inches";

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
        final Category category = new Category(ELECTRONIC);

        final CategoriesDao categoriesDao = new CategoriesDao(connection);
        categoriesDao.insert(category);

        final List<Product> productCreated;

        final List<Category> categoryList = categoriesDao.list();

        final Product product = new Product(TV_LCD, _42_INCHES, categoryList.get(0).getId());

        final ProductsDao productsDao = new ProductsDao(connection);
        productsDao.insert(product);

        productCreated = productsDao.list();

        assertEquals(TV_LCD, productCreated.get(0).getName());
        assertEquals(_42_INCHES, productCreated.get(0).getDescription());
        assertEquals(ELECTRONIC, categoriesDao.findById(productCreated.get(0).getCategoryId()).getName());
        assertEquals(1, productCreated.size());
    }

    @Test
    public void updateProduct() throws SQLException {
        final Category category = new Category(ELECTRONIC);

        final CategoriesDao categoriesDao = new CategoriesDao(connection);
        categoriesDao.insert(category);

        final List<Product> productCreated;
        List<Product> productUpdated;

        final List<Category> categoryList = categoriesDao.list();

        Product product = new Product(TV_LCD, _42_INCHES, categoryList.get(0).getId());

        final ProductsDao productsDao = new ProductsDao(connection);
        productsDao.insert(product);

        productCreated = productsDao.list();

        assertEquals(TV_LCD, productCreated.get(0).getName());
        assertEquals(_42_INCHES, productCreated.get(0).getDescription());
        assertEquals(ELECTRONIC, categoriesDao.findById(productCreated.get(0).getCategoryId()).getName());
        assertEquals(1, productCreated.size());

        product.setName(TV_PLASMA);
        product.setDescription(_38_INCHES);
        productsDao.update(product);

        productUpdated = productsDao.list();

        assertEquals(TV_PLASMA, productUpdated.get(0).getName());
        assertEquals(_38_INCHES, productUpdated.get(0).getDescription());
        assertEquals(ELECTRONIC, categoriesDao.findById(productCreated.get(0).getCategoryId()).getName());
        assertEquals(1, productUpdated.size());
    }

    @Test
    public void deleteProduct() throws SQLException {
        final Category category = new Category(ELECTRONIC);

        final CategoriesDao categoriesDao = new CategoriesDao(connection);
        categoriesDao.insert(category);

        final List<Product> productCreated;
        final List<Product> productList;

        final List<Category> categoryList = categoriesDao.list();

        final Product product = new Product(TV_LCD, _42_INCHES, categoryList.get(0).getId());

        ProductsDao productsDao = new ProductsDao(connection);
        productsDao.insert(product);

        productCreated = productsDao.list();

        assertEquals(TV_LCD, productCreated.get(0).getName());
        assertEquals(_42_INCHES, productCreated.get(0).getDescription());
        assertEquals(ELECTRONIC, categoriesDao.findById(productCreated.get(0).getCategoryId()).getName());
        assertEquals(1, productCreated.size());

        productsDao.delete(product);

        productList = productsDao.list();

        assertEquals(0, productList.size());
    }
}