package com.genomics;

public class LocalAlignment implements IAlignment{

	public Cell[][] T;
	public char[] s1;
	public char[] s2;
	Output output = new Output();

	public LocalAlignment(char[] s1, char[] s2) {
		this.s1 = s1;
		this.s2 = s2;
 	}

	// To generate score matrix
	@Override
	public Output generateScoreMatrix() {
		int optimalScore = 0;
		int imax=0,jmax=0;
		T = new Cell[s1.length + 1][s2.length + 1];

		T[0][0] = new Cell();
		T[0][0].setInsertion(Constants.ZERO);
		T[0][0].setDeletion(Constants.ZERO);
		T[0][0].setSubstitution(Constants.ZERO);

		for (int i = 0; i < T.length; i++) {
			T[i][0] = new Cell();
			T[i][0].setSubstitution(Constants.ZERO);
			T[i][0].setDeletion(Constants.ZERO);
			T[i][0].setInsertion(Constants.ZERO);
		}

		for (int i = 1; i < T[0].length; i++) {
			T[0][i] = new Cell();
			T[0][i].setSubstitution(Constants.ZERO);
			T[0][i].setDeletion(Constants.ZERO);
			T[0][i].setInsertion(Constants.ZERO);
		}

		for (int i = 1; i < T.length; i++) {
			for (int j = 1; j < T[0].length; j++) {

				T[i][j] = new Cell();

				T[i][j].setInsertion(calculateMax(
						T[i][j - 1].getSubstitution() + Score.opening_gap_penalty + Score.gap_extension_penalty,
						T[i][j - 1].getInsertion() + Score.gap_extension_penalty,
						T[i][j - 1].getDeletion() + Score.opening_gap_penalty + Score.gap_extension_penalty,
						Constants.ZERO));

				T[i][j].setDeletion(calculateMax(
						T[i - 1][j].getSubstitution() + Score.opening_gap_penalty + Score.gap_extension_penalty,
						T[i - 1][j].getDeletion() + Score.gap_extension_penalty,
						T[i - 1][j].getInsertion() + Score.gap_extension_penalty + Score.opening_gap_penalty,
						Constants.ZERO));

				T[i][j].setSubstitution(calculateMax(T[i - 1][j - 1].getSubstitution(), T[i - 1][j - 1].getInsertion(),
						T[i - 1][j - 1].getDeletion()) + findScore(s1[i - 1], s2[j - 1]));
				
				if(calculateMax(T[i][j].getDeletion(),T[i][j].getInsertion(),T[i][j].getSubstitution())>optimalScore) {
					imax = i;
					jmax = j;
					optimalScore = calculateMax(optimalScore,T[i][j].getDeletion(),T[i][j].getSubstitution(),T[i][j].getInsertion());
				}
			}
		}
		
		//printMatrix();
		
		output.setOptimalScore(optimalScore);
		generateOptimalPath(imax,jmax);
		return output;
	}

	// To generate optimal path using the Score matrix
	public void generateOptimalPath(int imax,int jmax) {
		try {
			int i = imax;
			int j = jmax;
			int max = 0;
			int numOpeningGaps = 0;
			int numGaps = 0;
			int numMatches = 0;
			int numMismatches = 0;
			String alignedStringS1 = "";
			String alignedStringS2 = "";
	
			while (calculateMax(T[i][j].getDeletion(),T[i][j].getInsertion(),T[i][j].getSubstitution())>0) {
				max = calculateMax(T[i][j].getInsertion(), T[i][j].getDeletion(), T[i][j].getSubstitution());
				if (max == calculateMax(T[i][j - 1].getInsertion() + Score.gap_extension_penalty,
						T[i][j - 1].getDeletion() + Score.gap_extension_penalty + Score.opening_gap_penalty,
						T[i][j - 1].getSubstitution() + Score.opening_gap_penalty + Score.gap_extension_penalty)) {
					if (calculateMax(T[i][j - 1].getInsertion() + Score.gap_extension_penalty,
							T[i][j - 1].getDeletion() + Score.gap_extension_penalty + Score.opening_gap_penalty,
							T[i][j - 1].getSubstitution() + Score.opening_gap_penalty
									+ Score.gap_extension_penalty,0) == T[i][j - 1].getDeletion()
											+ Score.gap_extension_penalty + Score.opening_gap_penalty
							|| calculateMax(T[i][j - 1].getInsertion() + Score.gap_extension_penalty,
									T[i][j - 1].getDeletion() + Score.gap_extension_penalty + Score.opening_gap_penalty,
									T[i][j - 1].getSubstitution() + Score.opening_gap_penalty
											+ Score.gap_extension_penalty,0) == T[i][j - 1].getSubstitution()
													+ Score.opening_gap_penalty + Score.gap_extension_penalty) {
						numOpeningGaps++;
					}
	
					alignedStringS1 = alignedStringS1 + "-";
					alignedStringS2 = alignedStringS2 + s2[j - 1];
					j--;
					numGaps++;
				} else if (max == calculateMax(
						T[i - 1][j].getInsertion() + Score.gap_extension_penalty + Score.opening_gap_penalty,
						T[i - 1][j].getDeletion() + Score.gap_extension_penalty,
						T[i - 1][j].getSubstitution() + Score.gap_extension_penalty + Score.opening_gap_penalty)) {
					if (calculateMax(T[i - 1][j].getInsertion() + Score.gap_extension_penalty + Score.opening_gap_penalty,
							T[i - 1][j].getDeletion() + Score.gap_extension_penalty,
							T[i - 1][j].getSubstitution() + Score.gap_extension_penalty
									+ Score.opening_gap_penalty,0) == T[i - 1][j].getInsertion() + Score.gap_extension_penalty
											+ Score.opening_gap_penalty
							|| calculateMax(
									T[i - 1][j].getInsertion() + Score.gap_extension_penalty + Score.opening_gap_penalty,
									T[i - 1][j].getDeletion() + Score.gap_extension_penalty,
									T[i - 1][j].getSubstitution() + Score.gap_extension_penalty
											+ Score.opening_gap_penalty,0) == T[i - 1][j].getSubstitution()
													+ Score.gap_extension_penalty + Score.opening_gap_penalty) {
						numOpeningGaps++;
					}
					alignedStringS1 = alignedStringS1 + s1[i - 1];
					alignedStringS2 = alignedStringS2 + "-";
					i--;
					numGaps++;
				} else if (max == (calculateMax(T[i - 1][j - 1].getInsertion(), T[i - 1][j - 1].getDeletion(),
						T[i - 1][j - 1].getSubstitution()) + findScore(s1[i - 1], s2[j - 1]))) {
					alignedStringS1 = alignedStringS1 + s1[i - 1];
					alignedStringS2 = alignedStringS2 + s2[j - 1];
					if (s1[i - 1] == s2[j - 1]) {
						numMatches++;
					} else
						numMismatches++;
					i--;
					j--;
				}
			}
	
			output.setNumGaps(numGaps);
			output.setNumMatches(numMatches);
			output.setNumMismatches(numMismatches);
			output.setNumOpeningGaps(numOpeningGaps);
			output.setS1(alignedStringS1);
			output.setS2(alignedStringS2);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Calculates maximum of all the numbers
	private int calculateMax(int ... a) {
		int max=-1000000;
		for(int i=0;i<a.length;i++) {
			if(max<a[i])
				max = a[i];
		}
			return max;
	}

	// Finds whether to give a score or penalty
	private int findScore(char c, char d) {
		if (c == d) {
			return Score.match_score;
		} else {
			return Score.mismatch_penalty;
		}
	}

	// to print the score matrix (m x n)
	void printMatrix() {
		for (int i = 0; i < T.length; i++) {
			for (int j = 0; j < T[0].length; j++) {
				System.out.print(
						calculateMax(T[i][j].getSubstitution(), T[i][j].getDeletion(), T[i][j].getInsertion()) + " ");
			}
			System.out.println();
		}
	}
}
