package info.kyrcha.tiltnet.calculators;

import java.util.Random;

public class Deck {
	
	private Random random = new Random();
	
	private Card[] cards = new Card[Card.NUM_CARDS];
	
	private int position;
	
	public Deck() {
		position = 0;
		for(int i = 0; i < Card.NUM_CARDS; i++) {
			cards[i] = new Card(i);
		}
		random.setSeed(System.currentTimeMillis());
		random.nextInt();
		random.nextInt();
		random.nextInt();
		random.nextInt();
		random.nextInt();
	}
	
   /**
    * Shuffles the deck
    */
	public void shuffle() {
		Card  tempCard;
		for (int i = 0; i < Card.NUM_CARDS; i++) {
			int j = i + random.nextInt(Card.NUM_CARDS - i);
			tempCard = cards[j];
			cards[j] = cards[i];
			cards[i] = tempCard;
		}
		position = 0;
	}
	
	public boolean removeCards(String cards) {
		int l = cards.length(); 
		if(l % 2 != 0) {
			return false;
		} else {
			int numOfCards = l / 2;
			for(int i = 0; i < numOfCards; i++) {
				Card card = new Card(cards.substring(2 * i, 2 * i +2));
				if(!remove(card)) return false;
			}
			return true;
		}
	}
	
	public boolean remove(Card card) {
		int index = card.getIndex();
		if(cards[index] != null) {
			cards[index] = null;
			return true;
		}  else {
			return false;
		}
	}
	
	public boolean putBack(Card card) {
		int index = card.getIndex();
		if(cards[index] == null) {
			cards[index] = card;
			return true;
		}  else {
			return false;
		}
	}
	
	public void reset() { 
		position = 0;
	}
	
	public boolean hasCards() {
		return (position < Card.NUM_CARDS) ? true : false;
	}
	
	public Card getCard(int index) {
		return cards[index];
	}
	
	public Card nextCard() {
		while(cards[position] == null) {
			position++;
		}
		return cards[position];
	}

}
