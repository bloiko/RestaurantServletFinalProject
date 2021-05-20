package filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/AdminController")
public class AdminFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //empty
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpSession session = servletRequest.getSession();
        if (session.getAttribute("username") == null &&
                servletRequest.getRequestURI().endsWith("/AdminController")) {
            servletRequest.getRequestDispatcher("/LoginAdminController").forward(request, response);
        } else chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        //empty
    }
}
