package controller;

import entity.*;
import dao.*;
import exception.DBException;
import service.CartService;
import service.OrderService;
import service.UserService;

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
    private CartService cartService;
    private UserService userService;
    private OrderService orderService;

    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    public void init() throws ServletException {
        super.init();
        cartService = new CartService();
        try {
            userService = new UserService();
            orderService = new OrderService();
        } catch (DBException e) {
            e.printStackTrace();
        }
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
        } else if ("ORDER".equals(theCommand)) {
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
                response.sendRedirect("/LoginMainController?command=ORDER");
                return;
            }
            List<Item> cart = (List<Item>) session.getAttribute("cart");
            try {
                int orderId = orderService.addOrderAndGetId(cart, user);
                request.setAttribute("orderId", orderId);
                session.setAttribute("cart", new ArrayList<Order>());
                request.getRequestDispatcher("thanks-page.jsp").forward(request, response);
            } catch (DBException e) {
                e.printStackTrace();
            }
        }
        request.getRequestDispatcher("/CartController").forward(request, response);
    }
}
