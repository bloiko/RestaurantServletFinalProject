package controller;

import dao.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/LoginController")
public class LoginController extends HttpServlet {
    private UserDAO userListDAO;
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            userListDAO = UserDAO.getInstance();
        } catch (Exception exc) {
            throw new ServletException(exc);
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (userListDAO.isCorrectAdmin(username,password)) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/AdminController");
            HttpSession session = request.getSession();
            session.setAttribute("username",username);
            requestDispatcher.include(request, response);
        } else {
            request.setAttribute("message","Account's Invalid");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    public void setUserListDAO(UserDAO userListDAO) {
        this.userListDAO = userListDAO;
    }
}
