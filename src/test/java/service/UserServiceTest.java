package service;

import database.dao.OrderDAO;
import database.dao.UserDAO;
import database.entity.User;
import exception.DBException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
public class UserServiceTest {

    private UserService service;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private OrderDAO orderDAO;
    private UserDAO userDAO;

    @Before
    public void setUp() throws DBException {
        orderDAO = mock(OrderDAO.class);
        userDAO = mock(UserDAO.class);
        service = new UserService(userDAO, orderDAO);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    public void testService_isCorrectAdmin_ShouldReturnTrue() throws Exception {
        User user = new User(1, "first", "last", "username", "password", "email", "address", "+380981180662", "ADMIN");
        when(userDAO.getUserByUserName("username")).thenReturn(user);
        boolean shouldBeTrue = service.isCorrectAdmin("username", "password");
        Assert.assertTrue(shouldBeTrue);
    }

    /*@Test
    public void testService_getUserOrdersSortByOrderDateReversed_ShouldReturnList() throws Exception {
        User user = new User(1, "first", "last", "user", "pass", "email", "address", "+380981180662", "ADMIN");
        when(userDAO.getUserId(any(User.class))).thenReturn(1);
        when(orderDAO.getOrdersByUserId(1)).thenReturn(new ArrayList<>());
        service.getUserOrdersSortByOrderDateReversed(user.getUserName());
        verify(userDAO, times(1)).getUserId(any(User.class));
        verify(orderDAO, times(1)).getOrdersByUserId(anyInt());
    }*/
    @Test
    public void testService_addUserIfNotExistsAndReturnId_ShouldReturnMinusOne() throws Exception {
        User user = new User(1, "first", "last", "user", "pass", "email", "address", "+380981180662", "ADMIN");
        when(userDAO.getUserId(any(User.class))).thenReturn(-1);
        int shoulBeOne = service.addUserAndReturnId(user);
        verify(userDAO, times(1)).addUser(any(User.class));
        Assert.assertEquals(-1, shoulBeOne);
    }
    @Test
    public void testService_isCorrectAdmin_ShouldReturnFalse() throws Exception {
        User user = new User(1, "first", "last", "user", "pass", "email", "address", "+380981180662", "ADMIN");
        when(userDAO.getUserByUserName("user")).thenReturn(user);
        boolean shouldBeFalse = service.isCorrectAdmin("username", "password");
        Assert.assertFalse(shouldBeFalse);
    }

    @Test
    public void testService_isCorrectPhoneNumber_ShouldReturnTrue() throws Exception {
        boolean shouldBeTrue = service.isCorrectPhoneNumber("+380976655443");
        Assert.assertTrue(shouldBeTrue);
    }

    @Test
    public void testService_isCorrectPhoneNumber_ShouldReturnTrue3() throws Exception {
        boolean shouldBeTrue = service.isCorrectPhoneNumber("0976655443");
        Assert.assertTrue(shouldBeTrue);
    }

    @Test
    public void testService_isCorrectPhoneNumber_ShouldReturnFalse() throws Exception {
        boolean shouldBeFalse = service.isCorrectPhoneNumber("380976655443");
        Assert.assertFalse(shouldBeFalse);
    }

    @Test
    public void testService_isCorrectPhoneNumber_ShouldReturnFalse2() throws Exception {
        boolean shouldBeFalse = service.isCorrectPhoneNumber("666565976655443");
        Assert.assertFalse(shouldBeFalse);
    }


    @Test
    public void testService_isCorrectEmail_ShouldReturnTrue() throws Exception {
        boolean shouldBeTrue = service.isCorrectEmail("firstlast@gmail.com");
        Assert.assertTrue(shouldBeTrue);
    }

    @Test
    public void testService_isCorrectEmail_ShouldReturnTrue3() throws Exception {
        boolean shouldBeTrue = service.isCorrectEmail("firstlast@gmail.com.com");
        Assert.assertTrue(shouldBeTrue);
    }

    @Test
    public void testService_isCorrectEmail_ShouldReturnFalse() throws Exception {
        boolean shouldBeFalse = service.isCorrectEmail("firstlast@gmailcom");
        Assert.assertFalse(shouldBeFalse);
    }

    @Test
    public void testService_isCorrectEmail_ShouldReturnFalse2() throws Exception {
        boolean shouldBeFalse = service.isCorrectEmail("firstlastgmail.com");
        Assert.assertFalse(shouldBeFalse);
    }
}