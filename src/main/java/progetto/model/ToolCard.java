package progetto.model;

import java.io.Serializable;

/**
 * Tool card
 */
public class ToolCard implements Serializable {

	private String name;
	private String effect;
	private Color color;
	private int index;

	/**
	 * Constructor with name, effect and color of the card
	 * @param name name of the card
	 * @param effect effect of the card
	 */
	ToolCard(String name, String effect, Color color, int index)
	{
		this.name = name;
		this.effect = effect;
		this.color = color;
		this.index = index;
	}

	/**
	 * Get tool tip
	 * @return tool tip (name and effect)
	 */
	public String getToolTip()
	{
		//return card name and effect description
		return index + " " + color + " " + name + " " + effect;
	}

	/**
	 * Get index of the card
	 * @return index
	 */
	public int getIndex()
	{
		return index;
	}

	/**
	 * Get color of the card
	 * @return color
	 */
	public Color getColor() {
		return color;
	}
}