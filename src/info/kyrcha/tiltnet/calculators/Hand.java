/*
 * TO PUT LICENSE INFO
 */
package info.kyrcha.tiltnet.calculators;

/**
 * @author Kyriakos Chatzidimitriou (kyrcha@gmail.com)
 */
public class Hand {

    public final static int BETTER = 0;

    public final static int WORSE = 1;

    public final static int TIED = 2;

    public final static int MAX_CARDS = 7;

    public final static int NUM_HANDS = 9;

    public final static int STRAIGHT_FLUSH = 8;
    public final static int FOUR = 7;
    public final static int FULL_HOUSE = 6;
    public final static int FLUSH = 5;
    public final static int STRAIGHT = 4;
    public final static int TRIPS = 3;
    public final static int TWO_PAIRS = 2;
    public final static int PAIR = 1;
    public final static int HIGH_CARD = 0;
    public final static int BAD_HAND = -1;

    private final static int SHIFT = Card.NUM_RANKS * Card.NUM_RANKS * 
	Card.NUM_RANKS * Card.NUM_RANKS * Card.NUM_RANKS;

    private int[] cards;

    private int cardIndex;

    public Hand() { 
	cardIndex = 0;
	cards = new int[MAX_CARDS];
    }

    public Hand(String handString) {
	cardIndex = 0;
	cards = new int[MAX_CARDS];
	addCards(handString);
    }

    public final static int compareHands(Hand one, Hand two) {
	int rankOne = one.rankHand();
	int rankTwo = two.rankHand();
	if(rankOne < rankTwo) {
	    return WORSE;
	} else if(rankOne > rankTwo) {
	    return BETTER;
	} else {
	    return TIED;
	}
    }
    
    public int rankHand() {
	return rankHand(this);
    }
    
    private final static int rankHand(Hand hand) {
	boolean[] hands = new boolean[NUM_HANDS];
	boolean foundHand = false;
	int[] ranks = new int[Card.NUM_RANKS];
	int[] suits = new int[Card.NUM_SUITS];
	int[] kickers = {Card.BAD_CARD, Card.BAD_CARD, Card.BAD_CARD, 
			 Card.BAD_CARD, Card.BAD_CARD};

	// Parse with one pass and update ranks and suits registers
	for(int i = 0; i < hand.getNumOfCards(); i++) {
	    int index = hand.getCardIndex(i);
	    ranks[Card.getRank(index)]++;
	    suits[Card.getSuit(index)]++;
	}

	// Check for flush
	int flushSuit = -1;
	for(int i = 0; i < Card.NUM_SUITS; i++) {
	    if(suits[i] >= 5) {
		hands[FLUSH] = true;
		foundHand = true;
		flushSuit = i;
		break;
	    }
	}
	if(hands[FLUSH]) {
	    // Add kickers
	    for(int i = 0; i < hand.getNumOfCards(); i++) {
		int index = hand.getCardIndex(i);
		int cardRank = Card.getRank(index);
		int cardSuit = Card.getSuit(index);
		if(cardSuit == flushSuit) {
		    for(int j = 0; j < kickers.length; j++) {
			if(kickers[j] > cardRank) {
			    continue;
			} else {
			    // Shift
			    for(int k = (kickers.length-2); k >= j; k--) {
				kickers[k + 1] = kickers[k];
			    }
			    kickers[j] = cardRank;
			    break;
			}
		    }
		}
	    }
	}

	// Check for straight
	int consecutive = (ranks[Card.ACE] > 0) ? 1 : 0;
	int highestRank = -1;
	for(int i = 0; i < Card.NUM_RANKS; i++) {
	    if(ranks[i] > 0) {
		consecutive++;
		if(consecutive >= 5) {
		    hands[STRAIGHT] = true;
		    foundHand = true;
		    highestRank = i;
		}
	    } else {
		consecutive = 0;
	    }
	}
	
	// Check for straight-flush
	if(hands[FLUSH] && hands[STRAIGHT]) {
	    int counter = 0;
	    for(int i = 0; i < hand.getNumOfCards(); i++) {
		int index = hand.getCardIndex(i);
		int cardRank = Card.getRank(index);
		int cardSuit = Card.getSuit(index);
		if(cardSuit == flushSuit) {
		    if(highestRank == 3 && cardRank == Card.ACE) {
			counter++;
			continue;
		    }
		    if((cardRank <= highestRank) && cardRank >= (highestRank - 4)) {
			counter++;
		    }		    
		}
	    }
	    if(counter >= 5) {
		hands[STRAIGHT_FLUSH] = true;
		foundHand = true;
	    }
	}

	// Check for full house, four cards, three cards, two pairs or one pair
	int pairOne = -1;
	int pairTwo = -1;
	int trips = -1;
	int quads = -1;
	int kickerOne = -1;
	int kickerTwo = -1;
	for(int i = 0; i < Card.NUM_RANKS; i++) {
	    if(ranks[i] > 1) {
		if(ranks[i] == 4) {
		    hands[FOUR] = true;
		    foundHand = true;
		    quads = i;
		    break;
		} else if(ranks[i] == 3) {		    
		    trips = i;
		} else {
		    if(pairOne == Card.BAD_CARD) {
			pairOne = i;
		    } else {
			pairTwo = pairOne;
			pairOne = i;
		    }
		}
	    } else if(ranks[i] == 1) {
		if(kickerOne < i) {
		    kickerTwo = kickerOne;
		    kickerOne = i;		       
		} else if(kickerTwo < i) {
		    kickerTwo = i;
		}
	    }
	}
	if(trips != -1 && pairOne != -1) {
	    hands[FULL_HOUSE] = true;
	    foundHand = true;
	} else if(trips != -1 && pairOne == -1) {
	    hands[TRIPS] = true;
	    foundHand = true;
	} else if(pairOne != -1 && pairTwo != -1) {
	    hands[TWO_PAIRS] = true;
	    foundHand = true;
	} else if(pairOne != -1) {
	    hands[PAIR] = true;
	    foundHand = true;
	}

	if(!foundHand) {
	    // High card
	    hands[HIGH_CARD] = true;	    
	    // Add kickers
	    for(int i = 0; i < hand.getNumOfCards(); i++) {
		int index = hand.getCardIndex(i);
		int cardRank = Card.getRank(index);
		int cardSuit = Card.getSuit(index);
		for(int j = 0; j < kickers.length; j++) {
		    if(kickers[j] > cardRank) {
			continue;
		    } else {
			// Shift
			for(int k = (kickers.length-2); k >= j; k--) {
			    kickers[k + 1] = kickers[k];
			}
			kickers[j] = cardRank;
			break;
		    }
		}
	    }
	}
	
	// Calculate rank
	int rank = -1;
	if(hands[STRAIGHT_FLUSH]) { // STRAIGHT FLUSH
	    rank = STRAIGHT_FLUSH * SHIFT;
	    rank += highestRank;
	} else if(hands[FOUR]) { // FOUR OF A KIND
	    rank = FOUR * SHIFT;
	    rank += quads;
	} else if(hands[FULL_HOUSE]) { // FULL HOUSE
	    rank = FULL_HOUSE * SHIFT;
	    rank += trips * Card.NUM_RANKS;
	    rank += pairOne;
	} else if(hands[FLUSH]) { // FLUSH
	    rank = FLUSH * SHIFT;
	    int power = 1;
	    for(int i = (kickers.length-1); i >= 0; i--) {
		rank += kickers[i] * power;
		power *= Card.NUM_RANKS;
	    }
	} else if(hands[STRAIGHT]) { //STRAIGHT
	    rank = STRAIGHT * SHIFT;
	    rank += highestRank;
	} else if(hands[TRIPS]) { // TRIPS
	    rank = TRIPS * SHIFT;
	    rank += trips * Card.NUM_RANKS * Card.NUM_RANKS;
	    rank += kickerOne * Card.NUM_RANKS;
	    rank += kickerTwo;
	} else if(hands[TWO_PAIRS]) { // TWO PAIRS
	    rank = TWO_PAIRS * SHIFT;
	    rank += pairOne * Card.NUM_RANKS * Card.NUM_RANKS;
	    rank += pairTwo * Card.NUM_RANKS;
	    rank += kickerOne;
	} else if(hands[PAIR]) { // ONE PAIR
	    rank = PAIR * SHIFT;
	    rank += pairOne * Card.NUM_RANKS;
	} else if(hands[HIGH_CARD]) { // HIGH CARD
	    rank = HIGH_CARD * SHIFT;
	    int power = 1;
	    for(int i = (kickers.length-1); i >= 0; i--) {
		rank += kickers[i] * power;
		power *= Card.NUM_RANKS;
	    }
	}
	return rank;
    }
    
    public int getNumOfCards() {
	return cardIndex;
    }
    
    public int compareHandWith(Hand hand) {
	return compareHands(this, hand);
    }
    
    public Card getCard(int position) {
	if(position > (cardIndex - 1)) {
	    return new Card(cards[position]);
	}
	return null;
    }
    
    public int getCardIndex(int position) {
	if(position <= (cardIndex - 1)) {
	    return cards[position];
	}
	return -1;
    }
    
    public boolean addCard(Card card) {
	if(cardIndex >= MAX_CARDS) {
	    return false;
	} else {
	    cards[cardIndex] = card.getIndex();
	    cardIndex++;
	    return true;
	}
    }
    
    public boolean addCards(String handString) {
	String[] cardStrings = handString.split("\\:");
	if(cardStrings.length > (MAX_CARDS - cardIndex)) {
	    return false;
	} else {
	    for(int i = 0; i < cardStrings.length; i++) {
		addCard(new Card(cardStrings[i]));
	    }
	    return true;
	}
    }

    public final static String nameOfHand(int rank){
	int majorHand = rank / SHIFT;
	switch(majorHand) {
	case STRAIGHT_FLUSH: return "Straight Flush";
	case FOUR: return "Four of a Kind";
	case FULL_HOUSE: return "Full House";
	case FLUSH: return "Flush"; 
	case STRAIGHT: return "Straight"; 
	case TRIPS: return "Three of a Kind"; 
	case TWO_PAIRS: return "Two Pairs"; 
	case PAIR: return "Pair"; 
	case HIGH_CARD: return "High Card"; 
	default: return "Bad Card"; 
	}
    }
    
    public static void main(String[] args) {
	Hand handOne = new Hand();
	handOne.addCards("7c:8s:Ah:Kh:7d:2h:Qh");
	Hand handTwo = new Hand("Ad:2s:Kc:Qd:4c:5d");
	System.out.println(handOne.rankHand());
	System.out.println(handTwo.rankHand());
	System.out.println(nameOfHand(handOne.rankHand()));
	System.out.println(nameOfHand(handTwo.rankHand()));
    }
    
}