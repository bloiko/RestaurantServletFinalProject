package controller;

import dao.FoodJDBCDAO;
import entity.FoodItem;
import entity.Item;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@WebServlet("/FoodItemController")
public class FoodItemController extends HttpServlet {
    private static final int NUMBER_ITEMS_ON_PAGE = 5;


    private FoodJDBCDAO foodItemDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            foodItemDAO = FoodJDBCDAO.getInstance();
        } catch (Exception exc) {
            throw new ServletException(exc);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String command = request.getParameter("command");
        HttpSession session = request.getSession();
        List<Item> cart;
           if (session.getAttribute("cart") == null) {
                cart = new ArrayList<>();
                session.setAttribute("cart", cart);
            }
        if ("ORDER".equals(command)) {
            String foodId = request.getParameter("foodId");

            if (session.getAttribute("cart") == null) {
                cart = new ArrayList<>();
                try {
                    cart.add(new Item(1, foodItemDAO.getFoodItem(foodId), 1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                session.setAttribute("cart", cart);
            } else {
                cart = (List<Item>) session.getAttribute("cart");
                int index = isExisting(Integer.parseInt(foodId), cart);
                if (index == -1) {
                    try {
                        cart.add(new Item(1, foodItemDAO.getFoodItem(foodId), 1));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    int quantity = cart.get(index).getQuantity() + 1;
                    cart.get(index).setQuantity(quantity);
                }
                session.setAttribute("cart", cart);
            }
            try {
                response.sendRedirect("/FoodItemController");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                listFoodItems(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void listFoodItems(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        HttpSession session = request.getSession();
        List<FoodItem> foodItems = null;
        if (session.getAttribute("menu") != null) {
            foodItems = (List<FoodItem>) session.getAttribute("menu");
        } else {
            foodItems = foodItemDAO.getFoodItems();
        }
        String sort = request.getParameter("sort");
        String sessionSort = (String) session.getAttribute("sort");
        String order = (String) session.getAttribute("order");
        if (sort != null) {
            if (sort.equals("NAME")) {
                if ("NAME".equals(sessionSort) && "ASC".equals(order)) {
                    foodItems.sort(Comparator.comparing(FoodItem::getName).reversed());
                    session.setAttribute("order", "DESC");
                } else {
                    foodItems.sort(Comparator.comparing(FoodItem::getName));
                    session.setAttribute("order", "ASC");
                }
            } else if (sort.equals("PRICE")) {
                if ("PRICE".equals(sessionSort) && "ASC".equals(order)) {
                    foodItems.sort(Comparator.comparing(FoodItem::getPrice).reversed());
                    session.setAttribute("order", "DESC");
                } else {
                    foodItems.sort(Comparator.comparing(FoodItem::getPrice));
                    session.setAttribute("order", "ASC");
                }
            } else if (sort.equals("CATEGORY") ) {
                if ("CATEGORY".equals(sessionSort)&& "ASC".equals(order)) {
                    foodItems.sort(Comparator.comparing(FoodItem::getCategory).reversed());
                    session.setAttribute("order", "DESC");
                } else {
                    foodItems.sort(Comparator.comparing(FoodItem::getCategory));
                    session.setAttribute("order", "ASC");
                }
            }
            session.setAttribute("menu", foodItems);
            session.setAttribute("sort", sort);
        }
        int page;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        } else if (session.getAttribute("page") != null) {
            page = (int) session.getAttribute("page");
        } else {
            page = 1;
        }
        session.setAttribute("page", page);
        List<FoodItem> shortFoodItems = foodItems.stream().skip((page - 1) * NUMBER_ITEMS_ON_PAGE).limit(NUMBER_ITEMS_ON_PAGE).collect(Collectors.toList());
        request.setAttribute("FOOD_LIST", shortFoodItems);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/list-food.jsp");
        dispatcher.forward(request, response);
    }

    private int isExisting(int id, List<Item> cart) {
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getFoodItem().getId() == id) {
                return i;
            }
        }
        return -1;
    }
    public void setFoodItemDAO(FoodJDBCDAO foodItemDAO) {
       this.foodItemDAO = foodItemDAO;
    }

}













