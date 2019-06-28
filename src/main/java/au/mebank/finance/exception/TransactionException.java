package au.mebank.finance.exception;

public class TransactionException extends RuntimeException {

    public TransactionException(String errorMessage){
        super(errorMessage);
    }
}
