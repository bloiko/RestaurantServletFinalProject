package controller;

import exception.DBException;
import service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
/**
 * Login Main controller.
 * This login is used main part of the program. Also, after registration.
 *
 * @author B.Loiko
 *
 */
@WebServlet("/LoginMainController")
public class LoginMainController extends HttpServlet {
    public static final String COMMAND = "command";
    private UserService userService;
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            userService = new UserService();
        } catch (Exception exc) {
            throw new ServletException(exc);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(COMMAND,request.getAttribute(COMMAND));
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("login-main.jsp");
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        try {
            if (userService.isCorrectUser(username, password)) {
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                if ("ORDER_IN_CART".equals(session.getAttribute(COMMAND))) {
                    response.sendRedirect("/CartController?command=ORDER");
                    session.removeAttribute(COMMAND);
                } else {
                    response.sendRedirect("/FoodItemController");
                }
            } else {
                request.setAttribute("message", "Account's Invalid");
                response.sendRedirect("/LoginMainController");
            }
        } catch (DBException e) {
            e.printStackTrace();
        }
    }
}
