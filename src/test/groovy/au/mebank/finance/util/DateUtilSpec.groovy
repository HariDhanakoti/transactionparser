package au.mebank.finance.util

import au.mebank.finance.exception.TransactionException
import spock.lang.Specification

import java.text.ParseException

class DateUtilSpec extends Specification {

    def dateUtil = new DateUtil()

    def "It should parse the given string and return date object"() {
        when:
        dateUtil.getDate("21/10/2018 09:30:00")
        then:
        noExceptionThrown()
    }

    def "It should throw exception when invalid date format supplied"() {
        when:
        dateUtil.getDate("21/10/2018T09:30:00")
        then:
        thrown TransactionException
    }

    def "It should throw exception when null passed as input"() {
        when:
        dateUtil.getDate(null);
        then:
        thrown TransactionException
    }

}
