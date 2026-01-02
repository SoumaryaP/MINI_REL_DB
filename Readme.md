# MiniReLDB

A simple Java-based relational database management system (DBMS) for learning and experimentation.

## Features
- SQL-like interface: supports `CREATE TABLE`, `INSERT`, and `SELECT` queries
- Persistent storage of tables and schemas
- Transaction support (BEGIN, COMMIT, ROLLBACK)
- Simple file-based storage engine
- Extensible modular codebase

## Getting Started

### Prerequisites
- Java 17 or later
- Git (for cloning)

### Build
From the project root:
```sh
javac -d out src/client/DBConsole.java src/engine/*.java src/index/*.java src/parser/*.java src/schema/*.java src/storage/*.java src/transaction/*.java
```

### Run
From the project root:
```sh
java -cp out client.DBConsole
```

## Example Usage
```
MiniRelDB started. Type EXIT to quit.
MiniRelDB > CREATE TABLE students (id INT PRIMARY KEY, name STRING NOT NULL)
Table created.
MiniRelDB > INSERT INTO students VALUES (1,'Amit')
1 row inserted.
MiniRelDB > SELECT * FROM students
1|Amit
MiniRelDB > EXIT
```

## Supported SQL Syntax
- `CREATE TABLE tableName (col1 TYPE [PRIMARY KEY] [NOT NULL], col2 TYPE [NOT NULL], ...)`
- `INSERT INTO tableName VALUES (val1, val2, ...)`
- `SELECT * FROM tableName`
- `SELECT col1,col2 FROM tableName`
- `SELECT * FROM tableName WHERE col='value'`

## Project Structure
- `src/client/` — Console application entry point
- `src/engine/` — Query execution and transaction logic
- `src/parser/` — SQL parser and query classes
- `src/schema/` — Table schema and column definitions
- `src/storage/` — File-based storage engine
- `src/transaction/` — Transaction management
- `data/` — Persistent data and schema files

## Contributing
Pull requests and suggestions are welcome!

## License
MIT License
MiniRelDB/
│
├── src/
│   ├── cli/
│   │   └── DBConsole.java
│   │
│   ├── parser/
│   │   ├── SQLTokenizer.java
│   │   ├── SQLParser.java
│   │
│   ├── engine/
│   │   ├── QueryExecutor.java
│   │   ├── ExecutionContext.java
│   │
│   ├── storage/
│   │   ├── StorageManager.java
│   │   ├── TableFile.java
│   │   ├── Record.java
│   │
│   ├── schema/
│   │   ├── SchemaManager.java
│   │   ├── TableSchema.java
│   │   ├── Column.java
│   │
│   ├── index/
│   │   ├── HashIndex.java
│   │
│   ├── transaction/
│   │   ├── TransactionManager.java
│   │   ├── LogManager.java
│   │
│   └── util/
│       └── FileUtils.java
│
└── data/
    ├── tables/
    ├── schemas/
    ├── indexes/
    └── logs/
