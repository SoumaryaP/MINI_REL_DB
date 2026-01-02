package storage;

public class Record {
    private final String[] values;

    public Record(String[] values) {
        this.values = values;
    }

    public String serialize() {
        return String.join("|", values);
    }

    public static Record deserialize(String line) {
        return new Record(line.split("\\|"));
    }

    public String getValue(int index) {
        return values[index];
    }

    public int size() {
    return values.length;
    }

    public String[] getValues() {
    return values;
    }


}