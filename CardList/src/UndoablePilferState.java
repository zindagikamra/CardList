public class UndoablePilferState implements PilferState
{
	// See the comments in SimplePilferState for descriptions of these methods
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
	// Undo stack
	private UndoablePilferState[] undo = new UndoablePilferState[1];
	private int undoSize = 0;
	// Current state
	private UndoablePilferState current;
	// Redo stack
	private UndoablePilferState[] redo = new UndoablePilferState[1];
	private int redoSize = 0;
	
	public UndoablePilferState(int numPlayers, CardList<StandardFrenchCard> deck)
	{
		players = numPlayers;
		playerList = deck.deal(numPlayers, deck.size() + 1); 
		playerWins = new int[numPlayers];
	}
	
	
	private UndoablePilferState(int playerNum, int leadPlayerC, int winningPlayerC, CardList<StandardFrenchCard>[] playerCards, int[] playerWinsC)
	{
		players = playerNum;
		playerList = new CardList[playerNum];
		playerWins = new int[playerNum];
		for(int i = 0; i < playerNum; i++)
		{
			playerList[i] = new CardList(playerCards[i]);
			playerWins[i] = playerWinsC[i];
		}
		leadPlayer = leadPlayerC;
		winningPlayer = winningPlayerC;
	}
	
	public int getNumberOfPlayers()
	{
		return players;
	}

	public Iterable<StandardFrenchCard> getHandIterable(int player)
	{
		return playerList[player];
	}
	
	public int getNextLeadPlayer()
	{
		return leadPlayer;
	}
		
	public int playTrick(int[] iCardChoices)
	{
		// Adding states to the undo stack
		//int[] listToGive = playerWins;
		pushUndoStack(new UndoablePilferState(players, leadPlayer, winningPlayer, playerList, playerWins));
		
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
		// Saving current State 
		//current = new UndoablePilferState(players, leadPlayer, winningPlayer, playerList, playerWins);
		
		
		redo = new UndoablePilferState[1];
		redoSize = 0; 
		return roundWinner;
		
	}
		
	public int getNumberOfTricksWon(int player) 
	{
		return playerWins[player];
	}
	
	public int getWinner()
	{
		if(playerList[0].size() > 0)
		{
			return -1;
		}
		return winningPlayer;
	}

	// Brings the game state back to how it was before the last trick was played.
	// Returns true if the undo was successful, otherwise returns false if
	// there is no last trick to undo.
	public boolean undo()
	{
		if(undo[0] == null)
		{
			return false;
		}
		
		UndoablePilferState undone = popUndo();
		pushRedoStack(new UndoablePilferState(players, leadPlayer, winningPlayer, playerList, playerWins));
		
		// resetting fields
		players = undone.players;
		leadPlayer = undone.leadPlayer;
		winningPlayer = undone.winningPlayer;
		playerList = undone.playerList;
		playerWins = undone.playerWins;
		
		return true;
	}
	
	// Brings the game state back to how it was before the most recent undo.
	// Returns true if the redo was successful, otherwise returns false if
	// there is no undone trick to redo.
	public boolean redo()
	{
		if(redo[0] == null)
		{
			return false;
		}
		
		UndoablePilferState undone = popRedo();
		pushUndoStack(new UndoablePilferState(players, leadPlayer, winningPlayer, playerList, playerWins));
		
		players = undone.players;
		leadPlayer = undone.leadPlayer;
		winningPlayer = undone.winningPlayer;
		playerList = undone.playerList;
		playerWins = undone.playerWins;
		
		return true;
	}
	
	
	// Resizing methods for Stacks
	public void pushUndoStack(UndoablePilferState a)
	{
		if(undoSize+1 >= undo.length)
		{
			UndoablePilferState[] newArr = new UndoablePilferState[undo.length*2];
			for(int i = 0; i < undo.length; i++) 
			{
				newArr[i] = undo[i];
			}
			undo = newArr;
		}
		
		undo[undoSize] = a;
		undoSize++;
	}
	
	public UndoablePilferState popUndo()
	{
		undoSize--;
		UndoablePilferState value = undo[undoSize];
		undo[undoSize] = null;
		
		if(undoSize <= undo.length/4)
		{
			UndoablePilferState[] newArr = new UndoablePilferState[undo.length/2];
			for(int i = 0; i < newArr.length; i++)
			{
				newArr[i] = undo[i];
			}
			undo = newArr;
		}
		
		return value;
	}
	
	public void pushRedoStack(UndoablePilferState a)
	{
		if(redoSize+1 >= redo.length)
		{
			UndoablePilferState[] newArr = new UndoablePilferState[redo.length*2];
			for(int i = 0; i < redo.length; i++) 
			{
				newArr[i] = redo[i];
			}
			redo = newArr;
		}
		
		redo[redoSize] = a;
		redoSize++;
		
	}
	
	public UndoablePilferState popRedo()
	{
		redoSize--;
		UndoablePilferState value = redo[redoSize];
		redo[redoSize] = null;
		
		if(redoSize <= redo.length/4)
		{
			UndoablePilferState[] newArr = new UndoablePilferState[redo.length/2];
			for(int i = 0; i < newArr.length; i++)
			{
				newArr[i] = redo[i];
			}
			redo = newArr;
		}
		
		return value;
		
	}
	
	public static void main(String[] args)
	{
		CardList<StandardFrenchCard> deck = new CardList<StandardFrenchCard>();
		deck.add(new StandardFrenchCard(0,0));
		deck.add(new StandardFrenchCard(1, 0));
		deck.add(new StandardFrenchCard(2,1));
		deck.add(new StandardFrenchCard(3,1));
		deck.add(new StandardFrenchCard(0,1));
		deck.add(new StandardFrenchCard(1, 1));
		deck.add(new StandardFrenchCard(2,2));
		deck.add(new StandardFrenchCard(3,2));
		deck.add(new StandardFrenchCard(0,2));
		deck.add(new StandardFrenchCard(1, 2));
		deck.add(new StandardFrenchCard(2,3));
		deck.add(new StandardFrenchCard(3,3));
		deck.add(new StandardFrenchCard(0,3));
		deck.add(new StandardFrenchCard(1, 3));
		deck.add(new StandardFrenchCard(2,0));
		deck.add(new StandardFrenchCard(3,0));
		UndoablePilferState game = new UndoablePilferState(4, deck);
		
		/*int[] place = {0,0,0,0};
		System.out.println(game.undo());
		game.playTrick(place);
		System.out.println(game.getNumberOfTricksWon(0));
		System.out.println(game.getNumberOfTricksWon(1));
		System.out.println(game.getNumberOfTricksWon(2));
		System.out.println(game.getNumberOfTricksWon(3));
		System.out.println();
		
		System.out.println(game.undo());
		
		System.out.println(game.getNumberOfTricksWon(0));
		System.out.println(game.getNumberOfTricksWon(1));
		System.out.println(game.getNumberOfTricksWon(2));
		System.out.println(game.getNumberOfTricksWon(3));
		
		System.out.println(game.redo());
		System.out.println(game.redo());*/
		
		
	}
}