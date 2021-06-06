package database.dao;

import org.junit.AfterClass;
import org.junit.Before;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FoodDaoTest {
    private static Connection connection;
    private static final String JDBC_DRIVER = "org.h2.Driver";
/*
    private static final String url = "jdbc:mysql://localhost:3306/test_restaurant_system?useSSL=false&serverTimezone=UTC";
*/
    private static final String url = "jdbc:h2:~/test10";

    private static final String user = "root";
    private static final String password = "root";

    @Before
    public void setupBeforeClass() throws SQLException, ClassNotFoundException {
        Class.forName ("org.h2.Driver");
        connection = DriverManager.getConnection(url);
    }

 /*   @Test
    public void test_getFoodItems() throws DBException, SQLException {
        DBManager dbManager = mock(DBManager.class);
        try (MockedStatic<DBManager> utilities = Mockito.mockStatic(DBManager.class)) {
            utilities.when(() -> DBManager.getInstance()).thenReturn(dbManager);
        }
        when(dbManager.getConnection()).thenReturn(connection);
        FoodDAO foodDAO = new FoodDAO();
        List<FoodItem> items = foodDAO.getFoodItems();
        Assert.assertTrue(items != null);
    }
*/
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
