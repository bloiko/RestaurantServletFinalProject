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

public class RegistrationCommandTest {

    private RegistrationCommand command;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private UserService userService;
    @Before
    public void setUp() throws ServletException {
        command = new RegistrationCommand();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        userService = mock(UserService.class);
        command.setUserService(userService);
        command = Mockito.spy(command);
        doNothing().when(command).init();
    }

    @Test
    public void testExecute_ShouldRedirectToRegistrationOneMore() throws IOException, ServletException {
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter(anyString())).thenReturn("");

        String command = this.command.execute(request, response);

        //verify(session, times(1)).removeAttribute(eq("username"));
       // verify(session, times(1)).removeAttribute(eq("cart"));
        Assert.assertEquals("registration.jsp", command);
    }
    @Test
    public void testExecute_ShouldRedirectToLoginMain() throws IOException, ServletException, DBException {
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter(anyString())).thenReturn(" ");
        when(request.getParameter("password")).thenReturn("8888 8888");

        when(userService.isCorrectPhoneNumber(anyString())).thenReturn(true);
        when(userService.isCorrectEmail(anyString())).thenReturn(true);
        when(userService.getUserByUserName(anyString())).thenReturn(null);


        String command = this.command.execute(request, response);

        //verify(session, times(1)).removeAttribute(eq("username"));
        // verify(session, times(1)).removeAttribute(eq("cart"));
        Assert.assertEquals("login-main.jsp", command);
    }
}