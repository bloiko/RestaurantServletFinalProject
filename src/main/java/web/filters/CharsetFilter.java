package web.filters;

import org.apache.log4j.Logger;

import javax.servlet.*;
import java.io.IOException;

public class CharsetFilter implements Filter {
    private String encoding;
    private static final Logger log = Logger.getLogger(CharsetFilter.class);


    public void init(FilterConfig config) throws ServletException {
        log.debug("Filter creation starts");

        encoding = config.getInitParameter("requestEncoding");
        if (encoding == null) encoding = "UTF-8";

        log.debug("Filter creation finished");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain next)
            throws IOException, ServletException {
        log.debug("Filter starts");

        if (null == request.getCharacterEncoding()) {
            request.setCharacterEncoding(encoding);
        }

        // Set the default response content type and encoding
        String contentType = "text/html; charset=UTF-8";
        response.setContentType(contentType);
        log.trace("Setting to the response content type: "+contentType);

        String characterEncoding = "UTF-8";
        response.setCharacterEncoding(characterEncoding);
        log.trace("Setting to the response Character Encoding: "+characterEncoding);

        log.debug("Filter finished");
        next.doFilter(request, response);
    }

    @Override
    public void destroy() {
        log.debug("Filter destruction starts");
        // do nothing
        log.debug("Filter destruction finished");
    }
}