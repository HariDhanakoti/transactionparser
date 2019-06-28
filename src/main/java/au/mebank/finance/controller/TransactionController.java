package au.mebank.finance.controller;

import au.mebank.finance.model.BalanceViewer;
import au.mebank.finance.model.Transaction;
import au.mebank.finance.model.TransactionLimit;
import au.mebank.finance.service.CsvReaderService;
import au.mebank.finance.service.TransactionService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private CsvReaderService csvReaderService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping(value = "/api/{accountId}/getAvailableBalance", produces = "application/json")
    public @ResponseBody BalanceViewer getAvailableBalanceByLimits(
            @PathVariable("accountId") String accountId,
            @RequestBody TransactionLimit transactionLimit) {
        log.info("Getting available balance for {}, Start time {}, end time {}", accountId, transactionLimit.getStartDate(), transactionLimit.getEndDate());
        return transactionService.getAccountBalanceWithinLimits(accountId, transactionLimit.getStartDate(), transactionLimit.getEndDate());
    }

    @PostMapping(value = "/api/{accountId}/getCurrentBalance", produces = "application/json")
    public @ResponseBody BalanceViewer getCurrentBalanceByLimits(
            @PathVariable("accountId") String accountId,
            @RequestBody TransactionLimit transactionLimit) {
        log.info("Getting current balance for {}, Start time {}, end time {}", accountId, transactionLimit.getStartDate(), transactionLimit.getEndDate());
        return transactionService.getBalanceWithoutReversal(accountId, transactionLimit.getStartDate(), transactionLimit.getEndDate());
    }

    @GetMapping(value = "/api/getAllTransactions", produces = "application/json")
    public @ResponseBody List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @PostMapping(value = "/api/getTimeBoundTransactions", produces = "application/json")
    public @ResponseBody List<Transaction> getTransactionByLimits(@RequestBody TransactionLimit transactionLimit) {
        log.info("Start time {}, end time {}", transactionLimit.getStartDate(), transactionLimit.getEndDate());
        return transactionService.getTransactionsWithinLimits(transactionLimit.getStartDate(), transactionLimit.getEndDate());
    }


}
