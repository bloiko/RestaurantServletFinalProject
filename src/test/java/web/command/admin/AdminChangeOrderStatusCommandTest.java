package web.command.admin;

import database.entity.Order;
import database.entity.OrderStatus;
import database.entity.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class AdminChangeOrderStatusCommandTest {

    private AdminChangeOrderStatusCommand command;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private OrderService orderService;

    @Before
    public void setUp() throws ServletException {
        command = new AdminChangeOrderStatusCommand();
        orderService = mock(OrderService.class);
        command.setOrderService(orderService);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        command =  Mockito.spy(command);
        doNothing().when(command).init();
    }

    @Test
    public void testServlet_showAllOrdersWithChangeStatusCommand() throws Exception {
        when(request.getParameter("status")).thenReturn("DONE");
        when(request.getParameter("orderId")).thenReturn("1");

        Order order = new Order(1, new Timestamp(10L), new User(), new ArrayList<>(), OrderStatus.DELIVERED);
        when(orderService.getOrder(eq("1"))).thenReturn(order);
        doNothing().when(orderService).updateOrder(eq(1), any(OrderStatus.class));

        String url = command.execute(request, response);

        verify(orderService, times(1)).updateOrder(1, OrderStatus.getOrderStatus("DONE"));
        Assert.assertEquals("/controller?command=adminList",url);
    }
}