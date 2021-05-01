package dao;

import entity.FoodItem;
import entity.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderDAO{
    private List<Order> orderList;

    public OrderDAO() {
        orderList = new ArrayList<>();

    }

    public List<Order> getOrders() throws Exception {
        return orderList;

    }

    public void addFoodItemToOrder(String theOrderId,FoodItem foodItem){
        long orderId = Integer.parseInt(theOrderId);
        for (Order order : orderList) {
            if (order.getId() == orderId) {
                order.addFoodItem(foodItem);
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















