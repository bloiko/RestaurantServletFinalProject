package service;

import dao.OrderJDBCDAO;
import entity.Item;
import entity.Order;
import entity.OrderStatus;
import entity.User;
import exception.DBException;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class OrderService {
    private OrderJDBCDAO orderDAO;

    public OrderService() throws DBException {
        orderDAO = OrderJDBCDAO.getInstance();
    }

    public int addOrderAndGetId(List<Item> cart, User user) throws DBException {
        Order order = new Order();
        order.setId(0);
        order.setUser(user);
        order.setOrderDate(new Timestamp(new Date().getTime()));
        order.setStatus(OrderStatus.WAITING);
        if (cart != null && !cart.isEmpty()) {
            order.setItems(cart);
            orderDAO.addOrder(order);
        }
        return orderDAO.getOrderId(order);
    }
    public void deleteOrder(String orderId) throws DBException {
        orderDAO.deleteOrder(orderId);
    }
    public List<OrderStatus> getStatuses() throws DBException {
        return orderDAO.getStatuses();
    }
   public List<Order> getDoneOrders() throws DBException {
        return orderDAO.getDoneOrders();
    }
    public List<Order> getNotDoneOrders() throws DBException {
        return orderDAO.getNotDoneOrders();
    }
}