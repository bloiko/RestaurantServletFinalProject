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

public class OrderJDBCDAO {
    private DataSource dataSource;
    private static volatile OrderJDBCDAO instance;

    private OrderJDBCDAO() throws DBException {
        Context initContext;
        try {
            initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            this.dataSource = (DataSource) envContext.lookup("jdbc/restaurant_system");

        } catch (NamingException e) {
            throw new DBException("Cannot connect to the database", e);
        }
    }

    public static OrderJDBCDAO getInstance() throws DBException {
        OrderJDBCDAO localInstance = instance;
        if (localInstance == null) {
            synchronized (OrderJDBCDAO.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new OrderJDBCDAO();
                }
            }
        }
        return localInstance;
    }


    public List<Order> getOrders() throws DBException {
        List<Order> orders = new ArrayList<>();
        int foodOrderId;
        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;
        try {
            myConn = dataSource.getConnection();
            String sql = "select * from food_order" +
                    " join user u on u.id = food_order.user_id" +
                    " join status s on s.id = food_order.status_id";
            myStmt = myConn.createStatement();
            myRs = myStmt.executeQuery(sql);
            while (myRs.next()) {
                foodOrderId = myRs.getInt("food_order.id");
                Timestamp orderDate = myRs.getTimestamp("order_date");
                int userId = myRs.getInt("user_id");
                String statusName = myRs.getString("status_name");
                UserDAO userDAO = UserDAO.getInstance();
                User user = userDAO.getUserByUserId(userId);
                List<Item> items = getOrderItems(foodOrderId);
                Order order = new Order(foodOrderId, orderDate, user, items, OrderStatus.getOrderStatus(statusName));
                orders.add(order);
            }
            return orders;
        } catch (SQLException throwables) {
            throw new DBException("Cannot get all orders from database", throwables);
        } finally {
            close(myConn, myStmt, myRs);
        }
    }

    public List<Order> getOrdersByUserId(int theUserId) throws DBException {
        List<Order> orders = new ArrayList<>();
        int foodOrderId;
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        try {
            myConn = dataSource.getConnection();
            String sql = "select * from food_order" +
                    " join user u on u.id = food_order.user_id" +
                    " join status s on s.id = food_order.status_id where u.id=?";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setInt(1, theUserId);
            myRs = myStmt.executeQuery();
            while (myRs.next()) {
                foodOrderId = myRs.getInt("food_order.id");
                Timestamp orderDate = myRs.getTimestamp("order_date");
                int userId = myRs.getInt("user_id");
                String statusName = myRs.getString("status_name");
                UserDAO userDAO = UserDAO.getInstance();
                User user = userDAO.getUserByUserId(userId);
                List<Item> items = getOrderItems(foodOrderId);
                Order order = new Order(foodOrderId, orderDate, user, items, OrderStatus.getOrderStatus(statusName));
                orders.add(order);
            }
            return orders;
        } catch (SQLException throwables) {
            throw new DBException("Cannot get all orders by user id " + theUserId + " from database", throwables);
        } finally {
            close(myConn, myStmt, myRs);
        }
    }

    private List<Item> getOrderItems(int orderId) throws DBException {
        List<Item> items = new ArrayList<>();
        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {
            String sql = "select * from order_item " +
                    "where order_id= ? ;";
            myConn = dataSource.getConnection();
            PreparedStatement myStmtRole = myConn.prepareStatement(sql);
            myStmtRole.setInt(1, orderId);
            ResultSet resultSet = myStmtRole.executeQuery();
            while (resultSet.next()) {
                int itemId = resultSet.getInt("item_id");
                Item item = getItemById(itemId);
                items.add(item);
            }
            return items;
        } catch (SQLException throwables) {
            throw new DBException("Cannot get order items with order id " + orderId + " from database", throwables);
        } finally {
            close(myConn, myStmt, null);
        }
    }

    private Item getItemById(int itemId) throws DBException {
        Item item;
        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {
            String sql = "select * from item " +
                    "where id= ? ";
            myConn = dataSource.getConnection();
            PreparedStatement myStmtRole = myConn.prepareStatement(sql);
            myStmtRole.setInt(1, itemId);
            ResultSet resultSet = myStmtRole.executeQuery();
            if (resultSet.next()) {
                itemId = resultSet.getInt("id");
                int foodId = resultSet.getInt("food_id");
                int quantity = resultSet.getInt("quantity");
                FoodJDBCDAO foodJDBCDAO = FoodJDBCDAO.getInstance();
                FoodItem foodItem = foodJDBCDAO.getFoodItem(String.valueOf(foodId));
                item = new Item(itemId, foodItem, quantity);
            } else {
                throw new DBException("Could not find item by id: " + itemId);
            }
            return item;
        } catch (SQLException throwables) {
            throw new DBException("Cannot get item by id" + itemId + " from database", throwables);
        } finally {
            close(myConn, myStmt, null);
        }
    }

    private void close(Connection myConn, Statement myStmt, ResultSet myRs) throws DBException {
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
        } catch (SQLException throwables) {
            throw new DBException("Cannot close connection with database", throwables);
        }
    }

    public void addItemToOrder(Order order, Item item) throws DBException {
        int orderId = getOrderId(order);
        addItemToDataBase(item);
        int itemId = getItemId(item);
        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {
            myConn = dataSource.getConnection();
            String sql = "insert into order_item(order_id, item_id) " +
                    "VALUES (?,?)";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setInt(1, orderId);
            myStmt.setInt(2, itemId);
            myStmt.execute();
        } catch (SQLException throwables) {
            throw new DBException("Cannot add item to order in the database", throwables);
        } finally {
            close(myConn, myStmt, null);
        }
    }

    private int getOrderId(Order order) throws DBException {
        List<Item> items = null;
        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {
            String sql = "select id from food_order " +
                    "where user_id=? AND status_id=? ORDER BY order_date DESC";
            myConn = dataSource.getConnection();
            PreparedStatement statement = myConn.prepareStatement(sql);
            statement.setInt(1, order.getUser().getId());
            statement.setInt(2, getStatusId(order.getStatus()));

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int orderId = resultSet.getInt("id");
                return orderId;
            }
        } catch (SQLException throwables) {
            throw new DBException("Cannot get order id from database", throwables);
        } finally {
            close(myConn, myStmt, null);
        }
        return -1;
    }

    private int getItemId(Item item) throws DBException {
        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {
            String sql = "select id from item " +
                    "where food_id= ? AND quantity=? ;";
            myConn = dataSource.getConnection();
            PreparedStatement statement = myConn.prepareStatement(sql);
            statement.setInt(1, item.getFoodItem().getId());
            statement.setInt(2, item.getQuantity());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int itemId = resultSet.getInt("id");
                return itemId;
            }
        } catch (SQLException throwables) {
            throw new DBException("Cannot get item id from the database", throwables);
        } finally {
            close(myConn, myStmt, null);
        }
        return -1;
    }

    private void addItemToDataBase(Item item) throws DBException {
        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {
            myConn = dataSource.getConnection();
            String sql = "insert into item(food_id, quantity) " +
                    "VALUES (?,?)";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setInt(1, item.getFoodItem().getId());
            myStmt.setInt(2, item.getQuantity());
            myStmt.execute();
        } catch (SQLException throwables) {
            throw new DBException("Cannot add item to the database", throwables);
        } finally {
            close(myConn, myStmt, null);
        }
    }

    public void addOrder(Order theOrder) throws DBException {
        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {
            myConn = dataSource.getConnection();
            String sql = "insert into food_order( order_date, user_id, status_id) " +
                    "VALUES (?,?,?)";
            myStmt = myConn.prepareStatement(sql);
            UserDAO userDAO = UserDAO.getInstance();
            int userId = userDAO.getUserId(theOrder.getUser());
            theOrder.getUser().setId(userId);
            myStmt.setTimestamp(1, theOrder.getOrderDate());
            myStmt.setInt(2, userId);
            myStmt.setInt(3, getStatusId(theOrder.getStatus()));
            myStmt.execute();
            for (Item item : theOrder.getItems()) {
                addItemToOrder(theOrder, item);
            }
        } catch (SQLException throwables) {
            throw new DBException("Cannot add order to the database", throwables);
        } finally {
            close(myConn, myStmt, null);
        }
    }

    private int getStatusId(OrderStatus status) throws DBException {
        Item item;
        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {
            String sql = "select * from status " +
                    "where status_name=? ";
            myConn = dataSource.getConnection();
            PreparedStatement myStmtRole = myConn.prepareStatement(sql);
            myStmtRole.setString(1, status.value());
            ResultSet resultSet = myStmtRole.executeQuery();
            if (resultSet.next()) {
                int statusId = resultSet.getInt("id");
                return statusId;
            } else {
                throw new DBException("Could not find status by name: " + status.value());
            }
        } catch (SQLException throwables) {
            throw new DBException("Cannot get status id from database", throwables);
        } finally {
            close(myConn, myStmt, null);
        }
    }

    public Order getOrder(String theOrderId) throws DBException {
        int foodOrderId;
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        try {
            myConn = dataSource.getConnection();
            String sql = "select * from food_order" +
                    " join user u on u.id = food_order.user_id " +
                    " join status s on s.id = food_order.status_id  where food_order.id=?";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setInt(1, Integer.parseInt(theOrderId));
            myRs = myStmt.executeQuery();
            if (myRs.next()) {
                foodOrderId = myRs.getInt("food_order.id");
                Timestamp orderDate = myRs.getTimestamp("order_date");
                int userId = myRs.getInt("user_id");
                String statusName = myRs.getString("status_name");
                UserDAO userDAO = UserDAO.getInstance();
                User user = userDAO.getUserByUserId(userId);
                List<Item> items = getOrderItems(foodOrderId);
                Order order = new Order(foodOrderId, orderDate, user, items, OrderStatus.getOrderStatus(statusName));
                return order;
            }
        } catch (SQLException throwables) {
            throw new DBException("Cannot get order by id " + theOrderId + " from database", throwables);
        } finally {
            close(myConn, myStmt, myRs);
        }
        return null;
    }

    public void updateOrder(int orderId, OrderStatus orderStatus) throws DBException {
        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {
            int statusId = getStatusId(orderStatus);
            myConn = dataSource.getConnection();
            String sql = "UPDATE food_order SET status_id =? WHERE id =? ;";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setInt(1, statusId);
            myStmt.setInt(2, orderId);
            myStmt.execute();
        } catch (SQLException throwables) {
            throw new DBException("Cannot update order in the database",throwables);
        }finally {
            close(myConn, myStmt, null);
        }

    }

    public void deleteOrder(String theOrderId) throws DBException {
        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {
            myConn = dataSource.getConnection();
            int orderId = Integer.parseInt(theOrderId);
            String sql = "delete from order_item where order_id=?";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setInt(1, orderId);
            myStmt.execute();
            sql = "delete from food_order where id=?";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setInt(1, orderId);
            myStmt.execute();
        } catch (SQLException throwables) {
            throw new DBException("Cannot delet order from database",throwables);
        }finally {
            close(myConn, myStmt, null);
        }
    }

/*    private void removeItem(Integer itemId) {
        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {
            String sql = "delete from item where id=?";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setInt(1, itemId);
            myStmt.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            close(myConn, myStmt, null);
        }
    }*/

    public List<OrderStatus> getStatuses() throws DBException {
        List<OrderStatus> statuses = new ArrayList<>();
        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;
        try {
            myConn = dataSource.getConnection();
            String sql = "select * from status;";
            myStmt = myConn.createStatement();
            myRs = myStmt.executeQuery(sql);
            while (myRs.next()) {
                String statusName = myRs.getString("status_name");
                OrderStatus orderStatus = OrderStatus.getOrderStatus(statusName);
                statuses.add(orderStatus);
            }
            return statuses;
        } catch (SQLException throwables) {
            throw new DBException("Cannot get all statuses from database",throwables);
        } finally {
            close(myConn, myStmt, myRs);
        }
    }
}















