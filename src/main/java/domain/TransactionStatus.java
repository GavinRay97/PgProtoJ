package domain;

public enum TransactionStatus {
    IDLE('I'),
    STARTED('T'),
    FAILED('E');

    private final char id;

    TransactionStatus(final char id) {
        this.id = id;
    }

    public char getId() {
        return id;
    }
}
