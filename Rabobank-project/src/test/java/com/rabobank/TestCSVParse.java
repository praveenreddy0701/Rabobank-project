package com.rabobank;

import org.junit.Test;

public class TestCSVParse {
	
	String inputFile = "resources/records.csv";
	String outputFile = "resources/csvoutput.csv";
		
	@Test
	public void testProcessFile() {
		CSVParse csvparse = new CSVParse();
		csvparse.processFile(inputFile, outputFile);
	}
}
