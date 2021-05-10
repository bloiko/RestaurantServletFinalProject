package controller;

import dao.OrderJDBCDAO;
import dao.OrderListDAO;
import entity.Item;
import entity.Order;
import entity.OrderStatus;
import entity.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet("/AdminController")
public class AdminController extends HttpServlet {
    private OrderJDBCDAO orderListDAO = OrderJDBCDAO.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String command = request.getParameter("command");
        if ("LIST".equals(command) || command==null) {

        } else if ("CHANGE_STATUS".equals(command)) {
            String orderIdString = request.getParameter("orderId");
            Order order;
            try {
                order = orderListDAO.getOrder(orderIdString);
                OrderStatus newStatus = order.getStatus().nextStatus();
                order.setStatus(newStatus);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("DELETE".equals(command)) {
            String orderIdString = request.getParameter("orderId");
            Order order;
            try {
                orderListDAO.deleteOrder(orderIdString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            List<Order> orders = orderListDAO.getOrders();
            request.setAttribute("ORDERS_LIST", orders);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("admin.jsp");
        requestDispatcher.forward(request, response);
    }
}
