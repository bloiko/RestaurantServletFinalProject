package web.command.login;

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

import static org.mockito.Mockito.*;

public class LoginAdminCommandTest {

    private LoginAdminCommand command;
    private UserService userService;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @Before
    public void setUp() throws ServletException {
        command = new LoginAdminCommand();
        userService = mock(UserService.class);
        command.setUserService(userService);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        command = Mockito.spy(command);
        doNothing().when(command).init();
    }

    @Test
    public void testCommand_execute_IsCorrectAdmin_ShouldRedirectToAdminList() throws IOException, ServletException, DBException {
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter(anyString())).thenReturn("user");
        when(userService.isCorrectAdmin(anyString(),anyString())).thenReturn(true);

        String url = command.execute(request, response);

        verify(request, times(1)).getParameter(eq("username"));
        verify(request, times(1)).getParameter(eq("password"));
        Assert.assertEquals("/controller?command=adminList", url);
    }
    @Test
    public void testCommand_execute_IsNotCorrectAdmin_ShouldForwardToLoginAdmin() throws IOException, ServletException, DBException {
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter(anyString())).thenReturn("user");
        when(userService.isCorrectAdmin(anyString(),anyString())).thenReturn(false);

        String page = command.execute(request, response);

        verify(request, times(1)).getParameter(eq("username"));
        verify(request, times(1)).getParameter(eq("password"));
        Assert.assertEquals("login-admin.jsp", page);
    }
}