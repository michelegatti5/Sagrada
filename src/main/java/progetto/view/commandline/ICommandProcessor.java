package progetto.view.commandline;

import java.util.List;

public interface ICommandProcessor extends ICommand, IExecutible
{
	void registerCommand(ICommand command);
	void deregisterCommand(ICommand command);
	void deregisterCommand(String name);
	boolean existCommand(String name);
	boolean existCommand(ICommand command);
	List<ICommand> getList();
	ICommand getCommand(String command);
	String getContent();

}
