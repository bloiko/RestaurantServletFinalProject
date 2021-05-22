package command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * Main interface for the command.Command pattern implementation.
 * 
 * @author D.Kolesnikov
 * 
 */
public abstract class Command implements Serializable {	
	private static final long serialVersionUID = 8879403039606311780L;
	public abstract void init() throws ServletException ;
	/**
	 * Execution method for command.
	 * @return Address to go once the command is executed.
	 */
	public abstract String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException;
	
	@Override
	public final String toString() {
		return getClass().getSimpleName();
	}
}