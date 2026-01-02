package schema;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SchemaManager {

    private static final String SCHEMA_DIR = "data/schemas/";

    public SchemaManager() {
        File dir = new File(SCHEMA_DIR);
        if (!dir.exists()) dir.mkdirs();
    }

    public void createTable(TableSchema schema) throws IOException {
        File f = new File(SCHEMA_DIR + schema.getTableName() + ".schema");
        if (f.exists()) {
            throw new RuntimeException("Table already exists");
        }

        try (FileWriter fw = new FileWriter(f)) {
            for (Column c : schema.getColumns()) {
                fw.write(c.serialize() + "\n");
            }
        }
    }

    public TableSchema loadSchema(String tableName) throws IOException {
        File f = new File(SCHEMA_DIR + tableName + ".schema");
        if (!f.exists()) {
            throw new RuntimeException("Schema not found for table " + tableName);
        }

        List<Column> cols = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                cols.add(Column.deserialize(line));
            }
        }
        return new TableSchema(tableName, cols);
    }

    public void dropTable(String tableName) {
        File f = new File(SCHEMA_DIR + tableName + ".schema");
        if (f.exists()) f.delete();
    }
}
