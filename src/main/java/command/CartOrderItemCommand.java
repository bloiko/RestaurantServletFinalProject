package command;

import entity.Item;
import entity.Order;
import entity.User;
import exception.DBException;
import service.CartService;
import service.OrderService;
import service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Cart controller.
 *
 * @author B.Loiko
 */

public class CartOrderItemCommand extends Command {
    private UserService userService;
    private OrderService orderService;
    public CartOrderItemCommand() throws ServletException {
        init();
    }
    @Override
    public void init() throws ServletException {
        try {
            userService = new UserService();
            orderService = new OrderService();
        } catch (DBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        User user = null;
        if (username != null) {
            try {
                user = userService.getUserByUserName(username);
            } catch (DBException e) {
                e.printStackTrace();
            }
        } else {
            session.setAttribute("command", "ORDER_IN_CART");
            return"login-main.jsp";
        }
        try {
            List<Item> cart = (List<Item>) session.getAttribute("cart");
            int orderId = orderService.addOrderAndGetId(cart, user);
            request.setAttribute("orderId", orderId);
            session.setAttribute("cart", new ArrayList<Order>());
            return "thanks-page.jsp";
        } catch (DBException e) {
            e.printStackTrace();
        }
        return "cart.jsp";
    }
}
