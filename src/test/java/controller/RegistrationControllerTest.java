package controller;

import dao.OrderJDBCDAO;
import dao.UserDAO;
import entity.Item;
import entity.Order;
import entity.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class RegistrationControllerTest {

    private RegistrationController servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private OrderJDBCDAO orderJDBCDAO;
    private UserDAO userDAO;

    @Before
    public void setUp() {
        orderJDBCDAO = mock(OrderJDBCDAO.class);
        userDAO = mock(UserDAO.class);
        servlet = new RegistrationController();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    public void testServletWithCorrectUserData() throws Exception {
        RegistrationController registrationControllerSpy = Mockito.spy(servlet);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        HttpSession session = mock(HttpSession.class);
        doNothing().when(registrationControllerSpy).init();

        doNothing().when(userDAO).addUser(any(User.class));
        doNothing().when(orderJDBCDAO).addOrder(any(Order.class));
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("first_name")).thenReturn("First");
        when(request.getParameter("last_name")).thenReturn("Last");
        when(request.getParameter("email")).thenReturn("fff@gmail.com");
        when(request.getParameter("address")).thenReturn("Volyn");
        when(request.getParameter("phoneNumber")).thenReturn("+380954430553");

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        doNothing().when(requestDispatcher).forward(request,response);
        registrationControllerSpy.doPost(request, response);

        verify(session, times(1)).setAttribute(eq("cart"),anyList());
    }
    @Test
    public void testServletWithIncorrectUserData() throws Exception {
        RegistrationController registrationControllerSpy = Mockito.spy(servlet);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        HttpSession session = mock(HttpSession.class);
        doNothing().when(registrationControllerSpy).init();


        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("cart")).thenReturn(new ArrayList<Item>());

        when(request.getParameter("first_name")).thenReturn("");
        when(request.getParameter("last_name")).thenReturn("Last");
        when(request.getParameter("email")).thenReturn("fff@gmail.com");
        when(request.getParameter("address")).thenReturn("Volyn");
        when(request.getParameter("phoneNumber")).thenReturn("430553");

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        doNothing().when(requestDispatcher).forward(request,response);
        registrationControllerSpy.doPost(request, response);

        verify(requestDispatcher, times(1)).include(request,response);

    }
}