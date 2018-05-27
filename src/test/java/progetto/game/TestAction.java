package progetto.game;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestAction {

	private Game game;

	@Before
	public void setUp()
	{
		game = new Game();
	}

	@Test
	public void testSetSeed()
	{
		game.sendAction(new SetSeedAction(2));
		new SetSeedAction();
		game.processAllPendingAction();
		Assert.assertEquals(2, game.getSeed());

	}

	@Test
	public void testSetPlayerCount()
	{
		game.sendAction(new SetPlayerCountAction(3));
		game.processAction();
		game.processAction();
		Assert.assertEquals(3, game.getMainBoard().getData().getPlayerCount());
		game.sendAction(new StartGameAction());
		game.processAllPendingAction();
		game.sendAction(new SetPlayerCountAction(2));
		game.processAction();
		Assert.assertEquals(3, game.getMainBoard().getData().getPlayerCount());
	}

	@Test
	public void testGetPlayerID()
	{
		AbstractGameAction action = new AbstractGameAction(-2) {

			@Override
			public boolean canBeExecuted(Game game) {
				return false;
			}

			@Override
			protected void execute(Game game) {

			}
		};

		Assert.assertEquals(-2, action.getCallerID());

	}

	@Test
	public void testGetActionName()
	{
		SetPlayerCountAction s = new SetPlayerCountAction(2);
		Assert.assertEquals(s.getName(), SetPlayerCountAction.class.getSimpleName());
	}

	@Test
	public void testStartGame()
	{
		WindowFrameCoupleArray windowFrameCoupleArray = new WindowFrameCoupleArray();
		for(int i=0; i<windowFrameCoupleArray.getWindowFrameCouples().size(); i++)
		{
			game.sendAction(new AddWindowFrameCoupleAction(windowFrameCoupleArray.getWindowFrameCouples().get(i)));
		}
		game.processAllPendingAction();
		game.sendAction(new StartGameAction());
		game.processAction();
		game.sendAction(new StartGameAction());
		game.processAction();
		Assert.assertEquals(FrameSelectionState.class, game.getMainBoard().getData().getGameState().getClass());
		Assert.assertEquals("Frame selection", game.getMainBoard().getData().getGameState().getName());
	}

	@Test
	public void testNonExecutibleAction()
	{
		game.sendAction(new StartGameAction());
		game.sendAction(new SetSeedAction(2));
		game.processAllPendingAction();
		Assert.assertEquals(0, game.getSeed());
	}

	@Test
	public void testActionFormat()
	{
		String s = (AbstractGameAction.getStructure(SetPlayerCountAction.class));
		Assert.assertEquals("SetPlayerCountAction <playerID> <playerCount> ", s);
	}

	@Test
	public void testActionCreation()
	{
		int[] args = new int[2];
		args[0] = -1;
		args[1] = 2;

		AbstractGameAction a = AbstractGameAction.createAction(SetPlayerCountAction.class, args);
		Assert.assertEquals("SetPlayerCountAction playerID: -1 playerCount: 2 ", a.getToolTip());
	}


	@Test
	public void testNonCreatableAction()
	{
		AbstractGameAction a = AbstractGameAction.createAction(
				AddWindowFrameCoupleAction.class,
				null);
		Assert.assertNull(a);
	}

	@Test
	public void testCanBeExecutedAddWindowFrameCoupleAction()
	{
		WindowFrameCoupleArray windowFrameCoupleArray = new WindowFrameCoupleArray();

		WindowFrameCouple windowFrameCouple = windowFrameCoupleArray.getWindowFrameCouples().get(1);

		AddWindowFrameCoupleAction a = new AddWindowFrameCoupleAction(windowFrameCouple);

		Assert.assertTrue(a.canBeExecuted(game));

		game.setState(new FrameSelectionState());

		Assert.assertFalse(a.canBeExecuted(game));

	}

	@Test
	public void testFrameSetAction()
	{
		WindowFrameCoupleArray windowFrameCoupleArray = new WindowFrameCoupleArray();
		for(int i=0; i<windowFrameCoupleArray.getWindowFrameCouples().size(); i++)
		{
			game.sendAction(new AddWindowFrameCoupleAction(windowFrameCoupleArray.getWindowFrameCouples().get(i)));
		}
		game.processAllPendingAction();
		FrameSetAction a = new FrameSetAction(0, 1, 0);
		Assert.assertFalse(a.canBeExecuted(game));
		game.setState(new FrameSelectionState());
		Assert.assertTrue(a.canBeExecuted(game));

	}

	@Test
	public void testPickDiceAction()
	{
		PickDiceAction p = new PickDiceAction(0, 0);
		Assert.assertFalse(p.canBeExecuted(game));
		game.setState(new RoundState());
		Assert.assertFalse(p.canBeExecuted(game));
		game.getMainBoard().getExtractedDices().addDice(new Dice(Value.ONE, Color.YELLOW));
		Assert.assertTrue(p.canBeExecuted(game));
		p = new PickDiceAction(1, 0);
		Assert.assertFalse(p.canBeExecuted(game));

	}

	@Test
	public void testPlaceDiceAction()
	{
		WindowFrame windowFrame = (new WindowFrameCoupleArray().getWindowFrameCouples().get(1).getWindowFrame(0));
		game.getPlayerBoard(0).setWindowFrame(windowFrame);
		PlaceDiceAction p = new PlaceDiceAction(0, 0, 0, 0);
		Assert.assertFalse(p.canBeExecuted(game));
		game.setState(new RoundState());
		Assert.assertFalse(p.canBeExecuted(game));
		game.getPlayerBoard(0).getPickedDicesSlot().add(new Dice(Value.FOUR, Color.YELLOW), true, true, true);
		Assert.assertTrue(p.canBeExecuted(game));
		game.sendAction(p);
		game.processAction();
		Assert.assertNotNull(game.getPlayerBoard(0).getDicePlacedFrame().getData().getDice(0 , 0));
		p = new PlaceDiceAction(1, 0, 0, 0);
		Assert.assertFalse(p.canBeExecuted(game));

	}

	@Test
	public void testAddWindowFrameCoupleAction() {
		WindowFrameCoupleArray windowFrameCoupleArray = new WindowFrameCoupleArray();
		WindowFrameCouple windowFrameCouple = windowFrameCoupleArray.getWindowFrameCouples().get(1);

		game.sendAction(new AddWindowFrameCoupleAction(windowFrameCouple));
		game.processAction();

		Assert.assertEquals(windowFrameCouple, game.getMainBoard().getData().getWindowFrame(0));

	}

	@Test
	public void testStartGameAction()
	{
		WindowFrameCoupleArray windowFrameCoupleArray = new WindowFrameCoupleArray();
		for(int i=0; i<windowFrameCoupleArray.getWindowFrameCouples().size(); i++)
		{
			game.sendAction(new AddWindowFrameCoupleAction(windowFrameCoupleArray.getWindowFrameCouples().get(i)));
		}
		game.processAllPendingAction();
		game.sendAction(new StartGameAction());
		game.processAction();
		Assert.assertEquals("Frame selection", game.getMainBoard().getData().getGameState().getName());
	}

	@Test
	public void testCurrentPlayer() {
		WindowFrameCoupleArray windowFrameCoupleArray = new WindowFrameCoupleArray();
		for(int i=0; i<windowFrameCoupleArray.getWindowFrameCouples().size(); i++)
		{
			game.sendAction(new AddWindowFrameCoupleAction(windowFrameCoupleArray.getWindowFrameCouples().get(i)));
		}
		game.processAllPendingAction();
		WindowFrameCouple windowFrameCouple = windowFrameCoupleArray.getWindowFrameCouples().get(1);
		WindowFrame windowFrame = windowFrameCouple.getWindowFrame(0);
		MainBoard mainBoard = game.getMainBoard();
		mainBoard.setPlayerCount(4);
		game.sendAction(new StartGameAction());
		game.processAction();
		game.sendAction(new FrameSetAction(0, -1, 0));
		game.sendAction(new FrameSetAction(1, -1, 0));
		game.sendAction(new FrameSetAction(2, -1, 0));
		game.sendAction(new FrameSetAction(3, -1, 0));
		game.processAllPendingAction();

		Assert.assertEquals(3, game.getMainBoard().getData().getCurrentFirstPlayer());
		Assert.assertEquals(3, game.getMainBoard().getData().getCurrentPlayer());
	}

	@Test
	public void testGetNDices()
	{
		WindowFrameCoupleArray windowFrameCoupleArray = new WindowFrameCoupleArray();
		WindowFrameCouple windowFrameCouple = windowFrameCoupleArray.getWindowFrameCouples().get(1);
		WindowFrame windowFrame = windowFrameCouple.getWindowFrame(0);
		MainBoard mainBoard = game.getMainBoard();
		mainBoard.setPlayerCount(4);
		game.sendAction(new StartGameAction());
		game.processAction();
		game.sendAction(new FrameSetAction(0, -1, 0));
		game.sendAction(new FrameSetAction(1, -1, 0));
		game.sendAction(new FrameSetAction(2, -1, 0));
		game.sendAction(new FrameSetAction(3, -1, 0));
		game.processAllPendingAction();

		Assert.assertEquals(9, game.getMainBoard().getExtractedDices().getData().getNumberOfDices());

	}

	@Test
	public void testPickDiceAndPlaceDiceAction()
	{
		WindowFrameCoupleArray windowFrameCoupleArray = new WindowFrameCoupleArray();
		for(int i=0; i<windowFrameCoupleArray.getWindowFrameCouples().size(); i++)
		{
			game.sendAction(new AddWindowFrameCoupleAction(windowFrameCoupleArray.getWindowFrameCouples().get(i)));
		}
		game.processAllPendingAction();
		WindowFrameCouple windowFrameCouple = windowFrameCoupleArray.getWindowFrameCouples().get(1);
		WindowFrame windowFrame = windowFrameCouple.getWindowFrame(0);
		MainBoard mainBoard = game.getMainBoard();
		mainBoard.setPlayerCount(4);
		game.getPlayerBoard(0).setWindowFrame(windowFrame);
		game.getPlayerBoard(1).setWindowFrame(windowFrame);
		game.getPlayerBoard(2).setWindowFrame(windowFrame);
		game.getPlayerBoard(3).setWindowFrame(windowFrame);
		game.sendAction(new StartGameAction());
		game.processAllPendingAction();
		game.sendAction(new FrameSetAction(0, -1, 0));
		game.sendAction(new FrameSetAction(1, -1, 0));
		game.sendAction(new FrameSetAction(2, -1, 0));
		game.sendAction(new FrameSetAction(3, -1, 0));
		game.processAllPendingAction();

		Assert.assertEquals(3, game.getMainBoard().getData().getCurrentPlayer());

		game.sendAction(new PickDiceAction(3, 0));
		game.processAction();
		Assert.assertEquals(Color.YELLOW, game.getPlayerBoard(3).getPickedDicesSlot().getData().getDicePlacementCondition(0).getDice().getColor());
		Assert.assertEquals(Value.FIVE, game.getPlayerBoard(3).getPickedDicesSlot().getData().getDicePlacementCondition(0).getDice().getValue());

		game.sendAction(new PlaceDiceAction(3, 0, 1, 0));
		game.processAction();
		Assert.assertEquals(1, game.getPlayerBoard(3).getDicePlacedFrame().getData().getNDices());
		Assert.assertEquals(Color.YELLOW, game.getPlayerBoard(3).getDicePlacedFrame().getData().getDice(1, 0).getColor());
		Assert.assertEquals(Value.FIVE, game.getPlayerBoard(3).getDicePlacedFrame().getData().getDice(1, 0).getValue());

	}

	@Test
	public void testEndTurnAction()
	{
		WindowFrameCoupleArray windowFrameCoupleArray = new WindowFrameCoupleArray();
		for(int i=0; i<windowFrameCoupleArray.getWindowFrameCouples().size(); i++)
		{
			game.sendAction(new AddWindowFrameCoupleAction(windowFrameCoupleArray.getWindowFrameCouples().get(i)));
		}
		game.processAllPendingAction();
		WindowFrameCouple windowFrameCouple = windowFrameCoupleArray.getWindowFrameCouples().get(1);
		MainBoard mainBoard = game.getMainBoard();
		mainBoard.setPlayerCount(4);
		game.sendAction(new StartGameAction());
		game.processAction();
		game.sendAction(new FrameSetAction(0, -1, 0));
		game.sendAction(new FrameSetAction(1, -1, 0));
		game.sendAction(new FrameSetAction(2, -1, 0));
		game.sendAction(new FrameSetAction(3, -1, 0));
		game.processAllPendingAction();

		game.sendAction(new EndTurnAction(2));
		game.processAction();
		Assert.assertEquals(3, game.getMainBoard().getData().getCurrentPlayer());
	}

	@Test
	public void testEndTurnDicePickedNotPlaced()
	{
		WindowFrameCoupleArray windowFrameCoupleArray = new WindowFrameCoupleArray();
		for(int i=0; i<windowFrameCoupleArray.getWindowFrameCouples().size(); i++)
		{
			game.sendAction(new AddWindowFrameCoupleAction(windowFrameCoupleArray.getWindowFrameCouples().get(i)));
		}
		game.processAllPendingAction();
		WindowFrameCouple windowFrameCouple = windowFrameCoupleArray.getWindowFrameCouples().get(1);
		WindowFrame windowFrame = windowFrameCouple.getWindowFrame(0);
		MainBoard mainBoard = game.getMainBoard();
		mainBoard.setPlayerCount(4);
		game.getPlayerBoard(0).setWindowFrame(windowFrame);
		game.getPlayerBoard(1).setWindowFrame(windowFrame);
		game.getPlayerBoard(2).setWindowFrame(windowFrame);
		game.getPlayerBoard(3).setWindowFrame(windowFrame);
		game.sendAction(new StartGameAction());
		game.processAction();
		game.sendAction(new FrameSetAction(0, -1, 0));
		game.sendAction(new FrameSetAction(1, -1, 0));
		game.sendAction(new FrameSetAction(2, -1, 0));
		game.sendAction(new FrameSetAction(3, -1, 0));
		game.processAllPendingAction();
		game.sendAction(new PickDiceAction(3, 0));
		game.processAction();
		game.sendAction(new PlaceDiceAction(3, 0, 1, 0));
		game.processAction();
		game.sendAction(new EndTurnAction(3));
		game.processAction();

		Dice dice = game.getMainBoard().getExtractedDices().getData().getDice(0);
		Assert.assertEquals(0, game.getMainBoard().getData().getCurrentPlayer());
		//pick dice
		game.sendAction(new PickDiceAction(0, 0));
		game.processAction();
		Assert.assertEquals(7, game.getMainBoard().getExtractedDices().getData().getNumberOfDices());
		//end turn, picked dice not placed
		game.sendAction(new EndTurnAction(0));
		game.processAction();
		//not placed dice back to extractedDices
		Assert.assertEquals(dice, game.getMainBoard().getExtractedDices().getData().getDice(7));
		Assert.assertEquals(8, game.getMainBoard().getExtractedDices().getData().getNumberOfDices());
	}

	@Test
	public void testStartRoundState()
	{
		game.getMainBoard().setPlayerCount(1);
		game.setState(new StartRoundState());
		Assert.assertEquals(4, game.getMainBoard().getExtractedDices().getData().getNumberOfDices());
	}

	@Test
	public void testEndRoundEndGame()
	{
		WindowFrameCoupleArray windowFrameCoupleArray = new WindowFrameCoupleArray();
		for(int i=0; i<windowFrameCoupleArray.getWindowFrameCouples().size(); i++)
		{
			game.sendAction(new AddWindowFrameCoupleAction(windowFrameCoupleArray.getWindowFrameCouples().get(i)));
		}
		game.processAllPendingAction();
		WindowFrameCouple windowFrameCouple = windowFrameCoupleArray.getWindowFrameCouples().get(1);
		WindowFrame windowFrame = windowFrameCouple.getWindowFrame(0);
		MainBoard mainBoard = game.getMainBoard();
		mainBoard.setPlayerCount(4);
		game.getPlayerBoard(0).setWindowFrame(windowFrame);
		game.getPlayerBoard(1).setWindowFrame(windowFrame);
		game.getPlayerBoard(2).setWindowFrame(windowFrame);
		game.getPlayerBoard(3).setWindowFrame(windowFrame);
		game.sendAction(new StartGameAction());
		game.processAction();
		game.sendAction(new FrameSetAction(0, -1, 0));
		game.sendAction(new FrameSetAction(1, -1, 0));
		game.sendAction(new FrameSetAction(2, -1, 0));
		game.sendAction(new FrameSetAction(3, -1, 0));
		game.processAllPendingAction();
		Assert.assertEquals(3, game.getMainBoard().getData().getCurrentPlayer());
		game.sendAction(new PickDiceAction(3, 0));
		game.processAction();
		game.sendAction(new PlaceDiceAction(3, 0, 1, 0));
		game.processAction();
		game.sendAction(new EndTurnAction(3));
		game.processAction();
		Assert.assertEquals(0, game.getMainBoard().getData().getCurrentPlayer());
		game.sendAction(new EndTurnAction(0));
		game.processAction();
		game.sendAction(new EndTurnAction(1));
		game.processAction();
		Assert.assertEquals(2, game.getMainBoard().getData().getCurrentPlayer());
		game.sendAction(new EndTurnAction(2));
		game.processAction();
		Assert.assertEquals(2, game.getMainBoard().getData().getCurrentPlayer());
		game.sendAction(new PickDiceAction(2, 1));
		game.processAction();
		game.sendAction(new EndTurnAction(2));
		game.processAction();
		Assert.assertEquals(1, game.getMainBoard().getData().getCurrentPlayer());
		game.sendAction(new EndTurnAction(1));
		game.processAction();
		Assert.assertEquals(0, game.getMainBoard().getData().getCurrentPlayer());
		game.sendAction(new EndTurnAction(0));
		game.processAction();
		Assert.assertEquals(3, game.getMainBoard().getData().getCurrentPlayer());
		game.sendAction(new EndTurnAction(3));
		game.processAction();
		Assert.assertEquals(0, game.getMainBoard().getData().getCurrentPlayer());
		Assert.assertFalse(game.getRoundTrack().getData().isFree(0));
		Assert.assertTrue(game.getRoundTrack().getData().isFree(1));
		Assert.assertEquals(1, game.getMainBoard().getData().getCurrentRound());
		for(int i=0; i<90; i++)
		{
			game.sendAction(new EndTurnAction(game.getMainBoard().getNextPlayer()));
			game.processAction();
		}

		Assert.assertEquals("End game", game.getMainBoard().getData().getGameState().getName());

	}

}
