package web.command;

import web.command.admin.AdminChangeOrderStatusCommand;
import web.command.admin.AdminListCommand;
import web.command.cart.CartDeleteItemCommand;
import web.command.cart.CartListCommand;
import web.command.cart.CartOrderItemCommand;
import web.command.login.LoginAdminCommand;
import web.command.login.LoginMainCommand;
import web.command.login.LogoutCommand;
import web.command.login.RegistrationCommand;
import web.command.menu.MenuListCommand;
import web.command.menu.MenuOrderCommand;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Holder for all commands.
 *
 * @author B.Loiko
 */
public class CommandContainer {

    private static final Logger log = Logger.getLogger(CommandContainer.class);

    private static Map<String, Command> commands = new TreeMap<String, Command>();

    static {
        try {
            //admin
            commands.put("adminList", new AdminListCommand());
            commands.put("changeOrderStatus", new AdminChangeOrderStatusCommand());
            //cart
            commands.put("cartList", new CartListCommand());
            commands.put("cartDeleteItem", new CartDeleteItemCommand());
            commands.put("cartOrderItem", new CartOrderItemCommand());
            //thanks page
            commands.put("thanks", new ThanksCommand());
            //menu page
            commands.put("menuList", new MenuListCommand());
            commands.put("menuOrder", new MenuOrderCommand());
            //my order page
            commands.put("myOrders", new MyOrdersCommand());
            //login and logout pages
            commands.put("loginMain", new LoginMainCommand());
            commands.put("loginAdmin", new LoginAdminCommand());
            commands.put("logout", new LogoutCommand());
            //registration page
            commands.put("registration", new RegistrationCommand());
            //no command page
            commands.put("noCommand", new NoCommand());


        } catch (ServletException e) {
            e.printStackTrace();
        }

        log.debug("Command container was successfully initialized");
        log.trace("Number of commands --> " + commands.size());
    }

    /**
     * Returns web.command object with the given name.
     *
     * @param commandName Name of the web.command.
     * @return Command object.
     */
    public static Command get(String commandName) {
        if (commandName == null || !commands.containsKey(commandName)) {
            log.trace("Command not found, name --> " + commandName);
            return commands.get("noCommand");
        }

        return commands.get(commandName);
    }

}