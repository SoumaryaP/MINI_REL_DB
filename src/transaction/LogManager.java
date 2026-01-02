package transaction;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogManager {

    private static final String LOG_DIR = "data/logs/";
    private static final String LOG_FILE = LOG_DIR + "db.log";

    public LogManager() {
        File dir = new File(LOG_DIR);
        if (!dir.exists()) dir.mkdirs();
    }

    public synchronized void log(String entry) throws IOException {
        try (FileWriter fw = new FileWriter(LOG_FILE, true)) {
            fw.write(entry + "\n");
        }
    }
}
