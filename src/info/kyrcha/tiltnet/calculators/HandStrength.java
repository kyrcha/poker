/*
 * LICENSE
 */
package info.kyrcha.tiltnet.calculators;

public class HandStrength {

	/**
	 * Given my hole cards and the board cards so far calculate the
	 * hand strength
	 * 
	 * @param holeCards the cards in my hand
	 * @param boardCards community cards
	 * @return hand strength in [0,1]
	 */
    public double handStrength(String holeCards, String boardCards) {
    	double ahead = 0;
    	double tied = 0;
    	double behind = 0;
    	// Rank my hand based on hole and board cards
    	int rank = 0;
    	// for each case(oppCards)
    	int oppRank = 0;
    	if(rank > oppRank) ahead++;
    	else if(rank == oppRank) tied++;
    	else behind++;
    	return (ahead + tied / 2) / (ahead + tied + behind);
    }

}