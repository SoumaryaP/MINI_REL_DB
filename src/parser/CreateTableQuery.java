package parser;

import schema.Column;
import java.util.List;

public class CreateTableQuery implements Query {
    public String tableName;
    public List<Column> columns;
}
