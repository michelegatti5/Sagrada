package progetto.model;

/**
 * Enum for available colors
 * @author Michele
 */
public enum GameColor {
	YELLOW, RED, BLUE, GREEN, PURPLE;


	/**
	 *
	 * @return color to string
	 */
	@Override
	public String toString()
	{
		String string;
		switch (this)
		{
			case YELLOW:
				string = "Giallo";
				break;
			case RED:
				string = "Rosso";
				break;
			case BLUE:
				string = "Blu";
				break;
			case GREEN:
				string = "Verde";
				break;
			default:
				string = "Viola";
		}
		return string;
	}
}