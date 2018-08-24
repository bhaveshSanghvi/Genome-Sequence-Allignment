package com.genomics;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ReadInput {
	public char[] s1;
	public char[] s2;
	public String descS1;
	public String descS2;

	public void getInput(String filename) {
		FileReader fr;
		String description[];
		String sequence[];

		List<String> desc = new ArrayList<String>();
		List<String> seq = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader("src/com/resources/" + filename));
			StringBuffer buffer = new StringBuffer();
			String line = in.readLine();

			if (line == null)
				throw new IOException(filename + " is an empty file");

			if (line.charAt(0) != '>')
				throw new IOException("First line of " + filename + " should start with '>'");
			else
				desc.add(line);
			for (line = in.readLine().trim(); line != null; line = in.readLine()) {
				if (line.length() > 0 && line.charAt(0) == '>') {
					seq.add(buffer.toString());
					buffer = new StringBuffer();
					desc.add(line);
				} else {
					buffer.append(line.trim());
				}
			}
			if (buffer.length() != 0)
				seq.add(buffer.toString());
		} catch (IOException e) {
			System.out.println("Error when reading " + filename);
			e.printStackTrace();
		}

		description = new String[desc.size()];
		sequence = new String[seq.size()];
		for (int i = 0; i < seq.size(); i++) {
			description[i] = (String) desc.get(i);
			sequence[i] = (String) seq.get(i);
		}
		s1 = sequence[0].toCharArray();
		s2 = sequence[1].toCharArray();
		descS1 = description[0];
		descS2 = description[1]; 
	}
	
	public void getScoreParameters() {
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream("src/com/resources/config.properties");

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			Score.setOpening_gap_penalty(Integer.parseInt(prop.getProperty("opening_gap_penalty")));
			Score.setGap_extension_penalty(Integer.parseInt(prop.getProperty("gap_extension_penalty")));
			Score.setMismatch_penalty(Integer.parseInt(prop.getProperty("mismatch_penalty")));
			Score.setMatch_score(Integer.parseInt(prop.getProperty("match_score")));

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
