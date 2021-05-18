package controller;

import dao.OrderJDBCDAO;
import dao.UserDAO;
import entity.Order;
import entity.OrderStatus;
import entity.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class LoginControllerTest {

    private LoginController servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private UserDAO userDAO;

    @Before
    public void setUp() {
        servlet = new LoginController();
        userDAO = mock(UserDAO.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    public void testServlet_correctAdmin() throws Exception {
        LoginController adminControllerSpy = Mockito.spy(servlet);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        doNothing().when(adminControllerSpy).init();

        when(request.getParameter("username")).thenReturn("username");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        doNothing().when(requestDispatcher).include(request, response);

        adminControllerSpy.doGet(request, response);
        verify(session, times(1)).setAttribute(eq("username"), eq("username"));
        verify(requestDispatcher, times(1)).include(request, response);
    }

    @Test
    public void testServlet_incorrectAdmin() throws Exception {
        LoginController adminControllerSpy = Mockito.spy(servlet);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        doNothing().when(adminControllerSpy).init();

        when(request.getParameter("username")).thenReturn("username");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        doNothing().when(requestDispatcher).include(request, response);

        adminControllerSpy.doGet(request, response);

        verify(request, times(1)).setAttribute(eq("message"), anyString());
        verify(requestDispatcher, times(1)).forward(request, response);
    }
}