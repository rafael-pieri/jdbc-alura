package br.com.alura.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import br.com.alura.jdbc.dao.CategoriesDao;
import br.com.alura.jdbc.dao.ProductsDao;
import br.com.alura.jdbc.model.Category;
import br.com.alura.jdbc.model.Product;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CategoriesDaoTest {

    private static Connection connection;

    @BeforeClass
    public static void openConnection() throws SQLException {
        connection = new ConnectionPool().getConnection();
        connection.setAutoCommit(false);

        Statement statement = connection.createStatement();

//        String sql = "create table category(id INTEGER NOT NULL AUTO_INCREMENT,  name VARCHAR(255) NOT NULL,  PRIMARY KEY (id))";

//        String sql2 = "create table product(id INTEGER NOT NULL AUTO_INCREMENT, description VARCHAR(255) NOT NULL, category_id integer, PRIMARY KEY (id))";

//        statement.executeUpdate(sql);
//        statement.executeUpdate(sql2);
    }

    @After
    public void rollbackChanges() throws SQLException {
        connection.rollback();
    }

    @AfterClass
    public static void closeConnection() throws SQLException {
        connection.close();
    }

    @Test
    public void insertCategory() throws SQLException {
        Category categoryCreated;

        Category category = new Category("Electronic");

        CategoriesDao categoriesDao = new CategoriesDao(connection);
        categoriesDao.insert(category);

        categoryCreated = categoriesDao.list().get(0);

        assertEquals("Electronic", categoryCreated.getName());
        assertNotNull(categoryCreated.getId());
    }

    @Test
    public void updateCategory() throws SQLException {
        Category categoryCreated;
        Category categoryUpdated;

        Category category = new Category("Home Appliances");

        CategoriesDao categoriesDao = new CategoriesDao(connection);
        categoriesDao.insert(category);

        categoryCreated = categoriesDao.list().get(0);

        assertEquals("Home Appliances", categoryCreated.getName());
        assertNotNull(categoryCreated.getId());

        categoryCreated.setName("Eletronic");
        categoriesDao.update(categoryCreated);

        categoryUpdated = categoriesDao.list().get(0);

        assertEquals("Eletronic", categoryUpdated.getName());
        assertNotNull(categoryUpdated.getId());
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

        categoriesDao.delete(categoryCreated.get(0));

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

        Product tvLcd = new Product("TV LCD", "42 polegadas", electronic.getId());
        productsDao.insert(tvLcd);

        Product blueray = new Product("Bluray", "DVD Player 3D", electronic.getId());
        productsDao.insert(blueray);

        Product microwave = new Product("Microwave", "30 liters", homeAppliances.getId());
        productsDao.insert(microwave);

        Product refrigerator = new Product("Refrigerator", "500 liters", homeAppliances.getId());
        productsDao.insert(refrigerator);

        List<Category> listProductsByCategory = categoriesDao.listByProducts();

        assertEquals(2, listProductsByCategory.size());

        assertEquals("Electronic", listProductsByCategory.get(0).getName());
        assertEquals("TV LCD", listProductsByCategory.get(0).getProducts().get(0).getName());
        assertEquals("Bluray", listProductsByCategory.get(0).getProducts().get(1).getName());

        assertEquals("Home appliances", listProductsByCategory.get(1).getName());
        assertEquals("Microwave", listProductsByCategory.get(1).getProducts().get(0).getName());
        assertEquals("Refrigerator", listProductsByCategory.get(1).getProducts().get(1).getName());
    }

}
