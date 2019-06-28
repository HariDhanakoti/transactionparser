package au.mebank.finance.controller

import au.mebank.finance.model.TransactionLimit
import au.mebank.finance.service.CsvReaderService
import au.mebank.finance.service.TransactionService
import spock.lang.Specification

class TransactionControllerSpec extends Specification {

    def csvReaderService = Mock(CsvReaderService )
    def transactionService = Mock(TransactionService )

    def controller = new TransactionController(
        csvReaderService: csvReaderService,
        transactionService: transactionService
    )

    def limit = new TransactionLimit(
            startDate: "20/10/2018 07:10:00",
            endDate: "21/10/2018 07:10:00",
    )

    def "It should get all transactions"() {
        when:
        controller.getAllTransactions()
        then:
        1 * transactionService.getAllTransactions()
    }

    def "It should get all transactions from database for the given limits"() {
        given:

        when:
        controller.getTransactionByLimits(limit)
        then:
        1 * transactionService.getTransactionsWithinLimits(_, *_)
    }

    def "It should get available balance based on the limits for the given account id"() {
        when:
        controller.getAvailableBalanceByLimits("3423423", limit)
        then:
        1 * transactionService.getAccountBalanceWithinLimits(_, _, *_)
    }

    def "It should get current balance based on the limits for the given account id"() {
        when:
        controller.getCurrentBalanceByLimits("3423423", limit)
        then:
        1 * transactionService.getBalanceWithoutReversal(_, _, *_)
    }

}
