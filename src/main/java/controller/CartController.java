package controller;

import dao.FoodDAOInterface;
import dao.FoodJDBCDAO;
import dao.FoodListDAO;
import entity.FoodItem;
import entity.Item;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/CartController")
public class CartController extends HttpServlet {
    private FoodDAOInterface foodItemDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            foodItemDAO = FoodJDBCDAO.getInstance();
        } catch (Exception exc) {
            throw new ServletException(exc);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String theCommand = request.getParameter("command");
         if (theCommand.equals("DELETE")) {
            String itemId = request.getParameter("itemId");
            HttpSession session = request.getSession();
            List<Item> cart = (List<Item>) session.getAttribute("cart");
            int index = isExisting(Integer.parseInt(itemId), cart);
            cart.remove(index);
            session.setAttribute("cart", cart);
            request.getRequestDispatcher("cart.jsp").forward(request, response);
        }
    }
    private int isExisting(int id, List<Item> cart) {
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getFoodItem().getId() == id) {
                return i;
            }
        }
        return -1;
    }

}
