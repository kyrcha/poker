public class HandStrength {

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