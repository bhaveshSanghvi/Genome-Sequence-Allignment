package com.genomics;

public class GlobalAlignment implements IAlignment {

	public Cell[][] T;
	public char[] s1;
	public char[] s2;
	Output output = new Output();

	public GlobalAlignment(char[] s1, char[] s2) {
		this.s1 = s1;
		this.s2 = s2;
	}

	@Override
	public Output generateScoreMatrix() {
		T = new Cell[s1.length + 1][s2.length + 1];

		T[0][0] = new Cell();
		T[0][0].setInsertion(Constants.NEGATIVE_INFINITY);
		T[0][0].setDeletion(Constants.NEGATIVE_INFINITY);
		T[0][0].setSubstitution(0);

		for (int i = 1; i < T.length; i++) {
			T[i][0] = new Cell();
			T[i][0].setSubstitution(Constants.NEGATIVE_INFINITY);
			T[i][0].setDeletion(Score.opening_gap_penalty + i * Score.gap_extension_penalty);
			T[i][0].setInsertion(Constants.NEGATIVE_INFINITY);
		}

		for (int i = 1; i < T[0].length; i++) {
			T[0][i] = new Cell();
			T[0][i].setSubstitution(Constants.NEGATIVE_INFINITY);
			T[0][i].setDeletion(Constants.NEGATIVE_INFINITY);
			T[0][i].setInsertion(Score.opening_gap_penalty + i * Score.gap_extension_penalty);
		}

		for (int i = 1; i < T.length; i++) {
			for (int j = 1; j < T[0].length; j++) {

				T[i][j] = new Cell();

				T[i][j].setInsertion(calculateMax(
						T[i][j - 1].getSubstitution() + Score.opening_gap_penalty + Score.gap_extension_penalty,
						T[i][j - 1].getInsertion() + Score.gap_extension_penalty,
						T[i][j - 1].getDeletion() + Score.opening_gap_penalty + Score.gap_extension_penalty));

				T[i][j].setDeletion(calculateMax(
						T[i - 1][j].getSubstitution() + Score.opening_gap_penalty + Score.gap_extension_penalty,
						T[i - 1][j].getDeletion() + Score.gap_extension_penalty,
						T[i - 1][j].getInsertion() + Score.gap_extension_penalty + Score.opening_gap_penalty));

				T[i][j].setSubstitution(calculateMax(T[i - 1][j - 1].getSubstitution(), T[i - 1][j - 1].getInsertion(),
						T[i - 1][j - 1].getDeletion()) + findScore(s1[i - 1], s2[j - 1]));

			}
		}

		//printMatrix();
		
		output.setOptimalScore(calculateMax(T[s1.length][s2.length].getSubstitution(),
				T[s1.length][s2.length].getDeletion(), T[s1.length][s2.length].getInsertion()));
		generateOptimalPath();
		return output;
		
	}

	public void generateOptimalPath() {

		int i = T.length - 1;
		int j = T[0].length - 1;
		int max = 0;
		int numOpeningGaps = 0;
		int numGaps = 0;	
		int numMatches = 0;
		int numMismatches = 0;
		String alignedStringS1 = "";
		String alignedStringS2 = "";

		while (i > 0 || j > 0) {
			max = calculateMax(T[i][j].getInsertion(), T[i][j].getDeletion(), T[i][j].getSubstitution());
			//System.out.println(max);
			if (max == calculateMax(T[i][j - 1].getInsertion() + Score.gap_extension_penalty,
					T[i][j - 1].getDeletion() + Score.gap_extension_penalty + Score.opening_gap_penalty,
					T[i][j - 1].getSubstitution() + Score.opening_gap_penalty + Score.gap_extension_penalty)) {

				if (calculateMax(T[i][j - 1].getInsertion() + Score.gap_extension_penalty,
						T[i][j - 1].getDeletion() + Score.gap_extension_penalty + Score.opening_gap_penalty,
						T[i][j - 1].getSubstitution() + Score.opening_gap_penalty
								+ Score.gap_extension_penalty) == T[i][j - 1].getDeletion()
										+ Score.gap_extension_penalty + Score.opening_gap_penalty
						|| calculateMax(T[i][j - 1].getInsertion() + Score.gap_extension_penalty,
								T[i][j - 1].getDeletion() + Score.gap_extension_penalty + Score.opening_gap_penalty,
								T[i][j - 1].getSubstitution() + Score.opening_gap_penalty
										+ Score.gap_extension_penalty) == T[i][j - 1].getSubstitution()
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
								+ Score.opening_gap_penalty) == T[i - 1][j].getInsertion() + Score.gap_extension_penalty
										+ Score.opening_gap_penalty
						|| calculateMax(
								T[i - 1][j].getInsertion() + Score.gap_extension_penalty + Score.opening_gap_penalty,
								T[i - 1][j].getDeletion() + Score.gap_extension_penalty,
								T[i - 1][j].getSubstitution() + Score.gap_extension_penalty
										+ Score.opening_gap_penalty) == T[i - 1][j].getSubstitution()
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
		output.setS1(new StringBuilder(alignedStringS1).reverse().toString());
		output.setS2(new StringBuilder(alignedStringS2).reverse().toString());
	}

	private int calculateMax(int a, int b, int c) {
		if (a >= b) {
			if (a >= c) {
				return a;
			} else {
				return c;
			}
		} else if (b >= c) {
			return b;
		} else {
			return c;
		}
	}

	private int findScore(char c, char d) {
		if (c == d) {
			return Score.match_score;
		} else {
			return Score.mismatch_penalty;
		}
	}

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
