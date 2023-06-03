import java.util.Iterator;
import java.util.NoSuchElementException;

public class CardList<Item> implements Iterable<Item>
{	
	private class Node
	{
		private Item item;
		private Node next;
		//private int index;
	}
	
	private class CustomIterator implements Iterator<Item>
	{
		private Node currentNode = first;
		
		public boolean hasNext()
	    {
	    	return (currentNode !=null);
	    }
		
	    public Item next()
	    {
	    	if(currentNode == null)
	    	{
	    		throw new NoSuchElementException();
	    	}
	    	Item toReturn = currentNode.item;
	    	currentNode = currentNode.next;
	    	return toReturn;
	    }
	}
	
	private Node first;
	private int length;
	private Node last;
	private int nextIndex = 0;
	// Creates an empty CardList
	public CardList()
	{
		first = null;
		last = null;
		length = 0;
	}
	
	// "Copy-constructor": Creates a clone of cardListP, populated
	// with copies of the elements from cardListP parameter.
	public CardList(CardList<Item> cardListP)
	{
		length = 0;
		Iterator<Item> iter = cardListP.iterator();
		
		while (iter.hasNext())
		{
			add(iter.next());
		}
	}
	 
	// Returns the number of cards stored in the CardList
	public int size()
	{
		return length;
	}
	
	// Returns the card at index k
	// PRECONDITION: 0 <= k < size()
	public Item get(int k)
	{
		if(/*first == null ||*/ k < 0 || k >= size())
		{
			throw new NoSuchElementException();
		}
		return getNodeAtIndex(k).item;
	}
	
	// Adds a card at the end of the CardList.
	public void add(Item newItem)
	{
		Node newNode = new Node();
		newNode.item = newItem;
		
		if(first == null)
		{
			//newNode.index = 0;
			first = newNode;
			last = newNode;
		}
		else
		{
			//newNode.index = last.index + 1;
			last.next = newNode;
			last = newNode;
		}
		length++;
	}
	
	// Removes and returns the card at the beginning of the list.  This is
	// the same as calling remove(0)
	// PRECONDITION: 0 < size()
	public Item remove()
	{
		Node oldNode = first;
		if(oldNode == null)
		{
			throw new NoSuchElementException();
		}
		if(oldNode.next != null)
		{
			first = first.next;
		}
		else
		{
			first = null;
			last = null;
		}
		length--;
		
		// PRINT STATEMENT FOR TESTING
		//System.out.println("Item Returned: " + oldNode.item);
		return oldNode.item;
	}
	
	// Removes and returns the card at index k, shifting the cards at
	// index k+1 and higher to the left (toward the beginning of the list).
	// PRECONDITION: 0 <= k < size()
	public Item remove(int k)
	{
		if(k == 0)
		{
			return remove();
		}
		Node currentNode = first;
		Node nodeToGet = null;
		if(/*first == null ||*/ k < 0 || k >= size())
		{
			throw new NoSuchElementException();
		}
		
		currentNode = getNodeAtIndex(k-1);
		
		nodeToGet = currentNode.next;
		if (nodeToGet==last)
		{
			last = currentNode;
		}
		
		if (currentNode==first &&  nodeToGet == last)
		{
			currentNode.next = null;
		}
		else
		{
			currentNode.next = nodeToGet.next;
		}
		
		length--;
			
		
		
		return nodeToGet.item;
	}
	
	
	private Node getNodeAtIndex(int k)
	{
		Node returnNode = first;
		
		for (int i=0; i<k;i++)
		{
			returnNode = returnNode.next;
		}
		
		return returnNode;
	}
	
	// Cuts the deck at index k, and completes the cut.
	// PRECONDITION: 0 < k < size()
	public void cut(int k)
	{
		
		if(/*first == null ||*/ k < 0 || k >= size())
		{
			throw new NoSuchElementException();
		}
		if(k != 0)
		{
			
			Node newLast = getNodeAtIndex(k-1);
			Node newFirst = newLast.next;
			Node inList = first;
			first = newFirst;
			last.next = inList;
			last = newLast;
			last.next = null;
		}
		
	}
	
	// Removes the cards from the CardList, one-by-one, inserting them
	// into the specified number of new CardLists, returning the
	// generated CardLists.
	public CardList<Item>[] deal(int numHands, int maxCardsPerHand)
	{
		CardList<Item>[] list = (CardList<Item>[]) new CardList[numHands];
		for(int i = 0; i < numHands; i++)
		{
			list[i] = new CardList();
		}
		
		int cardsPerHand = 0;
		while(maxCardsPerHand > cardsPerHand)
		{
			for(int i = 0; i < numHands; i++)
			{
				/*if(list[i] == null)
				{
					list[i] = new CardList(); 
				}*/
				if(size() == 0)
				{
					return list;
				}
				list[i].add(remove());
				// STATEMENT FOR TESTING
				//size();
			}
			cardsPerHand++;
		}
		return list;
		
	}
	
	// Returns a read-only iterator over the cards in the CardList
    public Iterator<Item> iterator()
    {
		return new CustomIterator();
    }
    // ####################### CHECK THIS ########################
    
    //Method made for testing
    public static void listSizeAndIterate(CardList<String> test)
    {
    	System.out.println("size " + test.size());
    	Iterator iter = test.iterator();
    	while(iter.hasNext())
    	{
    		System.out.print(iter.next()+ " ");
    		//System.out.println("Iteration Done");
    	}
    	System.out.println();
    }
    
    public static void main(String[] args)
    {
    	CardList<String> test = new CardList<String>();
    	//listSizeAndIterate(test); 
    	
    	test.add("100");
    	listSizeAndIterate(test);
    	
    	/*test.add("10");
    	listSizeAndIterate(test);
    
    	/*test.add("20");
    	listSizeAndIterate(test);

    	test.add("30");
    	listSizeAndIterate(test);

    	test.add("40");
    	listSizeAndIterate(test);
    	
    	test.cut(2);
    	listSizeAndIterate(test);
    	
    	test.remove(3);
    	listSizeAndIterate(test);
    	test.remove(0);
    	listSizeAndIterate(test);
    	test.cut(1);
    	listSizeAndIterate(test);
    	test.remove(1);
    	listSizeAndIterate(test);
    	
    	test.remove(0);
    	listSizeAndIterate(test);
    	
    	test.add("15");
    	listSizeAndIterate(test);*/

    }
    
    // ---------------------------------------------------------------------------------
    // The following methods are for *extra credit* only.  You may leave them
    // alone and still pass all the required tests for the project
    // ---------------------------------------------------------------------------------
    
	// EXTRA CREDIT ONLY
	// Inserts a card at index k, shifting the existing cards
	// at index k and higher (if any) to the right (toward the
	// end of the list).  If k == size(), this adds the card to
	// the end of the list.
	// PRECONDITION: 0 <= k <= size()
	private void add(int k, Item newItem)
	{
		throw new UnsupportedOperationException();
	}
    
	// EXTRA CREDIT ONLY
	// Shuffles the deck by interleaving the cards from 0 through
	// k - 1 with the cards from k through size() - 1.
	// PRECONDITION: 1 < k < size() - 1
	public void shuffle(int k)
	{
		throw new UnsupportedOperationException();
	}    
}