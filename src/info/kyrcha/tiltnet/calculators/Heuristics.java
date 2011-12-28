package info.kyrcha.tiltnet.calculators;

public class Heuristics {
	
	public final static double CHEN_MAX = 20.0d;
	
	public final static double CHEN_MIN = -1.5d;
	
	public static double ChenFormula(String holeCards) {
		Card c1 = new Card(holeCards.substring(0, 2));
		Card c2 = new Card(holeCards.substring(2, 4));
		int rank1 = c1.getRank();
		int rank2 = c2.getRank();
		int suit1 = c1.getSuit();
		int suit2 = c2.getSuit();
		int maxRank = Math.max(rank1, rank2);
		double strength = 0;
		// High card
		if(maxRank == Card.ACE) strength += 10;
		else if(maxRank == Card.KING) strength += 8;
		else if(maxRank == Card.QUEEN) strength += 7;
		else if(maxRank == Card.JACK) strength += 6;
		else strength += ((maxRank + 2) / 2.0);
		// Pairs
		if(rank1 == rank2) {
			int minimum = 5;
			if(rank1 == Card.FIVE) {
				minimum = 6;
			}
			strength = Math.max(strength * 2, minimum);
		}
		// Suited
		if(suit1 == suit2) strength += 2;
		// Closeness
		int gap = maxRank - Math.min(rank1, rank2) - 1;
		switch(gap) {
			case -1: break;
			case 0: strength++; break;
			case 1: strength--; break;
			case 2: strength -= 2; break;
			case 3: strength -= 4; break;
			default: strength -= 5; break;
		}
		if(gap == 1 && maxRank < Card.QUEEN) strength++;
		return strength;
	}
	
	public static void main(String[] args) {
		System.out.println("Testing Chen's formula for hole cards.");
		System.out.println("AdAs:\t" + ChenFormula("AdAs"));
		System.out.println("AdKd:\t" + ChenFormula("AdKd"));
		System.out.println("8d9d:\t" + ChenFormula("8d9d"));
		System.out.println("Kd9d:\t" + ChenFormula("Kd9d"));
		System.out.println("Jc9d:\t" + ChenFormula("Jc9d"));
		System.out.println("3d7c:\t" + ChenFormula("3d7c"));
		System.out.println("2d7c:\t" + ChenFormula("2d7c"));
	}

}
