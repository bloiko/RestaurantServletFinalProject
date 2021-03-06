package web.filters;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "UserFilter", urlPatterns = {"/*"})
public class UserFilter implements Filter {
    private static final Logger log = Logger.getLogger(UserFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("Filter creation starts");
        // do nothing
        log.debug("Filter creation finished");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.debug("Filter starts");

        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpSession session = servletRequest.getSession();
        String username = (String) session.getAttribute("username");
        log.trace("Get attribute from the session: username_admin --> " + username);

        if (username != null && ("loginMain".equals(servletRequest.getParameter("command")) ||
                "registration".equals(servletRequest.getParameter("command"))||
                servletRequest.getRequestURI().endsWith("admin-main.jsp"))) {
            log.info("User " + username + " try to get to the command" + servletRequest.getParameter("command"));
            log.debug("Filter finished");
            ((HttpServletResponse) response).sendRedirect("no-access.jsp");
            /* ((HttpServletResponse) response).sendRedirect("login-admin.jsp");*/
        } else {
            log.debug("Filter finished");
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        log.debug("Filter destruction starts");
        // do nothing
        log.debug("Filter destruction finished");
    }
}
