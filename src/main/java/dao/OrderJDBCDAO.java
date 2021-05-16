package dao;
import entity.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderJDBCDAO {
    private String sqlTable = "jdbc/restaurant_system";
    private DataSource dataSource;
    private static volatile OrderJDBCDAO instance;

    private OrderJDBCDAO() {
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

    public static OrderJDBCDAO getInstance() {
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


    public List<Order> getOrders() throws Exception {
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
        } finally {
            close(myConn, myStmt, myRs);
        }
    }

    private List<Item> getOrderItems(int orderId) throws Exception {
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
        } finally {
            close(myConn, myStmt, null);
        }
    }

    private Item getItemById(int itemId) throws Exception {
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
                throw new Exception("Could not find item by id: " + itemId);
            }
            return item;
        } finally {
            close(myConn, myStmt, null);
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

    public void addItemToOrder(Order order, Item item) throws Exception {
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
        } finally {
            close(myConn, myStmt, null);
        }
    }

    private int getOrderId(Order order) {
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
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(myConn, myStmt, null);
        }
        return -1;
    }

    private int getItemId(Item item) throws SQLException {
        List<Item> items = null;
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
        } finally {
            close(myConn, myStmt, null);
        }
        return -1;
    }

    private void addItemToDataBase(Item item) throws SQLException {
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
        } finally {
            close(myConn, myStmt, null);
        }
    }

    public void addOrder(Order theOrder) {
        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {
            myConn = dataSource.getConnection();
            String sql = "insert into food_order( order_date, user_id, status_id) " +
                    "VALUES (?,?,?)";
            myStmt = myConn.prepareStatement(sql);
            //  myStmt.setInt(1, theOrder.getId());
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
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(myConn, myStmt, null);
        }
    }

    private int getStatusId(OrderStatus status) throws Exception {
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
                throw new Exception("Could not find status by name: " + status.value());
            }
        } finally {
            close(myConn, myStmt, null);
        }
    }

    public Order getOrder(String theOrderId){
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
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(myConn, myStmt, myRs);
        }
        return null;
    }

    public void updateOrder(int orderId, OrderStatus orderStatus) {
        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {
            int statusId = getStatusId(orderStatus);
            myConn = dataSource.getConnection();
            String sql = "UPDATE food_order SET status_id =? WHERE id =? ;";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setInt(1, statusId);
            myStmt.setInt(2,orderId);
            myStmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(myConn, myStmt, null);
        }

    }

    public void deleteOrder(String theOrderId) {
        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {
            myConn = dataSource.getConnection();
            int orderId = Integer.parseInt(theOrderId);


/*            String sql = "select item_id from order_item where order_id=?";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setInt(1, orderId);
            ResultSet resultSet = myStmt.executeQuery();
            List<Integer> itemsToRemove = new ArrayList<>();
            while (resultSet.next()) {
                int itemId = resultSet.getInt("item_id");
                itemsToRemove.add(itemId);
            }*/

            String sql = "delete from order_item where order_id=?";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setInt(1, orderId);
            myStmt.execute();
/*

            for (Integer itemId : itemsToRemove) {
                removeItem(itemId);
            }
*/

            sql = "delete from food_order where id=?";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setInt(1, orderId);
            myStmt.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            close(myConn, myStmt, null);
        }
    }

    private void removeItem(Integer itemId) {
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
    }

    public List<OrderStatus> getStatuses() {
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
        } catch (Exception throwables) {
            throwables.printStackTrace();
        } finally {
            close(myConn, myStmt, myRs);
        }
        return  null;
    }
}















