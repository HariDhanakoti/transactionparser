package au.mebank.finance.service

import au.mebank.finance.model.Transaction
import au.mebank.finance.service.impl.CsvReaderServiceImpl
import org.supercsv.cellprocessor.ift.CellProcessor
import org.supercsv.io.CsvBeanReader
import org.supercsv.prefs.CsvPreference
import spock.lang.Specification

class CsvReaderServiceImplSpec extends Specification {

    def headers = ["TransactionId", "FromAccountId", "ToAccountId", "CreatedAt", "Amount", "TransactionType", "RelatedTransaction"]
    def transactionService = Mock(TransactionService)
    def cellProcessors = Mock(CellProcessor)
    def fileReader = Mock(FileReader)
    def beanReader

    def transaction = new Transaction (
        transactionId: "8976543",
        fromAccountId: "99887662",
        toAccountId: "9876541",
        createdAt: new Date("20/10/2018 07:10:00"),
        amount: 100.50,
        transactionType: "PAYMENT"
    )

    def csvReaderService = new CsvReaderServiceImpl(
        transactionService: transactionService
    )

    def setup() {
        beanReader = new CsvBeanReader(fileReader, CsvPreference.STANDARD_PREFERENCE)
        beanReader.getHeader(true) >> headers
    }

    def "it should insert each row of csv as a new record to database"() {
        given:
        def classLoader = getClass().getClassLoader();
        def file = new File(classLoader.getResource("sample.csv").getFile());
        beanReader.read(Transaction.class, headers, cellProcessors) >> transaction
        when:
        csvReaderService.insertTransaction(file.getAbsolutePath())
        then:
        2 * transactionService.insertTransaction(*_)
    }
}
