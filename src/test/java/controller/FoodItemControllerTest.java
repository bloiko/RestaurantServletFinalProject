package controller;

import database.entity.FoodItem;
import database.entity.Item;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import service.FoodItemService;

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
    private FoodItemService foodItemService;

    @Before
    public void setUp() {
        foodItemService = mock(FoodItemService.class);
        servlet = new FoodItemController();
        servlet.setFoodItemService(foodItemService);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    public void testServletWithoutCart_AddOneItem() throws Exception {
        FoodItemController foodItemControllerSpy = Mockito.spy(servlet);
        HttpSession session = mock(HttpSession.class);
        doNothing().when(foodItemControllerSpy).init();

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("web/command")).thenReturn("ORDER");
        when(request.getParameter("foodId")).thenReturn("1");
        foodItemControllerSpy.doGet(request, response);

        verify(foodItemService, times(1)).addFoodItemToCart(anyList(), eq("1"));
        verify(session, atLeast(1)).setAttribute(eq("cart"), anyList());
    }

    @Test
    public void testServletWithCommandNull() throws Exception {
        FoodItemController foodItemControllerSpy = Mockito.spy(servlet);
        HttpSession session = mock(HttpSession.class);
        doNothing().when(foodItemControllerSpy).init();

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("filter")).thenReturn("Desserts");
        when(request.getParameter("sort")).thenReturn("price");
        when(request.getParameter("order")).thenReturn("ASC");
        when(request.getParameter("page")).thenReturn("1");
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(new Item(1, new FoodItem(), 1));
        when(session.getAttribute("cart")).thenReturn(itemList);

        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        doNothing().when(requestDispatcher).forward(request, response);

        foodItemControllerSpy.doGet(request, response);

        verify(request, times(1)).getRequestDispatcher(eq("/list-food.jsp"));
        verify(requestDispatcher, times(1)).forward(request, response);
        verify(foodItemService, times(1)).getFoodItems(anyInt(),anyInt(),anyString(),anyString(),anyString());

    }


}