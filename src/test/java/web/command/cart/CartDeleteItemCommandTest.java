package web.command.cart;

import database.entity.FoodItem;
import database.entity.Item;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import service.CartService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class CartDeleteItemCommandTest {

    private CartDeleteItemCommand command;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private CartService cartService;

    @Before
    public void setUp() throws ServletException {
        command = new CartDeleteItemCommand();
        cartService = mock(CartService.class);
        command.setCartService(cartService);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    public void testServlet_deleteFirstElement() throws Exception {
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(new Item(1, new FoodItem(), 1));
        itemList.add(new Item(2, new FoodItem(), 2));

        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("itemId")).thenReturn("0");
        when(session.getAttribute("cart")).thenReturn(itemList);

       String pageWas =  command.execute(request, response);

        verify(cartService, times(1)).removeItemFromCart(anyList(),eq("0"));
        verify(session, times(1)).setAttribute(eq("cart"), anyList());
        Assert.assertEquals("cart.jsp",pageWas);
    }
   /* @Test(expected = IndexOutOfBoundsException.class)
    public void testServletIsExisting_ThrowIndexOutOfBoundsException() throws Exception {
        List<Item> itemList = new ArrayList<Item>();
        itemList.add(new Item(1, new FoodItem(1,"name",12,"image",new Category()), 1));
        itemList.add(new Item(2, new FoodItem(2,"name2",122,"image2",new Category()), 2));
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("web.command")).thenReturn("DELETE");
        when(request.getParameter("itemId")).thenReturn("3");
        when(session.getAttribute("cart")).thenReturn(itemList);
        doNothing().when(requestDispatcher).forward(request, response);

        servlet.doGet(request, response);

    }*/
}