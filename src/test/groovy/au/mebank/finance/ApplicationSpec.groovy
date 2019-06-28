package au.mebank.finance

import au.mebank.finance.service.CsvReaderService
import spock.lang.Specification

class ApplicationSpec extends Specification {

    def csvReaderService = Mock(CsvReaderService)

    def application = new Application(
        csvReaderService: csvReaderService
    )

    def str = new String[1]

    def "it should start the application by parsing the csv and inserting them to database"() {
        when:
        application.run(str)
        then:
        1 * csvReaderService.insertTransaction(*_)
    }
}
