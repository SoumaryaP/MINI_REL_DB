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
