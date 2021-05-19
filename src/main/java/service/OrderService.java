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
        if (cart != null && cart.size() != 0) {
            order.setItems(cart);
            orderDAO.addOrder(order);
        }
        return orderDAO.getOrderId(order);
    }
}