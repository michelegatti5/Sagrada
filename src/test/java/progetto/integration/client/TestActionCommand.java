package progetto.integration.client;

import org.junit.Assert;
import org.junit.Test;
import progetto.controller.GameController;
import progetto.controller.StartGameAction;
import progetto.model.AddWindowFrameCoupleAction;
import progetto.model.FrameSelectionState;
import progetto.model.WindowFrameCouple;
import progetto.model.WindowFrameCoupleArray;

import java.util.List;

public class TestActionCommand {
	@Test
	public void testActionCommand()
	{
		GameController game = new GameController();
		List<WindowFrameCouple> windowFrameCouples = WindowFrameCoupleArray.getInstance().getList();
		for(int i=0; i<windowFrameCouples.size(); i++)
		{
			game.sendAction(new AddWindowFrameCoupleAction(windowFrameCouples.get(i)));
		}
		game.processAllPendingAction();
		ActionCommand command = new ActionCommand(StartGameAction.class, game);
		Assert.assertEquals(command.getName(), StartGameAction.class.getSimpleName());
		Assert.assertEquals("StartGameAction <playerID> ", command.getHelp());
		String[] s = {"-1"};
		Assert.assertEquals("Sent command", command.execute(s));
		game.processAllPendingAction();
		Assert.assertEquals(FrameSelectionState.class, game.getModel().getMainBoard().getData().getGameState().getClass());
		s = new String[0];
		command.execute(s);
		String[] s2 = {"1-"};

		Assert.assertEquals("Parameter 0 invalid: 1-", command.execute(s2));
	}
}
