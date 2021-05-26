package web.command.menu;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import service.FoodItemService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.*;

public class MenuOrderCommandTest {
    private MenuOrderCommand command;
    private MenuOrderCommand menuOrderCommandSpy;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FoodItemService foodItemService;

    @Before
    public void setUp() throws ServletException {
        foodItemService = mock(FoodItemService.class);
        command = new MenuOrderCommand();
        command.setFoodItemService(foodItemService);
        menuOrderCommandSpy = Mockito.spy(command);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        doNothing().when(menuOrderCommandSpy).init();
    }

    @Test
    public void testServletWithoutCart_AddOneItem() throws Exception {
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        when(request.getParameter("foodId")).thenReturn("1");
        menuOrderCommandSpy.execute(request, response);

        verify(foodItemService, times(1)).addFoodItemToCart(anyList(), eq("1"));
        verify(session, atLeast(1)).setAttribute(eq("cart"), anyList());
    }

/*
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
*/



}