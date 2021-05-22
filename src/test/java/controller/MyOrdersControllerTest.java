package controller;

import database.entity.Order;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class MyOrdersControllerTest {

    private MyOrdersController servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private UserService userService;

    @Before
    public void setUp() {
        servlet = new MyOrdersController();
        userService = mock(UserService.class);
        servlet.setUserService(userService);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }


    @Test
    public void testServlet_desplayOrdersList_WithCookies() throws Exception {
        Cookie[] cookies = new Cookie[]{new Cookie("first_name","first_name"),
                new Cookie("last_name","last_name"),
                new Cookie("email","email"),
                new Cookie("address","address"),
                new Cookie("phoneNumber","PhoneNumber")};
        MyOrdersController myOrdersController = Mockito.spy(servlet);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        doNothing().when(myOrdersController).init();

        when(request.getCookies()).thenReturn(cookies);

        when(userService.getUserOrdersSortByOrderDateReversed(anyString())).thenReturn(new ArrayList<Order>());

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        doNothing().when(requestDispatcher).include(request, response);

        myOrdersController.doGet(request, response);
        verify(request, atLeast(1)).setAttribute(eq("ORDERS_LIST"),anyList());
        verify(requestDispatcher, times(1)).forward(request, response);
    }
    /*@Test
    public void testServlet_incorrectAdmin() throws Exception {
        LoginController adminControllerSpy = Mockito.spy(servlet);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        doNothing().when(adminControllerSpy).init();


        when(userService.isCorrectAdmin("username", "password")).thenReturn(false);

        when(request.getParameter("username")).thenReturn("username");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        doNothing().when(requestDispatcher).include(request, response);

        adminControllerSpy.doGet(request, response);

        verify(request, times(1)).setAttribute(eq("message"), anyString());
        verify(requestDispatcher, times(1)).forward(request, response);
    }*/
}