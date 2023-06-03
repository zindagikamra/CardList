import java.util.Comparator;



/**
 * Represents a standard French playing card, with values ranging from Ace to King
 * and the four suits: Spades, Diamonds, Clubs, Hearts 
 */
public class StandardFrenchCard 
{
	private int value;
	private int suit;

	/**
	 * Initializes a single standard French playing card
	 * @param valueP The value of the card ranging between 0 and 12 inclusive,
	 * where 0 is an Ace, 1 is a two, ..., 9 is a ten, and 10, 11, and 12
	 * represent Jack, Queen, and King, respectively. 
	 * @param suitP The suit of the card, represented by an integer between 0 and
	 * 3 inclusive where 0 is Spades, 1 is Diamonds, 2 is Clubs, and 3 is Hearts
	 */
	public StandardFrenchCard(int valueP, int suitP)
	{
		if (0 <= valueP && valueP <= 12 &&
				0 <= suitP && suitP <= 3)
		{
			value = valueP;
			suit = suitP;
		}
		else
		{
			throw new UnsupportedOperationException("StandardFrenchCard(" + valueP + ", " + suitP + ") constructor called with an invalid parameter");
		}
	}

	/**
	 *  Returns the 0-based value of the card ranging between 0 and 12 inclusive,
	 * where 0 is an Ace, 1 is a two, ..., 9 is a ten, and 10, 11, and 12
	 * represent Jack, Queen, and King, respectively. 
	 */
	public int getValue()
	{
		return value;
	}

	/**
	 * Returns the suit of the card, represented by an integer between 0 and
	 * 3 inclusive where 0 is Spades, 1 is Diamonds, 2 is Clubs, and 3 is Hearts
	 */
	public int getSuit()
	{
		return suit;
	}

	/**
	 * Returns a two-character String describing the value and suit of the card.
	 * The zero-based indices used to initialize the card are translated into human-friendly
	 * names consistent with the traditional writing on a standard French-style playing card
	 */
	public String toString()
	{
		final String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
		final String[] suits = {"S", "D", "C", "H"};
		return values[value] + suits[suit];
	}

}