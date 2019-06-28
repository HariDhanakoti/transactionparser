package au.mebank.finance.service

import au.mebank.finance.model.Transaction
import au.mebank.finance.repository.TransactionRepository
import au.mebank.finance.service.impl.TransactionServiceImpl
import au.mebank.finance.util.DateUtil
import spock.lang.Specification

class TransactionServiceImplSpec extends Specification {

    def repository = Mock(TransactionRepository)
    def dateUtil = Mock(DateUtil)

    def transactionService = new TransactionServiceImpl(
        repository: repository,
        dateUtil: dateUtil
    )

    def transcation1 = new Transaction (
            transactionId: "1234567",
            fromAccountId: "9876543",
            toAccountId: "34567",
            createdAt: new Date("20/10/2018 20:10:54"),
            amount: 10.50,
            transactionType: "PAYMENT"
    )

    def transcation2 = new Transaction (
            transactionId: "8976543",
            fromAccountId: "9876543",
            toAccountId: "9876541",
            createdAt: new Date("20/10/2018 10:20:50"),
            amount: 5.50,
            transactionType: "PAYMENT"
    )

    def transcation3 = new Transaction (
            transactionId: "8976543",
            fromAccountId: "9876543",
            toAccountId: "9876541",
            createdAt: new Date("20/10/2018 07:10:00"),
            amount: 15.50,
            transactionType: "PAYMENT"
    )

    def transcation4 = new Transaction (
            transactionId: "8976543",
            fromAccountId: "9876543",
            toAccountId: "9876541",
            createdAt: new Date("20/10/2018 07:10:00"),
            amount: 100.50,
            transactionType: "REVERSAL"
    )

    def transcation5 = new Transaction (
            transactionId: "8976543",
            fromAccountId: "99887662",
            toAccountId: "9876541",
            createdAt: new Date("20/10/2018 07:10:00"),
            amount: 100.50,
            transactionType: "PAYMENT"
    )

    def transactionList = new ArrayList()

    def "it should return the balance for the given limits"() {
        given:
        transactionList.add(transcation1)
        transactionList.add(transcation2)
        transactionList.add(transcation3)
        when:
        def balanceViewer = transactionService.getTotalBalance("8976543", "9876543", "9876543", transactionList, true)
        then:
        balanceViewer != null
        balanceViewer.accountId == "8976543"
        balanceViewer.availableBalance == 31.50
    }

    def "it should return the balance for the account given limits"() {
        given:
        transactionList.add(transcation1)
        transactionList.add(transcation2)
        transactionList.add(transcation3)
        transactionList.add(transcation4)
        transactionList.add(transcation5)
        when:
        def transactions = transactionService.getTransactionsWithinLimits("20/10/2018 07:10:00", "21/10/2018 17:10:00")
        then:
        1 * repository.findTransactionsWithinStartAndEndTime(_, *_) >> transactionList
        transactions.size() == 5
    }

    def "it should return the balance for the account given limits for the given account id"() {
        given:
        transactionList.add(transcation1)
        transactionList.add(transcation2)
        transactionList.add(transcation3)
        transactionList.add(transcation4)
        when:
        def balanceViewer = transactionService.getAccountBalanceWithinLimits("8976543", "20/10/2018 07:10:00", "21/10/2018 17:10:00")
        then:
        1 * repository.findTransactionForTheAccountWithinStartAndEndTime("8976543", _, *_) >> transactionList
        balanceViewer != null
        balanceViewer.accountId == "8976543"
        balanceViewer.availableBalance == 132.00
    }

    def "it should return the balance for the given limits without reversed transaction"() {
        given:
        transactionList.add(transcation1)
        transactionList.add(transcation2)
        transactionList.add(transcation3)
        transactionList.add(transcation4)
        when:
        def balanceViewer = transactionService.getBalanceWithoutReversal("8976543", "20/10/2018 07:10:00", "21/10/2018 17:10:00")
        then:
        1 * repository.findTransactionsForTheAccount("8976543") >> transactionList
        balanceViewer != null
        balanceViewer.accountId == "8976543"
        balanceViewer.availableBalance == 31.50
    }

    def "it should return the balance for the given limits skipping the reversed transaction"() {
        given:
        transactionList.add(transcation1)
        transactionList.add(transcation2)
        transactionList.add(transcation3)
        transactionList.add(transcation4)
        when:
        def balanceViewer = transactionService.getTotalBalance("8976543", "9876543", "9876543", transactionList, false)
        then:
        balanceViewer != null
        balanceViewer.accountId == "8976543"
        balanceViewer.availableBalance == 31.50
    }


    def "it should return all transaction from database"() {
        given:
        transactionList.add(transcation1)
        transactionList.add(transcation2)
        when:
        def transactionList = transactionService.getAllTransactions()
        then:
        1 * repository.findAll() >> transactionList
        transactionList.size() == 2
    }

    def "it should insert entry into database for the given input"() {
        when:
        transactionService.insertTransaction(transcation1)
        then:
        1 * repository.save(*_)
    }

    def "it should retrieve records from database with the date limits"() {
        when:
        transactionService.insertTransaction(transcation1)
        then:
        1 * repository.save(*_)
    }
}
