package index;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HashIndex {

    private final File indexFile;
    private final Map<String, Long> indexMap = new HashMap<>();

    public HashIndex(String tableName, String columnName) throws IOException {
        this.indexFile = new File(
                "data/indexes/" + tableName + "_" + columnName + ".idx"
        );

        File dir = indexFile.getParentFile();
        if (!dir.exists()) dir.mkdirs();

        if (indexFile.exists()) {
            load();
        }
    }

    private void load() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(indexFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|");
                indexMap.put(p[0], Long.parseLong(p[1]));
            }
        }
    }

    public void put(String key, long offset) {
        indexMap.put(key, offset);
    }

    public Long get(String key) {
        return indexMap.get(key);
    }

    public void persist() throws IOException {
        try (FileWriter fw = new FileWriter(indexFile)) {
            for (Map.Entry<String, Long> e : indexMap.entrySet()) {
                fw.write(e.getKey() + "|" + e.getValue() + "\n");
            }
        }
    }
}
