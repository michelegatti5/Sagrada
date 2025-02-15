package progetto.controller;

import progetto.model.IModel;
import progetto.model.Model;
import progetto.model.RoundState;
import progetto.model.ToolCardState;

import java.util.List;

/**
 * Action to use tool card
 * @author Michele
 */
public class UseToolCardAction extends AbstractExecutibleGameAction{

	private final int nCard;

	private static final int INDEX7 = 7;
	private static final int INDEX8 = 8;

	/**
	 * Constructor without parameters
	 */
	public UseToolCardAction()
	{
		super(-1);
		nCard = -1;
	}

	/**
	 * Constructor to set values
	 * @param player callerID
	 * @param nCard tool card on the MainBoard to use
	 */
	public UseToolCardAction(int player, int nCard)
	{
		super(player);
		this.nCard = nCard;
	}

	/**
	 * Verify if action can be executed
	 * @param game the model in which this command should be executed
	 * @return result of the check
	 */
	@Override
	public boolean canBeExecuted(IModel game)
	{
		int currentPlayer = game.getRoundInformation().getData().getCurrentPlayer();

		if(currentPlayer != getCallerID() ||
				game.getMainBoard().getData().getGameState().getClass() != RoundState.class ||
				nCard>=game.getMainBoard().getData().getToolCards().size() || nCard < 0 ||
				game.getRoundInformation().getData().getUsedToolCard() )
		{
			return false;
		}

		boolean tokenEnough = true;

		if(game.getMainBoard().getData().getPlayerCount() != 1)
		{
			int nCallToolCard = game.getMainBoard().getData().getNCallToolCard(nCard);
			int playerToken = game.getPlayerBoard(getCallerID()).getData().getToken();
			int askedToken = 1;

			if (nCallToolCard > 0) {
				askedToken++;
			}

			tokenEnough = playerToken >= askedToken;

		}

		return tokenEnough && check7(game) && check8(game);

	}

	/**
	 * Execute action
	 * @param game the model in which we want to execute this command
	 */
	@Override
	public void execute(Model game)
	{
		int nCallToolCard = game.getMainBoard().getData().getNCallToolCard(nCard);
		int askedToken = 1;
		if(nCallToolCard>0)
		{
			askedToken++;
		}

		game.getRoundInformation().setToken(askedToken);
		game.getRoundInformation().setNCard(nCard);

		game.setState(new ToolCardState(game.getMainBoard().getData().getToolCards().get(nCard).getIndex()));

	}

	/**
	 * Tool card 7 works only if it is the second turn of the player
	 * @param game game
	 * @return canBeUsed
	 */
	private boolean check7(IModel game)
	{
		int cardIndex = game.getMainBoard().getData().getToolCards().get(nCard).getIndex();
		if(cardIndex == INDEX7)
		{
			List<Integer> playerQueue = game.getRoundInformation().getData().getRoundPlayerList();
			int nPlayer = getCallerID();
			while(!playerQueue.isEmpty())
			{
				int nextP = playerQueue.remove(0);
				if (nextP == nPlayer)   //check second turn of the player
				{
					return false;
				}
			}

		}

		return true;

	}

	/**
	 * Tool card 8 works only if it is the first turn of the player
	 * @param game game
	 * @return canBeUsed
	 */
	private boolean check8(IModel game)
	{
		int cardIndex = game.getMainBoard().getData().getToolCards().get(nCard).getIndex();

		if(cardIndex == INDEX8)
		{
			List<Integer> playerQueue = game.getRoundInformation().getData().getRoundPlayerList();
			int nPlayer = getCallerID();

			while(!playerQueue.isEmpty())
			{
				int nextP = playerQueue.remove(0);
				if (nextP == nPlayer)   //check first turn of the player
				{
					return true;
				}
			}
			return false;
		}

		return true;

	}

}
