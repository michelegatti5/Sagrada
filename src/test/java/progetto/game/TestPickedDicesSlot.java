package progetto.game;

import junit.framework.TestCase;
import org.junit.Assert;

public class TestPickedDicesSlot extends TestCase {

	public void test1()
	{

		PickedDicesSlot pickedDicesSlot = new PickedDicesSlot();

		Dice dice0 = new Dice (Value.ONE , Color.YELLOW);

		assertEquals(0, pickedDicesSlot.getNDices());

		pickedDicesSlot.add(dice0, false, false, false);

		assertEquals(1, pickedDicesSlot.getNDices());

		Dice dice1 = new Dice (Value.TWO , Color.RED);

		pickedDicesSlot.add(dice1, true, false, false);

		assertEquals(2, pickedDicesSlot.getNDices());

		pickedDicesSlot.remove(1);

		assertEquals(1, pickedDicesSlot.getNDices());

		pickedDicesSlot.add(new Dice(Value.ONE, Color.YELLOW), false, true, false);
		assertEquals(2, pickedDicesSlot.getNDices());

		pickedDicesSlot.setIgnoreColor(1, true);
		assertEquals(true, (boolean)pickedDicesSlot.getPickedDicesSlotData().getDicePlacementCondition(1).getIgnoreColor());

		pickedDicesSlot.setIgnoreValue(1, false);
		assertEquals(false, (boolean)pickedDicesSlot.getPickedDicesSlotData().getDicePlacementCondition(1).getIgnoreValue());

		pickedDicesSlot.setIgnoreAdjacent(1, true);
		assertEquals(true, (boolean)pickedDicesSlot.getPickedDicesSlotData().getDicePlacementCondition(1).getIgnoreAdjacent());

		Assert.assertNull(pickedDicesSlot.getPickedDicesSlotData().getDicePlacementCondition(50));

		Assert.assertNull(pickedDicesSlot.getPickedDicesSlotData().setIgnoreAdjacent(50, false));
		Assert.assertNull(pickedDicesSlot.getPickedDicesSlotData().setIgnoreColor(50, false));
		Assert.assertNull(pickedDicesSlot.getPickedDicesSlotData().setIgnoreValue(50, false));


	}

}
