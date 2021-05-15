package controller;

import dao.FoodJDBCDAO;
import dao.OrderJDBCDAO;
import entity.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class AdminControllerTest {

    private AdminController servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private OrderJDBCDAO orderJDBCDAO;

    @Before
    public void setUp() {
        servlet = new AdminController();
        orderJDBCDAO = mock(OrderJDBCDAO.class);
        servlet.setOrderListDAO(orderJDBCDAO);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    public void testServlet_showAllOrders() throws Exception {
        AdminController adminControllerSpy = Mockito.spy(servlet);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        doNothing().when(adminControllerSpy).init();

        when(orderJDBCDAO.getOrders()).thenReturn(new ArrayList<>());
        when(request.getParameter("command")).thenReturn(null);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        doNothing().when(requestDispatcher).forward(request, response);

        adminControllerSpy.doGet(request, response);
        verify(request, atLeast(1)).setAttribute(eq("statusList"), anyList());
        verify(request, atLeast(1)).setAttribute(eq("NOT_DONE_ORDERS_LIST"), anyList());
        verify(request, atLeast(1)).setAttribute(eq("DONE_ORDERS_LIST"), anyList());

        verify(requestDispatcher, atLeast(1)).forward(request, response);
    }

    @Test
    public void testServlet_showAllOrdersWithDeleteCommand() throws Exception {
        AdminController adminControllerSpy = Mockito.spy(servlet);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        doNothing().when(adminControllerSpy).init();

        when(orderJDBCDAO.getOrders()).thenReturn(new ArrayList<>());
        when(request.getParameter("orderId")).thenReturn("1");
        doNothing().when(orderJDBCDAO).deleteOrder(eq("1"));
        when(request.getParameter("command")).thenReturn("DELETE");
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        doNothing().when(requestDispatcher).forward(request, response);

        adminControllerSpy.doGet(request, response);
        verify(orderJDBCDAO, atLeast(1)).deleteOrder(eq("1"));
        verify(request, atLeast(1)).setAttribute(eq("statusList"), anyList());
        verify(request, atLeast(1)).setAttribute(eq("NOT_DONE_ORDERS_LIST"), anyList());
        verify(request, atLeast(1)).setAttribute(eq("DONE_ORDERS_LIST"), anyList());

        verify(requestDispatcher, atLeast(1)).forward(request, response);
    }
    @Test
    public void testServlet_showAllOrdersWithChangeStatusCommand() throws Exception {
        AdminController adminControllerSpy = Mockito.spy(servlet);

        doNothing().when(adminControllerSpy).init();

        when(request.getParameter("orderId")).thenReturn("1");
        when(orderJDBCDAO.getOrder(eq("1"))).thenReturn(new Order(1, new Timestamp(10L), new User(), new ArrayList<>(), OrderStatus.WAITING));
        doNothing().when(orderJDBCDAO).updateOrder(eq(1), any(OrderStatus.class));
        when(request.getParameter("status")).thenReturn("DONE");
        doNothing().when(adminControllerSpy).doGet(request, response);

        adminControllerSpy.doPost(request, response);
        verify(orderJDBCDAO, atLeast(1)).updateOrder(1, OrderStatus.getOrderStatus("DONE"));
        verify(adminControllerSpy, atLeastOnce()).doGet(request,response);
    }
}