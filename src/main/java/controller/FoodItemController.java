package controller;

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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@WebServlet("/FoodItemController")
public class FoodItemController extends HttpServlet {
    private static final int NUMBER_ITEMS_ON_PAGE = 5;
    private FoodItemService foodItemService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            foodItemService = new FoodItemService();
        } catch (Exception exc) {
            throw new ServletException(exc);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            String command = request.getParameter("command");
            HttpSession session = request.getSession();
            List<Item> cart = getCart(session);
            if ("ORDER".equals(command)) {
                String foodId = request.getParameter("foodId");
                foodItemService.addFoodItemToCart(cart, foodId);
                session.setAttribute("cart", cart);
                response.sendRedirect("/FoodItemController");
            } else {
                listFoodItems(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private List<Item> getCart(HttpSession session) {
        List<Item> cart = (List<Item>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    private void listFoodItems(HttpServletRequest request, HttpServletResponse response) throws DBException, ServletException, IOException, CannotFetchItemsException {
        HttpSession session = request.getSession();
        String filterBy = request.getParameter("filter");
        if (filterBy == null) {
            filterBy = (String) session.getAttribute("filter");
        }
        if (filterBy != null && filterBy.equals("all_categories")) {
            filterBy = null;
        }
        if (filterBy != null && !filterBy.isEmpty()) {
            session.setAttribute("page", 1);
        }
        session.setAttribute("filter", filterBy);

        String sort = request.getParameter("sort");
        String sessionSort = (String) session.getAttribute("sort");
        String order = (String) session.getAttribute("order");
        if (sort != null && sort.equals(sessionSort)) {
            if ("ASC".equals(order)) {
                order = "DESC";
            } else {
                order = "ASC";
            }
        } else if (sort == null && sessionSort != null) {
            sort = sessionSort;
        }
        if (order == null) {
            order = "ASC";
        }
        session.setAttribute("order", order);
        session.setAttribute("sort", sort);
        int page;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        } else if (session.getAttribute("page") != null) {
            page = (int) session.getAttribute("page");
        } else {
            page = 1;
        }
        List<FoodItem> foodItems = foodItemService.getFoodItems(page, NUMBER_ITEMS_ON_PAGE, sort, order, filterBy);
        int numOfPages = filterBy == null || filterBy.isEmpty() ? getNumOfPages(foodItemService.getFoodItems()) : getNumOfPages(foodItems);
        request.setAttribute("numberOfPages", numOfPages);
        session.setAttribute("page", page);
        request.setAttribute("categories", foodItemService.getCategories());
        request.setAttribute("FOOD_LIST", foodItems);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/list-food.jsp");
        dispatcher.forward(request, response);
    }

    private int getNumOfPages(List<FoodItem> foodItems) {
        int modOfTheDivision = foodItems.size() % NUMBER_ITEMS_ON_PAGE;
        int incorrectNumOfPages = foodItems.size() / NUMBER_ITEMS_ON_PAGE;
        int numOfPages = modOfTheDivision == 0 ? incorrectNumOfPages : incorrectNumOfPages + 1;
        return numOfPages;
    }
 /*   private void listFoodItems(HttpServletRequest request, HttpServletResponse response) throws CannotFetchItemsException, DBException, ServletException, IOException {
        HttpSession session = request.getSession();
        String filterBy = request.getParameter("filter");
        List<FoodItem> foodItems = foodItemService.getFoodItems();
        if (filterBy != null ) {
            foodItems = foodItems.stream().filter(foodItem -> foodItem.getCategory().getName().equals(filterBy)).collect(Collectors.toList());
            session.setAttribute("page", 1);//
        }
        session.setAttribute("menu", foodItems);

        String sort = request.getParameter("sort");
        String sessionSort = (String) session.getAttribute("sort");
        String order = (String) session.getAttribute("order");
        if (sort != null) {
            if (sort.equals("name")) {
                if ("name".equals(sessionSort) && "ASC".equals(order)) {
                    foodItems.sort(Comparator.comparing(FoodItem::getName).reversed());
                    session.setAttribute("order", "DESC");
                } else {
                    foodItems.sort(Comparator.comparing(FoodItem::getName));
                    session.setAttribute("order", "ASC");
                }
            } else if (sort.equals("price")) {
                if ("price".equals(sessionSort) && "ASC".equals(order)) {
                    foodItems.sort(Comparator.comparing(FoodItem::getPrice).reversed());
                    session.setAttribute("order", "DESC");
                } else {
                    foodItems.sort(Comparator.comparing(FoodItem::getPrice));
                    session.setAttribute("order", "ASC");
                }
            } else if (sort.equals("category")) {
                if ("category".equals(sessionSort) && "ASC".equals(order)) {
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
        request.setAttribute("categories", foodItemService.getCategories());

        List<FoodItem> shortFoodItems = foodItems.stream().skip((long) (page - 1) * NUMBER_ITEMS_ON_PAGE).limit(NUMBER_ITEMS_ON_PAGE).collect(Collectors.toList());

        int modOfTheDivision = foodItems.size() % NUMBER_ITEMS_ON_PAGE;
        int incorrectNumOfPages = foodItems.size() / NUMBER_ITEMS_ON_PAGE;
        int numOfPages = modOfTheDivision == 0 ? incorrectNumOfPages : incorrectNumOfPages + 1;

        request.setAttribute("numberOfPages", numOfPages);

        request.setAttribute("FOOD_LIST", shortFoodItems);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/list-food.jsp");
        dispatcher.forward(request, response);
    }*/

}













