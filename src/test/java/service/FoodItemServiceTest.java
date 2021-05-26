package service;


import database.dao.FoodDAO;
import database.entity.Category;
import database.entity.FoodItem;
import database.entity.Item;
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

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
public class FoodItemServiceTest {

    private FoodItemService service;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FoodDAO foodDAO;

    @Before
    public void setUp() throws DBException {
        foodDAO = mock(FoodDAO.class);
        service = new FoodItemService(foodDAO);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    public void testService_isExisting_ShouldReturnCoorectIndex() throws Exception {
        List<Item> cart = new ArrayList<>();
        Item item = new Item(1, new FoodItem(1, "name","", 1, "image", new Category(1, "","")), 1);
        cart.add(item);
        int was = service.isExisting("1", cart);
        Assert.assertEquals(0, was);
    }

    @Test
    public void testService_isExisting_ShouldReturnMinusOne() throws Exception {
        List<Item> cart = new ArrayList<>();
        int was = service.isExisting("1", cart);
        Assert.assertEquals(-1, was);
    }

    @Test
    public void testService_addFoodItemToCart_ShouldAddOneMoreItem() throws Exception {
        List<Item> cart = new ArrayList<>();
        Item item = new Item(1, new FoodItem(1, "name","", 1, "image", new Category(1, "","")), 1);
        Item item2 = new Item(2, new FoodItem(2, "name2","", 1, "image2", new Category(1, "","")), 1);

        cart.add(item);
        when(foodDAO.getFoodItem("2")).thenReturn(item2.getFoodItem());
        cart = service.addFoodItemToCart(cart, "2");
        Assert.assertEquals(2, cart.size());
    }

    @Test
    public void testService_addFoodItemToCart_ShouldTheSameFoodItem() throws Exception {
        List<Item> cart = new ArrayList<>();
        Item item = new Item(1, new FoodItem(1, "name","", 1, "image", new Category(1, "","")), 1);

        cart.add(item);
        when(foodDAO.getFoodItem("1")).thenReturn(item.getFoodItem());
        cart = service.addFoodItemToCart(cart, "1");
        Assert.assertEquals(1, cart.size());
    }
/*    @Test(expected = CannotFetchItemsException.class)
    public void testService_getFoodItems_ShouldThrowException() throws Exception {
        when(foodDAO.getFoodItems()).thenThrow(DBException.class);
        service.getFoodItems();
    }*/
    @Test
    public void testService_getFoodItems_ShouldGetAllFoodItems() throws Exception {
        service.getFoodItems();
        verify(foodDAO, times(1)).getFoodItems();
    }
    @Test
    public void testService_getCategories_ShouldGetAllCategories() throws Exception {
        service.getCategories();
        verify(foodDAO, times(1)).getCategories();
    }
    @Test
    public void testService_getFoodItems_ShouldGetAllFoodItemsWithoutSort() throws Exception {
        service.getFoodItems(1, 5, null, null, "Desserts");
        verify(foodDAO, times(1)).getFoodItemsWithSkipLimitFilter(anyInt(), anyInt(), eq("Desserts"));
    }

    @Test
    public void testService_getFoodItems_ShouldGetAllFoodItemsWithoutSortAndFilter() throws Exception {
        service.getFoodItems(1, 5, null, null, null);
        verify(foodDAO, times(1)).getFoodItemsWithSkipAndLimit(anyInt(), anyInt());
    }

    @Test
    public void testService_getFoodItems_ShouldGetAllFoodItemsWithoutFilter() throws Exception {
        service.getFoodItems(1, 5, "price", "ASC", null);
        verify(foodDAO, times(1)).getFoodItemsWithSkipLimitAndOrder(anyInt(), anyInt(), eq("price"), eq("ASC"));
    }

    @Test
    public void testService_getFoodItems_ShouldGetAllFoodItemsWithAllParameters() throws Exception {
        service.getFoodItems(1, 5, "price", "ASC", "Desserts");
        verify(foodDAO, times(1)).getFoodItemsWithFilterSkipLimitAndOrder(eq("Desserts"), anyInt(), anyInt(), eq("price"), eq("ASC"));
    }
  /*  @Test
    public void testService_isExisting_() throws Exception {
        FoodItemService foodItemControllerSpy = Mockito.spy(service);
        HttpSession session = mock(HttpSession.class);
        doNothing().when(new FoodItemService());

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("web.command")).thenReturn("ORDER");
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
        when(request.getParameter("web.command")).thenReturn("ORDER");
        when(request.getParameter("foodId")).thenReturn("2");
        when(foodDAO.getFoodItem(anyString())).thenReturn(new FoodItem());
        foodItemControllerSpy.doGet(request, response);

        verify(foodDAO, times(1)).getFoodItem("2");
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
        when(foodDAO.getFoodItem(anyString())).thenReturn(new FoodItem());
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

        when(foodDAO.getFoodItem(anyString())).thenReturn(new FoodItem());
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

        when(foodDAO.getFoodItem(anyString())).thenReturn(new FoodItem());
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

        when(foodDAO.getFoodItem(anyString())).thenReturn(new FoodItem());
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

        when(foodDAO.getFoodItem(anyString())).thenReturn(new FoodItem());
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

        when(foodDAO.getFoodItem(anyString())).thenReturn(new FoodItem());
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

        when(foodDAO.getFoodItem(anyString())).thenReturn(new FoodItem());
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


        when(foodDAO.getFoodItem(anyString())).thenReturn(new FoodItem());
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        doNothing().when(requestDispatcher).forward(request, response);

        foodItemControllerSpy.doGet(request, response);

        verify(request, times(1)).setAttribute(eq("FOOD_LIST"), anyList());
        verify(request, times(1)).getRequestDispatcher(eq("/list-food.jsp"));
        verify(requestDispatcher, times(1)).forward(request, response);
    }*/
}