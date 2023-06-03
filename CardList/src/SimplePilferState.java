public class SimplePilferState implements PilferState
{
	// Initializes the Pilfer game for the specified number of players,
	// starting with the specified CardList, and dealing those cards to
	// the players, starting at player #0.  It is up to the client
	// to shuffle the deck before calling this.
	
	// to get num of players
	private int players;
	// to hold decks of cards
	private CardList<StandardFrenchCard>[] playerList;
	// changes in playTrick starts at 0
	private int leadPlayer = 0;
	// holds every players number of wins
	private int[] playerWins;
	// the player with the most wins, if game not done then returns -1
	private int winningPlayer = 0;
	
	public SimplePilferState(int numPlayers, CardList<StandardFrenchCard> deck)
	{
		players = numPlayers;
		playerList = deck.deal(numPlayers, deck.size() + 1); 
		playerWins = new int[numPlayers];
	}
	
	// "Copy-constructor": Creates a clone of spsOrig
	public SimplePilferState(SimplePilferState spsOrig)
	{
		players = spsOrig.getNumberOfPlayers();
		playerList = (CardList<StandardFrenchCard>[]) new CardList[spsOrig.getNumberOfPlayers()];
		for(int i = 0; i < spsOrig.getNumberOfPlayers(); i++)
		{
			playerList[i] = new CardList(spsOrig.playerList[i]);
		}
		playerWins = new int[spsOrig.getNumberOfPlayers()]; 
		
	}
	
	// Returns the number of players for this game
	public int getNumberOfPlayers()
	{
		return players;
	}

	// Returns an iterable for the specified player's hand.
	// PRECONDITION: 0 <= player < getNumPlayers()
	public Iterable<StandardFrenchCard> getHandIterable(int player)
	{
		return playerList[player];
	}
	
	// Plays a trick, given the choice of cards to play for each player.
	// PRECONDITION: iCardChoices.length == getNumPlayers()
	// PRECONDITION: iCardChoices[player] is a valid index of a card in player's hand
	public int playTrick(int[] iCardChoices)
	{
		StandardFrenchCard leadCard = playerList[leadPlayer].get(iCardChoices[leadPlayer]);
		int leadSuit = leadCard.getSuit(); 
		int maxValue = leadCard.getValue();

		int roundWinner = leadPlayer;
		

		for(int i = 0; i < iCardChoices.length; i++)
		{
			StandardFrenchCard currentCard = playerList[i].remove(iCardChoices[i]);
			if(currentCard.getSuit() == leadSuit && currentCard.getValue() > maxValue)
			{
				maxValue = currentCard.getValue();
				roundWinner = i;
			}
		}
		playerWins[roundWinner]++;
		int overallWinner = 0;
		for(int i = 0; i < playerWins.length; i++)
		{
			if(playerWins[i] > playerWins[overallWinner])
			{
				overallWinner = i;
			}
		}
		winningPlayer = overallWinner;
		leadPlayer = roundWinner;
		return roundWinner;
	}
		
	// Returns the number of the player who leads the next trick.  Initially, this is 0;
	// after the game has started, this is the player who won the previous trick
	public int getNextLeadPlayer()
	{
		return leadPlayer;
	}
		
	// Returns the number of tricks won by the specified player
	// PRECONDITION: 0 <= player < getNumPlayers()
	public int getNumberOfTricksWon(int player)
	{
		return playerWins[player];
	}
	
	// If the game is over, returns the index of the player who won
	// (the player who won the greatest number of tricks; if there
	// is a tie, any winning player may be returned).  If the game is
	// not over, returns -1.
	public int getWinner()
	{
		if(playerList[0].size() > 0)
		{
			return -1;
		}
		return winningPlayer;
	}
	
}