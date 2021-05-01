package controller;

import dao.FoodDAOInterface;
import dao.FoodListDAO;
import entity.FoodItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    private FoodDAOInterface foodItemDAO;

    @Resource(name = "jdbc/web_FoodItem_tracker")
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            foodItemDAO = new FoodListDAO();
        } catch (Exception exc) {
            throw new ServletException(exc);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            String theCommand = request.getParameter("command");

            // if the command is missing, then default to listing FoodItems
            if (theCommand == null) {
                theCommand = "LIST";
            }
            HttpSession session = request.getSession();
            // route to the appropriate method
            switch (theCommand) {

                case "LIST":
                    listFoodItems(request, response);
                    break;
                case "ORDER":
                    List<FoodItem> cart;
                    if (session.getAttribute("cart") == null) {
                        cart = new ArrayList<>();
                    } else {
                        cart = (List<FoodItem>) session.getAttribute("cart");
                    }
                    String foodId = request.getParameter("foodId");
                    cart.add(foodItemDAO.getFoodItem(foodId));
                    session.setAttribute("cart", cart);
                    request.getRequestDispatcher("cart.jsp").forward(request,response);
                    break;
			/*
			case "ADD":
				addFoodItem(request, response);
				break;
				
			case "LOAD":
				loadFoodItem(request, response);
				break;
				
			case "UPDATE":
				updateFoodItem(request, response);
				break;
			
			case "DELETE":
				deleteFoodItem(request, response);
				break;
			*/
                default:
                    listFoodItems(request, response);
            }

        } catch (Exception exc) {
            throw new ServletException(exc);
        }

    }

    private void listFoodItems(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        List<FoodItem> foodItems = foodItemDAO.getFoodItems();
        String sort = request.getParameter("sort");
        if (sort != null) {
            if (sort.equals("NAME")) {
                foodItems.sort(Comparator.comparing(FoodItem::getName));
            } else if (sort.equals("PRICE")) {
                foodItems.sort(Comparator.comparing(FoodItem::getPrice));
            } else if (sort.equals("CATEGORY")) {
                foodItems.sort(Comparator.comparing(FoodItem::getItemCategory));
            }
        }

        request.setAttribute("FOOD_LIST", foodItems);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/list-food.jsp");
        dispatcher.forward(request, response);
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













