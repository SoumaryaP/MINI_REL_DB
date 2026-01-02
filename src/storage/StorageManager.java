package storage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class StorageManager {

    private static final String TABLE_DIR = "data/tables/";

    public StorageManager() {
        File dir = new File(TABLE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public void createTable(String tableName) throws IOException {
        File tableFile = new File(TABLE_DIR + tableName + ".tbl");
        if (tableFile.exists()) {
            throw new RuntimeException("Table already exists: " + tableName);
        }
        tableFile.createNewFile();
    }

   
    public void insert(String tableName, Record record) throws IOException {
        TableFile table = new TableFile(tableName);
        table.insert(record);
    }

    public List<Record> readAll(String tableName) throws IOException {
        TableFile table = new TableFile(tableName);
        return table.readAll();
    }

    public void dropTable(String tableName) {
        File tableFile = new File(TABLE_DIR + tableName + ".tbl");
        if (tableFile.exists()) {
            tableFile.delete();
        }
    }

    public TableFile getTableFile(String tableName) {
    try {
        return new TableFile(tableName);
    } catch (IOException e) {
        throw new RuntimeException("Unable to open table file: " + tableName, e);
    }
}
}