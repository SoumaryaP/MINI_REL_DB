package parser;

import java.util.List;

public class SelectQuery implements Query {

    public String tableName;
    public List<String> columns;
    public String whereColumn;
    public String whereValue;
}
