package schema;

public class Column {

    private final String name;
    private final String type; 
    private final boolean primaryKey;
    private final boolean notNull;

    public Column(String name, String type, boolean primaryKey, boolean notNull) {
        this.name = name;
        this.type = type.toUpperCase();
        this.primaryKey = primaryKey;
        this.notNull = notNull;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public String serialize() {
        return name + "|" + type + "|" + primaryKey + "|" + notNull;
    }

    public static Column deserialize(String line) {
        String[] p = line.split("\\|");
        return new Column(
                p[0],
                p[1],
                Boolean.parseBoolean(p[2]),
                Boolean.parseBoolean(p[3])
        );
    }
}
