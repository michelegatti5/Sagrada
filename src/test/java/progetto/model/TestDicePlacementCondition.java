package progetto.model;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Test dice placement condition
 */
public class TestDicePlacementCondition extends TestCase {

	WindowFrameCoupleArray windowFrameCoupleArray;
	PlayerBoard playerBoard;

	@Before
	public void setUp()
	{
		List<WindowFrameCouple> windowFrameCouples = WindowFrameCoupleArray.getInstance().getList();
		playerBoard = new PlayerBoard();
		WindowFrame windowFrame = windowFrameCouples.get(1).getWindowFrame(0);
		playerBoard.setWindowFrame(windowFrame);

	}

	/**
	 * Test getter and setter
	 */
	@Test
	public void testGetSet()
	{
		Dice dice = new Dice(Value.FOUR, GameColor.GREEN);
		DicePlacementCondition dicePlacementCondition = new DicePlacementCondition(dice, false, true, false);

		Assert.assertEquals(dice, dicePlacementCondition.getDice());
		Assert.assertTrue(dicePlacementCondition.getIgnoreValue());
		Assert.assertFalse(dicePlacementCondition.getIgnoreColor());
		Assert.assertFalse(dicePlacementCondition.getIgnoreAdjacent());

		dicePlacementCondition = dicePlacementCondition.setIgnoreValue(false);
		Assert.assertFalse(dicePlacementCondition.getIgnoreValue());
		dicePlacementCondition = dicePlacementCondition.setIgnoreColor(true);
		Assert.assertTrue(dicePlacementCondition.getIgnoreColor());
		dicePlacementCondition = dicePlacementCondition.setIgnoreAdjacent(true);
		Assert.assertTrue(dicePlacementCondition.getIgnoreAdjacent());

	}

	/**
	 * Test add first dice near a edge
	 */
	@Test
	public void testPlacementConditionNearEdge()
	{
		Dice dice = new Dice(Value.FOUR, GameColor.GREEN);

		DicePlacementCondition dicePlacementCondition = new DicePlacementCondition(dice, false, false, false);

		//First dice near edge
		Assert.assertTrue(dicePlacementCondition.canBePlaced(0, 1, playerBoard));

	}

	/**
	 * Test add dice, value bond is respected
	 */
	@Test
	public void testPlacementConditionValueBondRespected()
	{
		Dice dice = new Dice(Value.FOUR, GameColor.GREEN);

		DicePlacementCondition dicePlacementCondition = new DicePlacementCondition(dice, false, false, false);

		//Value bond respected
		Assert.assertTrue(dicePlacementCondition.canBePlaced(0, 0, playerBoard));

	}

	/**
	 * Test add dice, color bond is respected
	 */
	@Test
	public void testPlacementConditionColorBondRespected()
	{
		Dice dice = new Dice(Value.FOUR, GameColor.GREEN);

		DicePlacementCondition dicePlacementCondition = new DicePlacementCondition(dice, false, false, false);

		//Color bond respected
		Assert.assertTrue(dicePlacementCondition.canBePlaced(0, 4, playerBoard));

	}

	/**
	 * Test add dice, adjacent bond is respected
	 */
	@Test
	public void testPlacementConditionAdjacentBondRespected()
	{
		Dice dice = new Dice(Value.FOUR, GameColor.GREEN);

		playerBoard.addDiceInPlacedFrame(dice, 0, 1);

		dice = new Dice(Value.ONE, GameColor.YELLOW);
		DicePlacementCondition dicePlacementCondition = new DicePlacementCondition(dice, false, false, false);

		//Adjacent bond respected
		Assert.assertTrue(dicePlacementCondition.canBePlaced(1, 1, playerBoard));

	}

	/**
	 * Test add dice - fail - first dice must be near a edge
	 */
	@Test
	public void testPlacementConditionFailDiceNotNearEdge()
	{
		Dice dice = new Dice(Value.ONE, GameColor.YELLOW);

		DicePlacementCondition dicePlacementCondition = new DicePlacementCondition(dice, false, false, false);

		//First dice not near edge
		Assert.assertFalse(dicePlacementCondition.canBePlaced(1, 1, playerBoard));

	}

	/**
	 * Test add dice - fail - value bond not respected
	 */
	@Test
	public void testPlacementConditionFailValueBondNotRespected()
	{
		Dice dice = new Dice(Value.ONE, GameColor.YELLOW);

		DicePlacementCondition dicePlacementCondition = new DicePlacementCondition(dice, false, false, false);

		//Value bond not respected
		Assert.assertFalse(dicePlacementCondition.canBePlaced(0, 0, playerBoard));

	}

	/**
	 * Test add dice - fail - color bond not respected
	 */
	@Test
	public void testPlacementConditionFailColorBondNotRespected()
	{
		Dice dice = new Dice(Value.ONE, GameColor.YELLOW);

		DicePlacementCondition dicePlacementCondition = new DicePlacementCondition(dice, false, false, false);

		//Color bond not respected
		Assert.assertFalse(dicePlacementCondition.canBePlaced(0, 4, playerBoard));

	}

	/**
	 * Test add dice - fail - adjacent bond not respected
	 */
	@Test
	public void testPlacementConditionFailAdjacentBondNotRespected()
	{
		Dice dice = new Dice(Value.ONE, GameColor.YELLOW);

		DicePlacementCondition dicePlacementCondition = new DicePlacementCondition(dice, false, false, false);

		dicePlacementCondition = new DicePlacementCondition(dice, false, false, false);

		playerBoard.addDiceInPlacedFrame(dice, 0, 1);

		//Adjacent bond not respected
		Assert.assertFalse(dicePlacementCondition.canBePlaced(2, 0, playerBoard));

	}

	/**
	 * Test add dice - fail - wrong index
	 */
	@Test
	public void testPlacementConditionFailWrongPosition()
	{
		Dice dice = new Dice(Value.ONE, GameColor.YELLOW);
		DicePlacementCondition dicePlacementCondition = new DicePlacementCondition(dice, false, false, false);

		Assert.assertFalse(dicePlacementCondition.canBePlaced(5, 5, playerBoard));

	}

	/**
	 * Test add dice - fail - dices with same color can't be near
	 */
	@Test
	public void testPlacementConditionSameColorNear()
	{
		Dice dice = new Dice(Value.ONE, GameColor.YELLOW);
		playerBoard.getDicePlacedFrame().addDice(dice, 0, 1);
		dice = new Dice(Value.TWO, GameColor.YELLOW);
		DicePlacementCondition dicePlacementCondition = new DicePlacementCondition(dice, false, false, false);
		Assert.assertFalse(dicePlacementCondition.canBePlaced(1, 1, playerBoard));

	}

	/**
	 * Test add dice - fail - dices with same value can't be near
	 */
	@Test
	public void testPlacementConditionSameValueNear()
	{
		Dice dice = new Dice(Value.ONE, GameColor.YELLOW);
		playerBoard.getDicePlacedFrame().addDice(dice, 0, 1);
		dice = new Dice(Value.ONE, GameColor.BLUE);
		DicePlacementCondition dicePlacementCondition = new DicePlacementCondition(dice, false, false, false);
		Assert.assertFalse(dicePlacementCondition.canBePlaced(1, 1, playerBoard));

	}

	/**
	 * Test add dice with ignore color bond
	 */
	@Test
	public void testPlacementConditionIgnoreColorBond()
	{
		Dice dice = new Dice(Value.ONE, GameColor.YELLOW);
		playerBoard.getDicePlacedFrame().addDice(dice, 0, 1);
		dice = new Dice(Value.TWO, GameColor.YELLOW);
		DicePlacementCondition dicePlacementCondition = new DicePlacementCondition(dice, true, false, false);
		Assert.assertTrue(dicePlacementCondition.canBePlaced(1, 1, playerBoard));

	}

	/**
	 * Test add dice with ignore value bond
	 */
	@Test
	public void testPlacementConditionIgnoreValueBond()
	{
		Dice dice = new Dice(Value.ONE, GameColor.YELLOW);
		playerBoard.getDicePlacedFrame().addDice(dice, 0, 1);
		dice = new Dice(Value.ONE, GameColor.BLUE);
		DicePlacementCondition dicePlacementCondition = new DicePlacementCondition(dice, false, true, false);
		Assert.assertTrue(dicePlacementCondition.canBePlaced(1, 1, playerBoard));

	}

	/**
	 * Test add dice with ignore adjacent bond
	 */
	@Test
	public void testPlacementConditionIgnoreAdjacentBond()
	{
		Dice dice = new Dice(Value.ONE, GameColor.YELLOW);
		playerBoard.getDicePlacedFrame().addDice(dice, 0, 1);
		dice = new Dice(Value.TWO, GameColor.BLUE);
		DicePlacementCondition dicePlacementCondition = new DicePlacementCondition(dice, false, false, true);
		Assert.assertTrue(dicePlacementCondition.canBePlaced(3, 4, playerBoard));

	}

	/**
	 * Test add dice - fail - in the selected position there is already a dice
	 */
	@Test
	public void testPlacementConditionFailTwoDiceSamePosition()
	{
		Dice dice = new Dice(Value.ONE, GameColor.YELLOW);
		playerBoard.getDicePlacedFrame().addDice(dice, 0, 1);
		dice = new Dice(Value.TWO, GameColor.BLUE);
		DicePlacementCondition dicePlacementCondition = new DicePlacementCondition(dice, true, true, true);
		Assert.assertFalse(dicePlacementCondition.canBePlaced(0, 1, playerBoard));

	}

	/**
	 * Test change dice
	 */
	@Test
	public void testChangeDice()
	{
		DicePlacementCondition dicePlacementCondition = new DicePlacementCondition(new Dice(Value.THREE, GameColor.PURPLE));
		dicePlacementCondition = dicePlacementCondition.changeDice(new Dice(Value.TWO, GameColor.RED));

		Assert.assertEquals(Value.TWO, dicePlacementCondition.getDice().getValue());
		Assert.assertEquals(GameColor.RED, dicePlacementCondition.getDice().getGameColor());

	}

}
