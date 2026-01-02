package engine;

import schema.SchemaManager;
import storage.StorageManager;
import transaction.TransactionManager;

public class ExecutionContext {

    public final StorageManager storageManager;
    public final SchemaManager schemaManager;
    public final TransactionManager transactionManager;

    public ExecutionContext(
            StorageManager storageManager,
            SchemaManager schemaManager,
            TransactionManager transactionManager
    ) {
        this.storageManager = storageManager;
        this.schemaManager = schemaManager;
        this.transactionManager = transactionManager;
    }
}
