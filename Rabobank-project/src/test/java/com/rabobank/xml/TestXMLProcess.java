package com.rabobank.xml;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.junit.Test;

public class TestXMLProcess {
	
	String inputFilePath = "resources/records.xml";
	String outputFilePath = "resources/xmloutput.csv";

	@Test
	public void testProcessFile() throws JAXBException, IOException {
		XMLProcess xmlprocess = new XMLProcess();
		xmlprocess.unMarsh(inputFilePath, outputFilePath);
	}
}
