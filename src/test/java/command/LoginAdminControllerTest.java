package command;

import controller.LoginAdminController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.*;

public class LoginAdminControllerTest {

    private LoginAdminController servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private UserService userService;

    @Before
    public void setUp() {
        servlet = new LoginAdminController();
        userService = mock(UserService.class);
        servlet.setUserService(userService);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    public void testServlet_correctAdmin() throws Exception {
        LoginAdminController adminControllerSpy = Mockito.spy(servlet);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        doNothing().when(adminControllerSpy).init();


        when(userService.isCorrectAdmin("username", "password")).thenReturn(true);

        when(request.getParameter("username")).thenReturn("username");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        doNothing().when(requestDispatcher).include(request, response);

       // adminControllerSpy.doGet(request, response);
        verify(session, atLeast(1)).setAttribute(eq("username"), eq("username"));
        verify(requestDispatcher, times(1)).include(request, response);
    }

    @Test
    public void testServlet_incorrectAdmin() throws Exception {
        LoginAdminController adminControllerSpy = Mockito.spy(servlet);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        doNothing().when(adminControllerSpy).init();


        when(userService.isCorrectAdmin("username", "password")).thenReturn(false);

        when(request.getParameter("username")).thenReturn("username");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        doNothing().when(requestDispatcher).include(request, response);

       // adminControllerSpy.doGet(request, response);

        verify(request, times(1)).setAttribute(eq("message"), anyString());
        verify(requestDispatcher, times(1)).forward(request, response);
    }
}