package controller;

import dao.FoodJDBCDAO;
import entity.Category;
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

public class CartControllerTest {

    private CartController servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FoodJDBCDAO foodJDBCDAO;

    @Before
    public void setUp() {
        servlet = new CartController();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    public void testServlet_deleteFirstElement() throws Exception {
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(new Item(1, new FoodItem(), 1));
        itemList.add(new Item(2, new FoodItem(), 2));
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("DELETE");
        when(request.getParameter("itemId")).thenReturn("0");
        when(session.getAttribute("cart")).thenReturn(itemList);
        doNothing().when(requestDispatcher).forward(request, response);

        servlet.doGet(request, response);

        verify(requestDispatcher, atLeast(1)).forward(request, response);
        verify(session, atLeast(1)).setAttribute(eq("cart"), anyList());
    }
    @Test(expected = IndexOutOfBoundsException.class)
    public void testServletIsExisting_ThrowIndexOutOfBoundsException() throws Exception {
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(new Item(1, new FoodItem(1,"name",12,"image",new Category()), 1));
        itemList.add(new Item(2, new FoodItem(2,"name2",122,"image2",new Category()), 2));
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("DELETE");
        when(request.getParameter("itemId")).thenReturn("3");
        when(session.getAttribute("cart")).thenReturn(itemList);
        doNothing().when(requestDispatcher).forward(request, response);

        servlet.doGet(request, response);

    }
}