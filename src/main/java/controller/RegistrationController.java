package controller;

import dao.UserDAO;
import dao.UserListDAO;
import entity.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/RegistrationController")
public class RegistrationController extends HttpServlet {
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
        if(firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() ||
                password.isEmpty() || address.isEmpty() || phoneNumber.isEmpty())
        {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("registration.jsp");
            requestDispatcher.include(request, response);
        }
        else
        {
            User user = new User(1,firstName,lastName,username,password,email,address,phoneNumber,"USER");
            try {
                userListDAO.addUser(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("login.jsp");
            requestDispatcher.forward(request, response);
        }
    }
}
