package com.genomics;

public class Score {
	public static int opening_gap_penalty;
	public static int gap_extension_penalty;
	public static int match_score;
	public static int mismatch_penalty;
	
	public static int getOpening_gap_penalty() {
		return opening_gap_penalty;
	}
	public static void setOpening_gap_penalty(int opening_gap_penalty) {
		Score.opening_gap_penalty = opening_gap_penalty;
	}
	public static int getGap_extension_penalty() {
		return gap_extension_penalty;
	}
	public static void setGap_extension_penalty(int gap_extension_penalty) {
		Score.gap_extension_penalty = gap_extension_penalty;
	}
	public static int getMatch_score() {
		return match_score;
	}
	public static void setMatch_score(int match_score) {
		Score.match_score = match_score;
	}
	public static int getMismatch_penalty() {
		return mismatch_penalty;
	}
	public static void setMismatch_penalty(int mismatch_penalty) {
		Score.mismatch_penalty = mismatch_penalty;
	}	
}
