package dao;

import entity.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodJDBCDAO {
    private String sqlTable = "jdbc/restaurant_system";
    private DataSource dataSource;
    private static volatile FoodJDBCDAO instance;

    private FoodJDBCDAO() {
        Context initContext = null;
        try {
            initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            this.dataSource = (DataSource) envContext.lookup("jdbc/restaurant_system");

        } catch (
                NamingException e) {
            e.printStackTrace();
        }

    }

    public static FoodJDBCDAO getInstance() {
        FoodJDBCDAO localInstance = instance;
        if (localInstance == null) {
            synchronized (FoodJDBCDAO.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new FoodJDBCDAO();
                }
            }
        }
        return localInstance;
    }

    public List<FoodItem> getFoodItems() throws Exception {
        List<FoodItem> foodItems = new ArrayList<>();
        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;
        String myConnectionString = "jdbc:mysql://localhost:3306/restaurant_system?useSSL=false&serverTimezone=UTC";
        try {
            myConn = dataSource.getConnection();
            String sql = "select fi.id, fi.name, fi.price,fi.image,category_id, c.name as category from food_item as fi" +
                    " inner join category as c on fi.category_id=c.id";
            myStmt = myConn.createStatement();
            myRs = myStmt.executeQuery(sql);
            while (myRs.next()) {
                int id = myRs.getInt("id");
                String name = myRs.getString("name");
                int price = myRs.getInt("price");
                String image = myRs.getString("image");
                int categoryId = myRs.getInt("category_id");
                String categoryName = myRs.getString("category");
                Category category = new Category(categoryId, categoryName);
                FoodItem foodItem = new FoodItem(id, name, price, image, category);
                foodItems.add(foodItem);
            }
            return foodItems;
        } finally {
            close(myConn, myStmt, myRs);
        }
    }

    private void close(Connection myConn, Statement myStmt, ResultSet myRs) {

        try {
            if (myRs != null) {
                myRs.close();
            }

            if (myStmt != null) {
                myStmt.close();
            }

            if (myConn != null) {
                myConn.close();
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }


    public void addFoodItem(FoodItem foodItem) throws Exception {
        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {
            myConn = dataSource.getConnection();
            String sql = "insert into food_item "
                    + "(name, price, image,category_id) "
                    + "values (?, ?, ?, ?)";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setString(1, foodItem.getName());
            myStmt.setInt(2, foodItem.getPrice());
            myStmt.setString(3, foodItem.getImage());
            myStmt.setInt(4, foodItem.getCategory().getId());
            myStmt.execute();
        } finally {
            close(myConn, myStmt, null);
        }
    }


    public FoodItem getFoodItem(String theFoodItemId) throws Exception {
        FoodItem foodItem = null;
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        int foodId;
        try {
            foodId = Integer.parseInt(theFoodItemId);
            myConn = dataSource.getConnection();
            String sql = "select fi.id, fi.name, fi.price,fi.image,category_id, c.name as category from food_item as fi" +
                    " inner join category as c on fi.category_id=c.id where fi.id=? ";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setInt(1, foodId);
            myRs = myStmt.executeQuery();
            if (myRs.next()) {
                int id = myRs.getInt("id");
                String name = myRs.getString("name");
                int price = myRs.getInt("price");
                String image = myRs.getString("image");
                String categoryName = myRs.getString("category");
                int categoryId = myRs.getInt("category_id");
                Category category = new Category(categoryId, categoryName);
                foodItem = new FoodItem(id, name, price, image, category);
            } else {
                throw new Exception("Could not find student id: " + foodId);
            }
            return foodItem;
        } finally {
            close(myConn, myStmt, myRs);
        }
    }


    public void updateFoodItem(FoodItem foodItem) throws Exception {
        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {
            myConn = dataSource.getConnection();
            String sql = "update food_item "
                    + "set name=?, price=?, image=?, category_id=?"
                    + "where id=?";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setString(1, foodItem.getName());
            myStmt.setInt(2, foodItem.getPrice());
            myStmt.setString(3, foodItem.getImage());
            myStmt.setInt(4, foodItem.getCategory().getId());
            myStmt.execute();
        } finally {
            close(myConn, myStmt, null);
        }
    }


    public void deleteFoodItem(String foodItemId) throws Exception {
        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {
            int foodId = Integer.parseInt(foodItemId);
            myConn = dataSource.getConnection();
            String sql = "delete from food_item where id=?";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setInt(1, foodId);
            myStmt.execute();
        } finally {
            close(myConn, myStmt, null);
        }
    }

    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;
        try {
            myConn = dataSource.getConnection();
            String sql = "select * from category;";
            myStmt = myConn.createStatement();
            myRs = myStmt.executeQuery(sql);
            while (myRs.next()) {
                int id = myRs.getInt("id");
                String name = myRs.getString("name");
                Category category = new Category(id, name);
                categories.add(category);
            }
            return categories;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            close(myConn, myStmt, myRs);
        }
        return null;
    }
}















