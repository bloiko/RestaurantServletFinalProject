package database.dao;


import database.dao.mapper.EntityMapper;
import database.entity.*;
import exception.DBException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    /**
     * Returns list of users from the database.
     *
     * @return list of all orders  .
     */
    public List<Order> getOrders() throws DBException {
        List<Order> orders = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DBManager.getInstance().getConnection();
            String sql = "select * from food_order" +
                    " join user u on u.id = food_order.user_id" +
                    " join status s on s.id = food_order.status_id";
            Statement myStmt = connection.createStatement();
            ResultSet myRs = myStmt.executeQuery(sql);
            while (myRs.next()) {
                OrderMapper mapper = new OrderMapper();
                Order order = mapper.mapRow(myRs);
                orders.add(order);
            }
            return orders;
        } catch (SQLException throwable) {
            DBManager.getInstance().rollbackAndClose(connection);
            throw new DBException("Cannot get all orders from database", throwable);
        } finally {
            DBManager.getInstance().commitAndClose(connection);
        }
    }

    /**
     * Returns list of users with DONE OrderStatus from the database.
     *
     * @return list of all orders  .
     */
    public List<Order> getDoneOrders() throws DBException {
        List<Order> orders = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DBManager.getInstance().getConnection();
            String sql = "select * from food_order" +
                    " join user u on u.id = food_order.user_id" +
                    " join status s on s.id = food_order.status_id" +
                    " where status_name ='DONE'";
            Statement myStmt = connection.createStatement();
            ResultSet myRs = myStmt.executeQuery(sql);
            while (myRs.next()) {
                OrderMapper mapper = new OrderMapper();
                Order order = mapper.mapRow(myRs);
                orders.add(order);
            }
            return orders;
        } catch (SQLException throwable) {
            DBManager.getInstance().rollbackAndClose(connection);
            throw new DBException("Cannot get all orders from database", throwable);
        } finally {
            DBManager.getInstance().commitAndClose(connection);
        }
    }

    /**
     * Returns list of users without DONE OrderStatus from the database.This list will be in
     * sorted order.
     *
     * @return list of all orders .
     */
    public List<Order> getNotDoneOrdersSortById() throws DBException {
        List<Order> orders = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DBManager.getInstance().getConnection();
            String sql = "select * from food_order " +
                    " join user u on u.id = food_order.user_id " +
                    " join status s on s.id = food_order.status_id " +
                    " where status_name !='DONE'" +
                    " order by food_order.id DESC ";
            Statement myStmt = connection.createStatement();
            ResultSet myRs = myStmt.executeQuery(sql);
            while (myRs.next()) {
                OrderMapper mapper = new OrderMapper();
                Order order = mapper.mapRow(myRs);
                orders.add(order);
            }
            return orders;
        } catch (SQLException throwable) {
            DBManager.getInstance().rollbackAndClose(connection);
            throw new DBException("Cannot get all orders from database", throwable);
        } finally {
            DBManager.getInstance().commitAndClose(connection);
        }
    }

    /**
     * Returns list of orders with some user identifier.
     *
     * @param theUserId is user identifier
     * @return list of all orders  .
     */
    public List<Order> getOrdersByUserId(int theUserId) throws DBException {
        List<Order> orders = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DBManager.getInstance().getConnection();
            String sql = "select * from food_order" +
                    " join user u on u.id = food_order.user_id" +
                    " join status s on s.id = food_order.status_id where u.id=?";
            PreparedStatement myStmt = connection.prepareStatement(sql);
            myStmt.setInt(1, theUserId);
            ResultSet myRs = myStmt.executeQuery();
            while (myRs.next()) {
                OrderMapper mapper = new OrderMapper();
                Order order = mapper.mapRow(myRs);
                orders.add(order);
            }
            return orders;
        } catch (SQLException throwable) {
            DBManager.getInstance().rollbackAndClose(connection);
            throw new DBException("Cannot get all orders by user id " + theUserId + " from database", throwable);
        } finally {
            DBManager.getInstance().commitAndClose(connection);
        }
    }

    /**
     * Returns list of items with some order identifier.
     *
     * @param orderId is order identifier
     * @return list of all items.
     */
    private List<Item> getOrderItems(int orderId) throws DBException {
        List<Item> items = new ArrayList<>();
        Connection connection = null;
        try {
            String sql = "select * from order_item " +
                    "where order_id= ? ;";
            connection = DBManager.getInstance().getConnection();
            PreparedStatement myStmt = connection.prepareStatement(sql);
            myStmt.setInt(1, orderId);
            ResultSet resultSet = myStmt.executeQuery();
            while (resultSet.next()) {
                int itemId = resultSet.getInt("item_id");
                Item item = getItemById(itemId);
                items.add(item);
            }
            return items;
        } catch (SQLException throwable) {
            DBManager.getInstance().rollbackAndClose(connection);
            throw new DBException("Cannot get order items with order id " + orderId + " from database", throwable);
        } finally {
            DBManager.getInstance().commitAndClose(connection);
        }
    }

    /**
     * Returns item with given identifier from database.
     *
     * @param itemId is item identifier
     * @return item that was found.
     */
    private Item getItemById(int itemId) throws DBException {
        Item item;
        Connection connection = null;
        try {
            String sql = "select * from item " +
                    "where id= ? ";
            connection = DBManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, itemId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                itemId = resultSet.getInt("id");
                int foodId = resultSet.getInt("food_id");
                int quantity = resultSet.getInt("quantity");
                FoodDAO foodDAO = new FoodDAO();
                FoodItem foodItem = foodDAO.getFoodItem(String.valueOf(foodId));
                item = new Item(itemId, foodItem, quantity);
            } else {
                throw new DBException("Could not find item by id: " + itemId);
            }
            return item;
        } catch (SQLException throwable) {
            DBManager.getInstance().rollbackAndClose(connection);
            throw new DBException("Cannot get item by id" + itemId + " from database", throwable);
        } finally {
            DBManager.getInstance().commitAndClose(connection);
        }
    }

    /**
     * Add item to the specified order.
     *
     * @param item  that should be added
     * @param order is the object were item should be added
     */
    public void addItemToOrder(Order order, Item item) throws DBException {
        int orderId = getOrderId(order);
        addItemToDataBase(item);
        int itemId = getItemId(item);
        Connection connection = null;
        try {
            connection = DBManager.getInstance().getConnection();
            String sql = "insert into order_item(order_id, item_id) " +
                    "VALUES (?,?)";
            PreparedStatement myStmt = connection.prepareStatement(sql);
            myStmt.setInt(1, orderId);
            myStmt.setInt(2, itemId);
            myStmt.execute();
        } catch (SQLException throwable) {
            DBManager.getInstance().rollbackAndClose(connection);
            throw new DBException("Cannot add item to order in the database", throwable);
        } finally {
            DBManager.getInstance().commitAndClose(connection);
        }
    }

    /**
     * Returns order identifier from the database.
     *
     * @param order that should be found
     * @return order identifier.
     */
    public int getOrderId(Order order) throws DBException {
        Connection connection = null;
        try {
            String sql = "select id from food_order " +
                    "where user_id=? AND status_id=? ORDER BY order_date DESC";
            connection = DBManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, order.getUser().getId());
            statement.setInt(2, getStatusId(order.getStatus()));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException throwable) {
            DBManager.getInstance().rollbackAndClose(connection);
            throw new DBException("Cannot get order id from database", throwable);
        } finally {
            DBManager.getInstance().commitAndClose(connection);
        }
        return -1;
    }

    /**
     * Returns item identifier of some item from database .
     *
     * @param item that should be found
     * @return item identifier.
     */
    private int getItemId(Item item) throws DBException {
        Connection connection = null;
        try {
            String sql = "select id from item " +
                    "where food_id= ? AND quantity=? ;";
            connection = DBManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, item.getFoodItem().getId());
            statement.setInt(2, item.getQuantity());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException throwable) {
            DBManager.getInstance().rollbackAndClose(connection);
            throw new DBException("Cannot get item id from the database", throwable);
        } finally {
            DBManager.getInstance().commitAndClose(connection);
        }
        return -1;
    }

    /**
     * Add item entity to the database.
     *
     * @param item that should be added
     */
    private void addItemToDataBase(Item item) throws DBException {
        Connection connection = null;
        try {
            connection = DBManager.getInstance().getConnection();
            String sql = "insert into item(food_id, quantity) " +
                    "VALUES (?,?)";
            PreparedStatement myStmt = connection.prepareStatement(sql);
            myStmt.setInt(1, item.getFoodItem().getId());
            myStmt.setInt(2, item.getQuantity());
            myStmt.execute();
        } catch (SQLException throwable) {
            DBManager.getInstance().rollbackAndClose(connection);
            throw new DBException("Cannot add item to the database", throwable);
        } finally {
            DBManager.getInstance().commitAndClose(connection);
        }
    }

    /**
     * Add order to the database .
     *
     * @param theOrder that should be added
     */
    public void addOrder(Order theOrder) throws DBException {
        Connection connection = null;
        try {
            connection = DBManager.getInstance().getConnection();
            String sql = "insert into food_order( order_date, user_id, status_id) " +
                    "VALUES (?,?,?)";
            PreparedStatement myStmt = connection.prepareStatement(sql);
            UserDAO userDAO = new UserDAO();
            int userId = userDAO.getUserId(theOrder.getUser());
            theOrder.getUser().setId(userId);
            myStmt.setTimestamp(1, theOrder.getOrderDate());
            myStmt.setInt(2, userId);
            myStmt.setInt(3, getStatusId(theOrder.getStatus()));
            myStmt.execute();
            for (Item item : theOrder.getItems()) {
                addItemToOrder(theOrder, item);
            }
        } catch (SQLException throwable) {
            DBManager.getInstance().rollbackAndClose(connection);
            throw new DBException("Cannot add order to the database", throwable);
        } finally {
            DBManager.getInstance().commitAndClose(connection);
        }
    }

    /**
     * Returns status identifier by specified status from database .
     *
     * @param status which identifier should be found
     * @return status identifier.
     */
    private int getStatusId(OrderStatus status) throws DBException {
        Connection connection = null;
        try {
            String sql = "select * from status " +
                    "where status_name=? ";
            connection = DBManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, status.value());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                throw new DBException("Could not find status by name: " + status.value());
            }
        } catch (SQLException throwable) {
            DBManager.getInstance().rollbackAndClose(connection);
            throw new DBException("Cannot get status id from database", throwable);
        } finally {
            DBManager.getInstance().commitAndClose(connection);
        }
    }

    /**
     * Returns order by identifier from database.
     *
     * @param theOrderId order identifier
     * @return order that was found.
     */
    public Order getOrder(String theOrderId) throws DBException {
        Connection connection = null;
        try {
            connection = DBManager.getInstance().getConnection();
            String sql = "select * from food_order" +
                    " join user u on u.id = food_order.user_id " +
                    " join status s on s.id = food_order.status_id  where food_order.id=?";
            PreparedStatement myStmt = connection.prepareStatement(sql);
            myStmt.setInt(1, Integer.parseInt(theOrderId));
            ResultSet myRs = myStmt.executeQuery();
            if (myRs.next()) {
                OrderMapper mapper = new OrderMapper();
                return mapper.mapRow(myRs);
            }
        } catch (SQLException throwable) {
            DBManager.getInstance().rollbackAndClose(connection);
            throw new DBException("Cannot get order by id " + theOrderId + " from database", throwable);
        } finally {
            DBManager.getInstance().commitAndClose(connection);
        }
        return null;
    }

    /**
     * Update order to the database .
     *
     * @param orderId     order identifier that should be changed
     * @param orderStatus order status that should be set
     */
    public void updateOrder(int orderId, OrderStatus orderStatus) throws DBException {
        Connection connection = null;
        try {
            int statusId = getStatusId(orderStatus);
            connection = DBManager.getInstance().getConnection();
            String sql = "UPDATE food_order SET status_id =? WHERE id =? ;";
            PreparedStatement myStmt = connection.prepareStatement(sql);
            myStmt.setInt(1, statusId);
            myStmt.setInt(2, orderId);
            myStmt.execute();
        } catch (SQLException throwable) {
            DBManager.getInstance().rollbackAndClose(connection);
            throw new DBException("Cannot update order in the database", throwable);
        } finally {
            DBManager.getInstance().commitAndClose(connection);
        }

    }

    /**
     * Delete order from the database .
     *
     * @param theOrderId order identifier that should be deleted
     */
    public void deleteOrder(String theOrderId) throws DBException {
        Connection connection = null;
        try {
            connection = DBManager.getInstance().getConnection();
            int orderId = Integer.parseInt(theOrderId);
            String sql = "delete from order_item where order_id=?";
            PreparedStatement myStmt = connection.prepareStatement(sql);
            myStmt.setInt(1, orderId);
            myStmt.execute();
            sql = "delete from food_order where id=?";
            myStmt = connection.prepareStatement(sql);
            myStmt.setInt(1, orderId);
            myStmt.execute();
        } catch (SQLException throwable) {
            DBManager.getInstance().rollbackAndClose(connection);
            throw new DBException("Cannot delete order from database", throwable);
        } finally {
            DBManager.getInstance().commitAndClose(connection);
        }
    }

    /**
     * Returns all statuses from the database database .
     *
     * @return list of the order statuses
     */
    public List<OrderStatus> getStatuses() throws DBException {
        List<OrderStatus> statuses = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DBManager.getInstance().getConnection();
            String sql = "select * from status;";
            Statement myStmt = connection.createStatement();
            ResultSet myRs = myStmt.executeQuery(sql);
            while (myRs.next()) {
                String statusName = myRs.getString("status_name");
                OrderStatus orderStatus = OrderStatus.getOrderStatus(statusName);
                statuses.add(orderStatus);
            }
            return statuses;
        } catch (SQLException throwable) {
            DBManager.getInstance().rollbackAndClose(connection);
            throw new DBException("Cannot get all statuses from database", throwable);
        } finally {
            DBManager.getInstance().commitAndClose(connection);
        }
    }

    /**
     * Extracts a order from the result set row.
     */
    public static class OrderMapper implements EntityMapper<Order> {
        public Order mapRow(ResultSet resultSet) throws SQLException {
            int foodOrderId;
            foodOrderId = resultSet.getInt("food_order.id");
            Timestamp orderDate = resultSet.getTimestamp("order_date");
            int userId = resultSet.getInt("user_id");
            String statusName = resultSet.getString("status_name");
            User user = null;
            List<Item> items = null;
            try {
                UserDAO userDAO = new UserDAO();
                user = userDAO.getUserByUserId(userId);
                OrderDAO orderDAO = new OrderDAO();
                items = orderDAO.getOrderItems(foodOrderId);
            } catch (DBException e) {
                e.printStackTrace();
            }
            return new Order(foodOrderId, orderDate, user, items, OrderStatus.getOrderStatus(statusName));
        }
    }
}















