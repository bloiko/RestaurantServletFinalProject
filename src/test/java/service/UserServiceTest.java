package service;

import dao.OrderJDBCDAO;
import dao.UserDAO;
import entity.*;
import exception.DBException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
public class UserServiceTest {

    private UserService service;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private OrderJDBCDAO orderJDBCDAO;
    private UserDAO userDAO;

    @Before
    public void setUp() throws DBException {
        orderJDBCDAO = mock(OrderJDBCDAO.class);
        userDAO = mock(UserDAO.class);
        service = new UserService(userDAO, orderJDBCDAO);
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
        when(orderJDBCDAO.getOrdersByUserId(1)).thenReturn(new ArrayList<>());
        service.getUserOrdersSortByOrderDateReversed(user.getUserName());
        verify(userDAO, times(1)).getUserId(any(User.class));
        verify(orderJDBCDAO, times(1)).getOrdersByUserId(anyInt());
    }*/

    @Test
    public void testService_addUserIfNotExistsAndReturnId_ShouldReturnUserId() throws Exception {
        User user = new User(1, "first", "last", "user", "pass", "email", "address", "+380981180662", "ADMIN");
        when(userDAO.getUserId(any(User.class))).thenReturn(1);
        int shoulBeOne = service.addUserIfNotExistsAndReturnId(user);
        //verify(userDAO, times(1)).addUser(any(User.class));
        Assert.assertEquals(1, shoulBeOne);
    }
    @Test
    public void testService_addUserIfNotExistsAndReturnId_ShouldReturnMinusOne() throws Exception {
        User user = new User(1, "first", "last", "user", "pass", "email", "address", "+380981180662", "ADMIN");
        when(userDAO.getUserId(any(User.class))).thenReturn(-1);
        int shoulBeOne = service.addUserIfNotExistsAndReturnId(user);
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
  /*  @Test
    public void testService_isExisting_ShouldReturnMinusOne() throws Exception {
        List<Item> cart = new ArrayList<>();
        int was = service.isExisting("1",cart);
        Assert.assertEquals(-1,was);
    }
   @Test
    public void testService_addFoodItemToCart_ShouldAddOneMoreItem() throws Exception {
        List<Item> cart = new ArrayList<>();
        Item item = new Item(1,new FoodItem(1,"name",1,"image", new Category(1,"")),1);
        Item item2 = new Item(2,new FoodItem(2,"name2",1,"image2", new Category(1,"")),1);

        cart.add(item);
        when(foodJDBCDAO.getFoodItem("2")).thenReturn(item2.getFoodItem());
        cart = service.addFoodItemToCart(cart,"2");
        Assert.assertEquals(2,cart.size());
    }
    @Test
    public void testService_addFoodItemToCart_ShouldTheSameFoodItem() throws Exception {
        List<Item> cart = new ArrayList<>();
        Item item = new Item(1,new FoodItem(1,"name",1,"image", new Category(1,"")),1);

        cart.add(item);
        when(foodJDBCDAO.getFoodItem("1")).thenReturn(item.getFoodItem());
        cart = service.addFoodItemToCart(cart,"1");
        Assert.assertEquals(1,cart.size());
    }*/
  /*  @Test
    public void testService_isExisting_() throws Exception {
        FoodItemService foodItemControllerSpy = Mockito.spy(service);
        HttpSession session = mock(HttpSession.class);
        doNothing().when(new FoodItemService());

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("ORDER");
        when(request.getParameter("foodId")).thenReturn("1");
        foodItemControllerSpy.doGet(request, response);

        verify(foodItemService, times(1)).addFoodItemToCart(anyList(), eq("1"));
        verify(session, atLeast(1)).setAttribute(eq("cart"), anyList());
    }*/
/*
    @Test
    public void testServletWithCart_AddOneItem() throws Exception {
        FoodItemController foodItemControllerSpy = Mockito.spy(servlet);
        HttpSession session = mock(HttpSession.class);
        doNothing().when(foodItemControllerSpy).init();

        when(request.getSession()).thenReturn(session);
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(new Item(1, new FoodItem(), 1));
        when(session.getAttribute("cart")).thenReturn(itemList);
        when(request.getParameter("command")).thenReturn("ORDER");
        when(request.getParameter("foodId")).thenReturn("2");
        when(foodJDBCDAO.getFoodItem(anyString())).thenReturn(new FoodItem());
        foodItemControllerSpy.doGet(request, response);

        verify(foodJDBCDAO, times(1)).getFoodItem("2");
        verify(session, times(1)).setAttribute(eq("cart"), anyList());
        verify(response, times(1)).sendRedirect(eq("/FoodItemController"));
    }

    @Test
    public void testServletWithCommandNull() throws Exception {
        FoodItemController foodItemControllerSpy = Mockito.spy(servlet);
        HttpSession session = mock(HttpSession.class);
        doNothing().when(foodItemControllerSpy).init();

        when(request.getSession()).thenReturn(session);
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(new Item(1, new FoodItem(), 1));
        when(session.getAttribute("cart")).thenReturn(itemList);
        when(foodJDBCDAO.getFoodItem(anyString())).thenReturn(new FoodItem());
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        doNothing().when(requestDispatcher).forward(request, response);
        foodItemControllerSpy.doGet(request, response);

        verify(request, times(1)).getRequestDispatcher(eq("/list-food.jsp"));
        verify(requestDispatcher, times(1)).forward(request, response);

    }

    @Test
    public void testServletWithCommandNull_SortByNameASC() throws Exception {
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(new Item(1, new FoodItem(), 1));
        itemList.add(new Item(2, new FoodItem(), 2));

        FoodItemController foodItemControllerSpy = Mockito.spy(servlet);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        HttpSession session = mock(HttpSession.class);

        doNothing().when(foodItemControllerSpy).init();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(eq("cart"))).thenReturn(itemList);
        when(request.getParameter(eq("sort"))).thenReturn("NAME");
        when(session.getAttribute(eq("menu"))).thenReturn(new ArrayList<FoodItem>());

        when(foodJDBCDAO.getFoodItem(anyString())).thenReturn(new FoodItem());
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        doNothing().when(requestDispatcher).forward(request, response);

        foodItemControllerSpy.doGet(request, response);


        verify(request, times(1)).getRequestDispatcher(eq("/list-food.jsp"));
        verify(session, times(1)).setAttribute(eq("order"), eq("ASC"));
        verify(requestDispatcher, times(1)).forward(request, response);

    }

    @Test
    public void testServletWithCommandNull_SortByNameDESC() throws Exception {
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(new Item(1, new FoodItem(), 1));
        itemList.add(new Item(2, new FoodItem(), 2));

        FoodItemController foodItemControllerSpy = Mockito.spy(servlet);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        HttpSession session = mock(HttpSession.class);

        doNothing().when(foodItemControllerSpy).init();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(eq("cart"))).thenReturn(itemList);
        when(session.getAttribute(eq("order"))).thenReturn("ASC");//program will convert to the DESC
        when(session.getAttribute(eq("sort"))).thenReturn("NAME");
        when(request.getParameter(eq("sort"))).thenReturn("NAME");
        when(session.getAttribute(eq("menu"))).thenReturn(new ArrayList<FoodItem>());

        when(foodJDBCDAO.getFoodItem(anyString())).thenReturn(new FoodItem());
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        doNothing().when(requestDispatcher).forward(request, response);

        foodItemControllerSpy.doGet(request, response);


        verify(request, times(1)).getRequestDispatcher(eq("/list-food.jsp"));
        verify(session, times(1)).setAttribute(eq("order"), eq("DESC"));
        verify(requestDispatcher, times(1)).forward(request, response);

    }

    @Test
    public void testServletWithCommandNull_SortByPriceASC() throws Exception {
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(new Item(1, new FoodItem(), 1));
        itemList.add(new Item(2, new FoodItem(), 2));

        FoodItemController foodItemControllerSpy = Mockito.spy(servlet);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        HttpSession session = mock(HttpSession.class);

        doNothing().when(foodItemControllerSpy).init();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(eq("cart"))).thenReturn(itemList);
        when(request.getParameter(eq("sort"))).thenReturn("PRICE");
        when(session.getAttribute(eq("menu"))).thenReturn(new ArrayList<FoodItem>());

        when(foodJDBCDAO.getFoodItem(anyString())).thenReturn(new FoodItem());
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        doNothing().when(requestDispatcher).forward(request, response);

        foodItemControllerSpy.doGet(request, response);


        verify(request, times(1)).getRequestDispatcher(eq("/list-food.jsp"));
        verify(session, times(1)).setAttribute(eq("order"), eq("ASC"));
        verify(requestDispatcher, times(1)).forward(request, response);

    }

    @Test
    public void testServletWithCommandNull_SortByPriceDESC() throws Exception {
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(new Item(1, new FoodItem(), 1));
        itemList.add(new Item(2, new FoodItem(), 2));

        FoodItemController foodItemControllerSpy = Mockito.spy(servlet);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        HttpSession session = mock(HttpSession.class);

        doNothing().when(foodItemControllerSpy).init();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(eq("cart"))).thenReturn(itemList);
        when(session.getAttribute(eq("order"))).thenReturn("ASC");//program will convert to the DESC
        when(session.getAttribute(eq("sort"))).thenReturn("PRICE");
        when(request.getParameter(eq("sort"))).thenReturn("PRICE");
        when(session.getAttribute(eq("menu"))).thenReturn(new ArrayList<FoodItem>());

        when(foodJDBCDAO.getFoodItem(anyString())).thenReturn(new FoodItem());
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        doNothing().when(requestDispatcher).forward(request, response);

        foodItemControllerSpy.doGet(request, response);


        verify(request, times(1)).getRequestDispatcher(eq("/list-food.jsp"));
        verify(session, times(1)).setAttribute(eq("order"), eq("DESC"));
        verify(requestDispatcher, times(1)).forward(request, response);

    }

    @Test
    public void testServletWithCommandNull_SortByCategoryASC() throws Exception {
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(new Item(1, new FoodItem(), 1));
        itemList.add(new Item(2, new FoodItem(), 2));

        FoodItemController foodItemControllerSpy = Mockito.spy(servlet);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        HttpSession session = mock(HttpSession.class);

        doNothing().when(foodItemControllerSpy).init();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(eq("cart"))).thenReturn(itemList);
        when(request.getParameter(eq("sort"))).thenReturn("CATEGORY");
        when(session.getAttribute(eq("menu"))).thenReturn(new ArrayList<FoodItem>());

        when(foodJDBCDAO.getFoodItem(anyString())).thenReturn(new FoodItem());
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        doNothing().when(requestDispatcher).forward(request, response);

        foodItemControllerSpy.doGet(request, response);


        verify(request, times(1)).getRequestDispatcher(eq("/list-food.jsp"));
        verify(session, times(1)).setAttribute(eq("order"), eq("ASC"));
        verify(requestDispatcher, times(1)).forward(request, response);

    }

    @Test
    public void testServletWithCommandNull_SortByCategoryDESC() throws Exception {
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(new Item(1, new FoodItem(), 1));
        itemList.add(new Item(2, new FoodItem(), 2));

        FoodItemController foodItemControllerSpy = Mockito.spy(servlet);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        HttpSession session = mock(HttpSession.class);

        doNothing().when(foodItemControllerSpy).init();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(eq("cart"))).thenReturn(itemList);
        when(session.getAttribute(eq("order"))).thenReturn("ASC");//program will convert to the DESC
        when(session.getAttribute(eq("sort"))).thenReturn("CATEGORY");
        when(request.getParameter(eq("sort"))).thenReturn("CATEGORY");
        when(session.getAttribute(eq("menu"))).thenReturn(new ArrayList<FoodItem>());

        when(foodJDBCDAO.getFoodItem(anyString())).thenReturn(new FoodItem());
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        doNothing().when(requestDispatcher).forward(request, response);

        foodItemControllerSpy.doGet(request, response);


        verify(request, times(1)).getRequestDispatcher(eq("/list-food.jsp"));
        verify(session, times(1)).setAttribute(eq("order"), eq("DESC"));
        verify(requestDispatcher, times(1)).forward(request, response);

    }

    @Test
    public void testServletWithPageTwo() throws Exception {
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(new Item(1, new FoodItem(), 1));
        itemList.add(new Item(2, new FoodItem(), 2));

        FoodItemController foodItemControllerSpy = Mockito.spy(servlet);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        HttpSession session = mock(HttpSession.class);

        doNothing().when(foodItemControllerSpy).init();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(eq("cart"))).thenReturn(itemList);
        when(request.getParameter("page")).thenReturn("2");
        when(session.getAttribute(eq("menu"))).thenReturn(new ArrayList<FoodItem>());


        when(foodJDBCDAO.getFoodItem(anyString())).thenReturn(new FoodItem());
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        doNothing().when(requestDispatcher).forward(request, response);

        foodItemControllerSpy.doGet(request, response);

        verify(request, times(1)).setAttribute(eq("FOOD_LIST"), anyList());
        verify(request, times(1)).getRequestDispatcher(eq("/list-food.jsp"));
        verify(requestDispatcher, times(1)).forward(request, response);
    }*/
}