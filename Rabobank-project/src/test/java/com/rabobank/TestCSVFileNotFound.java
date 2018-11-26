package com.rabobank;

import org.junit.Test;

public class TestCSVFileNotFound {
	
	String inputFile = "resources/record.csv";
	String outputFile = "resources/csvoutput.csv";	
	
	
	@Test
	public void testProcessFile() {
		
		CSVParse csvparse = new CSVParse();
		csvparse.processFile(inputFile, outputFile); 
		
	}

}
