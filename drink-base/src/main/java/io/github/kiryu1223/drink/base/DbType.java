package io.github.kiryu1223.drink.base;

public enum DbType
{
    /**
     * SQL
     */
    Any,
    MySQL,
    SQLServer,
    H2,
    Oracle,
    SQLite,
    PostgreSQL,
    Doris,

    /**
     * NoSQL
     */
    Neo4j,
}