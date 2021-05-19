package controller;

import dao.*;
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

@WebServlet("/LoginAdminController")
public class LoginAdminController extends HttpServlet {
    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

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
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        try {
            if (userService.isCorrectAdmin(username, password)) {
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/AdminController");
                requestDispatcher.include(request, response);
            } else {
                request.setAttribute("message", "Account's Invalid");
                request.getRequestDispatcher("login-admin.jsp").forward(request, response);
            }
        } catch (DBException e) {
            e.printStackTrace();
        }
    }
}
