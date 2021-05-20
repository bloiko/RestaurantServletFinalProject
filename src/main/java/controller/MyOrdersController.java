package controller;

import entity.Order;
import entity.User;
import exception.DBException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@WebServlet("/MyOrdersController")
public class MyOrdersController extends HttpServlet {
    private UserService userService;
    private static final Logger LOGGER = LogManager.getLogger(MyOrdersController.class);

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            userService = new UserService();
        } catch (Exception exc) {
            throw new ServletException(exc);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.debug("Controller starts");
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        LOGGER.trace("Session atribute : username"+ username);

        List<Order> orders = new LinkedList<>();
        if(username!=null) {
            try {
                orders = userService.getUserOrdersSortByOrderDateReversed(username);
            } catch (DBException e) {
                e.printStackTrace();
            }
        }
        request.setAttribute("ORDERS_LIST", orders);
        LOGGER.trace("Session atribute : username"+ username);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("my-orders.jsp");
        requestDispatcher.forward(request, response);
        LOGGER.debug("Controller finished");
    }

    /*private User getUserFromCookies(Cookie[] cookies) {
        User user = new User();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("first_name")) {
                user.setFirstName(cookie.getValue());
            }
            if (cookie.getName().equals("last_name")) {
                user.setLastName(cookie.getValue());
            }
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
        return user;
    }*/
}
