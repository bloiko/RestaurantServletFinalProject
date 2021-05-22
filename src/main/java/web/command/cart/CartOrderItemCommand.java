package web.command.cart;

import database.entity.Item;
import database.entity.Order;
import database.entity.User;
import exception.DBException;
import org.apache.log4j.Logger;
import service.OrderService;
import service.UserService;
import web.command.Command;

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
    private static final Logger log = Logger.getLogger(CartOrderItemCommand.class);

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
        log.debug("Command starts");
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        log.trace("Set attribute to the session: username --> "+username);

        User user = null;
        if (username != null) {
            try {
                user = userService.getUserByUserName(username);
                log.info("User with username "+username+" was taken from the database");
            } catch (DBException e) {
                e.printStackTrace();
            }
        } else {
            session.setAttribute("command", "ORDER_IN_CART");
            log.trace("Set attribute to the session: command --> "+"ORDER_IN_CART");

            log.debug("Command finished");
            return"login-main.jsp";
        }

        try {
            List<Item> cart = (List<Item>) session.getAttribute("cart");
            int orderId = orderService.addOrderAndGetId(cart, user);
            request.setAttribute("orderId", orderId);
            log.trace("Set attribute to the request: orderId --> "+orderId);

            session.setAttribute("cart", new ArrayList<Order>());
            log.trace("Set attribute to the request: cart --> new list with 0 size");

            log.debug("Command finished");
            return "thanks-page.jsp";
        } catch (DBException e) {
            e.printStackTrace();
        }

        log.debug("Command finished");
        return "cart.jsp";
    }
}
