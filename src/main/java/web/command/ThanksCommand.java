package web.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Thanks controller.
 * This controller simply shows thanks-page.jsp after succesful food ordering.
 *
 * @author B.Loiko
 */
public class ThanksCommand extends Command {
    public ThanksCommand() {
        try {
            init();
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void init() throws ServletException {

    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        return "thanks-page.jsp";
    }
}
