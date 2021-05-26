package web.command.admin;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class AdminListCommandTest {

    private AdminListCommand command;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private OrderService orderService;

    @Before
    public void setUp() throws ServletException {
        command = new AdminListCommand();
        orderService = mock(OrderService.class);
        command.setOrderService(orderService);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        command = Mockito.spy(command);
        doNothing().when(command).init();
    }

    @Test
    public void testServlet_showAllOrders() throws Exception {
        HttpSession session = mock(HttpSession.class);

        when(orderService.getStatuses()).thenReturn(new ArrayList<>());
        when(orderService.getDoneOrders()).thenReturn(new ArrayList<>());
        when(orderService.getNotDoneOrdersSortById()).thenReturn(new ArrayList<>());

        when(request.getSession()).thenReturn(session);
        String page = command.execute(request, response);
        verify(request, times(1)).setAttribute(eq("statusList"), anyList());
        verify(request, times(1)).setAttribute(eq("NOT_DONE_ORDERS_LIST"), anyList());
        verify(request, times(1)).setAttribute(eq("DONE_ORDERS_LIST"), anyList());
        Assert.assertEquals("admin.jsp", page);
    }


}