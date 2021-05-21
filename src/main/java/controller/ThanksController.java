package controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * Thanks controller.
 * This controller simply shows thanks-page.jsp after succesful food ordering.
 *
 * @author B.Loiko
 *
 */
@WebServlet("/ThanksController")
public class ThanksController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("thanks-page.jsp");
        requestDispatcher.forward(request, response);
    }
}
