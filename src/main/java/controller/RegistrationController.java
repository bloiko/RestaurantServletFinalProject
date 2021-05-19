package controller;

import entity.*;
import dao.*;
import exception.DBException;
import service.OrderService;
import service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/RegistrationController")
public class RegistrationController extends HttpServlet {
    private UserDAO userDAO;
    private UserService userService;
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            orderService = new OrderService();
            userService = new UserService();
            userDAO = UserDAO.getInstance();
        } catch (Exception exc) {
            throw new ServletException(exc);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        int number = getNumberOfParametersInCookies(cookies);
        if (number == 5) {
            setCookiesToRequestParameters(request, cookies);
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("registration.jsp");
        requestDispatcher.include(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = getUserIfCorrectData(request);
        if (user == null) {
            request.setAttribute("first_name", request.getParameter("first_name"));
            request.setAttribute("last_name", request.getParameter("last_name"));
            request.setAttribute("email", request.getParameter("email"));
            request.setAttribute("address", request.getParameter("address"));
            request.setAttribute("phoneNumber", request.getParameter("phoneNumber"));
            request.setAttribute("command", "REDIRECT");
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("registration.jsp");
            requestDispatcher.include(request, response);
        } else {
            try {
                getUserCookiesAndSetToResponse(request, response);
                int userId = userService.addUserIfNotExistsAndReturnId(user);
                user.setId(userId);
                List<Item> cart = (List<Item>) session.getAttribute("cart");
                session.setAttribute("user", user);
                request.setAttribute("orderId", orderService.addOrderAndGetId(cart, user));
            } catch (DBException e) {
                e.printStackTrace();
            }
        }

        session.setAttribute("cart", new ArrayList<Item>());
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("thanks-page.jsp");
        requestDispatcher.forward(request, response);
    }

    private void setCookiesToRequestParameters(HttpServletRequest request, Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("first_name")) {
                request.setAttribute("first_name", cookie.getValue());
            }
            if (cookie.getName().equals("last_name")) {
                request.setAttribute("last_name", cookie.getValue());
            }
            if (cookie.getName().equals("email")) {
                request.setAttribute("email", cookie.getValue());
            }
            if (cookie.getName().equals("address")) {
                request.setAttribute("address", cookie.getValue());
            }
            if (cookie.getName().equals("phoneNumber")) {
                request.setAttribute("phoneNumber", cookie.getValue());
            }
        }
    }


    private void getUserCookiesAndSetToResponse(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookieFirstName = new Cookie("first_name", request.getParameter("first_name"));
        Cookie cookieLastName = new Cookie("last_name", request.getParameter("last_name"));
        Cookie cookieEmail = new Cookie("email", request.getParameter("email"));
        Cookie cookieAddress = new Cookie("address", request.getParameter("address"));
        Cookie cookiePhoneNumber = new Cookie("phoneNumber", request.getParameter("phoneNumber"));
        response.addCookie(cookieFirstName);
        response.addCookie(cookieLastName);
        response.addCookie(cookieEmail);
        response.addCookie(cookieAddress);
        response.addCookie(cookiePhoneNumber);
    }

    private int getNumberOfParametersInCookies(Cookie[] cookies) {
        int counter = 0;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("first_name")) {
                counter++;
            }
            if (cookie.getName().equals("last_name")) {
                counter++;
            }
            if (cookie.getName().equals("email")) {
                counter++;
            }
            if (cookie.getName().equals("address")) {
                counter++;
            }
            if (cookie.getName().equals("phoneNumber")) {
                counter++;
            }
        }
        return counter;
    }

    private User getUserIfCorrectData(HttpServletRequest request) {
        boolean isCorrect = true;
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String phoneNumber = request.getParameter("phoneNumber");
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || address.isEmpty() || phoneNumber.isEmpty()) {
            request.setAttribute("error_message", "The data can not be empty!");
            isCorrect = false;
        }
        //validate phone number
        String patterns = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";
        Pattern pattern = Pattern.compile(patterns);
        Matcher matcher = pattern.matcher(phoneNumber);
        if (!matcher.matches()) {
            request.setAttribute("phone_number_error_message", "Incorrect phone number format!");
            isCorrect = false;
        }

        //validate email
        patterns = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        pattern = Pattern.compile(patterns);
        matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            request.setAttribute("email_error_message", "Incorrect email format!");
            isCorrect = false;
        }
        if (isCorrect) {
            return new User(0, firstName, lastName, "", "", email, address, phoneNumber, "USER");
        } else {
            return null;
        }
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
}

