package web.filters;


import database.dao.OrderDAO;
import database.dao.UserDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.*;

public class AdminFilterTest {

    private AdminFilter filter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private OrderDAO orderDAO;
    private UserDAO userDAO;

    @Before
    public void setUp() {
        orderDAO = mock(OrderDAO.class);
        userDAO = mock(UserDAO.class);
        filter = new AdminFilter();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    public void testFilterWithUsername() throws Exception {
        AdminFilter filterSpy = Mockito.spy(filter);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
        session.setAttribute("username_admin", "user");
        FilterChain filterChain = mock(FilterChain.class);
        filterSpy.doFilter(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void testFilterWithoutUsername() throws Exception {
        AdminFilter filterSpy = Mockito.spy(filter);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("adminList");
        when(session.getAttribute("username_admin")).thenReturn(null );
        FilterChain filterChain = mock(FilterChain.class);
        filterSpy.doFilter(request, response, filterChain);

        verify(response, times(1)).sendRedirect(eq("login-admin.jsp"));
    }
}