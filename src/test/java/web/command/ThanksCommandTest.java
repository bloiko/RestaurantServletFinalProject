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

public class ThanksCommandTest {

    private ThanksCommand command;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private UserService userService;
    private PrintWriter printWriter;

    @Before
    public void setUp() throws ServletException, DBException {
        userService = mock(UserService.class);
        command = new ThanksCommand();
        command = Mockito.spy(command);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        printWriter = mock(PrintWriter.class);
        doNothing().when(command).init();
        try {
            when(response.getWriter()).thenReturn(printWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        doNothing().when(printWriter).write(anyString());

    }

    @Test
    public void testCommand_ShouldForwardToThanksPage() throws Exception {
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        String page = command.execute(request, response);
        Assert.assertEquals("thanks-page.jsp", page);
    }
}