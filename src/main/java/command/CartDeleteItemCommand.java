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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
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

public class CartDeleteItemCommand extends Command {
    private CartService cartService;
    private UserService userService;
    private OrderService orderService;

    public CartDeleteItemCommand() throws ServletException {
        init();
    }

    @Override
    public void init() throws ServletException {
        cartService = new CartService();
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
        List<Item> cart = (List<Item>) session.getAttribute("cart");
        String itemId = request.getParameter("itemId");
        cart = cartService.removeItemFromCart(cart, itemId);
        session.setAttribute("cart", cart);
        return "cart.jsp";

    }
}
