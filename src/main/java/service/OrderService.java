package service;


import database.dao.OrderDAO;
import database.entity.Item;
import database.entity.Order;
import database.entity.OrderStatus;
import database.entity.User;
import exception.DBException;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


public class OrderService {
    private OrderDAO orderDAO;

    public OrderService(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    public OrderService() throws DBException {
        orderDAO = OrderDAO.getInstance();
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

    /*    public void deleteOrder(String orderId) throws DBException {
            orderDAO.deleteOrder(orderId);
        }*/
    public List<OrderStatus> getStatuses() throws DBException {
        return orderDAO.getStatuses();
    }

    public List<Order> getDoneOrders() throws DBException {
        return orderDAO.getDoneOrders();
    }

    public List<Order> getNotDoneOrdersSortById() throws DBException {
        return orderDAO.getNotDoneOrdersSortById();
    }

    public Order getOrder(String orderIdString) throws DBException {
        return orderDAO.getOrder(orderIdString);
    }

    public void updateOrder(int id, OrderStatus newStatus) throws DBException {
        orderDAO.updateOrder(id, newStatus);
    }
}