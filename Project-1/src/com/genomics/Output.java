package com.genomics;

public class Output {
	private int optimalScore;
	private int numMismatches;
	private int numMatches;
	private int numGaps;
	private int numOpeningGaps;
	private String s1;
	private String s2;

	public int getOptimalScore() {
		return optimalScore;
	}

	public void setOptimalScore(int globalOptimalScore) {
		this.optimalScore = globalOptimalScore;
	}

	public int getNumMismatches() {
		return numMismatches;
	}

	public void setNumMismatches(int numMismatches) {
		this.numMismatches = numMismatches;
	}

	public int getNumMatches() {
		return numMatches;
	}

	public void setNumMatches(int numMatches) {
		this.numMatches = numMatches;
	}

	public int getNumGaps() {
		return numGaps;
	}

	public void setNumGaps(int numGaps) {
		this.numGaps = numGaps;
	}

	public int getNumOpeningGaps() {
		return numOpeningGaps;
	}

	public void setNumOpeningGaps(int numOpeningGaps) {
		this.numOpeningGaps = numOpeningGaps;
	}

	public String getS1() {
		return s1;
	}

	public void setS1(String s1) {
		this.s1 = s1;
	}

	public String getS2() {
		return s2;
	}

	public void setS2(String s2) {
		this.s2 = s2;
	}

	@Override
	public String toString() {
		return "Output [optimalScore=" + optimalScore + ", numMismatches=" + numMismatches + ", numMatches="
				+ numMatches + ", numGaps=" + numGaps + ", numOpeningGaps=" + numOpeningGaps + ", s1=" + s1 + ", s2="
				+ s2 + "]";
	}
}
