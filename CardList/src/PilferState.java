public interface PilferState
{
	public int getNumberOfPlayers();
	public Iterable<StandardFrenchCard> getHandIterable(int player);
	public int getNextLeadPlayer();
	public int playTrick(int[] iCardChoices);
	public int getNumberOfTricksWon(int player);
	public int getWinner();
}