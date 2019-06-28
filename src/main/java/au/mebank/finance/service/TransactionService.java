package au.mebank.finance.service;

import au.mebank.finance.model.BalanceViewer;
import au.mebank.finance.model.Transaction;

import java.util.List;

public interface TransactionService {

    void insertTransaction(Transaction transaction);

    List<Transaction> getTransactionsWithinLimits(String startLimit, String endLimit);

    BalanceViewer getAccountBalanceWithinLimits(String accountId, String startLimit, String endLimit);

    List<Transaction> getAllTransactions();

    BalanceViewer getBalanceWithoutReversal(String accountId, String startLimit, String endLimit);
}
