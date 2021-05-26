package web.command;

import database.entity.Order;
import exception.DBException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class MyOrdersControllerTest {

    private MyOrdersCommand command;
    private MyOrdersCommand myOrdersCommandSpy;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private UserService userService;

    @Before
    public void setUp() throws ServletException, DBException {
        userService = mock(UserService.class);
        command = new MyOrdersCommand();
        command.setUserService(userService);
        myOrdersCommandSpy = Mockito.spy(command);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        doNothing().when(myOrdersCommandSpy).init();

    }


    @Test
    public void testServlet_desplayOrdersList_WithCookies() throws Exception {
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("username")).thenReturn("username");
        when(userService.getUserOrdersSortByOrderDateReversed(anyString())).thenReturn(new ArrayList<Order>());


        myOrdersCommandSpy.execute(request, response);
        verify(request, atLeast(1)).setAttribute(eq("ORDERS_LIST"),anyList());
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