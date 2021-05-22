package controller;

import database.dao.OrderDAO;
import database.entity.Order;
import database.entity.OrderStatus;
import database.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class AdminControllerTest {

    private AdminController servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private OrderDAO orderDAO;

    @Before
    public void setUp() {
        servlet = new AdminController();
        orderDAO = mock(OrderDAO.class);
        servlet.setOrderListDAO(orderDAO);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    public void testServlet_showAllOrders() throws Exception {
        AdminController adminControllerSpy = Mockito.spy(servlet);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        doNothing().when(adminControllerSpy).init();

        when(orderDAO.getOrders()).thenReturn(new ArrayList<>());
        when(request.getParameter("web/command")).thenReturn(null);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        doNothing().when(requestDispatcher).forward(request, response);

        adminControllerSpy.doGet(request, response);
        verify(request, times(1)).setAttribute(eq("statusList"), anyList());
        verify(request, times(1)).setAttribute(eq("NOT_DONE_ORDERS_LIST"), anyList());
        verify(request, times(1)).setAttribute(eq("DONE_ORDERS_LIST"), anyList());

        verify(requestDispatcher, times(1)).forward(request, response);
    }

    @Test
    public void testServlet_showAllOrdersWithDeleteCommand() throws Exception {
        AdminController adminControllerSpy = Mockito.spy(servlet);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        doNothing().when(adminControllerSpy).init();

        when(orderDAO.getOrders()).thenReturn(new ArrayList<>());
        when(request.getParameter("orderId")).thenReturn("1");
        doNothing().when(orderDAO).deleteOrder(eq("1"));
        when(request.getParameter("web/command")).thenReturn("DELETE");
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        doNothing().when(requestDispatcher).forward(request, response);

        adminControllerSpy.doGet(request, response);
        verify(orderDAO, times(1)).deleteOrder(eq("1"));
        verify(request, times(1)).setAttribute(eq("statusList"), anyList());
        verify(request, times(1)).setAttribute(eq("NOT_DONE_ORDERS_LIST"), anyList());
        verify(request, times(1)).setAttribute(eq("DONE_ORDERS_LIST"), anyList());

        verify(requestDispatcher, times(1)).forward(request, response);
    }
    @Test
    public void testServlet_showAllOrdersWithChangeStatusCommand() throws Exception {
        AdminController adminControllerSpy = Mockito.spy(servlet);

        doNothing().when(adminControllerSpy).init();

        when(request.getParameter("orderId")).thenReturn("1");
        when(orderDAO.getOrder(eq("1"))).thenReturn(new Order(1, new Timestamp(10L), new User(), new ArrayList<>(), OrderStatus.WAITING));
        doNothing().when(orderDAO).updateOrder(eq(1), any(OrderStatus.class));
        when(request.getParameter("status")).thenReturn("DONE");
        doNothing().when(adminControllerSpy).doGet(request, response);

        adminControllerSpy.doPost(request, response);
        verify(orderDAO, times(1)).updateOrder(1, OrderStatus.getOrderStatus("DONE"));
        verify(adminControllerSpy, atLeastOnce()).doGet(request,response);
    }
}