package controller;

import dao.OrderJDBCDAO;
import entity.Order;
import entity.OrderStatus;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/AdminController")
public class AdminController extends HttpServlet {
    private OrderJDBCDAO orderListDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            orderListDAO = OrderJDBCDAO.getInstance();
        } catch (Exception exc) {
            throw new ServletException(exc);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String command = request.getParameter("command");
        if ("DELETE".equals(command)) {
            String orderIdString = request.getParameter("orderId");
            try {
                orderListDAO.deleteOrder(orderIdString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            List<Order> orders = orderListDAO.getOrders();
            List<OrderStatus> orderStatuses = orderListDAO.getStatuses();
            request.setAttribute("statusList", orderStatuses);
            List<Order> notDoneOrders = orders.stream()
                    .filter(order -> !order.getStatus().equals(OrderStatus.DONE))
                    .sorted(Comparator.comparing(Order::getId).reversed())
                    .collect(Collectors.toList());
            request.setAttribute("NOT_DONE_ORDERS_LIST", notDoneOrders);
            List<Order> doneOrders = orders.stream()
                    .filter(order -> order.getStatus().equals(OrderStatus.DONE))
                    .collect(Collectors.toList());
            request.setAttribute("DONE_ORDERS_LIST", doneOrders);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("admin.jsp");
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String statusName = request.getParameter("status");
        String orderIdString = request.getParameter("orderId");
        Order order;
        try {
            order = orderListDAO.getOrder(orderIdString);
            OrderStatus newStatus = OrderStatus.getOrderStatus(statusName);
            orderListDAO.updateOrder(order.getId(), newStatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
        doGet(request, response);
    }

    public void setOrderListDAO(OrderJDBCDAO orderListDAO) {
        this.orderListDAO = orderListDAO;
    }
}
