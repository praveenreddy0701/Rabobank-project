package com.rabobank.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.opencsv.CSVWriter;
import com.rabobank.FailureDescription;

public class XMLProcess {

	DecimalFormat df = new DecimalFormat("#.00");

	public void unMarsh(String inputFilePath, String outputFile) throws JAXBException, IOException {
		try {
			Set<String> uniqueTransRefs = new HashSet<String>();

			CSVWriter writer = new CSVWriter(new FileWriter(outputFile));
			writeOutputFile(writer, "Transaction Ref", "Failure Description", "Description");

			JAXBContext jaxbContext = JAXBContext.newInstance(Records.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Records rec = (Records) jaxbUnmarshaller.unmarshal(new File(inputFilePath));

			List<Record> recList = rec.getRecord();
			for (Record record : recList) {
				String failureReason = null;
				String transactionRef = record.getReference().toString();
				String trasctiondec = record.getDescription();
				String expecEndBalance = roundFloatNum(record.getStartBalance().add(record.getMutation()));
				String endBalance = roundFloatNum(record.getEndBalance());

				if (!uniqueTransRefs.contains(transactionRef)) {
					uniqueTransRefs.add(transactionRef);
					if (!expecEndBalance.equals(endBalance)) {
						failureReason = FailureDescription.INCORRECT_END_BALANCE.toString();
						System.out.println("TrasactionID:" + transactionRef + " Description:" + trasctiondec
								+ " Failure reason:" + failureReason);
						writeOutputFile(writer, transactionRef, failureReason, trasctiondec);
					} else {
						failureReason = FailureDescription.NON_UNIQUE_TRANSACTION_REF.toString();
						System.out.println("TrasactionID:" + transactionRef + " Description:" + trasctiondec
									+ " Failure reason:" + failureReason);
						writeOutputFile(writer, transactionRef, failureReason, trasctiondec);
					}
				}
			}
		} catch (FileNotFoundException exception) {
			System.out.println("The file is not present.");
		} catch (IOException e) {
			System.out.println("Failure in opening the read/write file");
		}
	}

	private String roundFloatNum(BigDecimal bigDecimal) {
		return df.format(bigDecimal);
	}

	private void writeOutputFile(CSVWriter writer, String transactionRef, String failureReson, String transdec) {
		writer.writeNext(new String[] { transactionRef, failureReson, transdec });
	}

}
