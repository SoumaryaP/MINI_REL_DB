
package parser;

import schema.Column;
import java.util.*;

public class SQLParser {


    public Query parse(String sql) {
        sql = sql.trim();
        System.out.println("[DEBUG] SQL: '" + sql + "'");
        if (sql.toUpperCase().startsWith("CREATE TABLE")) {
            System.out.println("[DEBUG] Recognized CREATE TABLE");
            return parseCreateTable(sql);
        }
        if (sql.toUpperCase().startsWith("SELECT")) {
            System.out.println("[DEBUG] Recognized SELECT");
            return parseSelect(sql);
        }
        if (sql.toUpperCase().startsWith("INSERT")) {
            System.out.println("[DEBUG] Recognized INSERT");
            return parseInsert(sql);
        }
        System.out.println("[DEBUG] Unsupported SQL");
        throw new RuntimeException("Unsupported SQL");
    }

    private CreateTableQuery parseCreateTable(String sql) {
        CreateTableQuery q = new CreateTableQuery();
        // CREATE TABLE students (id INT PRIMARY KEY, name STRING NOT NULL)
        String inside = sql.substring(sql.indexOf("(") + 1, sql.lastIndexOf(")"));
        String header = sql.substring(0, sql.indexOf("(")).trim();
        q.tableName = header.replace("CREATE TABLE", "").trim();
        String[] defs = inside.split(",");
        List<Column> columns = new ArrayList<>();
        for (String def : defs) {
            String[] parts = def.trim().split("\\s+");
            String name = parts[0];
            String type = parts[1];
            boolean primaryKey = false;
            boolean notNull = false;
            for (int i = 2; i < parts.length; i++) {
                if (parts[i].equalsIgnoreCase("PRIMARY")) {
                    primaryKey = true;
                }
                if (parts[i].equalsIgnoreCase("NOT")) {
                    notNull = true;
                }
            }
            columns.add(new Column(name, type, primaryKey, notNull));
        }
        q.columns = columns;
        return q;
    }

    private SelectQuery parseSelect(String sql) {
        SelectQuery q = new SelectQuery();

        String[] parts = sql.split("FROM");
        String colPart = parts[0].replace("SELECT", "").trim();

        q.columns = colPart.equals("*")
                ? new ArrayList<>()
                : Arrays.asList(colPart.split(","));

        String rest = parts[1].trim();

        if (rest.contains("WHERE")) {
            String[] w = rest.split("WHERE");
            q.tableName = w[0].trim();

            String[] cond = w[1].split("=");
            q.whereColumn = cond[0].trim();
            q.whereValue = cond[1].replace("'", "").trim();
        } else {
            q.tableName = rest;
        }

        return q;
    }

    private InsertQuery parseInsert(String sql) {
        InsertQuery q = new InsertQuery();

        String[] parts = sql.split("VALUES");
        q.tableName = parts[0].replace("INSERT INTO", "").trim();

        String vals = parts[1]
                .replace("(", "")
                .replace(")", "")
                .trim();

        q.values = Arrays.asList(vals.split(","));
        return q;
    }
}
