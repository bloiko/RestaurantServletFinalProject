package web.command;

import exception.DBException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.*;

public class NoCommandControllerTest {

    private NoCommand command;
    private NoCommand noCommandSpy;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private UserService userService;
    private PrintWriter printWriter;

    @Before
    public void setUp() throws ServletException, DBException {
        userService = mock(UserService.class);
        command = new NoCommand();
        noCommandSpy = Mockito.spy(command);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        printWriter = mock(PrintWriter.class);
        doNothing().when(noCommandSpy).init();
        try {
            when(response.getWriter()).thenReturn(printWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        doNothing().when(printWriter).write(anyString());

    }


    @Test
    public void testServlet_desplayOrdersList_WithCookies() throws Exception {
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(request.getAttribute("javax.servlet.error.status_code")).thenReturn(505);

        String command = noCommandSpy.execute(request, response);
        Assert.assertEquals("error-page.jsp",command);
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