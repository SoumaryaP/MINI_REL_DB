package engine;

import index.HashIndex;
import schema.Column;
import schema.TableSchema;
import storage.Record;
import storage.TableFile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class QueryExecutor {

    private final ExecutionContext ctx;

    public QueryExecutor(ExecutionContext ctx) {
        this.ctx = ctx;
    }

    /* =========================
       INSERT (NON-TRANSACTIONAL)
       ========================= */

    public void insert(String tableName, Record record) throws IOException {

        TableSchema schema = ctx.schemaManager.loadSchema(tableName);
        List<Record> existing = ctx.storageManager.readAll(tableName);

        ConstraintValidator.validateInsert(
                schema,
                record,
                existing
        );

        int pkIndex = -1;
        String pkColumn = null;

        for (Column col : schema.getColumns()) {
            if (col.isPrimaryKey()) {
                pkIndex = schema.getColumnIndex(col.getName());
                pkColumn = col.getName();
                break;
            }
        }

        TableFile tableFile =
                ctx.storageManager.getTableFile(tableName);

        long offset =
                tableFile.insertAndReturnOffset(record);

        if (pkIndex != -1) {
            HashIndex index =
                    new HashIndex(tableName, pkColumn);
            index.put(record.getValue(pkIndex), offset);
            index.persist();
        }
    }

    /* =========================
       INSERT (TRANSACTIONAL)
       ========================= */

    public void insert(
            String tableName,
            Record record,
            String txId
    ) throws IOException {

        ctx.transactionManager.logInsert(
                txId,
                tableName,
                record
        );

        insert(tableName, record);
    }

    /* =========================
       SELECT *
       ========================= */

    public List<Record> selectAll(String tableName)
            throws IOException {
        return ctx.storageManager.readAll(tableName);
    }

    /* =========================
       SELECT WHERE
       ========================= */

    public List<Record> selectWhere(
            String tableName,
            String column,
            String value
    ) throws IOException {

        TableSchema schema =
                ctx.schemaManager.loadSchema(tableName);

        for (Column col : schema.getColumns()) {
            if (col.isPrimaryKey() &&
                col.getName().equalsIgnoreCase(column)) {

                Record r =
                        selectByPrimaryKey(
                                tableName,
                                column,
                                value
                        );

                return r == null
                        ? List.of()
                        : List.of(r);
            }
        }

        int idx = schema.getColumnIndex(column);
        List<Record> result = new ArrayList<>();

        for (Record r :
                ctx.storageManager.readAll(tableName)) {

            if (r.getValue(idx).equals(value)) {
                result.add(r);
            }
        }
        return result;
    }

    /* =========================
       INDEXED LOOKUP
       ========================= */

    private Record selectByPrimaryKey(
            String tableName,
            String column,
            String value
    ) throws IOException {

        HashIndex index =
                new HashIndex(tableName, column);

        Long offset = index.get(value);
        if (offset == null) return null;

        File tableFile =
                new File("data/tables/" + tableName + ".tbl");

        try (RandomAccessFile raf =
                     new RandomAccessFile(tableFile, "r")) {

            raf.seek(offset);
            String line = raf.readLine();
            return Record.deserialize(line);
        }
    }
}
