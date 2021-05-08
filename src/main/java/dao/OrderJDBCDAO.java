package dao;

import entity.Item;
import entity.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderJDBCDAO {
    private List<Order> orderList;
    private static OrderJDBCDAO instance;

    private OrderJDBCDAO() {
        orderList = new ArrayList<>();

    }

    public static OrderJDBCDAO getInstance() {
        if (instance == null) {
            return instance = new OrderJDBCDAO();
        } else return instance;
    }


    public List<Order> getOrders() throws Exception {
        return orderList;

    }

    public void addItemToOrder(String theOrderId, Item item){
        long orderId = Integer.parseInt(theOrderId);
        for (Order order : orderList) {
            if (order.getId() == orderId) {
                order.addFoodItem(item);
            }
        }
    }
    public void addOrder(Order theOrder) throws Exception {
        orderList.add(theOrder);
    }

    public Order getOrder(String theOrderId) throws Exception {
        long orderId = Integer.parseInt(theOrderId);
        for (Order Order : orderList) {
            if (Order.getId() == orderId) {
                return Order;
            }
        }
        return null;
    }

    public void updateOrder(Order theOrder) throws Exception {
        for (Order Order : orderList) {
            if (Order.getId() == theOrder.getId()) {
                Order = theOrder;
            }
        }

    }

    public void deleteOrder(String theOrderId) throws Exception {
        int id = Integer.parseInt(theOrderId);
        Order OrderToDelete = null;
        for (Order Order : orderList) {
            if (Order.getId() == id) {
                OrderToDelete = Order;
            }
        }
        if (OrderToDelete != null) {
            orderList.remove(OrderToDelete);
        }
    }
}















