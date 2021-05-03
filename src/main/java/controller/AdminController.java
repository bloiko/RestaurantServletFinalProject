package controller;

import dao.OrderDAO;
import entity.Item;
import entity.Order;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet("/AdminController")
public class AdminController extends HttpServlet {
    OrderDAO orderDAO = OrderDAO.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<Item> cart = (List<Item>) session.getAttribute("cart");
        Order order = new Order();
        order.setOrderDate(new Date());
        if (cart.size() != 0) {
            order.setItems(cart);

            try {
                orderDAO.addOrder(order);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        session.setAttribute("cart", new ArrayList<Item>());
        try {
            request.setAttribute("ORDERS_LIST", orderDAO.getOrders());
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("admin.jsp");
        requestDispatcher.forward(request, response);
    }
}
