package web.command.login;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class LogoutCommandTest {

    private LogoutCommand command;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @Before
    public void setUp() throws ServletException {
        command = new LogoutCommand();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        command = Mockito.spy(command);
        doNothing().when(command).init();
    }

    @Test
    public void testCommand_execute_ShouldLogoutUser() throws IOException, ServletException {
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        String url = command.execute(request, response);

        verify(session, times(1)).removeAttribute(eq("username"));
        verify(session, times(1)).removeAttribute(eq("cart"));
        Assert.assertEquals("/controller?command=menuList", url);
    }

}