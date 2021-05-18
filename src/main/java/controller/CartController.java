package controller;

import entity.*;
import dao.*;
import service.CartService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/CartController")
public class CartController extends HttpServlet {
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        super.init();
        cartService = new CartService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String theCommand = request.getParameter("command");
        HttpSession session = request.getSession();
        if ("DELETE".equals(theCommand)) {
            List<Item> cart = (List<Item>) session.getAttribute("cart");
            String itemId = request.getParameter("itemId");
            cart = cartService.removeItemFromCart(cart, itemId);
            session.setAttribute("cart", cart);
        }
        request.getRequestDispatcher("cart.jsp").forward(request, response);

    }
}
