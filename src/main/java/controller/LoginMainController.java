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
 */
@WebServlet("/LoginMainController")
public class LoginMainController extends HttpServlet {
    public static final String COMMAND = "web/command";
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
        request.setAttribute(COMMAND, request.getAttribute(COMMAND));
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("login-main.jsp");
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        removePastErrorMessagesIfExist(session);
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        try {
            if (userService.isCorrectUser(username, password)) {
                session.setAttribute("username", username);
                if ("ORDER_IN_CART".equals(session.getAttribute(COMMAND))) {
                    session.removeAttribute(COMMAND);
                    response.sendRedirect("/CartController");
                } else {
                    response.sendRedirect("/FoodItemController");
                }
            } else {
                session.setAttribute("message", "Account's Invalid");
                response.sendRedirect("/LoginMainController");
            }
        } catch (DBException e) {
            e.printStackTrace();
        }
    }

    private void removePastErrorMessagesIfExist(HttpSession session) {
        String pastMessage = (String) session.getAttribute("message");
        if (pastMessage != null) {
            session.removeAttribute("message");
        }
    }
}
