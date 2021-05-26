package web.command.cart;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.Mockito.mock;

public class CartListCommandTest {

    private CartListCommand command;
    private HttpServletRequest request;
    private HttpServletResponse response;
   // private CartService cartService;

    @Before
    public void setUp() throws ServletException {
        command = new CartListCommand();
        //cartService = mock(CartService.class);
        //command.setCartService(cartService);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }
    @Test
    public void testCommand_execute_ShouldReturnRightPage() throws IOException, ServletException {
        String page = command.execute(request,response);
        Assert.assertEquals("cart.jsp",page);
    }

}