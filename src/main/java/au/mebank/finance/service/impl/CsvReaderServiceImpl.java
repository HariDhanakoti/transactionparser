package au.mebank.finance.service.impl;

import au.mebank.finance.model.Transaction;
import au.mebank.finance.service.CsvReaderService;
import au.mebank.finance.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.supercsv.cellprocessor.*;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@Component
public class CsvReaderServiceImpl implements CsvReaderService {

    @Autowired
    private TransactionService transactionService;

    public void insertTransaction(String filePath) {
        try(ICsvBeanReader beanReader = new CsvBeanReader(new FileReader(filePath), CsvPreference.STANDARD_PREFERENCE)) {
            final String[] headers = beanReader.getHeader(true);
            final CellProcessor[] processors = getProcessors();

            Transaction transaction;
            while ((transaction = beanReader.read(Transaction.class, headers, processors)) != null) {
                System.out.println("Transaction " + transaction.toString());
                transactionService.insertTransaction(transaction);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up the processors used for the examples.
     */
    private static CellProcessor[] getProcessors() {
        final CellProcessor[] processors = new CellProcessor[] {
                new NotNull(), // TransactionId
                new NotNull(), // FromAccountId
                new NotNull(), // ToAccountId
                new ParseDate("dd/MM/yyyy HH:mm:ss"), // CreatedAt
                new ParseBigDecimal(), // Amount
                new NotNull(), // TransactionType
                new Optional() // RelatedTransaction
        };
        return processors;
    }
}
