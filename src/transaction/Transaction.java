package transaction;

public class Transaction {

    private final String id;
    private boolean active = true;

    public Transaction(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public void commit() {
        active = false;
    }

    public void rollback() {
        active = false;
    }
}
