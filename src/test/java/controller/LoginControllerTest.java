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
        servlet.setUserListDAO(userDAO);
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
        when(userDAO.isCorrectAdmin(anyString(),anyString())).thenReturn(true);
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
        when(userDAO.isCorrectAdmin(anyString(),anyString())).thenReturn(false);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        doNothing().when(requestDispatcher).include(request, response);

        adminControllerSpy.doGet(request, response);

        verify(request, times(1)).setAttribute(eq("message"), anyString());
        verify(requestDispatcher, times(1)).forward(request, response);
    }
    /*@Test
    public void testServlet_showAllOrdersWithChangeStatusCommand() throws Exception {
        AdminController adminControllerSpy = Mockito.spy(servlet);

        doNothing().when(adminControllerSpy).init();

        when(request.getParameter("orderId")).thenReturn("1");
        when(userDAO.getOrder(eq("1"))).thenReturn(new Order(1, new Timestamp(10L), new User(), new ArrayList<>(), OrderStatus.WAITING));
        doNothing().when(userDAO).updateOrder(eq(1), any(OrderStatus.class));
        when(request.getParameter("status")).thenReturn("DONE");
        doNothing().when(adminControllerSpy).doGet(request, response);

        adminControllerSpy.doPost(request, response);
        verify(userDAO, times(1)).updateOrder(1, OrderStatus.getOrderStatus("DONE"));
        verify(adminControllerSpy, atLeastOnce()).doGet(request,response);
    }*/
}