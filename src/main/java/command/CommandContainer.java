package command;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Holder for all commands.<br/>
 * 
 * @author D.Kolesnikov
 * 
 */
public class CommandContainer {
	
	private static final Logger log = Logger.getLogger(CommandContainer.class);
	
	private static Map<String, Command> commands = new TreeMap<String, Command>();
	
	static {
		try {
			commands.put("adminList", new AdminListCommand());
			commands.put("changeOrderStatus", new AdminChangeOrderStatusCommand());

			commands.put("cartList", new CartListCommand());
			commands.put("cartDeleteItem", new CartDeleteItemCommand());
			commands.put("cartOrderItem", new CartOrderItemCommand());

			commands.put("thanks", new ThanksCommand());

			commands.put("menuList", new MenuListCommand());
			commands.put("menuOrder", new MenuOrderCommand());

			commands.put("myOrders", new MyOrdersCommand());

			commands.put("loginMain", new LoginMainCommand());

			commands.put("loginAdmin", new LoginAdminCommand());

			commands.put("logout", new LogoutCommand());

			commands.put("registration", new RegistrationCommand());
		} catch (ServletException e) {
			e.printStackTrace();
		}

		log.debug("command.Command container was successfully initialized");
		log.trace("Number of commands --> " + commands.size());
	}

	/**
	 * Returns command object with the given name.
	 * 
	 * @param commandName
	 *            Name of the command.
	 * @return command.Command object.
	 */
	public static Command get(String commandName) {
		if (commandName == null || !commands.containsKey(commandName)) {
			log.trace("command.Command not found, name --> " + commandName);
			return commands.get("noCommand"); 
		}
		
		return commands.get(commandName);
	}
	
}