package controller;

import dao.OrderJDBCDAO;
import dao.UserDAO;
import dao.UserListDAO;
import entity.Item;
import entity.Order;
import entity.OrderStatus;
import entity.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet("/RegistrationController")
public class RegistrationController extends HttpServlet {
    private OrderJDBCDAO orderListDAO = OrderJDBCDAO.getInstance();
    private UserDAO userListDAO = UserDAO.getInstance();
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String phoneNumber = request.getParameter("phoneNumber");

        //Validation
        //@TO_DO
        if(firstName.isEmpty() || lastName.isEmpty() ||  address.isEmpty() || phoneNumber.isEmpty())
        {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("registration.jsp");
            requestDispatcher.include(request, response);
        }
        else
        {
            User user = new User(1,firstName,lastName,"","",email,address,phoneNumber,"USER");
            try {
                userListDAO.addUser(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
            HttpSession session = request.getSession();
            List<Item> cart = (List<Item>) session.getAttribute("cart");
            session.setAttribute("user",user);
            Order order = new Order();
            try {
                order.setId(orderListDAO.getOrders().size()+1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            order.setUser(user);
            order.setOrderDate(new Timestamp(new Date().getTime()));
            order.setStatus(OrderStatus.WAITING);
            if (cart!=null && cart.size() != 0) {
                order.setItems(cart);
                try {
                    orderListDAO.addOrder(order);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            session.setAttribute("cart", new ArrayList<Item>());
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("login.jsp");
            requestDispatcher.forward(request, response);
        }
    }
}
