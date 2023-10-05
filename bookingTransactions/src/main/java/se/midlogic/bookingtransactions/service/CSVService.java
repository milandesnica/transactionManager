package se.midlogic.bookingtransactions.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.stereotype.Service;
import se.midlogic.bookingtransactions.controller.Transaction;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CSVService {

    public List<Transaction> parseCSVString(String csvString) {
        List<Transaction> transactions = new ArrayList<>();

        try (CSVParser csvParser = CSVParser.parse(new StringReader(csvString), CSVFormat.DEFAULT)) {
            return StreamSupport.stream(csvParser.spliterator(), false)
                    .filter(csvRecord -> csvRecord.size() == 5)
                    .map(csvRecord -> new Transaction(
                            csvRecord.get(0),
                            csvRecord.get(1),
                            csvRecord.get(2),
                            Double.parseDouble(csvRecord.get(3)),
                            csvRecord.get(4)
                    ))
                    .collect(Collectors.toList());
        }
        catch (IOException io) {
            System.err.println("Error parsing CSV string: " + io.getMessage());
        }

        return transactions;
    }
}