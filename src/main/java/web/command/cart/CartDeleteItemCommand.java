package web.command.cart;

import database.entity.Item;
import service.CartService;
import web.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Cart controller.
 *
 * @author B.Loiko
 */

public class CartDeleteItemCommand extends Command {
    private CartService cartService;

    public CartDeleteItemCommand() throws ServletException {
        init();
    }

    @Override
    public void init() throws ServletException {
        cartService = new CartService();
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
