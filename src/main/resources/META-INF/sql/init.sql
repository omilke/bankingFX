CREATE SCHEMA IF NOT EXISTS PUBLIC;

DROP TABLE IF EXISTS ENTRY CASCADE;

CREATE TABLE ENTRY
(
    id         BIGINT NOT NULL auto_increment primary key,
    amount     NUMERIC(12, 2),
    entryDate  DATE,
    comment    VARCHAR,
    category   VARCHAR,
    saving     BOOLEAN,
    sequence   INTEGER,
    orderIndex INTEGER,
    entryYear  INTEGER,
    entryMonth INTEGER,
    entryDay   INTEGER
);

CREATE INDEX IDX_ENTRY_DATE ON ENTRY (entryDate);

DROP TABLE IF EXISTS RECURRING_ENTRY CASCADE;

CREATE TABLE RECURRING_ENTRY
(
    id                 BIGINT NOT NULL auto_increment primary key,
    amount             NUMERIC(12, 2),
    startOfRecurrence  DATE,
    lastRecurrence     DATE,
    comment            VARCHAR,
    category           VARCHAR,
    saving             BOOLEAN,
    sequence           INTEGER,
    orderIndex         INTEGER,
    recurrenceStrategy VARCHAR
);