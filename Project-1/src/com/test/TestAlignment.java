package com.test;

import java.text.DecimalFormat;

import com.genomics.GlobalAlignment;
import com.genomics.IAlignment;
import com.genomics.LocalAlignment;
import com.genomics.Output;
import com.genomics.ReadInput;
import com.genomics.Score;

public class TestAlignment {
	public static void main(String args[]) {
		String filename = args[0];
		String choice = args[1];
		ReadInput r = new ReadInput();
		r.getInput(filename);
		r.getScoreParameters();
		
		switch(choice) {
			case "0":	// perform global alignment
				IAlignment ga = new GlobalAlignment(r.s1,r.s2);
				Output output1 = ga.generateScoreMatrix();
				
				print(r.descS1,r.descS2,output1);
				
				break;
				
			case "1":	// perform local alignment
				IAlignment la = new LocalAlignment(r.s1,r.s2);
				Output output = la.generateScoreMatrix();
				
				print(r.descS1,r.descS2,output);

				break;
				
			default: System.out.println("Invalid Input! Kindly try again with valid input in the below format\n java <classname> <filename> <0:global, 1:local>");
		}
	}
	
	public static void print(String descS1, String descS2,Output output1) {
		
		int x=0;
		int y=0;
		
		char s1[] = output1.getS1().toCharArray();
		char s2[] = output1.getS2().toCharArray();
		System.out.println();
		System.out.println("Scores: match = "+Score.match_score+", mismatch = "+Score.mismatch_penalty+", h = "+Score.opening_gap_penalty+", g = "+Score.gap_extension_penalty);
		System.out.println();
		System.out.println("Sequence 1 =\""+descS1+", length = "+s1.length+" Characters");
		System.out.println("Sequence 1 =\""+descS2+", length = "+s2.length+" Characters");
		System.out.println();
		
		for (int i=0; x<s1.length;i++) {
			System.out.print("s1\t"+x+"\t");
			for(int j=0;j<60 && x<s1.length;j++) {
				System.out.print(s1[x]);
				x++;
			}
			System.out.println();
			x=y;
			
			System.out.print("\t\t");
			
			for (int j = 0; j < 60 && x<s1.length; j++) {
				if(s1[x]=='-' && s2[x]=='-') {
					System.out.print(" ");
				}
				else if(s1[x]==s2[x]) {
					System.out.print("|");
				} else {
					System.out.print(" ");
				}
				x++;
			}
			System.out.println();
			x=y;
			
			System.out.print("s2\t"+x+"\t");
			for (int j = 0; j < 60 && x<s1.length; j++) {
				System.out.print(s2[x]);
				x++;
			}
			System.out.println();
			System.out.println();
			y=x;
		}
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(3);
		System.out.println("Report:\n");
		System.out.println("Global Optimal Score = "+output1.getOptimalScore()+"\n");
		System.out.println("Number of: Matches = "+output1.getNumMatches()+" , Mismatches = "+output1.getNumMismatches()+ " , Gaps = "+output1.getNumGaps()+ " , Opening Gaps = "+output1.getNumOpeningGaps());
		System.out.println("Identities = "+output1.getNumGaps()+"/"+s1.length+" ("+(df.format((float)output1.getNumGaps()/(float)s1.length*100))+"%), Gaps = "+output1.getNumGaps()+"/"+s1.length+" ("+df.format((float)output1.getNumGaps()/(float)s1.length*100)+"%)");
	}
}
