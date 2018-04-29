package progetto.game;

import progetto.utils.AbstractObservable;

/**
 * Support class to contain the dice (max 9) positioned in the RoundTrack
 */
public final class NineDices extends AbstractObservable<NineDices> {

	private static final int MAX_NUMBER_OF_DICES = 9;

	private Dice[] dice = new Dice[MAX_NUMBER_OF_DICES];
	private int numberOfDices=0;

	public Value getValue(int index)
	{
		return dice[index].getValue();
	}

	public Color getColor(int index)
	{
		return dice[index].getColor();
	}

	/**
	 * Add a dice to the group
	 */
	void addDice(Dice newDice)
	{
		if(numberOfDices>=MAX_NUMBER_OF_DICES)
		{
			return;
		}
		change(this);
		dice[numberOfDices]=newDice;
		numberOfDices++;
	}

	public int getNumberOfDices()
	{
		return numberOfDices;
	}

	/**
	 * Get a dice from the group, do not remove it
	 */
	public Dice getDice(int index)
	{
		return dice[index];
	}

	/**
	 * Put the new dice in the position index (in place of the previous one)
	 */
	void changeDice(int index, Dice newDice)
	{
		if (dice[index] != null)
		{
			change(this);
			dice[index]=newDice;
		}
	}
}
