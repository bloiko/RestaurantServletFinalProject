package web.command.login;

import database.entity.User;
import exception.DBException;
import org.apache.log4j.Logger;
import service.UserService;
import web.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Command process data from the registration.
 *
 * @author B.Loiko
 */
public class RegistrationCommand extends Command {
    public static final String PASSWORD = "password";
    public static final String USERNAME = "username";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String ADDRESS = "address";
    public static final String EMAIL = "email";
    public static final String LAST_NAME = "last_name";
    public static final String FIRST_NAME = "first_name";
    private UserService userService;
    private static final Logger log = Logger.getLogger(RegistrationCommand.class);

    public RegistrationCommand() throws ServletException {
        init();
    }

    @Override
    public void init() throws ServletException {
        try {
            userService = new UserService();
        } catch (DBException exc) {
            throw new ServletException(exc);
        }
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Command starts");
        User user = getUserIfCorrectData(request);
        if (user == null) {
            log.info("User data is invalid");
            prepareDataToTheRedirection(request);
            log.trace("User data was prepared to the redirection");

            log.debug("Command finished");
            return "registration.jsp";
        } else {
            try {
                userService.addUserAndReturnId(user);
                log.info("User was added to the database");
            } catch (DBException e) {
                log.error("This is DBException", e);
                e.printStackTrace();
            }

            log.debug("Command finished");
            return "login-main.jsp";
        }
    }

    private void prepareDataToTheRedirection(HttpServletRequest request) {
        String firstName = request.getParameter(FIRST_NAME);
        request.setAttribute(FIRST_NAME, firstName);
        log.trace("Set parameter to the request: FIRST_NAME --> " + firstName);

        String lastName = request.getParameter(LAST_NAME);
        request.setAttribute(LAST_NAME, lastName);
        log.trace("Set parameter to the request: LAST_NAME --> " + lastName);

        String email = request.getParameter(EMAIL);
        request.setAttribute(EMAIL, email);
        log.trace("Set parameter to the request: EMAIL --> " + email);

        String address = request.getParameter(ADDRESS);
        request.setAttribute(ADDRESS, address);
        log.trace("Set parameter to the request: ADDRESS --> " + address);

        String phoneNumber = request.getParameter(PHONE_NUMBER);
        request.setAttribute(PHONE_NUMBER, phoneNumber);
        log.trace("Set parameter to the request: PHONE_NUMBER --> " + phoneNumber);


        String username = request.getParameter(USERNAME);
        request.setAttribute(USERNAME, username);
        log.trace("Set parameter to the request: USERNAME --> " + username);

        String password = request.getParameter(PASSWORD);
        request.setAttribute(PASSWORD, password);
        log.trace("Set parameter to the request: PASSWORD --> " + password);

        request.setAttribute("command", "REDIRECT");
        log.trace("Set attribute to the request: command --> " + "REDIRECT");

    }

    private User getUserIfCorrectData(HttpServletRequest request) {
        log.debug("Check data on correctness");
        boolean isCorrect = true;
        String firstName = request.getParameter(FIRST_NAME);
        String lastName = request.getParameter(LAST_NAME);
        String email = request.getParameter(EMAIL);
        String address = request.getParameter(ADDRESS);
        String phoneNumber = request.getParameter(PHONE_NUMBER);
        String username = request.getParameter(USERNAME);
        String password = request.getParameter(PASSWORD);
        HttpSession session = request.getSession();
        session.removeAttribute("error_message");
        session.removeAttribute("password_error_message");
        session.removeAttribute("username_error_message");
        session.removeAttribute("phone_number_error_message");
        session.removeAttribute("email_error_message");
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || address.isEmpty() || phoneNumber.isEmpty() || username.isEmpty() || password.isEmpty()) {
            session.setAttribute("error_message", "Everything must be filled!");
            isCorrect = false;
        }
        try {
            if (username != null && userService.getUserByUserName(username) != null) {//if user exists
                session.setAttribute("username_error_message", "Username should be unique!");
                isCorrect = false;
            }
        } catch (DBException e) {
            e.printStackTrace();
        }
        if (password.length() < 8) {
            session.setAttribute("password_error_message", "Password should has at least 8 symbols!");
            isCorrect = false;
        }

        if (!userService.isCorrectPhoneNumber(phoneNumber)) {
            session.setAttribute("phone_number_error_message", "Incorrect phone number format!");
            isCorrect = false;
        }
        if (!userService.isCorrectEmail(email)) {
            session.setAttribute("email_error_message", "Incorrect email format!");
            isCorrect = false;
        }
        log.debug("Data was checked with result: " + isCorrect);
        if (isCorrect) {
            return new User(0, firstName, lastName, username, password, email, address, phoneNumber, "USER");
        } else {
            return null;
        }
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}

