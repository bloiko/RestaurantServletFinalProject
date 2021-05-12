package controller;

import dao.FoodJDBCDAO;
import entity.FoodItem;
import entity.Item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;


@WebServlet("/FoodItemController")
public class FoodItemController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public static final int NUMBER_ITEMS_ON_PAGE = 5;

    private FoodJDBCDAO foodItemDAO;

    @Resource(name = "jdbc/web_FoodItem_tracker")
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            foodItemDAO = FoodJDBCDAO.getInstance();
        } catch (Exception exc) {
            throw new ServletException(exc);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            String command = request.getParameter("command");
            HttpSession session = request.getSession();
            List<Item> cart;
            if (session.getAttribute("cart") == null) {
                cart = new ArrayList<>();
                session.setAttribute("cart", cart);
            }
            if ("LIST".equals(command)) {
                listFoodItems(request, response);
            } else if ("ORDER".equals(command)) {
                String foodId = request.getParameter("foodId");

                if (session.getAttribute("cart") == null) {
                    cart = new ArrayList<>();
                    try {
                        cart.add(new Item(1,foodItemDAO.getFoodItem(foodId), 1));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    session.setAttribute("cart", cart);
                } else {
                    cart = (List<Item>) session.getAttribute("cart");
                    int index = isExisting(Integer.parseInt(foodId), cart);
                    if (index == -1) {
                        try {
                            cart.add(new Item(1,foodItemDAO.getFoodItem(foodId), 1));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        int quantity = cart.get(index).getQuantity() + 1;
                        cart.get(index).setQuantity(quantity);
                    }
                    session.setAttribute("cart", cart);
                }
                // request.getRequestDispatcher("list-food.jsp").forward(request,response);
               // response.sendRedirect("/FoodItemController");
            } else {
                listFoodItems(request, response);
            }

        } catch (Exception exc) {
            throw new ServletException(exc);
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
                if("NAME".equals(sessionSort) && "ASC".equals(order)){
                    foodItems.sort(Comparator.comparing(FoodItem::getName).reversed());
                    session.setAttribute("order","DESC");
                }else {
                    foodItems.sort(Comparator.comparing(FoodItem::getName));
                    session.setAttribute("order", "ASC");
                }
            } else if (sort.equals("PRICE")) {
                if("PRICE".equals(sessionSort)&& "ASC".equals(order)){
                    foodItems.sort(Comparator.comparing(FoodItem::getPrice).reversed());
                    session.setAttribute("order","DESC");
                }else {
                    foodItems.sort(Comparator.comparing(FoodItem::getPrice));
                    session.setAttribute("order", "ASC");
                }
            } else if (sort.equals("CATEGORY")&& "ASC".equals(order)) {
                if("CATEGORY".equals(sessionSort)){
                    foodItems.sort(Comparator.comparing(FoodItem::getCategory).reversed());
                    session.setAttribute("order","DESC");
                }else {
                    foodItems.sort(Comparator.comparing(FoodItem::getCategory));
                    session.setAttribute("order", "ASC");
                }
            }
            session.setAttribute("menu", foodItems);
            session.setAttribute("sort",sort);
        }

        int page;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }else if (session.getAttribute("page") != null) {
            page = (int) session.getAttribute("page");
        }else {
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
	/*private void deleteFoodItem(HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		// read FoodItem id from form data
		String theFoodItemId = request.getParameter("foodItemId");

		// delete FoodItem from database
		foodItemDAO.deleteFoodItem(theFoodItemId);

		// send them back to "list FoodItems" page
		listFoodItems(request, response);
	}

	private void updateFoodItem(HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		// read FoodItem info from form data
		int id = Integer.parseInt(request.getParameter("foodItemId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");

		// create a new FoodItem object
		FoodItem theFoodItem = new FoodItem(id, firstName, lastName, email);

		// perform update on database
		foodItemDAO.updateFoodItem(theFoodItem);

		// send them back to the "list FoodItems" page
		listFoodItems(request, response);

	}

	private void loadFoodItem(HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		// read FoodItem id from form data
		String theFoodItemId = request.getParameter("FoodItemId");

		// get FoodItem from database (db util)
		FoodItem theFoodItem = foodItemDAO.getFoodItem(theFoodItemId);

		// place FoodItem in the request attribute
		request.setAttribute("THE_FoodItem", theFoodItem);

		// send to jsp page: update-FoodItem-form.jsp
		RequestDispatcher dispatcher =
				request.getRequestDispatcher("/update-FoodItem-form.jsp");
		dispatcher.forward(request, response);
	}

	private void addFoodItem(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// read FoodItem info from form data
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");

		// create a new FoodItem object
		FoodItem theFoodItem = new FoodItem(firstName, lastName, email);

		// add the FoodItem to the database
		foodItemDAO.addFoodItem(theFoodItem);

		// send back to main page (the FoodItem list)
		listFoodItems(request, response);
	}*/


}













