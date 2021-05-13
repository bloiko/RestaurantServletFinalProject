package controller;

import entity.*;
import dao.*;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/RegistrationController")
public class RegistrationController extends HttpServlet {
    private OrderJDBCDAO orderListDAO = OrderJDBCDAO.getInstance();
    private UserDAO userListDAO = UserDAO.getInstance();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        User user = getUserIfCorrectData(request, response);
        if (user == null) {
            request.setAttribute("first_name",request.getParameter("first_name"));
            request.setAttribute("last_name",request.getParameter("last_name"));
            request.setAttribute("email",request.getParameter("email"));
            request.setAttribute("address",request.getParameter("address"));
            request.setAttribute("phoneNumber",request.getParameter("phoneNumber"));            RequestDispatcher requestDispatcher = request.getRequestDispatcher("registration.jsp");
            requestDispatcher.include(request, response);
        } else {
            UserDAO userDAO = UserDAO.getInstance();
            try {
                userDAO.addUser(user);
                userListDAO.addUser(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
            HttpSession session = request.getSession();
            List<Item> cart = (List<Item>) session.getAttribute("cart");
            session.setAttribute("user", user);
            Order order = new Order();
            try {
                order.setId(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            order.setUser(user);
            order.setOrderDate(new Timestamp(new Date().getTime()));
            order.setStatus(OrderStatus.WAITING);
            if (cart != null && cart.size() != 0) {
                order.setItems(cart);
                try {
                    orderListDAO.addOrder(order);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            session.setAttribute("cart", new ArrayList<Item>());
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("login.jsp");
            requestDispatcher.forward(request, response);
        }

    }

    private User getUserIfCorrectData(HttpServletRequest request, HttpServletResponse response) {
        boolean isCorrect = true;
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String phoneNumber = request.getParameter("phoneNumber");
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || address.isEmpty() || phoneNumber.isEmpty()) {
            request.setAttribute("error_message", "The data can not be empty!");
            isCorrect = false;
        }
        //validate phone number
        String patterns
                = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";
        Pattern pattern = Pattern.compile(patterns);
        Matcher matcher = pattern.matcher(phoneNumber);
        if (!matcher.matches()) {
            request.setAttribute("phone_number_error_message", "Incorrect phone number format!");
            isCorrect = false;
        }
        if (isCorrect) {
            return new User(0, firstName, lastName, "", "", email, address, phoneNumber, "USER");
        } else {
            return null;
        }
    }
}

