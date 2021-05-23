package web.command;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Command that show thanks page.
 *
 * @author B.Loiko
 *
 */
public class ThanksCommand extends Command {
    private static final Logger log = LogManager.getLogger(ThanksCommand.class);

    public ThanksCommand() {
        try {
            init();
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void init() throws ServletException {
        //empty
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("Command starts");

        log.debug("Command finished");
        return "thanks-page.jsp";
    }
}
