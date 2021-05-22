package web.command.cart;

import database.entity.Item;
import org.apache.log4j.Logger;
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
    private static final Logger log = Logger.getLogger(CartDeleteItemCommand.class);


    public CartDeleteItemCommand() throws ServletException {
        init();
    }

    @Override
    public void init() throws ServletException {
        cartService = new CartService();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("Command starts");
        HttpSession session = request.getSession();
        List<Item> cart = (List<Item>) session.getAttribute("cart");
        log.trace("Get attribute from the session: cart -->"+cart);

        String itemId = request.getParameter("itemId");
        log.trace("Get parameter from the request: itemId -->"+itemId);

        cart = cartService.removeItemFromCart(cart, itemId);
        log.trace("Remove item "+itemId+" from the cart using CartService");

        session.setAttribute("cart", cart);
        log.trace("Set attribute to the request: cart -->"+cart);

        log.debug("Command finished");
        return "cart.jsp";
    }
}
