package controller;

import entity.*;
import exception.DBException;
import service.OrderService;
import service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Registration controller.
 * This controller show registration page and proccess dota from the registration form.
 *
 * @author B.Loiko
 *
 */
@WebServlet("/RegistrationController")
public class RegistrationController extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            userService = new UserService();
        } catch (DBException exc) {
            throw new ServletException(exc);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("registration.jsp");
        requestDispatcher.include(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = getUserIfCorrectData(request);
        if (user == null) {
            request.setAttribute("first_name", request.getParameter("first_name"));
            request.setAttribute("last_name", request.getParameter("last_name"));
            request.setAttribute("email", request.getParameter("email"));
            request.setAttribute("address", request.getParameter("address"));
            request.setAttribute("phoneNumber", request.getParameter("phoneNumber"));
            request.setAttribute("username", request.getParameter("username"));
            request.setAttribute("password", request.getParameter("password"));
            request.setAttribute("command", "REDIRECT");
            doGet(request, response);
        } else {
            try {
                userService.addUserIfNotExistsAndReturnId(user);
            } catch (DBException e) {
                e.printStackTrace();
            }
            response.sendRedirect("login-main.jsp");
        }
    }

    private User getUserIfCorrectData(HttpServletRequest request) {
        boolean isCorrect = true;
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String phoneNumber = request.getParameter("phoneNumber");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || address.isEmpty() || phoneNumber.isEmpty()||username.isEmpty()||password.isEmpty()) {
            request.setAttribute("error_message", "The data can not be empty!");
            isCorrect = false;
        }
        if (password.length()<8) {
            request.setAttribute("password_error_message", "Password should has at least 8 symbols!");
            isCorrect = false;
        }
        if (!userService.isCorrectPhoneNumber(phoneNumber)) {
            request.setAttribute("phone_number_error_message", "Incorrect phone number format!");
            isCorrect = false;
        }
        if (!userService.isCorrectEmail(email)) {
            request.setAttribute("email_error_message", "Incorrect email format!");
            isCorrect = false;
        }
        if (isCorrect) {
            return new User(0, firstName, lastName, username, password, email, address, phoneNumber, "USER");
        } else {
            return null;
        }
    }
}

