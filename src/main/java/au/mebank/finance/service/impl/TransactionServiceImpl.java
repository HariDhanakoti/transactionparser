package au.mebank.finance.service.impl;

import au.mebank.finance.model.BalanceViewer;
import au.mebank.finance.model.Transaction;
import au.mebank.finance.repository.TransactionRepository;
import au.mebank.finance.service.TransactionService;
import au.mebank.finance.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private DateUtil dateUtil;

    @Override
    public void insertTransaction(Transaction transaction) {
        log.info("Inserting transaction {}", transaction.toString());
        repository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionsWithinLimits(String startLimit, String endLimit) {
        log.info("Listing transaction between {} and {}", dateUtil.getDate(startLimit), dateUtil.getDate(endLimit));
        return repository.findTransactionsWithinStartAndEndTime(dateUtil.getDate(startLimit), dateUtil.getDate(endLimit));
    }

    @Override
    public BalanceViewer getAccountBalanceWithinLimits(String accountId, String startLimit, String endLimit) {
        log.info("Listing transaction between {} and {}", dateUtil.getDate(startLimit), dateUtil.getDate(endLimit));
        List<Transaction> transactionList = repository.findTransactionForTheAccountWithinStartAndEndTime(accountId, dateUtil.getDate(startLimit), dateUtil.getDate(endLimit));
        return getTotalBalance(accountId, startLimit, endLimit, transactionList, true);
    }

    @Override
    public BalanceViewer getBalanceWithoutReversal(String accountId, String startLimit, String endLimit) {
        List<Transaction> transactionList = repository.findTransactionsForTheAccount(accountId);
        return getTotalBalance(accountId, startLimit, endLimit, transactionList, false);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactionList = (List<Transaction>) repository.findAll();
        for (Transaction transaction : transactionList) {
            log.info("Transaction {}", transaction.toString());
        }
        return transactionList;
    }

    private BalanceViewer getTotalBalance(String accountId, String startTime, String endTime, List<Transaction> transactionList, boolean considerReversal) {
        BalanceViewer balanceViewer = new BalanceViewer();
        BigDecimal availableBalance = new BigDecimal(0);
        for (Transaction transaction : transactionList) {
            if (considerReversal) {
                availableBalance = availableBalance.add(transaction.getAmount());
            }
            else if (!transaction.getTransactionType().equalsIgnoreCase("REVERSAL")) {
                log.info("transaction amount {}", transaction.getAmount());
                availableBalance = availableBalance.add(transaction.getAmount());
            }
        }
        log.info("Available balance {}", availableBalance);
        balanceViewer.setAccountId(accountId);
        balanceViewer.setFromDate(startTime);
        balanceViewer.setToDate(endTime);
        balanceViewer.setAvailableBalance(availableBalance);
        return balanceViewer;
    }
}
