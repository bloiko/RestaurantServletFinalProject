package controller;

import dao.FoodJDBCDAO;
import entity.FoodItem;
import entity.Item;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class FoodItemControllerTest {

    private FoodItemController servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FoodJDBCDAO foodJDBCDAO;

    @Before
    public void setUp() {
        foodJDBCDAO = mock(FoodJDBCDAO.class);
        servlet = new FoodItemController();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    public void testServletWithoutCart_AddOneItem() throws Exception {
        FoodItemController foodItemControllerSpy = Mockito.spy(servlet);
        HttpSession session = mock(HttpSession.class);
        doNothing().when(foodItemControllerSpy).init();

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("ORDER");
        when(request.getParameter("foodId")).thenReturn("1");
        when(foodJDBCDAO.getFoodItem(anyString())).thenReturn(new FoodItem());
        foodItemControllerSpy.doGet(request, response);

        verify(foodJDBCDAO, times(1)).getFoodItem("1");
        verify(session, times(1)).setAttribute(eq("cart"), anyList());
    }
    @Test
    public void testServletWithCart_AddOneItem() throws Exception {
        FoodItemController foodItemControllerSpy = Mockito.spy(servlet);
        HttpSession session = mock(HttpSession.class);
        doNothing().when(foodItemControllerSpy).init();

        when(request.getSession()).thenReturn(session);
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(new Item(1,new FoodItem(),1));
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
        itemList.add(new Item(1,new FoodItem(),1));
        when(session.getAttribute("cart")).thenReturn(itemList);
        when(foodJDBCDAO.getFoodItem(anyString())).thenReturn(new FoodItem());
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        doNothing().when(requestDispatcher).forward(request,response);
        foodItemControllerSpy.doGet(request, response);

        verify(request, times(1)).getRequestDispatcher(eq("/list-food.jsp"));
        verify(requestDispatcher, times(1)).forward(request,response);

    }
    @Test
    public void testServletWithCommandNull_SortByNameASC() throws Exception {
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(new Item(1,new FoodItem(),1));
        itemList.add(new Item(2,new FoodItem(),2));

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
        doNothing().when(requestDispatcher).forward(request,response);

        foodItemControllerSpy.doGet(request, response);


        verify(request, times(1)).getRequestDispatcher(eq("/list-food.jsp"));
        verify(session, times(1)).setAttribute(eq("order"),eq("ASC"));
        verify(requestDispatcher, times(1)).forward(request,response);

    }
    @Test
    public void testServletWithCommandNull_SortByNameDESC() throws Exception {
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(new Item(1,new FoodItem(),1));
        itemList.add(new Item(2,new FoodItem(),2));

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
        doNothing().when(requestDispatcher).forward(request,response);

        foodItemControllerSpy.doGet(request, response);


        verify(request, times(1)).getRequestDispatcher(eq("/list-food.jsp"));
        verify(session, times(1)).setAttribute(eq("order"),eq("DESC"));
        verify(requestDispatcher, times(1)).forward(request,response);

    }
    @Test
    public void testServletWithCommandNull_SortByPriceASC() throws Exception {
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(new Item(1,new FoodItem(),1));
        itemList.add(new Item(2,new FoodItem(),2));

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
        doNothing().when(requestDispatcher).forward(request,response);

        foodItemControllerSpy.doGet(request, response);


        verify(request, times(1)).getRequestDispatcher(eq("/list-food.jsp"));
        verify(session, times(1)).setAttribute(eq("order"),eq("ASC"));
        verify(requestDispatcher, times(1)).forward(request,response);

    }
    @Test
    public void testServletWithCommandNull_SortByPriceDESC() throws Exception {
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(new Item(1,new FoodItem(),1));
        itemList.add(new Item(2,new FoodItem(),2));

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
        doNothing().when(requestDispatcher).forward(request,response);

        foodItemControllerSpy.doGet(request, response);


        verify(request, times(1)).getRequestDispatcher(eq("/list-food.jsp"));
        verify(session, times(1)).setAttribute(eq("order"),eq("DESC"));
        verify(requestDispatcher, times(1)).forward(request,response);

    }

    @Test
    public void testServletWithCommandNull_SortByCategoryASC() throws Exception {
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(new Item(1,new FoodItem(),1));
        itemList.add(new Item(2,new FoodItem(),2));

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
        doNothing().when(requestDispatcher).forward(request,response);

        foodItemControllerSpy.doGet(request, response);


        verify(request, times(1)).getRequestDispatcher(eq("/list-food.jsp"));
        verify(session, times(1)).setAttribute(eq("order"),eq("ASC"));
        verify(requestDispatcher, times(1)).forward(request,response);

    }
    @Test
    public void testServletWithCommandNull_SortByCategoryDESC() throws Exception {
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(new Item(1,new FoodItem(),1));
        itemList.add(new Item(2,new FoodItem(),2));

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
        doNothing().when(requestDispatcher).forward(request,response);

        foodItemControllerSpy.doGet(request, response);


        verify(request, times(1)).getRequestDispatcher(eq("/list-food.jsp"));
        verify(session, times(1)).setAttribute(eq("order"),eq("DESC"));
        verify(requestDispatcher, times(1)).forward(request,response);

    }

    @Test
    public void testServletWithPageTwo() throws Exception {
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(new Item(1,new FoodItem(),1));
        itemList.add(new Item(2,new FoodItem(),2));

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
        doNothing().when(requestDispatcher).forward(request,response);

        foodItemControllerSpy.doGet(request, response);

        verify(request, times(1)).setAttribute(eq("FOOD_LIST"),anyList());
        verify(request, times(1)).getRequestDispatcher(eq("/list-food.jsp"));
        verify(requestDispatcher, times(1)).forward(request,response);
    }
}