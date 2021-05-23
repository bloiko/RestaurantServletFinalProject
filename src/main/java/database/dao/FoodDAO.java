package database.dao;

import database.dao.mapper.EntityMapper;
import database.entity.Category;
import database.entity.FoodItem;
import exception.DBException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodDAO {

    /**
     * Returns list of food items from the database.
     *
     * @return list of all food items  .
     */
    public List<FoodItem> getFoodItems() throws DBException {
        List<FoodItem> foodItems = new ArrayList<>();
        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;
        try {
            myConn = DBManager.getInstance().getConnection();
            String sql = "select fi.id, fi.name, fi.price,fi.image,category_id, c.name as category " +
                    "from food_item as fi" +
                    " inner join category as c on fi.category_id=c.id";
            myStmt = myConn.createStatement();
            myRs = myStmt.executeQuery(sql);
            while (myRs.next()) {
                FoodItemMapper mapper = new FoodItemMapper();
                FoodItem foodItem = mapper.mapRow(myRs);
                foodItems.add(foodItem);
            }
            return foodItems;
        } catch (SQLException throwable) {
            DBManager.getInstance().rollbackAndClose(myConn);
            throw new DBException("Cannot get food items from database", throwable);
        } finally {
            DBManager.getInstance().commitAndClose(myConn);
        }
    }

    /**
     * Returns list of food items from the database using skip and limit .
     *
     * @param limit  shows how many food items to take
     * @param offset shows how many food items need to skip
     * @return list of food items  .
     */
    public List<FoodItem> getFoodItemsWithSkipAndLimit(int limit, int offset) throws DBException {
        List<FoodItem> foodItems = new ArrayList<>();
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        try {
            myConn = DBManager.getInstance().getConnection();
            String sql = "select fi.id, fi.name, fi.price,fi.image,category_id, c.name as category  " +
                    " from food_item as fi " +
                    " inner join category as c on fi.category_id=c.id" +
                    " LIMIT ? OFFSET ?";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setInt(1, limit);
            myStmt.setInt(2, offset);
            myRs = myStmt.executeQuery();
            while (myRs.next()) {
                FoodItemMapper mapper = new FoodItemMapper();
                FoodItem foodItem = mapper.mapRow(myRs);
                foodItems.add(foodItem);
            }
            return foodItems;
        } catch (SQLException throwable) {
            DBManager.getInstance().rollbackAndClose(myConn);
            throw new DBException("Cannot get food items from database", throwable);
        } finally {
            DBManager.getInstance().commitAndClose(myConn);
        }
    }

    /**
     * Returns list of food items from the database using skip, limit and filter .
     *
     * @param limit  shows how many food items to take
     * @param offset shows how many food items need to skip
     * @param filter shows which category use to filter data
     * @return list of food items  .
     */
    public List<FoodItem> getFoodItemsWithSkipLimitFilter(int limit, int offset, String filter) throws DBException {
        List<FoodItem> foodItems = new ArrayList<>();
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        try {
            myConn = DBManager.getInstance().getConnection();
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
                FoodItemMapper mapper = new FoodItemMapper();
                FoodItem foodItem = mapper.mapRow(myRs);
                foodItems.add(foodItem);
            }
            return foodItems;
        } catch (SQLException throwable) {
            DBManager.getInstance().rollbackAndClose(myConn);
            throw new DBException("Cannot get food items from database", throwable);
        } finally {
            DBManager.getInstance().commitAndClose(myConn);
        }
    }

    /**
     * Returns list of food items from the database using skip and limit. Also
     * data should be in sorted in specified order.
     *
     * @param limit  shows how many food items to take
     * @param offset shows how many food items need to skip
     * @param sortBy shows which entity field use to sort data
     * @param order  shows in which way data should be sorted ASC or DESC
     * @return list of food items  .
     */
    public List<FoodItem> getFoodItemsWithSkipLimitAndOrder(int limit, int offset, String sortBy, String order) throws DBException {
        List<FoodItem> foodItems = new ArrayList<>();
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        try {
            myConn = DBManager.getInstance().getConnection();
            String sql = "select fi.id, fi.name as name, fi.price as price,fi.image,category_id, c.name as category  " +
                    " from food_item as fi " +
                    " inner join category as c on fi.category_id=c.id" +
                    " ORDER BY " + sortBy + " " + order +
                    " LIMIT ? OFFSET ?";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setInt(1, limit);
            myStmt.setInt(2, offset);
            myRs = myStmt.executeQuery();
            while (myRs.next()) {
                FoodItemMapper mapper = new FoodItemMapper();
                FoodItem foodItem = mapper.mapRow(myRs);
                foodItems.add(foodItem);
            }
            return foodItems;
        } catch (SQLException throwable) {
            DBManager.getInstance().rollbackAndClose(myConn);
            throw new DBException("Cannot get food items from database", throwable);
        } finally {
            DBManager.getInstance().commitAndClose(myConn);
        }
    }

    /**
     * Returns list of food items from the database using skip, limit and filter . Also
     * data should be in sorted in specified order.
     *
     * @param limit  shows how many food items to take
     * @param offset shows how many food items need to skip
     * @param filter shows which category use to filter data
     * @param sortBy shows which entity field use to sort data
     * @param order  shows in which way data should be sorted ASC or DESC
     * @return list of food items  .
     */
    public List<FoodItem> getFoodItemsWithFilterSkipLimitAndOrder(String filter, int limit, int offset, String sortBy, String order) throws DBException {
        List<FoodItem> foodItems = new ArrayList<>();
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        try {
            myConn = DBManager.getInstance().getConnection();
            String sql = "select fi.id, fi.name, fi.price,fi.image,category_id, c.name as category  " +
                    " from food_item as fi " +
                    " inner join category as c on fi.category_id=c.id" +
                    " WHERE c.name = ? " +
                    " ORDER BY " + sortBy + " " + order +
                    " LIMIT ? OFFSET ?";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setString(1, filter);
            myStmt.setInt(2, limit);
            myStmt.setInt(3, offset);
            myRs = myStmt.executeQuery();
            while (myRs.next()) {
                FoodItemMapper mapper = new FoodItemMapper();
                FoodItem foodItem = mapper.mapRow(myRs);
                foodItems.add(foodItem);
            }
            return foodItems;
        } catch (SQLException throwable) {
            DBManager.getInstance().rollbackAndClose(myConn);
            throw new DBException("Cannot get food items from database", throwable);
        } finally {
            DBManager.getInstance().commitAndClose(myConn);
        }
    }

    /**
     * Returns food item with given identifier from database.
     *
     * @param theFoodItemId is food item identifier
     * @return food item that was found.
     */
    public FoodItem getFoodItem(String theFoodItemId) throws DBException {
        FoodItem foodItem = null;
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet resultSet = null;
        int foodId;
        try {
            foodId = Integer.parseInt(theFoodItemId);
            myConn = DBManager.getInstance().getConnection();
            String sql = "select fi.id, fi.name, fi.price,fi.image,category_id, c.name as category" +
                    " from food_item as fi" +
                    " inner join category as c on fi.category_id=c.id " +
                    "where fi.id=? ";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setInt(1, foodId);
            resultSet = myStmt.executeQuery();
            if (resultSet.next()) {
                FoodItemMapper mapper = new FoodItemMapper();
                foodItem = mapper.mapRow(resultSet);
            } else {
                throw new DBException("Could not find food item with id: " + theFoodItemId);
            }
            return foodItem;
        } catch (SQLException throwable) {
            DBManager.getInstance().rollbackAndClose(myConn);
            throw new DBException("Cannot get food item with id" + theFoodItemId + " from database", throwable);
        } finally {
            DBManager.getInstance().commitAndClose(myConn);
        }
    }

    /**
     * Returns list of all categories from database.
     *
     * @return list of all categories.
     */
    public List<Category> getCategories() throws DBException {
        List<Category> categories = new ArrayList<>();
        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;
        try {
            myConn = DBManager.getInstance().getConnection();
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
        } catch (SQLException throwable) {
            DBManager.getInstance().rollbackAndClose(myConn);
            throw new DBException("Cannot get all categories from database", throwable);
        } finally {
            DBManager.getInstance().commitAndClose(myConn);
        }
    }

    /**
     * Extracts a food item from the result set row.
     */
    public static class FoodItemMapper implements EntityMapper<FoodItem> {
        public FoodItem mapRow(ResultSet myRs) throws SQLException {
            int id = myRs.getInt("id");
            String name = myRs.getString("name");
            int price = myRs.getInt("price");
            String image = myRs.getString("image");
            int categoryId = myRs.getInt("category_id");
            String categoryName = myRs.getString("category");
            Category category = new Category(categoryId, categoryName);
            return new FoodItem(id, name, price, image, category);
        }
    }
}















