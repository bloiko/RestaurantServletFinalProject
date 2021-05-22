package command;

import entity.FoodItem;
import entity.Item;
import exception.CannotFetchItemsException;
import exception.DBException;
import service.FoodItemService;

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
 * Food Item controller.
 *
 * @author B.Loiko
 */
public class MenuOrderCommand extends Command {
    private FoodItemService foodItemService;
    public MenuOrderCommand() throws ServletException {
        init();
    }
    @Override
    public void init() throws ServletException {
        try {
            foodItemService = new FoodItemService();
        } catch (Exception exc) {
            throw new ServletException(exc);
        }
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        List<Item> cart = getCart(session);
        String foodId = request.getParameter("foodId");
        try {
            foodItemService.addFoodItemToCart(cart, foodId);
        } catch (DBException e) {
            e.printStackTrace();
        }
        session.setAttribute("cart", cart);

        return "/controller?command=menuList";
    }

    private List<Item> getCart(HttpSession session) {
        List<Item> cart = (List<Item>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }
}













