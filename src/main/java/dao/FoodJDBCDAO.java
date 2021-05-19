package dao;

import entity.*;
import exception.DBException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodJDBCDAO {
    private DataSource dataSource;
    private static volatile FoodJDBCDAO instance;

    private FoodJDBCDAO() throws DBException {
        Context initContext = null;
        try {
            initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            this.dataSource = (DataSource) envContext.lookup("jdbc/restaurant_system");
        } catch (NamingException e) {
            throw new DBException("Cannot connect to the database", e);
        }

    }

    public static FoodJDBCDAO getInstance() throws DBException {
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

    public List<FoodItem> getFoodItems() throws DBException {
        List<FoodItem> foodItems = new ArrayList<>();
        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;
        try {
            myConn = dataSource.getConnection();
            String sql = "select fi.id, fi.name, fi.price,fi.image,category_id, c.name as category " +
                    "from food_item as fi" +
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
        } catch (SQLException throwables) {
            throw new DBException("Cannot get food items from database", throwables);
        } finally {
            close(myConn, myStmt, myRs);
        }
    }
    public List<FoodItem> getFoodItemsWithSkipAndLimit(int limit, int offset) throws DBException {
        List<FoodItem> foodItems = new ArrayList<>();
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        try {
            myConn = dataSource.getConnection();
            String sql = "select fi.id, fi.name, fi.price,fi.image,category_id, c.name as category  " +
                    " from food_item as fi " +
                    " inner join category as c on fi.category_id=c.id" +
                    " LIMIT ? OFFSET ?";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setInt(1, limit);
            myStmt.setInt(2, offset);
            myRs = myStmt.executeQuery();
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
        } catch (SQLException throwables) {
            throw new DBException("Cannot get food items from database", throwables);
        } finally {
            close(myConn, myStmt, myRs);
        }
    }
    public List<FoodItem> getFoodItemsWithSkipLimitFilter(int limit, int offset,String filter) throws DBException {
        List<FoodItem> foodItems = new ArrayList<>();
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        try {
            myConn = dataSource.getConnection();
            String sql = "select fi.id, fi.name, fi.price,fi.image,category_id, c.name as category  " +
                    " from food_item as fi " +
                    " inner join category as c on fi.category_id=c.id" +
                    " WHERE c.name = ? " +
                    " LIMIT ? OFFSET ?";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setString(1, filter);
            myStmt.setInt(2, limit);
            myStmt.setInt(3, offset);
            myRs = myStmt.executeQuery();
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
        } catch (SQLException throwables) {
            throw new DBException("Cannot get food items from database", throwables);
        } finally {
            close(myConn, myStmt, myRs);
        }
    }
    public List<FoodItem> getFoodItemsWithSkipLimitAndOrder(int limit, int offset, String sortBy, String order) throws DBException {
        List<FoodItem> foodItems = new ArrayList<>();
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        try {
            myConn = dataSource.getConnection();
            String sql = "select fi.id, fi.name as name, fi.price as price,fi.image,category_id, c.name as category  " +
                    " from food_item as fi " +
                    " inner join category as c on fi.category_id=c.id" +
                    " ORDER BY " +sortBy+" "+order+
                    " LIMIT ? OFFSET ?";
            myStmt = myConn.prepareStatement(sql);
          /*  myStmt.setString(1, sortBy);*/
            myStmt.setInt(1, limit);
            myStmt.setInt(2, offset);
            myRs = myStmt.executeQuery();
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
        } catch (SQLException throwables) {
            throw new DBException("Cannot get food items from database", throwables);
        } finally {
            close(myConn, myStmt, myRs);
        }
    }
    public List<FoodItem> getFoodItemsWithFilterSkipLimitAndOrder(String filter, int limit, int offset, String sortBy, String order) throws DBException {
        List<FoodItem> foodItems = new ArrayList<>();
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        try {
            myConn = dataSource.getConnection();
            String sql = "select fi.id, fi.name, fi.price,fi.image,category_id, c.name as category  " +
                    " from food_item as fi " +
                    " inner join category as c on fi.category_id=c.id" +
                    " WHERE c.name = ? " +
                    " ORDER BY "+sortBy+" "+order+
                    " LIMIT ? OFFSET ?";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setString(1, filter);
            myStmt.setInt(2, limit);
            myStmt.setInt(3, offset);
            myRs = myStmt.executeQuery();
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
        } catch (SQLException throwables) {
            throw new DBException("Cannot get food items from database", throwables);
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


    public FoodItem getFoodItem(String theFoodItemId) throws DBException {
        FoodItem foodItem = null;
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        int foodId;
        try {
            foodId = Integer.parseInt(theFoodItemId);
            myConn = dataSource.getConnection();
            String sql = "select fi.id, fi.name, fi.price,fi.image,category_id, c.name as category" +
                    " from food_item as fi" +
                    " inner join category as c on fi.category_id=c.id " +
                    "where fi.id=? ";
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
                throw new DBException("Could not find food item with id: " + theFoodItemId);
            }
            return foodItem;
        } catch (SQLException throwables) {
            throw new DBException("Cannot get food item with id" + theFoodItemId + " from database", throwables);
        } finally {
            close(myConn, myStmt, myRs);
        }
    }


    public List<Category> getCategories() throws DBException {
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
            throw new DBException("Cannot get all categories from database", throwables);
        } finally {
            close(myConn, myStmt, myRs);
        }
    }


}















