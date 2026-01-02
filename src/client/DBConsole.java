package client;

import engine.ExecutionContext;
import engine.QueryExecutor;
import parser.*;
import parser.CreateTableQuery;
import schema.TableSchema;
import schema.SchemaManager;
import storage.Record;
import storage.StorageManager;
import transaction.Transaction;
import transaction.TransactionManager;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class DBConsole {

    public static void main(String[] args) throws IOException {

        StorageManager storageManager = new StorageManager();
        SchemaManager schemaManager = new SchemaManager();
        TransactionManager transactionManager = new TransactionManager();

        ExecutionContext context =
                new ExecutionContext(
                        storageManager,
                        schemaManager,
                        transactionManager
                );

        QueryExecutor executor =
                new QueryExecutor(context);

        SQLParser parser = new SQLParser();

        // Crash recovery
        transactionManager.recover();

        Scanner sc = new Scanner(System.in);
        Transaction currentTx = null;
        int txCounter = 1;

        System.out.println("MiniRelDB started. Type EXIT to quit.");

        while (true) {
            System.out.print("MiniRelDB > ");
            String sql = sc.nextLine().trim();

            if (sql.equalsIgnoreCase("EXIT")) {
                break;
            }
            try {

                if (sql.equalsIgnoreCase("BEGIN")) {
                    currentTx =
                            transactionManager.begin("T" + txCounter++);
                    System.out.println("Transaction started");
                    continue;
                }

                if (sql.equalsIgnoreCase("COMMIT")) {
                    transactionManager.commit(currentTx);
                    currentTx = null;
                    System.out.println("Transaction committed");
                    continue;
                }

                if (sql.equalsIgnoreCase("ROLLBACK")) {
                    transactionManager.rollback(currentTx);
                    currentTx = null;
                    System.out.println("Transaction rolled back");
                    continue;
                }


                Query q = parser.parse(sql);

                if (q instanceof CreateTableQuery ctq) {
                    TableSchema schema = new TableSchema(ctq.tableName, ctq.columns);
                    schemaManager.createTable(schema);
                    storageManager.createTable(ctq.tableName);
                    System.out.println("Table created.");
                    continue;
                }

                if (q instanceof SelectQuery sq) {
                    List<Record> result;
                    if (sq.whereColumn == null) {
                        result = executor.selectAll(sq.tableName);
                    } else {
                        result = executor.selectWhere(
                                sq.tableName,
                                sq.whereColumn,
                                sq.whereValue
                        );
                    }
                    for (Record r : result) {
                        System.out.println(r.serialize());
                    }
                } else if (q instanceof InsertQuery iq) {
                    Record record = new Record(
                            iq.values.stream()
                                    .map(String::trim)
                                    .map(v -> v.replace("'", ""))
                                    .toArray(String[]::new)
                    );
                    if (currentTx != null) {
                        executor.insert(
                                iq.tableName,
                                record,
                                currentTx.getId()
                        );
                    } else {
                        executor.insert(
                                iq.tableName,
                                record
                        );
                    }
                    System.out.println("1 row inserted.");
                }

            } catch (Exception e) {
                System.err.println("ERROR: " + e.getMessage());
            }
        }

        sc.close();
    }
}
