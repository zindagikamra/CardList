public class State
{
	private int players;
	// to hold decks of cards
	private CardList<StandardFrenchCard>[] playerList;
	// changes in playTrick starts at 0
	private int leadPlayer;
	// holds every players number of wins
	private int[] playerWins;
	// the player with the most wins, if game not done then returns -1
	private int winningPlayer;
	
	public State(UndoablePilferState current, CardList<StandardFrenchCard>[] playerCards, int[] playerWins)
	{
		players = current.getNumberOfPlayers();
		playerList = (CardList<StandardFrenchCard>[]) new CardList[current.getNumberOfPlayers()];
		for(int i = 0; i < current.getNumberOfPlayers(); i++)
		{
			playerList[i] = new CardList(playerCards[i]);
		}
		leadPlayer = current.getNextLeadPlayer();
		this.playerWins = playerWins;
		winningPlayer = current.getWinner();
	}
	
	public int getPlayers()
	{
		return players;
	}
	
	public CardList<StandardFrenchCard>[] getPlayerCards()
	{
		return playerList;
	}
	
	public int getLeadPlayer()
	{
		return leadPlayer;
	}
	public int[] getPlayerWins()
	{
		return playerWins;
	}
	public int getWinningPlayer()
	{
		return winningPlayer;
	}
}