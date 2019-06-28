package au.mebank.finance.util;

import au.mebank.finance.exception.TransactionException;
import au.mebank.finance.service.impl.TransactionServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Component
public class DateUtil {

    private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.ENGLISH);

    public Date getDate(String dateString) {
        Date date = null;
        try {
            date = formatter.parse(dateString);
        } catch (Exception e) {
            log.error("Unable to parse date {}", dateString);
            throw new TransactionException("Unable to parse date " + dateString);
        }
        return date;
    }
}
