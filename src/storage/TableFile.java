package storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TableFile {

    private final File file;

    public TableFile(String tableName) throws IOException {
        this.file = new File("data/tables/" + tableName + ".tbl");
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            if (!parent.mkdirs()) {
                throw new IOException("Failed to create directories: " + parent.getPath());
            }
        }
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException("Failed to create table file: " + file.getPath());
            }
        }
    }

    public void insert(Record record) throws IOException {
        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(record.serialize() + "\n");
        }
    }

    public long insertAndReturnOffset(Record record) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            long offset = raf.length();
            raf.seek(offset);
            byte[] bytes = (record.serialize() + "\n").getBytes(StandardCharsets.UTF_8);
            raf.write(bytes);
            return offset;
        }
    }

    public List<Record> readAll() throws IOException {
        List<Record> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                records.add(Record.deserialize(line));
            }
        }
        return records;
    }

}