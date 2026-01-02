package parser;

import java.util.List;

public class InsertQuery implements Query {

    public String tableName;
    public List<String> values;
}
