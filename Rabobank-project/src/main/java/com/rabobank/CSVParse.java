package com.rabobank;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class CSVParse {
	DecimalFormat df = new DecimalFormat("#.00");
	

	public void processFile(String inputFile, String outputFile) {
		Set<String> uniqueTransRefs = new HashSet<String>();
		try (CSVReader reader = new CSVReader(new FileReader(inputFile));
				CSVWriter writer = new CSVWriter(new FileWriter(outputFile))) { //try with resources, flushes and closes the stream object automatically

			writeOutputFile(writer, "Transaction Ref", "Failure Description", "Description"); //Output file header
			String[] record = reader.readNext(); 								// skip the header row
			while ((record = reader.readNext()) != null) {
				String failureReason = null;
				String transactionRef = record[0];
				String trasctiondec = record[2];
				try {
					if (!uniqueTransRefs.contains(transactionRef)) { 			//check the set if TransactionRef is already available
						uniqueTransRefs.add(transactionRef);
						String expecEndBalance = roundFloatNum(new Float(record[3]) + new Float(record[4]));
						String endBalance = roundFloatNum(new Float(record[5]));
						if (!expecEndBalance.equals(endBalance)) {
							failureReason = FailureDescription.INCORRECT_END_BALANCE.toString();
							System.out.println("TrasactionID:" + transactionRef + " Description:" + trasctiondec
									+ " Failure reason:" + failureReason);
						}
					}
					else {
						failureReason = FailureDescription.NON_UNIQUE_TRANSACTION_REF.toString();
						System.out.println("TrasactionID:" + transactionRef + " Description:" + trasctiondec
									+ " Failure reason:" + failureReason);
					}
				}
				catch (NumberFormatException e) {
					failureReason = FailureDescription.BALANCE_NOT_A_NUMBER.toString();
				}
				if (failureReason != null) {
					writeOutputFile(writer, transactionRef, failureReason, trasctiondec);
				}
			}
		} catch(FileNotFoundException exception)
	    {
	        System.out.println("The file is not present.");
	    }
		
		catch (IOException e) {
			System.out.println("Failure in opening the read/write file");
		}
	}

	private String roundFloatNum(Float balance) {
		return df.format(balance);
	}

	private void writeOutputFile(CSVWriter writer, String transRef, String failureReson, String transdec) {
		writer.writeNext(new String[] { transRef, failureReson, transdec });
	}
}
