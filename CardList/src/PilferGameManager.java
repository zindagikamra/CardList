import java.util.Scanner;



public class PilferGameManager 
{
	private int numPlayers;
	private UndoablePilferState game;
	
	public PilferGameManager(int numPlayersP)
	{
		numPlayers = numPlayersP;

		// Fresh deck from the box
		CardList<StandardFrenchCard> deck = new CardList<StandardFrenchCard>();
		for (int suit=0; suit < 4; suit++)
		{
			for (int value=0; value < 13; value++)
			{
				deck.add(new StandardFrenchCard(value, suit));
			}
		}
		
		// Mix up the cards by randomly cutting them 1000 times
		for (int i=0; i < 1000; i++)
		{
			deck.cut((int) (Math.random() * 51 + 1));
		}

		game = new UndoablePilferState(numPlayers, deck);
	}
	
	public void play()
	{
		Scanner scanner = new Scanner(System.in);
		while (game.getWinner() == -1)
		{
			printStats();
			for (int player = 0; player < numPlayers; player++)
			{
				printHand(player);
			}
			
			System.out.println("Player " + game.getNextLeadPlayer() + ": You lead the trick.  Type in the card index you choose.");
			System.out.println("Or, you may type undo or redo if applicable.");
			String input = scanner.nextLine();
			if (input.equals("undo"))
			{
				if (game.undo())
				{
					System.out.println("Undo completed");
				}
				else
				{
					System.out.println("Can't undo");
				}
			}
			else if (input.equals("redo"))
			{
				if (game.redo())
				{
					System.out.println("Redo completed");
				}
				else
				{
					System.out.println("Can't redo");
				}
			}
			else
			{
				int nextLead = playTrick(scanner, Integer.parseInt(input));
				System.out.println("Player " + nextLead + " wins the trick.");
			}
		}
		
		System.out.println("Player " + game.getWinner() + " wins!");
	}
	
	private void printStats()
	{
		System.out.println("Player\tNumber of tricks won");
		for (int player = 0; player < numPlayers; player++)
		{
			System.out.println(player + "\t" + game.getNumberOfTricksWon(player));
		}
	}
	
	private void printHand(int player)
	{
		System.out.print("Player " + player + ": ");
		
		int iCard = 0;
		for (StandardFrenchCard card : game.getHandIterable(player))
		{
			System.out.print(iCard + ":" + card + " ");
			iCard++;
		}
		
		System.out.println();
	}
	
	private int playTrick(Scanner scanner, int leadCardIndex)
	{
		int[] iCards = new int[numPlayers];
		iCards[game.getNextLeadPlayer()] = leadCardIndex;
		
		for (int i = 1; i < numPlayers; i++)
		{
			int player = (game.getNextLeadPlayer() + i) % numPlayers;
			System.out.println("Player " + player + ": type the index for the card you wish to play.");
			iCards[player] = Integer.parseInt(scanner.nextLine());
		}
		
		return game.playTrick(iCards);
	}
	
	public static void main(String[] args) 
	{
		// Configure a game for 4 players
		PilferGameManager mgr = new PilferGameManager(4);
		
		// Play the game
		mgr.play();
	}


}