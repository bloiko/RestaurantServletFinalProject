package service;


import database.dao.OrderDAO;
import database.entity.*;
import exception.DBException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
public class OrderServiceTest {

    private OrderService service;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private OrderDAO orderDAO;

    @Before
    public void setUp() throws DBException {
        orderDAO = mock(OrderDAO.class);
        service = new OrderService(orderDAO);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    public void testService_addOrderAndGetId_ShouldAddOneOrder() throws Exception {
        doNothing().when(orderDAO).addOrder(any(Order.class));
        when(orderDAO.getOrderId(any(Order.class))).thenReturn(1);
        List<Item> cart = new ArrayList<>();
        Item item = new Item(1,new FoodItem(1,"name",1,"image", new Category(1,"")),1);
        cart.add(item);

        int orderId = service.addOrderAndGetId(cart, new User());
        //verify(orderDAO, times(1)).addOrder(any(Order.class));
        verify(orderDAO, times(1)).getOrderId(any(Order.class));
        Assert.assertEquals(1, orderId);
    }

/*
    @Test
    public void testService_deleteOrder_ShouldDeleteOrder() throws Exception {
        service.deleteOrder("1");
        verify(orderDAO, times(1)).deleteOrder(eq("1"));
    }
*/

    @Test
    public void testService_getStatuses_ShouldGetAllStatuses() throws Exception {
        service.getStatuses();
        verify(orderDAO, times(1)).getStatuses();
    }

    @Test
    public void testService_getDoneOrders_ShouldGetDoneOrders() throws Exception {
        service.getDoneOrders();
        verify(orderDAO, times(1)).getDoneOrders();
    }

    @Test
    public void testService_getNotDoneOrdersSortById_ShouldGetNotDoneOrdersAndSort() throws Exception {
        service.getNotDoneOrdersSortById();
        verify(orderDAO, times(1)).getNotDoneOrdersSortById();
    }
   /* @Test
    public void testService_addUserIfNotExistsAndReturnId_ShouldReturnUserId() throws Exception {
        User user = new User(1, "first", "last", "user", "pass", "email", "address", "+380981180662", "ADMIN");
        when(userDAO.getUserId(any(User.class))).thenReturn(1);
        int shoulBeOne = service.addUserIfNotExistsAndReturnId(user);
        //verify(userDAO, times(1)).addUser(any(User.class));
        Assert.assertEquals(1, shoulBeOne);
    }*/


}