package controller;

import dao.OrderJDBCDAO;
import dao.UserDAO;
import entity.Order;
import entity.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
@WebServlet("/MyOrdersController")
public class MyOrdersController extends HttpServlet {
    private OrderJDBCDAO orderListDAO;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            userDAO = UserDAO.getInstance();
            orderListDAO = OrderJDBCDAO.getInstance();
        } catch (Exception exc) {
            throw new ServletException(exc);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = new User();
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("first_name")) {
                    user.setFirstName(cookie.getValue());
                }
                if (cookie.getName().equals("last_name")) {
                    user.setLastName(cookie.getValue());                }
                if (cookie.getName().equals("email")) {
                    user.setEmail(cookie.getValue());
                }
                if (cookie.getName().equals("address")) {
                    user.setAddress(cookie.getValue());
                }
                if (cookie.getName().equals("phoneNumber")) {
                    user.setPhoneNumber(cookie.getValue());
                }
            }
            int userId = userDAO.getUserId(user);
            List<Order> orders = orderListDAO.getOrdersByUserId(userId);
            orders.sort(Comparator.comparing(Order::getOrderDate).reversed());
            request.setAttribute("ORDERS_LIST", orders);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("my-orders.jsp");
        requestDispatcher.forward(request, response);
    }
}
