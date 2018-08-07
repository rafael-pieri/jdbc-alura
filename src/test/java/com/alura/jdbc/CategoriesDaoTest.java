package com.alura.jdbc;

import static org.junit.Assert.assertEquals;

import com.alura.jdbc.dao.CategoriesDao;
import com.alura.jdbc.dao.ProductsDao;
import com.alura.jdbc.model.Category;
import com.alura.jdbc.model.Product;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CategoriesDaoTest {

    private static final String ELECTRONIC = "Electronic";
    private static final String HOME_APPLIANCES = "Home Appliances";
    private static final String TV_LCD = "TV LCD";
    private static final String BLU_RAY = "Blu-ray";
    private static final String DVD_PLAYER_3_D = "DVD Player 3D";
    private static final String MICROWAVE = "Microwave";
    private static final String REFRIGERATOR = "Refrigerator";
    private static final String _42_INCHES = "42 inches";
    private static final String _30_LITERS = "30 liters";
    private static final String _500_LITERS = "500 liters";

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
        final List<Category> categoryCreated;

        final Category category = new Category(ELECTRONIC);

        final CategoriesDao categoriesDao = new CategoriesDao(connection);
        categoriesDao.insert(category);

        categoryCreated = categoriesDao.list();

        assertEquals(ELECTRONIC, categoryCreated.get(0).getName());
        assertEquals(1, categoryCreated.size());
    }

    @Test
    public void updateCategory() throws SQLException {
        final List<Category> categoryCreated;
        final List<Category> categoryUpdated;

        Category category = new Category(HOME_APPLIANCES);

        final CategoriesDao categoriesDao = new CategoriesDao(connection);
        categoriesDao.insert(category);

        categoryCreated = categoriesDao.list();

        assertEquals(HOME_APPLIANCES, categoryCreated.get(0).getName());
        assertEquals(1, categoryCreated.size());

        category.setName(ELECTRONIC);
        categoriesDao.update(category);

        categoryUpdated = categoriesDao.list();

        assertEquals(ELECTRONIC, categoryUpdated.get(0).getName());
        assertEquals(1, categoryUpdated.size());
    }

    @Test
    public void deleteCategory() throws SQLException {
        final List<Category> categoryCreated;
        final List<Category> categoryList;

        final Category category = new Category(ELECTRONIC);

        final CategoriesDao categoriesDao = new CategoriesDao(connection);
        categoriesDao.insert(category);

        categoryCreated = categoriesDao.list();

        assertEquals(ELECTRONIC, categoryCreated.get(0).getName());
        assertEquals(1, categoryCreated.size());

        categoriesDao.delete(category);

        categoryList = categoriesDao.list();

        assertEquals(0, categoryList.size());
    }

    @Test
    public void returnProductsByCategory() throws SQLException {
        final CategoriesDao categoriesDao = new CategoriesDao(connection);

        final Category electronic = new Category(ELECTRONIC);
        categoriesDao.insert(electronic);

        final Category homeAppliances = new Category(HOME_APPLIANCES);
        categoriesDao.insert(homeAppliances);

        final ProductsDao productsDao = new ProductsDao(connection);

        final Product tvLcd = new Product(TV_LCD, _42_INCHES, electronic.getId());
        productsDao.insert(tvLcd);

        final Product bluray = new Product(BLU_RAY, DVD_PLAYER_3_D, electronic.getId());
        productsDao.insert(bluray);

        final Product microwave = new Product(MICROWAVE, _30_LITERS, homeAppliances.getId());
        productsDao.insert(microwave);

        final Product refrigerator = new Product(REFRIGERATOR, _500_LITERS, homeAppliances.getId());
        productsDao.insert(refrigerator);

        final List<Category> listProductsByCategory = categoriesDao.listByProducts();

        assertEquals(2, listProductsByCategory.size());

        assertEquals(ELECTRONIC, listProductsByCategory.get(0).getName());
        assertEquals(TV_LCD, listProductsByCategory.get(0).getProducts().get(0).getName());
        assertEquals(BLU_RAY, listProductsByCategory.get(0).getProducts().get(1).getName());

        assertEquals(HOME_APPLIANCES, listProductsByCategory.get(1).getName());
        assertEquals(MICROWAVE, listProductsByCategory.get(1).getProducts().get(0).getName());
        assertEquals(REFRIGERATOR, listProductsByCategory.get(1).getProducts().get(1).getName());
    }
}