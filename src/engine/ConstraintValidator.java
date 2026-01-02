package engine;

import schema.Column;
import schema.TableSchema;
import storage.Record;

import java.io.IOException;
import java.util.List;

public class ConstraintValidator {

    public static void validateInsert(
            TableSchema schema,
            Record newRecord,
            List<Record> existingRecords
    ) {

        if (newRecord.size() != schema.getColumns().size()) {
            throw new RuntimeException("Column count mismatch");
        }

        for (int i = 0; i < schema.getColumns().size(); i++) {
            Column col = schema.getColumns().get(i);
            String value = newRecord.getValue(i);

            // NOT NULL
            if (col.isNotNull() && (value == null || value.isEmpty())) {
                throw new RuntimeException(
                        "NOT NULL constraint failed on " + col.getName());
            }

            // TYPE CHECK
            validateType(col.getType(), value);

            // PRIMARY KEY
            if (col.isPrimaryKey()) {
                checkPrimaryKeyUniqueness(i, value, existingRecords);
            }
        }
    }

    private static void validateType(String type, String value) {
        try {
            switch (type) {
                case "INT" -> Integer.parseInt(value);
                case "FLOAT" -> Float.parseFloat(value);
                case "STRING" -> {
                }
                default -> throw new RuntimeException("Unknown type " + type);
            }
        } catch (Exception e) {
            throw new RuntimeException("Type mismatch for value: " + value);
        }
    }

    private static void checkPrimaryKeyUniqueness(
            int colIndex,
            String value,
            List<Record> records
    ) {
        for (Record r : records) {
            if (r.getValue(colIndex).equals(value)) {
                throw new RuntimeException(
                        "PRIMARY KEY constraint violated: " + value);
            }
        }
    }
}
