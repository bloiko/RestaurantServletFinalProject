package database.dao;

import database.entity.FoodItem;
import exception.DBException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class FoodDaoTest {
    private static Connection connection;
    private static final String url = "jdbc:mysql://localhost:3306/test_restaurant_system?useSSL=false&serverTimezone=UTC";
    private static final String user = "root";
    private static final String password = "root";


    @Before
    public void setupBeforeClass() throws SQLException, ClassNotFoundException, NamingException, IllegalAccessException, InstantiationException {

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection connection = DriverManager.getConnection(url, user, password);
        try (MockedStatic<DBManager> utilities = Mockito.mockStatic(DBManager.class)) {
            utilities.when(DBManager.getInstance()::getConnection).thenReturn(connection);
        }
    }

    @Test
    public void test_getFoodItems() throws DBException {
        FoodDAO foodDAO = new FoodDAO();
        List<FoodItem> items = foodDAO.getFoodItems();
        Assert.assertTrue(items != null);
    }

    @AfterClass
    public static void afterClass() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
