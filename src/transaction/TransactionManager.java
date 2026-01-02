package transaction;

import storage.Record;

import java.io.*;
import java.util.*;

public class TransactionManager {

    private final LogManager logManager = new LogManager();
    private final Map<String, List<String>> undoBuffer = new HashMap<>();

    public Transaction begin(String txId) throws IOException {
        logManager.log("BEGIN " + txId);
        undoBuffer.put(txId, new ArrayList<>());
        return new Transaction(txId);
    }

    public void logInsert(String txId, String table, Record record)
            throws IOException {

        String entry =
                "INSERT " + txId + " " + table + " " + record.serialize();

        logManager.log(entry);
        undoBuffer.get(txId).add(entry);
    }

    public void commit(Transaction tx) throws IOException {
        logManager.log("COMMIT " + tx.getId());
        tx.commit();
        undoBuffer.remove(tx.getId());
    }

    public void rollback(Transaction tx) throws IOException {
        logManager.log("ROLLBACK " + tx.getId());
        undoBuffer.remove(tx.getId());
        tx.rollback();
    }


    public void recover() throws IOException {

        File logFile = new File("data/logs/db.log");
        if (!logFile.exists()) return;

        Set<String> committed = new HashSet<>();
        Set<String> active = new HashSet<>();

        try (BufferedReader br =
                     new BufferedReader(new FileReader(logFile))) {

            String line;
            while ((line = br.readLine()) != null) {

                String[] p = line.split(" ");

                if (p[0].equals("BEGIN")) {
                    active.add(p[1]);
                }
                if (p[0].equals("COMMIT")) {
                    committed.add(p[1]);
                }
            }
        }
        for (String tx : active) {
            if (!committed.contains(tx)) {
                System.out.println(
                        "Rolled back incomplete transaction " + tx);
            }
        }
    }
}
