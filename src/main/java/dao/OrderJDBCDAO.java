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
    //@Resource(name = "jdbc/restaurant_system")
    private DataSource dataSource;
    private static OrderJDBCDAO instance;

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
        if (instance == null) {
            return instance = new OrderJDBCDAO();
        } else return instance;
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
                    " join order_item oi on food_order.id = oi.order_id" +
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
        List<Item> items = null;
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

    public void addItemToOrder(String theOrderId, Item item) throws SQLException {
        int orderId = Integer.parseInt(theOrderId);
        int itemId = item.getId();
        addItemToDataBase(item);
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

    public void addOrder(Order theOrder) throws Exception {
        Connection myConn = null;
        PreparedStatement myStmt = null;
        try {
            myConn = dataSource.getConnection();
            String sql = "insert into food_order(id, order_date, user_id, status_id)\n" +
                    "VALUES (?,?,?,?)";
            myStmt = myConn.prepareStatement(sql);
            myStmt.setInt(1, theOrder.getId());
            myStmt.setTimestamp(2, theOrder.getOrderDate());
            myStmt.setInt(3, theOrder.getUser().getId());
            myStmt.setInt(4, getStatusId(theOrder.getStatus()));
            myStmt.execute();
            for (Item item : theOrder.getItems()){
                addItemToOrder(String.valueOf(theOrder.getId()),item);
            }
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

    public Order getOrder(String theOrderId) throws Exception {
       return new Order();
    }

    public void updateOrder(Order theOrder) throws Exception {


    }

    public void deleteOrder(String theOrderId) throws Exception {

    }
}















