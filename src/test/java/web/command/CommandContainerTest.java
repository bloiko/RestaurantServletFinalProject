package web.command;

import org.junit.Assert;
import org.junit.Test;
import web.command.login.RegistrationCommand;

public class CommandContainerTest {
    @Test
    public void testCommandConatainerGet_ShouldReturnCorrectCommand(){
        Command command = CommandContainer.get("registration");

        Assert.assertTrue(command!=null);
        Assert.assertTrue(command instanceof RegistrationCommand);
    }
    @Test
    public void testCommandConatainerGet_ShouldReturnNoCommand(){
        Command command = CommandContainer.get("incorrectCommand");

        Assert.assertTrue(command!=null);
        Assert.assertTrue(command instanceof NoCommand);
    }
}
