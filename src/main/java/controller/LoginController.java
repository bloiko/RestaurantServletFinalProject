package controller;

import dao.UserDAO;

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
    private UserDAO userListDAO = UserDAO.getInstance();

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
        /*String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (username.isEmpty() || password.isEmpty() || !userListDAO.isCorrectUser(username, password)) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("registration.jsp");
            requestDispatcher.include(request, response);
        } else {
            try {
                User user = userListDAO.getUserByUserName(username);

                HttpSession session = request.getSession();
                session.setAttribute("user",user);
            } catch (Exception e) {
                e.printStackTrace();
            }
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/FoodItemController");
            requestDispatcher.forward(request, response);
        }*/
    }
}
