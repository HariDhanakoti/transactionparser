package au.mebank.finance.model;

public enum TransactionType {
    PAYMENT,
    REVERSAL;

    private String type;

    public String getType() {
        return this.type;
    }
}
